package org.onap.vid.aai;

import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.ACCEPT;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.CONTENT_TYPE;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.FROM_APP_ID_HEADER;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.REQUEST_ID;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.TRANSACTION_ID_HEADER;

import io.joshworks.restclient.http.HttpResponse;
import io.vavr.collection.HashMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import lombok.val;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.aai.util.AAIProperties;
import org.onap.vid.client.SyncRestClientInterface;

public class AaiOverTLSClient implements AaiOverTLSClientInterface {

    private final AaiOverTLSPropertySupplier propertySupplier;
    private SyncRestClientInterface syncRestClient;
    private boolean useClientCert;
    private static String CALLER_APP_ID = "VidAaiController";
    private String urlBase;

    public AaiOverTLSClient(SyncRestClientInterface syncRestClient, AaiOverTLSPropertySupplier propertySupplier) {
        this(syncRestClient, propertySupplier, SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL));
    }

    AaiOverTLSClient(SyncRestClientInterface syncRestClient, AaiOverTLSPropertySupplier propertySupplier, String baseUrl) {
        this.syncRestClient = syncRestClient;
        this.propertySupplier = propertySupplier;
        this.urlBase = baseUrl;
    }

    @Override
    public void setUseClientCert(boolean useClientCert) {
        this.useClientCert = useClientCert;
    }

    @Override
    public HttpResponse<AaiNodeQueryResponse> searchNodeTypeByName(String name, ResourceType type) {
        val uri = urlBase + String.format(URIS.NODE_TYPE_BY_NAME, type.getAaiFormat(), type.getNameFilter(), name);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), AaiNodeQueryResponse.class);
    }

    private Map<String, String> getRequestHeaders() {
        val result = HashMap.of(
            TRANSACTION_ID_HEADER, propertySupplier.getRandomUUID(),
            FROM_APP_ID_HEADER, CALLER_APP_ID,
            CONTENT_TYPE, MediaType.APPLICATION_JSON,
            REQUEST_ID, propertySupplier.getRequestId(),
            ACCEPT, MediaType.APPLICATION_JSON)
            .toJavaMap();
        result.putAll(getAuthorizationHeader());
        return result;
    }

    private Map<String, String> getAuthorizationHeader() {
        if (!useClientCert) {
            val vidUsername = propertySupplier.getUsername();
            val vidPassword = propertySupplier.getPassword();
            val encoded = Base64.getEncoder()
                .encodeToString((vidUsername + ":" + vidPassword).getBytes(StandardCharsets.UTF_8));
            return HashMap.of("Authorization", "Basic " + encoded).toJavaMap();
        }
        return HashMap.<String, String>empty().toJavaMap();
    }

}
