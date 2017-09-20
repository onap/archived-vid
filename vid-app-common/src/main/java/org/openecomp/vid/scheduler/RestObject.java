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

package org.openecomp.vid.scheduler;


public class RestObject<T> {
	
    private T t;
   
    private int statusCode= 0;
    
    public String uuid;
    
    public void set(T t) { this.t = t; }
    
    public T get() { return t; }
	   
    public void setStatusCode(int v) { this.statusCode = v; }
       
    public int getStatusCode() { return this.statusCode; }
        
    public void setUUID(String uuid) { this.uuid = uuid; }
    
    public String getUUID() { return this.uuid; }
}

