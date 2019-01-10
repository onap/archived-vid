package org.onap.vid.services;

import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.rest.RequestDetails;

public interface CloudOwnerService {

    void enrichRequestWithCloudOwner(RequestDetails msoRequest);

    void enrichCloudConfigurationWithCloudOwner(CloudConfiguration cloudConfiguration, String lcpCloudRegionId);

}
