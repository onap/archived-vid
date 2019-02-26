/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

package org.onap.vid.mso;

import com.google.common.base.MoreObjects;

public class RestObject<T> {

    private T t;

    /**
     * The string source of t, if available
     */
    private String rawT;

    private int statusCode = 0;

    public RestObject() {
    }

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public void setStatusCode(int v) {
        this.statusCode = v;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getRaw() {
        return rawT;
    }

    public void setRaw(String rawT) {
        this.rawT = rawT;
    }

    public void copyFrom(RestObject<T> src) {
        set(src.get());
        setRaw(src.getRaw());
        setStatusCode(src.getStatusCode());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("t", t)
                .add("rawT", rawT)
                .add("statusCode", statusCode)
                .toString();
    }
}

