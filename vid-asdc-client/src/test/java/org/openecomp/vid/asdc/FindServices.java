package org.openecomp.vid.asdc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.asdc.beans.tosca.Group;
import org.openecomp.vid.asdc.beans.tosca.NodeTemplate;
import org.openecomp.vid.asdc.beans.tosca.ToscaCsar;
import org.openecomp.vid.asdc.beans.tosca.ToscaModel;
import org.openecomp.vid.asdc.rest.RestfulAsdcClient;

/**
 * The Class FindServices.
 */
public class FindServices {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the URI syntax exception
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public static void main(String[] args) throws IOException, URISyntaxException, AsdcCatalogException {
		
		final InputStream propertiesFile = FindServices.class.getClassLoader().getResourceAsStream("asdc.properties");		
		
		final Properties properties = new Properties();
		properties.load(propertiesFile);
		
		final String protocol = properties.getProperty("protocol", "http");
		
		final Client restClient = ClientBuilder.newClient();
		final URI uri = new URI(protocol + "://" + properties.getProperty("host", "localhost") + ":" + properties.getProperty("port", "80") + "/");
		final String auth = properties.getProperty("auth");
		
		final AsdcClient client = new RestfulAsdcClient.Builder(restClient, uri).auth(auth).build();
		
		try {
			final Collection<Service> services = client.getServices(Collections.singletonMap("distributionStatus", new String[] {"DISTRIBUTED"}));
			
			for (Service service : services) {

					final ToscaCsar toscaCsar;
					
					try {
						toscaCsar = client.getServiceToscaModel(UUID.fromString(service.getUuid()));
					} catch (NullPointerException e) {
						//System.out.println(service.getUuid() + " has a bad tosca metadata entry");
						continue;
					} catch (Throwable t) {
						System.out.println(t.getMessage());
						continue;
					}
					
					System.out.println(service.getUuid() + ", " + service.getName());
					
					if (checkToscaModelForVnf(toscaCsar.getParent())) {
						for (ToscaModel vnfModel : toscaCsar.getChildren()) {
							if (checkToscaModelForVfModule(vnfModel)) {
								System.out.println("******" + service);
							}
						}
					}
			}
		} catch (AsdcCatalogException e) {
			throw e;
		}
	}
	
	/**
	 * Check tosca model for vf module.
	 *
	 * @param model the model
	 * @return true, if successful
	 */
	private static boolean checkToscaModelForVfModule(ToscaModel model) {

		for (Entry<String, Group> component : model.gettopology_template().getGroups().entrySet()) {
			final Group group = component.getValue();
			final String type = group.getType();
			
			if (type.startsWith("com.att.d2.groups.VfModule")) {
				final String rawValue = group.getProperties().get("volume_group");
				if (Boolean.valueOf(rawValue)) {
					return true;
				}
			}
		}
		
		return false;
	}
		
	/**
	 * Check tosca model for vnf.
	 *
	 * @param model the model
	 * @return true, if successful
	 */
	private static boolean checkToscaModelForVnf(ToscaModel model) {

		for (Entry<String, NodeTemplate> component: model.gettopology_template().getnode_templates().entrySet()) {
			final NodeTemplate nodeTemplate = component.getValue();
			final String type = nodeTemplate.getType();
			
			if (type.startsWith("com.att.d2.resource.vf")) {
				return true;
			}
		}
		
		return false;
	}
}
