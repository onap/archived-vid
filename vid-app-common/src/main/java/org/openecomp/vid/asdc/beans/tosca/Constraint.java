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
package org.openecomp.vid.asdc.beans.tosca;
import java.util.List;
import java.util.ArrayList;

/**
 * The Class Constraint.
 */

public class Constraint {
	private  List<Object> valid_values;  
	private Object equal;
	private Object greater_than;
	private Object greater_or_equal;
	private Object less_than;
	private Object less_or_equal;
	private List<Object> in_range;
	private Object length;
	private Object min_length;
	private Object max_length;
	
	/**
	 * Instantiates a new Constraint.
	 */
	public Constraint() {
		valid_values = new ArrayList<Object>();
		in_range = new ArrayList<Object>();
	}
	
	/**
	 * Gets the valid_values.
	 *
	 * @return the valid_values
	 */
	public List<Object> getvalid_values() {
		return valid_values;
	}
	/**
	 * Gets equal.
	 *
	 * @return equal
	 */
	public Object getEqual() {
		return equal;
	}
	/**
	 * Gets greater_than.
	 *
	 * @return greater_than
	 */
	public Object getGreater_than() {
		return greater_than;
	}
	/**
	 * Gets greater_or_equal.
	 *
	 * @return greater_or_equal
	 */
	public Object getGreater_or_equal() {
		return greater_or_equal;
	}
	/**
	 * Gets less_than.
	 *
	 * @return less_than
	 */
	public Object getLess_than() {
		return less_than;
	}
	/**
	 * Gets less_or_equal.
	 *
	 * @return less_or_equal
	 */
	public Object getLess_or_equal() {
		return less_or_equal;
	}
	/**
	 * Gets in_range.
	 *
	 * @return in_range
	 */
	public List<Object> getIn_range() {
		return in_range;
	}
	/**
	 * Gets length.
	 *
	 * @return length
	 */
	public Object getLength() {
		return length;
	}
	/**
	 * Gets min_length.
	 *
	 * @return min_length
	 */
	public Object getMin_length() {
		return min_length;
	}
	/**
	 * Gets max_length.
	 *
	 * @return max_length
	 */
	public Object getMax_length() {
		return max_length;
	}
	/**
	 * Sets the valid_values.
	 *
	 * @param op the new valid_values
	 */
	public void setvalid_values(List<Object> vlist) {
		this.valid_values = vlist;
	}
	/**
	 * Sets equal.
	 *
	 * @param e the new equal
	 */
	public void setEqual(Object e) {
		this.equal = e;
	}
	/**
	 * Sets greater_than.
	 *
	 * @param e the new greater_than
	 */
	public void setGreater_than(Object e) {
		this.greater_than = e;
	}
	/**
	 * Sets less_than.
	 *
	 * @param e the new less_than
	 */
	public void setLess_than(Object e) {
		this.less_than = e;
	}
	/**
	 * Sets in_range.
	 *
	 * @param e the new in_range
	 */
	public void setIn_range(List<Object> e) {
		this.in_range = e;
	}
	/**
	 * Sets length.
	 *
	 * @param e the length
	 */
	public void setLength(Object e) {
		this.length = e;
	}
	/**
	 * Sets min_length.
	 *
	 * @param e the min_length
	 */
	public void setMin_length(Object e) {
		this.min_length = e;
	}
	/**
	 * Sets max_length.
	 *
	 * @param e the max_length
	 */
	public void setMax_length(Object e) {
		this.max_length = e;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "valid_values=" + valid_values;
	}
}
