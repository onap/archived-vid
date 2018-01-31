package org.onap.vid.services;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface CsvService {
    List<String[]> readCsv(String filePath) throws IOException;
    JSONObject convertCsvToJson (List<String[]> myEntries) throws InstantiationException, IllegalAccessException;
    List<String[]> readCsv(MultipartFile file) throws IOException;

}
