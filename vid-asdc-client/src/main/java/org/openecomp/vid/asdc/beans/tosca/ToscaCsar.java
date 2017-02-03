/*-
 * ============LICENSE_START=======================================================
 * VID ASDC Client
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

package org.openecomp.vid.asdc.beans.tosca;

import java.util.Collection;
import java.util.LinkedList;

/**
 * The Class ToscaCsar.
 */
public class ToscaCsar {

	/** The parent. */
	private final ToscaModel parent;
	
	/** The children. */
	private final Collection<ToscaModel> children;
	
	/**
	 * The Class Builder.
	 */
	public static class Builder {
		
		/** The parent. */
		private final ToscaModel parent;
		
		/** The children. */
		private Collection<ToscaModel> children = new LinkedList<ToscaModel> ();
		
		/**
		 * Instantiates a new builder.
		 *
		 * @param parent the parent
		 */
		public Builder(ToscaModel parent) {
			this.parent = parent;
		}
		
		/**
		 * Adds the vnf.
		 *
		 * @param child the child
		 * @return the builder
		 */
		public Builder addVnf(ToscaModel child) {
			children.add(child);
			return this;
		}
		
		/**
		 * Builds the.
		 *
		 * @return the tosca csar
		 */
		public ToscaCsar build() {
			return new ToscaCsar(this);
		}
	}
	
	/**
	 * Instantiates a new tosca csar.
	 *
	 * @param builder the builder
	 */
	public ToscaCsar(Builder builder) {
		this.parent = builder.parent;
		this.children = builder.children;
	}
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public ToscaModel getParent() { return parent; }
	
	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public Collection<ToscaModel> getChildren() { return children; }
}
