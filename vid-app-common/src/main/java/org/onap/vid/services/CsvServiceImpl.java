/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.services;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;
import static org.onap.vid.utils.Logging.getMethodName;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.BadRequestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvServiceImpl implements CsvService{


    /** The logger. */
    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(CsvServiceImpl.class);

    private static final String ARRAY_REGEX = "\\[(.*?)\\]";


    /**
     * In UTF-8 the first line starts with "\uFEFF" so need to remove it
     * @param line is first line contains BOM
     * @return line after change
     */
    private String [] removeBOMFromCsv(String [] line){
        if (line.length>0)
            line[0] = line[0].replaceFirst("\uFEFF","").replaceFirst("ï»¿","");
        return line;
    }

    /**
     * read a csv file and puts its content in list of string arrays (without the empty lines)
     * @param filePath - the path of file to read
     * @return the content of file
     * @throws IOException
     */
    @Override
    public List<String[]> readCsv(String filePath) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        return readCsv(reader);
    }

    @Override
    public List<String[]> readCsv(MultipartFile file) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
        return readCsv(reader);
    }

    private List<String[]> addLineWithoutSpaces(List<String[]> myEntries, String [] line){
        line = Arrays.stream(line).filter(x -> !"".equals(x)).toArray(String[]::new);
        if(line.length > 0)
            myEntries.add(line);
        return myEntries;
    }


    private  List<String[]> readCsv(CSVReader reader) {
        try {
            List<String[]> myEntries = new ArrayList<>() ;
            String [] line;
            Boolean firstLine = true;
            while ((line = reader.readNext())!= null) {
                if (firstLine) {
                    line = removeBOMFromCsv(line);
                    firstLine = false;
                }
                myEntries = addLineWithoutSpaces(myEntries, line);
            }
            return myEntries;
        }
        catch (Exception e){
            logger.error("error during reading CSV file. exception:" + e.getMessage());
            return rethrow(e);
        }

    }

    /**
     * main function that call to the recursive function with initial parameters
     * @param myEntries - the matrix with file content
     * @return the json
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public JSONObject convertCsvToJson (List<String[]> myEntries) throws InstantiationException, IllegalAccessException {
        try {
            return buildJSON(myEntries, 0, 0, myEntries.size(), JSONObject.class);
        }
        catch (Exception e){
            logger.error("error during parsing CSV file. exception:" + e.getMessage());
            throw e;
        }

    }

    /**
     * it goes over the matrix column while the values are the same and returns the index of changed value
     * @param myEntries the matrix
     * @param i row index refer to the whole matrix
     * @param j column index
     * @param numLines the length of the current inner matrix
     * @param startLine row index of inner matrix
     * @return the index of changed line
     */
    private int findIndexOfChangedLine(List<String[]> myEntries, final int i, final int j, final int numLines, final int startLine) {
        int k;
        for(k = 0; k + i - startLine < numLines && myEntries.get(i)[j].equals(myEntries.get(k + i)[j]) ; k++);
        return k;
    }

    /**
     *  check in array if its first element or if the key already exist in the previous item
     * @param jsonArray - the array to search in
     * @param key - the key to check
     * @return if exists or first element return true, otherwise- false
     */
    private Boolean keyExistsOrFirstElement( JSONArray jsonArray,String key){
        Boolean exists = false;
        Boolean first = false;
        JSONObject lastItem = lastItemInArray(jsonArray);
        if (lastItem == null) {
            first = true;
        }
        else {
            if (lastItem.has(key)) {
                exists = true;
            }
        }
        return exists||first;
    }

    /**
     * return last json in json array
     * @param jsonArray
     * @return last item or null if the array is empty
     */
    private JSONObject lastItemInArray(JSONArray jsonArray){
        JSONObject lastItem = null;
        if(jsonArray.length()>0) {
            lastItem = (JSONObject) jsonArray.get(jsonArray.length() - 1);
        }
        return lastItem;
    }

    /**
     * append current json to the main json
     * @param json - the main json to append to it
     * @param key - key to append
     * @param values - value(s) to append
     * @param <T> can be JSONObject or JSONArray
     * @param <E> string or jsonObject or jsonArray
     * @return json after put
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private <T, E> T putJson(T json, String key, E values) {
        if (json instanceof JSONArray){
            JSONArray currentJson= ((JSONArray)json);
            if (values == null) //array of strings (for last item)
            {
                currentJson.put(key);
            }
            else {
                if (keyExistsOrFirstElement(currentJson, key)) {
                    currentJson.put(new JSONObject().put(key, values));
                } else {
                JSONObject lastItem = lastItemInArray(currentJson);
                    if(lastItem != null) {
                        lastItem.put(key, values);
                    }
                }
            }
        }
        if (json instanceof JSONObject){
            if (values == null)
                throw new BadRequestException("Invalid csv file");
            ((JSONObject)json).put(key,values);
        }
        return json;
    }


    /**
     *  recursive function to build JSON. Each time it counts the same values in left and send the smaller matrix
     *  (until the changed value) to the next time.
     *
     * @param myEntries - the whole matrix
     * @param i- row index of the whole matrix
     * @param j - column index
     * @param numLines - number of lines of inner matrix (num of same values in the left column)
     * @param clazz JSONArray or JSONObject
     * @param <T> JSONArray or JSONObject
     * @return the json object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private <T> T buildJSON(List<String[]> myEntries, int i, final int j, final int numLines, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        logger.debug(EELFLoggerDelegate.debugLogger, "start {}({}, {}, {})", getMethodName(), i, j, numLines);
        T json = clazz.newInstance();
        int startLine = i;
        while(i < numLines + startLine){
            String[] currentRow = myEntries.get(i);
            int length = currentRow.length;
            int currentDuplicateRows = findIndexOfChangedLine(myEntries,i,j,numLines, startLine);
            String key = currentRow[j];
            if (j == length-1) {
                json = putJson(json,currentRow[j],null);
            }
            else
            {
                json = buildJsonRow(myEntries, i, j, json, currentRow, length, currentDuplicateRows, key);
            }
            i += currentDuplicateRows;
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "end {} json = {}", getMethodName(), json);
        return json;
    }

    private <T> T buildJsonRow(List<String[]> myEntries, int i, int j, T json, String[] currentRow, int length, int currentDuplicateRows, String key) throws IllegalAccessException, InstantiationException {
        if (key.matches(ARRAY_REGEX)){
            JSONArray arrayObjects = buildJSON(myEntries, i, j + 1, currentDuplicateRows, JSONArray.class);
            json = putJson(json,key.replaceAll("\\[","").replaceAll("]",""),arrayObjects);
        }
        else {
            if (j < length - 2) {
                json = putJson(json, currentRow[j], buildJSON(myEntries, i, j + 1, currentDuplicateRows, JSONObject.class));
            }
            else
            {
                if (j == length - 2)//last object
                {
                    if(currentDuplicateRows > 1) {
                        throw new BadRequestException("Invalid csv file");
                    }
                    json = putJson(json, currentRow[j], currentRow[j + 1]);
                }
            }
        }
        return json;
    }

}

