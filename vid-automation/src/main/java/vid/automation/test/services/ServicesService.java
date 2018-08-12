package vid.automation.test.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import vid.automation.test.model.Service;
import vid.automation.test.model.ServiceModel;
import vid.automation.test.model.ServiceModelsList;
import vid.automation.test.model.ServicesObject;
import vid.automation.test.utils.ReadFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by itzikliderman on 08/09/2017.
 */
public class ServicesService {
    private HashMap<String, Service> services;
    private HashMap<String, ServiceModel> serviceModels;

    public ServicesService() {
        services = getServicesFromJson();
        serviceModels = getServiceInstancesFromJson();
    }

    HashMap<String, Service> getServicesFromJson() {
        String fileName = "services";
        ObjectMapper mapper = new ObjectMapper();
        ServicesObject servicesObject;
        try {
            File servicesFile = FileHandling.getConfigFile(fileName);
            if(!servicesFile.exists()) {
                String basePath = System.getProperty("BASE_PATH");
                servicesFile = new File( basePath + File.separator + "conf" + File.separator + fileName);
            }
            servicesObject = mapper.readValue(servicesFile, ServicesObject.class);
            return servicesObject.services;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    HashMap<String, ServiceModel> getServiceInstancesFromJson() {
        return ReadFile.getJsonFile("serviceModels", ServiceModelsList.class).serviceModels;
    }

    public Service getService(String serviceId) {
        return services.get(serviceId);
    }

    public ServiceModel getServiceModel(String modelUUID) {
        return (ServiceModel) serviceModels.get(modelUUID);
    }
}
