//package org.onap.vid.services;
//import org.apache.commons.io.IOUtils;
//import org.onap.vid.services.CsvServiceImpl;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.skyscreamer.jsonassert.JSONCompareMode;
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//import org.json.JSONObject;
//
//import javax.ws.rs.BadRequestException;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.net.URL;
//import java.util.List;
//
//@Test
//public class CsvServiceTest {
//
//    private CsvServiceImpl csvService = new CsvServiceImpl();
//    private final static String CSV_FOLDER = "csv_files/{CSV_FILE}";
//    private final static String VALID_CSV = "csv_to_json.csv";
//    private final static String ONE_LINE_CSV = "one_line.csv";
//    private final static String EMPTY_CSV = "empty_file.csv";
//    private final static String MISSING_CONTENT_CSV = "missing_content.csv";
//    private final static String MISSING_VALUES_CSV = "missing_values.csv";
//
//
///*
//    @Test
//    public void parseValidCsv() throws IllegalAccessException, IOException, InstantiationException {
//        String expectedJson = getExpectation("vnfConfigUpdatePayload.json");
//        readAndParse(VALID_CSV,16,expectedJson);
//
//    }*/
//
//    private String getExpectation(String modelFileName) throws IOException {
//        // load expected result
//        final URL resource = this.getClass().getResource("/" + modelFileName);
//        String expected = IOUtils.toString(resource, "UTF-8");
//        return expected;
//    }
///*
//    @Test
//    public void parseOneLineCsv() throws IllegalAccessException, IOException, InstantiationException {
//        String expectedJson ="{\"payload\":{\"request-parameters\":{\"vnf-name\":\"ibcx0099v\"}}}";
//        readAndParse(ONE_LINE_CSV,1,expectedJson);
//    }*/
//
//    @DataProvider
//    public static Object[][] invalidFiles(Method test) {
//        return new Object[][]{
//                {MISSING_CONTENT_CSV}, {MISSING_VALUES_CSV}
//        };
//    }
///*
//    @Test(dataProvider = "invalidFiles", expectedExceptions = {BadRequestException.class}, expectedExceptionsMessageRegExp = "Invalid csv file")
//    public void parseMissingContentCsv(String invalidFile) throws IllegalAccessException, IOException, InstantiationException {
//        readAndParse(invalidFile, 2, null);
//    }
//
//
//    @Test
//    public void parseEmptyCsv() throws IllegalAccessException, IOException, InstantiationException {
//        String expectedJson ="{}";
//        readAndParse(EMPTY_CSV,0,expectedJson);
//    }
//
//    private void readAndParse(String fileName, int expectedNumRows, String expectedJson) throws IllegalAccessException, IOException, InstantiationException {
//        final URL resource = CsvServiceTest.class.getClassLoader().getResource(CSV_FOLDER.replaceFirst("\\{CSV_FILE\\}", fileName));
//        Assert.assertNotNull(resource, "The csv file was not found");
//        List<String[]> content = csvService.readCsv(resource.getPath());
//        Assert.assertEquals(content.size(), expectedNumRows, "The number of non-empty lines in file is wrong");
//        JSONObject json = csvService.convertCsvToJson (content);
//        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.STRICT);
//
//    }*/
//
//
//}
