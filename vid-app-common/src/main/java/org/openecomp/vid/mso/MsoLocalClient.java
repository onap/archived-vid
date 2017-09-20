package org.openecomp.vid.mso;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.changeManagement.ChangeManagementRequest;
import org.openecomp.vid.controller.VidController;
import org.openecomp.vid.mso.rest.RequestDetails;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by pickjonathan on 20/06/2017.
 */
public class MsoLocalClient implements MsoRestInterfaceIfc {

    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoLocalClient.class);

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    /**
     * The client.
     */
    private static Client client = null;

    /**
     * The common headers.
     */
    private MultivaluedHashMap<String, Object> commonHeaders;

    /**
     * Instantiates a new mso rest interface.
     */
    public MsoLocalClient() {
        super();
    }

    public void initMsoClient() {
        final String methodname = "initRestClient()";
    }

    @Override
    public <T> void Get(T t, String sourceId, String path, RestObject<T> restObject) throws Exception {

    }

    @Override
    public <T> void Delete(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception {

    }

    @Override
    public <T> void Post(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception {
        initMsoClient();

        final InputStream asdcServicesFile = MsoLocalClient.class.getClassLoader().getResourceAsStream("mso_create_instance_response.json");

        t = (T) IOUtils.toString(asdcServicesFile);
        restObject.setStatusCode(200);
        restObject.set(t);
    }

    @Override
    public void logRequest(RequestDetails r) {

    }

	@Override
	public <T> void Put(T t, ChangeManagementRequest r, String sourceID, String path, RestObject<T> restObject)
			throws Exception {
		
		
	}
}
