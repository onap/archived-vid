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

package org.onap.vid.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;


public class FnAppDoaImpl {

	/** The logger. */
	static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnAppDoaImpl.class);
		
		public int getProfileCount(String driver, String URL, String username, String password) {
			Connection dbc = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			String q = null;
			int count = 0;
			try {
				 	dbc = getConnection(driver,URL,username,password);
				   logger.debug(EELFLoggerDelegate.debugLogger, "getConnection:::"+ dbc);
				q = "select count(*) from fn_app";
					pst = dbc.prepareStatement(q);
					rs = pst.executeQuery();
					
					if (rs.next())
						count = rs.getInt(1);
			} catch(Exception ex) {
				logger.error(EELFLoggerDelegate.errorLogger, "Failed to perform health check", ex);
			} finally {
				cleanup(rs,pst,dbc);
			}
			logger.debug(EELFLoggerDelegate.debugLogger, "count:::"+ count);
			return count;
		}

		public static Connection getConnection(String driver2, String url, String username, String password) throws IOException, SQLException, ClassNotFoundException{
			java.sql.Connection con=null;
		
			if( url!=null && username!=null && password!=null ){
			    con = DriverManager.getConnection(url, username, password);
			}
			
			   System.out.println("Connection Successful");		    	
			return con;
			
		}
		
		public static void cleanup(ResultSet rs, PreparedStatement st, Connection c) {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					if (logger != null)
						logger.error("Error when trying to close result set", e);
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e) {
					if (logger != null)
						logger.error("Error when trying to close statement", e);
				}
			}
			if (c != null) {
				try {
					c.rollback();
				} catch (Exception e) {
					if (logger != null)
						logger.error("Error when trying to rollback connection", e);
				}
				try {
					c.close();
				} catch (Exception e) {
					if (logger != null)
						logger.error("Error when trying to close connection", e);
				}
			}
		}
}
