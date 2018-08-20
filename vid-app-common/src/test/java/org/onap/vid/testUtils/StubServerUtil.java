package org.onap.vid.testUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;

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

    public void stopServer() {
        stubServer.stop();
    }


    public String constructTargetUrl(String protocol, String relativePath) {
        return String.format("%s://localhost:%s/%s", protocol, stubServer.getPort(), relativePath);
    }

    public void prepareGetCall(String path, Object returnObj, Action expectedAction) throws JsonProcessingException {
        whenHttp(stubServer)
                .match(Condition.get(path))
                .then(expectedAction, jsonContent(returnObj), contentType(APPLICATION_JSON));
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

    private Action jsonContent(Object returnObj) throws JsonProcessingException {
        return stringContent(objectMapper.writeValueAsString(returnObj));
    }

}
