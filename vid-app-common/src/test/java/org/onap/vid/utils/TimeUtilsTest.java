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

package org.onap.vid.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class TimeUtilsTest {
    private static final Logger logger = LogManager.getLogger(TimeUtilsTest.class);

    @DataProvider
    public static Object[][] goodData() {
        return new Object[][]{
                {"Wed, 15 Oct 2014 13:01:52 GMT",   1413378112, "Timestamp as described in the documentation"},
                {"Wed, 15 Oct 2014 14:01:52 +0100", 1413378112, "GMT +1"},
                {"Wed, 15 Oct 2014 11:01:52 -0200", 1413378112, "GMT -2"}
        };
    }

    @DataProvider
    public static Object[][] goodDataToString() {
        return new Object[][]{
                {"Wed, 15 Oct 2014 13:01:52 GMT",   1413378112, "UTC", "Timestamp as described in the documentation"},
                {"Wed, 15 Oct 2014 14:01:52 +0100", 1413378112, "+1", "GMT +1"},
                {"Wed, 15 Oct 2014 11:01:52 -0200", 1413378112, "-2", "GMT -2"}
        };
    }

    @DataProvider
    public static Object[][] badData() {
        return new Object[][]{
                {"Wed, 15 Oct 2014 13:01:52", "No offset"},
                {"Wed, 15 Oct 2014 13:01:52 UTC", "UTC"},
                {"Wed, 15 Oct 2014 13:01:52 UT", "UT"},
                {"Wed, 15 Oct 2014 13:01:52Z", "Zulu time"},
                {"Wed, 15 Oct 2014 13:01:52 EST", "EST time"}
        };
    }

    @Test(dataProvider = "goodData")
    public void parseSuccessTest(String timestamp, long expectedResult, String description) {
        logger.info(description);
        ZonedDateTime parsedTime = TimeUtils.parseZonedDateTime(timestamp);
        Assert.assertEquals(parsedTime.toEpochSecond(), expectedResult);
    }

    @Test(expectedExceptions = DateTimeParseException.class, dataProvider = "badData")
    public void parseFailedTest(String timestamp, String description) {
        logger.info(description);
        TimeUtils.parseZonedDateTime(timestamp);
    }

    @Test(dataProvider = "goodDataToString")
    public void toStringSuccessTest(String expectedResult, long epochTime, String zoneId, String description) {
        logger.info(description);
        Instant instant = Instant.ofEpochSecond(epochTime);
        ZonedDateTime time = ZonedDateTime.ofInstant(instant, ZoneId.of(zoneId));
        String timeStamp = TimeUtils.zonedDateTimeToString(time);
        Assert.assertEquals(timeStamp, expectedResult);
    }
}
