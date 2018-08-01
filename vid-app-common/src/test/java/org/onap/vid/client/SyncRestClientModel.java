/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.client;

class SyncRestClientModel {

    static class TestModel{

        public TestModel() {
            // needed by the object mappers
        }

        public TestModel(long key, String value) {
            this.key = key;
            this.value = value;
        }

        private long key;
        private String value;

        public void setKey(long key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public long getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

}
