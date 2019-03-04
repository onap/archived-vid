/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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
package org.onap.vid.mso.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.changeManagement.RequestDetails;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.controller.LocalWebConfig;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;


@ContextConfiguration(classes = {LocalWebConfig.class, SystemProperties.class})
@WebAppConfiguration
public class MsoRestClientTest {


    private MsoBusinessLogic msoBusinessLogic = new MsoBusinessLogicImpl(new MsoRestClientNew(new SyncRestClient(), ""), null);
    private ObjectMapper om = new ObjectMapper();

    @Test
    public void createInPlaceMsoRequest() {
        String result = null;
        try {
            RequestDetails requestDetails = MsoRestClientTestUtil.generateMockMsoRequest();
            result = om.writeValueAsString(msoBusinessLogic.generateInPlaceMsoRequest(requestDetails));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            Assert.fail("Failed to create mso request");
        }
        JSONObject jsonObj = new JSONObject(result);
        Assert.assertNotNull(jsonObj.getJSONObject("requestDetails"));
    }
}
