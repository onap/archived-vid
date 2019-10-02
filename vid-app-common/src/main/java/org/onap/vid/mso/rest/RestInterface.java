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

package org.onap.vid.mso.rest;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.RestObjectWithRequestInfo;

/**
 * Created by pickjonathan on 26/06/2017.
 */
public interface RestInterface {

    /**
     * Gets the.
     *
     * @param <T> the generic type
     * @param t the t
     * @param path the path
     * @param restObject the rest object
     * @param warpException
     * @throws Exception the exception
     */
    <T> RestObjectWithRequestInfo<T> Get(T t, String path, RestObject<T> restObject, boolean warpException);

    /**
     * Post.
     *
     * @param t the t
     * @param r the r
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    void Post(String t, Object r, String path, RestObject<String> restObject);

    /**
     * Put.
     *
     * @param <T> the generic type
     * @param t the t
     * @param r the r
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    <T> void Put(T t, RequestDetailsWrapper r, String path, RestObject<T> restObject);

    <T> RestObject<T> GetForObject(String path, Class<T> clazz);

}
