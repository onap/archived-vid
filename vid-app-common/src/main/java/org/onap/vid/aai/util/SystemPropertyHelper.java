package org.onap.vid.aai.util;

import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.exceptions.InvalidPropertyException;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;

public class SystemPropertyHelper {

    public Optional<String> getAAIUseClientCert(){
        return getSystemProperty(AAIProperties.AAI_USE_CLIENT_CERT);
    }

    public Optional<String> getAAIServerUrl(){
        return getSystemProperty(AAIProperties.AAI_SERVER_URL);
    }

    public Optional<String> getAAIVIDUsername(){
        return getSystemProperty(AAIProperties.AAI_VID_USERNAME);
    }

    public Optional<String> getAAIVIDPasswd(){
        return getSystemProperty(AAIProperties.AAI_VID_PASSWD_X);
    }

    public boolean isClientCertEnabled() {
        return getAAIUseClientCert().isPresent() &&
                getAAIUseClientCert().orElseGet(() -> "false").equalsIgnoreCase("true");
    }

    public String getFullServicePath(String path) {
        return getAAIServerUrl().orElse("") + path;
    }

    public String getEncodedCredentials() throws InvalidPropertyException, UnsupportedEncodingException {
        String vidUsername = getAAIVIDUsername().orElseThrow(InvalidPropertyException::new);
        String vidPassword = Password.deobfuscate(getAAIVIDPasswd().orElseThrow(InvalidPropertyException::new));
        return Base64.getEncoder().encodeToString((vidUsername + ":" + vidPassword).getBytes("utf-8"));
    }

    private Optional<String> getSystemProperty(String propertyKey){
        return Optional.ofNullable(SystemProperties.getProperty(propertyKey));
    }
}
