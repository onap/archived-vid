/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.asdc.beans.tosca;

import org.onap.vid.asdc.AsdcCatalogException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class ToscaMeta.
 */
public class ToscaMeta {

	/** The metadata. */
	private final Map<String, String> metadata;
	
	/**
	 * Instantiates a new tosca meta.
	 *
	 * @param builder the builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	private ToscaMeta(Builder builder) throws IOException, AsdcCatalogException {
		metadata = new HashMap<> ();
		
		read(builder.inputStream);
	}
	
	/**
	 * The Class Builder.
	 */
	public static class Builder {
		
		/** The input stream. */
		private final InputStream inputStream;
		
		/**
		 * Instantiates a new builder.
		 *
		 * @param inputStream the input stream
		 */
		public Builder(InputStream inputStream) {
			this.inputStream = inputStream;
		}
		
		/**
		 * Builds the.
		 *
		 * @return the tosca meta
		 * @throws IOException Signals that an I/O exception has occurred.
		 * @throws AsdcCatalogException the asdc catalog exception
		 */
		public ToscaMeta build() throws IOException, AsdcCatalogException {
			return new ToscaMeta(this);
		}
	}
	
	/**
	 * Gets the.
	 *
	 * @param property the property
	 * @return the string
	 */
	public String get(String property) {
		return metadata.get(property);
	}
	
	/**
	 * Read.
	 *
	 * @param inputStream the input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	private void read(InputStream inputStream) throws IOException, AsdcCatalogException {
		
		final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		
		String line;
		
		try {
			while ((line = br.readLine()) != null) {
				if ( line.length() > 0 ) {
					final String[] entry = line.split(":");
					
					if (entry.length != 2) 
                        throw new AsdcCatalogException("TOSCA.meta file cannot be parsed (more than 1 colon found on a single line");
					if (!entry[1].startsWith(" "))
                        throw new AsdcCatalogException("TOSCA.meta file cannot be parsed (: not immediately followed by ' ')");
					
					metadata.put(entry[0], entry[1].substring(1));
				}
			}
		} catch (IOException | AsdcCatalogException e) {
			metadata.clear();
			throw e;
		}
	}
}
