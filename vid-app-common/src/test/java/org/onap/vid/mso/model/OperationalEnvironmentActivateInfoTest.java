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

package org.onap.vid.mso.model;

import org.mockito.Mock;
import org.onap.vid.controller.OperationalEnvironmentController;
import org.onap.vid.controller.OperationalEnvironmentController.OperationalEnvironmentManifest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OperationalEnvironmentActivateInfoTest {

    @Mock
    private OperationalEnvironmentController.OperationalEnvironmentActivateBody operationalEnvironmentActivateBody;

    @BeforeClass
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldProperlyCreateOperationalEnvironmentActivateInfoWithParameters() {
        //  given
        String relatedInstanceId = "testRelatedInstanceId";
        String relatedInstanceName = "testRelatedInstanceName";
        String workloadContext = "testWorkloadContext";
        String userId = "testUserId";
        String operationalEnvironmentId = "testOperationalEnvironmentId";
        OperationalEnvironmentManifest manifest = new OperationalEnvironmentManifest();

        when(operationalEnvironmentActivateBody.getRelatedInstanceId()).thenReturn(relatedInstanceId);
        when(operationalEnvironmentActivateBody.getRelatedInstanceName()).thenReturn(relatedInstanceName);
        when(operationalEnvironmentActivateBody.getWorkloadContext()).thenReturn(workloadContext);
        when(operationalEnvironmentActivateBody.getManifest()).thenReturn(manifest);


        //  when
        OperationalEnvironmentActivateInfo operationalEnvironmentActivateInfo =
                new OperationalEnvironmentActivateInfo(operationalEnvironmentActivateBody, userId, operationalEnvironmentId);


        //  then
        assertThat(operationalEnvironmentActivateInfo.getUserId()).isEqualTo(userId);
        assertThat(operationalEnvironmentActivateInfo.getOperationalEnvironmentId()).isEqualTo(operationalEnvironmentId);

        assertThat(operationalEnvironmentActivateInfo.toString()).isEqualToIgnoringWhitespace(
                "OperationalEnvironmentActivateInfo{operationalEnvironmentId="+operationalEnvironmentId+"," +
                        " userId="+userId+"," +
                        " super=OperationalEnvironmentActivateInfo{" +
                        " relatedInstanceId="+relatedInstanceId+"," +
                        " relatedInstanceName="+relatedInstanceName+"," +
                        " workloadContext="+workloadContext+"," +
                        " manifest="+manifest.toString()+"}}"
        );

    }

}