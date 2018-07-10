package org.onap.vid.asdc.parser;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.beans.tosca.NodeTemplate;
import org.onap.vid.asdc.beans.tosca.ToscaCsar;
import org.onap.vid.asdc.beans.tosca.ToscaMeta;
import org.onap.vid.asdc.beans.tosca.ToscaModel;
import org.onap.vid.model.*;
import org.onap.vid.properties.VidProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipFile;

public class ToscaParserImpl implements ToscaParser {
	/** The Constant LOG. */
	static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(ToscaParserImpl.class);

	private static final String ASDC_MODEL_NAMESPACE = VidProperties.getAsdcModelNamespace();
	private static final String VNF_TAG = ASDC_MODEL_NAMESPACE + ModelConstants.VNF;
	private static final String NETWORK_TAG = ASDC_MODEL_NAMESPACE + ModelConstants.NETWORK;


	@Override
	public ToscaCsar parse(Path path) throws AsdcCatalogException {
		return getToscaCsar(path);
	}

	private ToscaCsar getToscaCsar(final Path csarFile) throws AsdcCatalogException {
		try (final ZipFile csar = new ZipFile(csarFile.toFile())) {

			final InputStream toscaMetaStream = csar.getInputStream(csar.getEntry("TOSCA-Metadata/TOSCA.meta"));
			final ToscaMeta toscaMeta = new ToscaMeta.Builder(toscaMetaStream).build();
			final String entryDefinitions = toscaMeta.get("Entry-Definitions");
			final InputStream toscaParentEntryYamlStream = csar.getInputStream(csar.getEntry(entryDefinitions));

			try {
				return createToscaCsar(csar, toscaParentEntryYamlStream);
			} catch (YAMLException e) {
				throw new AsdcCatalogException("Caught exception while processing TOSCA YAML", e);
			}
		} catch (IOException e) {
			throw new AsdcCatalogException("Caught IOException while processing CSAR", e);
		}
	}

	private ToscaCsar createToscaCsar(ZipFile csar, InputStream toscaParentEntryYamlStream) throws IOException {
		final Yaml yaml = new Yaml();
		final ToscaModel parentModel = yaml.loadAs(toscaParentEntryYamlStream, ToscaModel.class);

		final ToscaCsar.Builder csarBuilder = new ToscaCsar.Builder(parentModel);

		for (Map<String, Map<String, String>> imports : parentModel.getImports()) {
			LOG.debug("imports = " + imports.toString());
			for (Entry<String, Map<String, String>> entry : imports.entrySet()) {
				if (entry.getValue() != null) {
					String fname = entry.getValue().get("file");
					if ((fname != null) && (fname.startsWith("service") || fname.startsWith("resource"))) {
						LOG.debug("fname = " + fname);
						final InputStream toscaChildEntryYamlStream = csar
								.getInputStream(csar.getEntry("Definitions/" + fname));

						final ToscaModel childModel = yaml.loadAs(toscaChildEntryYamlStream, ToscaModel.class);
						csarBuilder.addVnf(childModel);
					}
				}
			}
		}

		return csarBuilder.build();
	}

	public ServiceModel makeServiceModel(String uuid, final Path serviceCsar,Service service ) throws AsdcCatalogException {


		final ServiceModel serviceModel = new ServiceModel();
		ToscaCsar toscaCsar = getToscaCsar(serviceCsar);
		String methodName = "getServices";
		MutableBoolean isNewFlow = new MutableBoolean(false);
		final Map<String, VNF> vnfs = new HashMap<>();
		final Map<String, Network> networks = new HashMap<>();
		final ToscaModel asdcServiceToscaModel = toscaCsar.getParent();
		serviceModel.setService(ServiceModel.extractService(asdcServiceToscaModel, service));



		populateVnfsAndNetwork(methodName, isNewFlow, vnfs, networks, asdcServiceToscaModel, serviceModel);


		// If we see customization uuid under vnf or network, follow 1702 flow
		if (isNewFlow.getValue()) {
			return (getCustomizedServices(asdcServiceToscaModel, serviceModel));
		} else {
			VNF vnf = null;
			for (ToscaModel vnfModel : toscaCsar.getChildren()) {
				// using uuid to match should only be valid for 1610 models
				final String vnfUuid = (vnfModel.getMetadata().getUUID());
				// find the VNF with that uuid, uuid is not the key anymore
				vnf = findVNFAccordingToUUID(vnfs, vnfUuid);
				if (vnf == null) {
					LOG.warn("Couldn't find VNF object " + vnfUuid + ". Problem with Tosca model?");
					continue;
				}
				extractAndUpdateInputs(vnf, vnfModel);
				ServiceModel.extractGroups(vnfModel, serviceModel);
			}

			serviceModel.setVnfs(vnfs);
			serviceModel.setNetworks(networks);
			return serviceModel;
		}
	}

