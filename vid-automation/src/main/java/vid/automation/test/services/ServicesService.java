package vid.automation.test.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import vid.automation.test.model.Service;
import vid.automation.test.model.ServicesObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by itzikliderman on 08/09/2017.
 */
public class ServicesService {
    private HashMap<String, Service> services;

    public ServicesService() throws IOException {
        services = getServicesFromJson();

    }

    HashMap<String, Service> getServicesFromJson() throws IOException {
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

    public Service getService(String serviceId) {
        return services.get(serviceId);
    }
}
