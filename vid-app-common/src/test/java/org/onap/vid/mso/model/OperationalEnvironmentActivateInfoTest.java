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

import org.junit.Test;
import org.onap.vid.controller.OperationalEnvironmentController;
import org.onap.vid.controller.OperationalEnvironmentController.OperationalEnvironmentActivateBody;
import org.onap.vid.controller.OperationalEnvironmentController.OperationalEnvironmentManifest;

public class OperationalEnvironmentActivateInfoTest {

    private OperationalEnvironmentActivateInfo createTestSubject() {
        OperationalEnvironmentController.OperationalEnvironmentActivateBody a = new OperationalEnvironmentActivateBody("a", "b", "c", new OperationalEnvironmentManifest());
        return new OperationalEnvironmentActivateInfo(a, "", "");
    }

    @Test
    public void testGetUserId() throws Exception {
        OperationalEnvironmentActivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getUserId();
    }

    @Test
    public void testGetOperationalEnvironmentId() throws Exception {
        OperationalEnvironmentActivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getOperationalEnvironmentId();
    }

    @Test
    public void testToString() throws Exception {
        OperationalEnvironmentActivateInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}