	private VNF findVNFAccordingToUUID(final Map<String, VNF> vnfs,  final String vnfUuid) {
		VNF vnf = null;
		for (Entry<String, VNF> vnfComp : vnfs.entrySet()) {
			if (((vnfComp.getValue().getUuid()).equalsIgnoreCase(vnfUuid))) {
				// found the vnf
				vnf = vnfComp.getValue();
			}
		}
		return vnf;
	}

	private void extractAndUpdateInputs(VNF vnf, ToscaModel vnfModel) {
		vnf.setInputs(vnfModel.gettopology_template().getInputs());
	}

	private static void populateVnfsAndNetwork(String methodName, MutableBoolean isNewFlow, final Map<String, VNF> vnfs,
											   final Map<String, Network> networks, final ToscaModel asdcServiceToscaModel, ServiceModel serviceModel) {
		for (Entry<String, NodeTemplate> component : extractNodeTemplates(asdcServiceToscaModel)) {
			final String modelCustomizationName = component.getKey();
			LOG.debug(EELFLoggerDelegate.debugLogger, methodName
					+ " model customization name: " + modelCustomizationName);
			final NodeTemplate nodeTemplate = component.getValue();
			final String type = nodeTemplate.getType();

			if (type.startsWith(VNF_TAG)) {
				LOG.debug(EELFLoggerDelegate.debugLogger,
							methodName + " found node template type: " + type);
				final VNF vnf = new VNF();
				vnf.extractVnf(modelCustomizationName, nodeTemplate);
				LOG.debug(EELFLoggerDelegate.debugLogger,
						 	methodName + " VNF commands: " + vnf.getCommands());
				vnfs.put(modelCustomizationName, vnf);
				isNewFlow.setValue(isNewFlow(vnf));
			}
			// Networks
			if (type.startsWith(NETWORK_TAG)) {
				LOG.debug(EELFLoggerDelegate.debugLogger,
						 methodName + " found node template type: " + type);
				final Network network = new Network();
				network.extractNetwork(modelCustomizationName, nodeTemplate);
				isNewFlow.setValue(isNewFlow(network));
				networks.put(modelCustomizationName, network);

			}
		}
		serviceModel.setVnfs(vnfs);
		serviceModel.setNetworks(networks);

	}

	private static Set<Entry<String, NodeTemplate>> extractNodeTemplates(final ToscaModel asdcServiceToscaModel) {
		return asdcServiceToscaModel.gettopology_template().getnode_templates().entrySet();
	}

	private static boolean isNewFlow(Node node) {
		return (node.getCustomizationUuid() != null) && (node.getCustomizationUuid().length() > 0);
	}

	private ServiceModel getCustomizedServices(ToscaModel asdcServiceToscaModel, ServiceModel serviceModel) {
		String methodName = "asdcServiceToscaModel";

		// asdcServiceToscaModel should have vf modules and vol groups populated
		// at this point but
		// they are not associated with the VNFs
		ServiceModel.extractGroups(asdcServiceToscaModel,serviceModel);
		// Now put the vf modules and volume groups under the VNF they belong
		// too
		serviceModel.associateGroups();
		return (serviceModel);
	}

}