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

package org.onap.vid.model;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JobAuditStatusTest {

    @DataProvider
    public static Object[][] AdditionalInfoSizes() {
        return new Object[][]{
                {5, 5},
                {1999,1999},
                {2000, 2000},
                {2001, 2000},
                {10000, 2000}
        };
    }

    @Test(dataProvider = "AdditionalInfoSizes")
    public void testAdditionalInfoMaxLength(int originalSize, int finalSize) {
        JobAuditStatus jobAuditStatus = new JobAuditStatus();
        jobAuditStatus.setAdditionalInfo(StringUtils.repeat("a", originalSize));
        assertThat(jobAuditStatus.getAdditionalInfo().length(), is(finalSize));
    }

    @Test(dataProvider = "AdditionalInfoSizes")
    public void testAdditionalInfoMaxLengthInConstructor(int originalSize, int finalSize) {
        final String additionalInfo = StringUtils.repeat("a", originalSize);
        JobAuditStatus jobAuditStatus = new JobAuditStatus(UUID.randomUUID(), "myJobStatus", JobAuditStatus.SourceStatus.MSO, UUID.randomUUID(), additionalInfo, new Date());
        assertThat(jobAuditStatus.getAdditionalInfo().length(), is(finalSize));
    }

}
