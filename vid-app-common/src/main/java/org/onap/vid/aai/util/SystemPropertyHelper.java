/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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

    public Optional<String> getAAITruststorePasswd(){
        return getSystemProperty(AAIProperties.AAI_TRUSTSTORE_PASSWD_X);
    }

    public Optional<String> getAAITruststoreFilename(){
        return getSystemProperty(AAIProperties.AAI_TRUSTSTORE_FILENAME);
    }

    public Optional<String> getAAIKeystoreFilename(){
        return getSystemProperty(AAIProperties.AAI_KEYSTORE_FILENAME);
    }

    public Optional<String> getAAIKeystorePasswd(){
        return getSystemProperty(AAIProperties.AAI_KEYSTORE_PASSWD_X);
    }

    public boolean isClientCertEnabled() {
        return getAAIUseClientCert().orElse("false").equalsIgnoreCase("true");
    }

    public String getFullServicePath(String path) {
        return getAAIServerUrl().orElse("") + path;
    }

    public String getEncodedCredentials() throws InvalidPropertyException, UnsupportedEncodingException {
        String vidUsername = getAAIVIDUsername().orElseThrow(InvalidPropertyException::new);
        String vidPassword = Password.deobfuscate(getAAIVIDPasswd().orElseThrow(InvalidPropertyException::new));
        return Base64.getEncoder().encodeToString((vidUsername + ":" + vidPassword).getBytes("utf-8"));
    }

    public String getDecryptedTruststorePassword(){
        return Password.deobfuscate(getAAITruststorePasswd().orElse(""));
    }

    public String getDecryptedKeystorePassword(){
        return Password.deobfuscate(getAAIKeystorePasswd().orElse(""));
    }

    private Optional<String> getSystemProperty(String propertyKey){
        return Optional.ofNullable(SystemProperties.getProperty(propertyKey));
    }
}
