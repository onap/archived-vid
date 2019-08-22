/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.testUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Call;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;

import java.util.List;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.stringContent;

public class StubServerUtil {

    private static final String APPLICATION_JSON = "application/json";
    private ObjectMapper objectMapper;


    private StubServer stubServer;

    public StubServerUtil() {
        this.objectMapper = new ObjectMapper();
        this.stubServer = new StubServer();
    }


    public void runServer() {
        stubServer.run();
    }

    public void runSecuredServer(){
        stubServer.secured().run();
    }

    public void stopServer() {
        stubServer.stop();
    }


    public String constructTargetUrl(String protocol, String relativePath) {
        return String.format("%s://localhost:%s/%s", protocol, stubServer.getPort(), relativePath);
    }

    public void prepareGetCall(String path, Action actionToReturn, Action expectedAction, String contentType) throws JsonProcessingException {
        whenHttp(stubServer)
                .match(Condition.get(path))
                .then(expectedAction, actionToReturn, contentType(contentType));
    }


    public void prepareGetCall(String path, Object returnObj, Action expectedAction) throws JsonProcessingException {
        prepareGetCall(path, jsonContent(returnObj), expectedAction, APPLICATION_JSON);
    }

    public void prepareDeleteCall(String path, Object returnObj, Action expectedAction) throws JsonProcessingException {
        whenHttp(stubServer)
                .match(Condition.delete(path))
                .then(expectedAction, jsonContent(returnObj), contentType(APPLICATION_JSON));
    }

    public void preparePostCall(String path, Object returnObj, Action expectedAction) throws JsonProcessingException {
        whenHttp(stubServer)
                .match(Condition.post(path),
                        Condition.withPostBodyContaining(objectMapper.writeValueAsString(returnObj)))
                .then(expectedAction, jsonContent(returnObj), contentType(APPLICATION_JSON));
    }

    public void preparePutCall(String path, Object returnObj, Action expectedStatus) throws JsonProcessingException {
        whenHttp(stubServer)
                .match(Condition.put(path),
                        Condition.withPostBodyContaining(objectMapper.writeValueAsString(returnObj)))
                .then(expectedStatus, jsonContent(returnObj), contentType(APPLICATION_JSON));
    }

    public List<Call> getServerCalls() {
        return stubServer.getCalls();
    }

    private Action jsonContent(Object returnObj) throws JsonProcessingException {
        return stringContent(objectMapper.writeValueAsString(returnObj));
    }

}
