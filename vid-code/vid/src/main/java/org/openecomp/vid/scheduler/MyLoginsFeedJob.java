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




import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;


import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.conf.ExternalAppConfig;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MyLoginsFeedJob extends QuartzJobBean{
	
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MyLoginsFeedJob.class);


    private Connection connection;
   
    private String APP_QUERY ;
	
	private String USER_LOG_QUERY ;
	
	private String ALL_ACCOUNTS_LOG_QUERY ;
	
	private String PROFILE_LOG_QUERY ;
	
	private String USER_PROFILE_LOG_QUERY ;
	
    
    class App {
    	
    	private Integer appId;
    	private Integer motsId;
    	private String mlAppName;
    	
		public Integer getAppId() {
			return appId;
		}
		public void setAppId(Integer appId) {
			this.appId = appId;
		}
		public Integer getMotsId() {
			return motsId;
		}
		public void setMotsId(Integer motsId) {
			this.motsId = motsId;
		}
		public String getMlAppName() {
			return mlAppName;
		}
		public void setMlAppName(String mlAppName) {
			this.mlAppName = mlAppName;
		}
    	
    }
    
    
	
	
	private String getOutputFolder(){
		String outputFolder = SystemProperties.getProperty("my_login_feed_output_dir") +  File.separator;
		
		return outputFolder;
	}
	
	
	public List<App> getApplicationList(Connection conn) {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<App> appList = new ArrayList<App>();
		try{
			APP_QUERY = SystemProperties.getProperty("app.query");
			stmt = conn.prepareStatement(this.APP_QUERY);
			rs = stmt.executeQuery();
			while(rs.next()) {
				
				App app = new App();
				app.setAppId(rs.getInt("APP_ID"));
				app.setMotsId(rs.getInt("MOTS_ID"));
				app.setMlAppName(rs.getString("ML_APP_NAME"));
				appList.add(app);
			}
				
			}
		catch (Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error getting connection. " + e.getMessage()));
		}
		finally{
			try {
				if(rs != null) {
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
			}
			catch (Exception e){
				logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error getting connection. " + e.getMessage()));

			}
		}
		
		return appList;
	}
	

	
	public  void generateLogs(){
		Connection con = null;
		try{
			con = getConnection();
			
			List<App> appList = getApplicationList(con);
			
			for(App app : appList) {
			
			this.generateUserLogs(con, app);
			this.generateProfileLogs(con, app); 
			this.generateUserProfileLogs(con, app);
			this.generateAllAccountsLogs(con, app);
			
			}
			
		} catch (Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error getting connection. " + e.getMessage()));
		}
		finally{
			try {
				if(con != null){
					con.close();
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error closing connection"));

			}
		}
	}
	
	private void generateAllAccountsLogs(Connection con, App app){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			
			ALL_ACCOUNTS_LOG_QUERY = SystemProperties.getProperty("all.accounts.log.query");
			stmt = con.prepareStatement(ALL_ACCOUNTS_LOG_QUERY);
			stmt.setInt(1, app.getAppId());
			rs = stmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			FileOutputStream fos = new java.io.FileOutputStream(this.getOutputFolder()+app.getMlAppName() + "_AllAccounts_" + sdf.format(new Date()) + ".txt");
            BufferedOutputStream bout = new BufferedOutputStream(fos);
            
			String header = "ATTUID,ACCOUNT_STATUS,APPLICATION_USERID,LAST_LOGON_DATE,LAST_PASSWORD_CHANGE_DATE,PROFILE_NAME";
			bout.write(header.getBytes());
			int userCount = 0; //need to include header and footer in the count
			while (rs.next()){
				userCount ++;
				bout.write("\n".getBytes());
				String CUID = rs.getString("CUID") == null ? "" : rs.getString("CUID");
				String activeYN = rs.getString("ACTIVE_YN") == null ? "" : rs.getString("ACTIVE_YN");
				String APPLICATIONUSERID = rs.getString("APPLICATIONUSERID") == null ? "" : rs.getString("APPLICATIONUSERID");
				String LAST_LOGON_DATE = rs.getString("LAST_LOGON_DATE") == null ? "" : rs.getString("LAST_LOGON_DATE");
				String LAST_PASSWORD_CHANGE_DATE = rs.getString("LAST_PASSWORD_CHANGE_DATE") == null ? "" : rs.getString("LAST_PASSWORD_CHANGE_DATE");
				String PROFILE_NAME = rs.getString("PROFILE_NAME") == null ? "" : rs.getString("PROFILE_NAME");

				String dat = 				
						CUID + "," + activeYN + "," + APPLICATIONUSERID  + "," + 
								LAST_LOGON_DATE  + "," + LAST_PASSWORD_CHANGE_DATE  + "," + PROFILE_NAME 
						;
				bout.write(dat.getBytes());
			
			}
			bout.write("\n".getBytes());
			bout.write(("TotalRecords," + userCount + "," + (app.getMotsId() != null ? app.getMotsId():app.getMlAppName())).getBytes());
			bout.close();
            
			
		} catch (Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error While writing user data. " + e.getMessage()));

		}
		finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error closing user rs / stmt"));

			}
		}
		
	}
	
	private void generateUserLogs(Connection con, App app){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			
			USER_LOG_QUERY = SystemProperties.getProperty("user.log.query");
			stmt = con.prepareStatement(USER_LOG_QUERY);
			stmt.setInt(1, app.getAppId());
			rs = stmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			FileOutputStream fos = new java.io.FileOutputStream(this.getOutputFolder()+app.getMlAppName() + "_User_" + sdf.format(new Date()) + ".txt");
            BufferedOutputStream bout = new BufferedOutputStream(fos);
            
			String header = "CUID,AWID,APPLICATIONUSERID,FIRST_NAME,MIDDLE_INITIAL,LAST_NAME,LAST_LOGON_DATE,ACCOUNT_ACTIVATION_DATE,LAST_DATE_ACCOUNT_MODIFIED,LAST_PASSWORD_CHANGE_DATE,FULL_USER_NAME,NT_ID,EMAIL";
			bout.write(header.getBytes());
			int userCount = 2; //need to include header and footer in the count
			while (rs.next()){
				userCount ++;
				bout.write("\n".getBytes());
				String CUID = rs.getString("CUID") == null ? "" : rs.getString("CUID");
				String AWID = rs.getString("AWID") == null ? "" : rs.getString("AWID");
				String APPLICATIONUSERID = rs.getString("APPLICATIONUSERID") == null ? "" : rs.getString("APPLICATIONUSERID");
				String FIRST_NAME = rs.getString("FIRST_NAME") == null ? "" : rs.getString("FIRST_NAME");
				String MIDDLE_INITIAL = rs.getString("MIDDLE_INITIAL") == null ? "" : rs.getString("MIDDLE_INITIAL");
				String LAST_NAME = rs.getString("LAST_NAME") == null ? "" : rs.getString("LAST_NAME");
				String LAST_LOGON_DATE = rs.getString("LAST_LOGON_DATE") == null ? "" : rs.getString("LAST_LOGON_DATE");
				String ACCOUNT_ACTIVATION_DATE = rs.getString("ACCOUNT_ACTIVATION_DATE") == null ? "" : rs.getString("ACCOUNT_ACTIVATION_DATE");
				String LAST_DATE_ACCOUNT_MODIFIED = rs.getString("LAST_DATE_ACCOUNT_MODIFIED") == null ? "" : rs.getString("LAST_DATE_ACCOUNT_MODIFIED");
				String LAST_PASSWORD_CHANGE_DATE = rs.getString("LAST_PASSWORD_CHANGE_DATE") == null ? "" : rs.getString("LAST_PASSWORD_CHANGE_DATE");
				String FULL_USER_NAME = rs.getString("FULL_USER_NAME") == null ? "" : rs.getString("FULL_USER_NAME");
				String NT_ID = rs.getString("NT_ID") == null ? "" : rs.getString("NT_ID");
				String EMAIL = rs.getString("EMAIL") == null ? "" : rs.getString("EMAIL");
				String dat = 				
						CUID + "," + AWID + "," + APPLICATIONUSERID  + "," + 
						FIRST_NAME  + "," + MIDDLE_INITIAL  + "," + LAST_NAME + "," + LAST_LOGON_DATE  + "," +  
						ACCOUNT_ACTIVATION_DATE  + "," + 
						LAST_DATE_ACCOUNT_MODIFIED  + "," + LAST_PASSWORD_CHANGE_DATE  + "," + 
						FULL_USER_NAME + "," +  NT_ID + "," + EMAIL 
						;
				bout.write(dat.getBytes());
			
			}
			bout.write("\n".getBytes());
			bout.write(("TotalRecords," + userCount + ",\"UADM USER DATA STANDARD 2.2\"").getBytes());
			bout.close();
            
			
		} catch (Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error While writing user data. " + e.getMessage()));
		}
		finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error closing user rs / stmt"));
			}
		}
		
	}
	
	private void generateProfileLogs(Connection con, App app){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			
			PROFILE_LOG_QUERY = SystemProperties.getProperty("profile.log.query");
			stmt = con.prepareStatement(PROFILE_LOG_QUERY);
			stmt.setInt(1, app.getAppId());
			rs = stmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			FileOutputStream fos = new java.io.FileOutputStream(this.getOutputFolder()+app.getMlAppName() + "_profile_" + sdf.format(new Date()) + ".txt");
            BufferedOutputStream bout = new BufferedOutputStream(fos);
            
			String header = "PROFILE_NAME,SECURITY_SETTINGS";
			bout.write(header.getBytes());
			int userCount = 2; //need to include header and footer in count= count
			while (rs.next()){
				userCount ++;
				bout.write("\n".getBytes());
				String PROFILE_NAME = rs.getString("PROFILE_NAME") == null ? "" : rs.getString("PROFILE_NAME");
				String SECURITY_SETTINGS = rs.getString("SECURITY_SETTINGS") == null ? "" : rs.getString("SECURITY_SETTINGS");
				String dat = 				
						PROFILE_NAME + "," +  SECURITY_SETTINGS ;
				bout.write(dat.getBytes());
			
			}
			bout.write("\n".getBytes());
			bout.write(("TotalRecords," + userCount + ",\"UADM PROFILE DATA STANDARD 2.2\"").getBytes());
			bout.close();
            
			
		} catch (Exception e){
			System.out.println("UADM Audit Log. "+ new Date().toString() + ". Error While writing profile data. " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error closing profile rs / stmt"));
			}
		}
		
	}
	
	private void generateUserProfileLogs(Connection con, App app){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			USER_PROFILE_LOG_QUERY = SystemProperties.getProperty("user.profile.log.query");
			stmt = con.prepareStatement(USER_PROFILE_LOG_QUERY);
			stmt.setInt(1, app.getAppId());
			rs = stmt.executeQuery();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			FileOutputStream fos = new java.io.FileOutputStream(this.getOutputFolder()+app.getMlAppName() + "_userprofiles_" + sdf.format(new Date()) + ".txt");
            BufferedOutputStream bout = new BufferedOutputStream(fos);
            
			String header = "CUID,AWID,APPLICATIONUSERID,PROFILE_NAME";
			bout.write(header.getBytes());
			int userCount = 2; //need to include header and footer in count= count
			while (rs.next()){
				userCount ++;
				bout.write("\n".getBytes());
				String CUID = rs.getString("CUID") == null ? "" : rs.getString("CUID");
				String AWID = rs.getString("AWID") == null ? "" : rs.getString("AWID");
				String APPLICATIONUSERID = rs.getString("APPLICATIONUSERID") == null ? "" : rs.getString("APPLICATIONUSERID");
				String PROFILE_NAME = rs.getString("PROFILE_NAME") == null ? "" : rs.getString("PROFILE_NAME");
				String dat = 				
					CUID + "," + AWID + "," +  APPLICATIONUSERID + "," + PROFILE_NAME ;
				bout.write(dat.getBytes());
			
			}
			bout.write("\n".getBytes());
			bout.write(("TotalRecords," + userCount + ",\"UADM USER PROFILE DATA STANDARD 2.2\"").getBytes());
			bout.close();
            
			
		} catch (Exception e){
			System.out.println("UADM Audit Log. "+ new Date().toString() + ". Error While writing user profile data. " + e.getMessage());
			e.printStackTrace();
		}
		finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, ("UADM Audit Log. "+ new Date().toString() + ". Error closing user profile rs / stmt"));
			}
		}
		
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		DataSource dataSource = (DataSource)context.getMergedJobDataMap().get("dataSource");
		
		try {
			setConnection(dataSource.getConnection());
		} catch (SQLException e) {
			logger.error(EELFLoggerDelegate.errorLogger, (e.getMessage()));

			return;
		}
		logger.info(EELFLoggerDelegate.errorLogger, (" Generate MyLogins feeds"));

		generateLogs();
		
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}


	
}
