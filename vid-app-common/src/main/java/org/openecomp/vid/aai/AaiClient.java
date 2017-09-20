package org.openecomp.vid.aai;

import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import org.ecomp.aai.model.AaiAICZones.AicZones;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openecomp.aai.util.AAIRestInterface;
import org.apache.tiles.request.ApplicationContext;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.web.support.UserUtils;
import org.openecomp.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.model.SubscriberList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;


/**
 * Created by Oren on 7/4/17.
 */
public class AaiClient implements AaiClientInterface {

	/**
	 * The Constant dateFormat.
	 */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	protected String fromAppId = "VidAaiController";
	@Autowired
	ServletContext servletContext;
	/**
	 * The logger
	 */

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiClient.class);


	public AaiClient() {
		//        certiPath = getCertificatesFile().getAbsolutePath();
		//        depth = "0";
	}

	public AaiClient(ServletContext context) {
		servletContext = context;
	}



	private static String checkForNull(String local) {
		if (local != null)
			return local;
		else
			return "";

	}

	@Override
	public AaiResponse<SubscriberList> getAllSubscribers() {
		String certiPath = getCertificatesFile().getAbsolutePath();
		String depth = "0";
		Response resp = doAaiGet(certiPath, "business/customers?subscriber-type=INFRA&depth=" + depth, false);
		UserUtils userUtils = new UserUtils();
		return proccessAaiResponse(resp, SubscriberList.class,null);
	}


	@Override
	public AaiResponse getAllAicZones() {
		String certiPath = getCertificatesFile().getAbsolutePath();
		Response resp = doAaiGet(certiPath, "network/zones" , false);
		AaiResponse aaiAicZones = proccessAaiResponse(resp, AicZones.class,null);
		return aaiAicZones;
	}

	@Override
	public AaiResponse getSubscriberData(String subscriberId) {

		File certiPath = getCertificatesFile();
		String depth = "2";
		AaiResponse subscriberDataResponse;
		Response resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers/customer/" + subscriberId + "?depth=" + depth, false);
		subscriberDataResponse = proccessAaiResponse(resp, Services.class,null);
		return subscriberDataResponse;
	}

	@Override
	public AaiResponse getServices() {
		File certiPath = getCertificatesFile();
		Response resp = doAaiGet(certiPath.getAbsolutePath(), "service-design-and-creation/services", false);
		AaiResponse<GetServicesAAIRespone> getServicesResponse = proccessAaiResponse(resp, GetServicesAAIRespone.class,null);

		return getServicesResponse;
	}

	@Override
	public AaiResponse getTenants(String globalCustomerId, String serviceType) {
		File certiPath = getCertificatesFile();
		String url = "business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/" + serviceType;

		Response resp = doAaiGet(certiPath.getAbsolutePath(), url, false);
		String responseAsString  = parseForTenantsByServiceSubscription(resp.readEntity(String.class));

		AaiResponse<GetTenantsResponse[]> getTenantsResponse = proccessAaiResponse(resp, GetTenantsResponse[].class,responseAsString);
		return getTenantsResponse;
	}

	private AaiResponse proccessAaiResponse(Response resp, Class classType,String responseBody) {
		AaiResponse subscriberDataResponse;
		if (resp == null) {
			subscriberDataResponse = new AaiResponse<>(null, null, HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "Invalid response from AAI");
		} else {
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getSubscribers() resp=" + resp.getStatusInfo().toString());
			if (resp.getStatus() != HttpStatus.SC_OK) {
				logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "Invalid response from AAI");
				subscriberDataResponse = new AaiResponse<>(null, resp.readEntity(String.class), resp.getStatus());
			} else {
				String finalResponse;
				try {
					if (responseBody != null){
						finalResponse = responseBody;
					}
					else{
						finalResponse =  resp.readEntity(String.class);;
					}

					subscriberDataResponse = new AaiResponse<>((new ObjectMapper().readValue(finalResponse, classType)), null, HttpStatus.SC_OK);

				} catch (IOException e) {
					subscriberDataResponse = new AaiResponse<>(null, null, HttpStatus.SC_INTERNAL_SERVER_ERROR);
				}

			}
		}
		return subscriberDataResponse;
	}

	private File getCertificatesFile() {
		if (servletContext != null)
			return new File(servletContext.getRealPath("/WEB-INF/cert/"));
		return null;
	}

	@SuppressWarnings("all")
	protected Response doAaiGet(String certiPath, String uri, boolean xml) {
		String methodName = "getSubscriberList";
		String transId = UUID.randomUUID().toString();
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

		Response resp = null;
		try {

			AAIRestInterface restContrller = new AAIRestInterface(certiPath);
			resp = restContrller.RestGet(fromAppId, transId, uri, xml);

		} catch (WebApplicationException e) {
			final String message = ((BadRequestException) e).getResponse().readEntity(String.class);
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
		} catch (Exception e) {
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
		}

		return resp;
	}

	private String parseForTenantsByServiceSubscription(String resp) {
		String tenantList = "";

		try {
			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(resp);

			return parseServiceSubscriptionObjectForTenants(jsonObject);
		} catch (Exception ex) {

		}

		return tenantList;
	}



	public static String parseServiceSubscriptionObjectForTenants(JSONObject jsonObject) {

		JSONArray tenantArray = new JSONArray();
		boolean bconvert = false;

		try {
			JSONObject relationShipListsObj = (JSONObject) jsonObject.get("relationship-list");
			if (relationShipListsObj != null) {
				JSONArray rShipArray = (JSONArray) relationShipListsObj.get("relationship");
				if (rShipArray != null) {
					Iterator i1 = rShipArray.iterator();

					while (i1.hasNext()) {

						JSONObject inner1Obj = (JSONObject) i1.next();

						if (inner1Obj == null)
							continue;

						String relatedTo = checkForNull((String) inner1Obj.get("related-to"));
						if (relatedTo.equalsIgnoreCase("tenant")) {
							JSONObject tenantNewObj = new JSONObject();

							String relatedLink = checkForNull((String) inner1Obj.get("related-link"));
							tenantNewObj.put("link", relatedLink);

							JSONArray rDataArray = (JSONArray) inner1Obj.get("relationship-data");
							if (rDataArray != null) {
								Iterator i2 = rDataArray.iterator();

								while (i2.hasNext()) {
									JSONObject inner2Obj = (JSONObject) i2.next();

									if (inner2Obj == null)
										continue;

									String rShipKey = checkForNull((String) inner2Obj.get("relationship-key"));
									String rShipVal = checkForNull((String) inner2Obj.get("relationship-value"));
									if (rShipKey.equalsIgnoreCase("cloud-region.cloud-owner")) {
										tenantNewObj.put("cloudOwner", rShipVal);
									} else if (rShipKey.equalsIgnoreCase("cloud-region.cloud-region-id")) {
										tenantNewObj.put("cloudRegionID", rShipVal);
									}

									if (rShipKey.equalsIgnoreCase("tenant.tenant-id")) {
										tenantNewObj.put("tenantID", rShipVal);
									}
								}
							}

							JSONArray relatedTPropArray = (JSONArray) inner1Obj.get("related-to-property");
							if (relatedTPropArray != null) {
								Iterator i3 = relatedTPropArray.iterator();

								while (i3.hasNext()) {
									JSONObject inner3Obj = (JSONObject) i3.next();

									if (inner3Obj == null)
										continue;

									String propKey = checkForNull((String) inner3Obj.get("property-key"));
									String propVal = checkForNull((String) inner3Obj.get("property-value"));
									if (propKey.equalsIgnoreCase("tenant.tenant-name")) {
										tenantNewObj.put("tenantName", propVal);
									}
								}
							}
							bconvert = true;
							tenantArray.add(tenantNewObj);
						}
					}

				}
			}
		} catch (NullPointerException ex) {


		}

		if (bconvert)
			return tenantArray.toJSONString();
		else
			return "";

	}

}