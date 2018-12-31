package org.onap.vid.job.command;

import org.apache.commons.lang3.StringUtils;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.services.VidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandUtils {

    private final VidService vidService;

    @Autowired
    public CommandUtils(VidService vidService) {
        this.vidService = vidService;
    }

    public boolean isVfModuleBaseModule(String serviceModelUuid, String vfModuleModelUUID) throws AsdcCatalogException{
        ServiceModel serviceModel =  vidService.getService(serviceModelUuid);

        if (serviceModel==null) {
            throw new AsdcCatalogException("Failed to retrieve model with uuid "+serviceModelUuid +" from SDC");
        }

        if (serviceModel.getVfModules() == null) {
            throw createAsdcCatalogVfModuleModelUUIDNotFoundException(serviceModelUuid, vfModuleModelUUID);
        }

        return serviceModel.getVfModules()
                .values()
                .stream()
                .filter(vfModule -> StringUtils.equals(vfModule.getUuid(), vfModuleModelUUID))
                .findFirst()
                .orElseThrow(() -> createAsdcCatalogVfModuleModelUUIDNotFoundException(serviceModelUuid, vfModuleModelUUID))
                .getProperties()
                .getBaseModule();
    }

    private AsdcCatalogException createAsdcCatalogVfModuleModelUUIDNotFoundException(String serviceModelUuid, String vfModuleModelUUID) {
        return new AsdcCatalogException("Failed to find vfModuleModelUUID: " + vfModuleModelUUID +
                "in model with uuid: " + serviceModelUuid);
    }

}
