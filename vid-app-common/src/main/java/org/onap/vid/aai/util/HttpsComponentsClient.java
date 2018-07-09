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

package org.onap.vid.aai.util;

import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.util.SystemProperties;


/**
 * The Class HttpsComponentsClient.
 */
public class HttpsComponentsClient{
	
	/**
	 * Gets the client.
	 *
	 * @param certFilePath the cert file path
	 * @return the client
	 * @throws KeyManagementException the key management exception
	 */
	public static CloseableHttpClient getClient(String certFilePath) throws Exception {
		CloseableHttpClient httpclient = null;
		FileInputStream fin = null;
		FileInputStream fin1 = null;
		try {
			
			String truststore_path = certFilePath + AAIProperties.FILESEPARTOR + SystemProperties.getProperty(AAIProperties.AAI_TRUSTSTORE_FILENAME);
			String truststore_password = SystemProperties.getProperty(AAIProperties.AAI_TRUSTSTORE_PASSWD_X);
			String decrypted_truststore_password = Password.deobfuscate(truststore_password);
			String keystore_path = certFilePath + AAIProperties.FILESEPARTOR + SystemProperties.getProperty(AAIProperties.AAI_KEYSTORE_FILENAME);
			String keystore_password = SystemProperties.getProperty(AAIProperties.AAI_KEYSTORE_PASSWD_X);
			String decrypted_keystore_password = Password.deobfuscate(keystore_password);
			
			SSLContextBuilder sslContextB = new SSLContextBuilder();
			
			KeyStore ks = KeyStore.getInstance("PKCS12");
			fin = new FileInputStream(keystore_path);
			char[] pwd = decrypted_keystore_password.toCharArray();
			ks.load(fin, pwd);
			
			sslContextB.loadKeyMaterial(ks, pwd);
			
			KeyStore ts = KeyStore.getInstance("JKS");
			fin1 = new FileInputStream(truststore_path);
			char[] pwd1 = decrypted_truststore_password.toCharArray();
			ts.load(fin1, pwd1);
			
			sslContextB.loadTrustMaterial(ts);
			sslContextB.loadKeyMaterial(ks, pwd);
			sslContextB.useTLS();
			
			SSLContext sslcontext = sslContextB.build();
			
			SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1.1", "TLSv1.2" },
	                null,
	            	SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
		
			httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslFactory)
	                .build();


		} catch (Exception e) {
			throw e;
		}
		finally {
			fin.close();
			fin1.close();
		}
		return httpclient;
	}


	
}  
