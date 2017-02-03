/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.openecomp.aai.util;

import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.openecomp.vid.encryption.EncryptedPropValue;

import org.openecomp.portalsdk.core.util.SystemProperties;

/**
 * The Class HttpsAuthClient.
 */
public class HttpsAuthClient{
	
	/**
	 * Gets the client.
	 *
	 * @param certFilePath the cert file path
	 * @return the client
	 * @throws KeyManagementException the key management exception
	 */
	public static Client getClient(String certFilePath) throws KeyManagementException {
				
		//config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		//config.getClasses().add(org.openecomp.aai.util.CustomJacksonJaxBJsonProvider.class);
		
		try {
			String truststore_path = certFilePath + AAIProperties.FILESEPARTOR + SystemProperties.getProperty(AAIProperties.AAI_TRUSTSTORE_FILENAME);
			String truststore_password = SystemProperties.getProperty(AAIProperties.AAI_TRUSTSTORE_PASSWD_X);
			String decrypted_truststore_password = EncryptedPropValue.decryptTriple(truststore_password);
//			String keystore_path = certFilePath + AAIProperties.FILESEPARTOR + SystemProperties.getProperty(AAIProperties.AAI_KEYSTORE_FILENAME);
//			String keystore_password = SystemProperties.getProperty(AAIProperties.AAI_KEYSTORE_PASSWD_X);
//			String decrypted_keystore_password = EncryptedPropValue.decryptTriple(keystore_password);

			
			
			
		    System.setProperty("javax.net.ssl.trustStore", truststore_path);
		    System.setProperty("javax.net.ssl.trustStorePassword", decrypted_truststore_password);
			HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
			    public boolean verify(String string,SSLSession ssls) {
			        return true;
			    }
			});
		
//			final SSLContext ctx = SSLContext.getInstance("TLS");
//			KeyManagerFactory kmf = null;
//			try {
//				kmf = KeyManagerFactory.getInstance("SunX509");
//				FileInputStream fin = new FileInputStream(keystore_path);
//				KeyStore ks = KeyStore.getInstance("PKCS12");
//				char[] pwd = decrypted_keystore_password.toCharArray();
//				ks.load(fin, pwd);
//				kmf.init(ks, pwd);
//			} catch (Exception e) {
//				System.out.println("Error setting up kmf: exiting");
//				e.printStackTrace();
//				System.exit(1);
//			}
//
//			ctx.init(kmf.getKeyManagers(), null, null);

			return ClientBuilder.newBuilder()
					.hostnameVerifier(new HostnameVerifier() {
						@Override
						public boolean verify( String s, SSLSession sslSession ) {
							return true;
						}
					}).build()
					.register(org.openecomp.aai.util.CustomJacksonJaxBJsonProvider.class);
			
		} catch (Exception e) {
			System.out.println("Error setting up config: exiting");
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}


	
}  
