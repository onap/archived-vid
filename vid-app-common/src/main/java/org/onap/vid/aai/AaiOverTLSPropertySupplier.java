package org.onap.vid.aai;

import java.util.UUID;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.util.AAIProperties;
import org.onap.vid.utils.Logging;

public class AaiOverTLSPropertySupplier {

    public String getUsername() {
        return SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
    }

    public String getPassword() {
        return Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
    }

    public String getRequestId() {
        return Logging.extractOrGenerateRequestId();
    }

    public String getRandomUUID(){
       return UUID.randomUUID().toString();
    }

}
