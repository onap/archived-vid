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
USE vid_openecomp_epsdk;

DELETE FROM `fn_datasource`;
TRUNCATE `fn_menu`;
DELETE FROM `fn_role_function`;
DELETE FROM `fn_restricted_url`;
DELETE FROM `fn_tab_selected`;
DELETE FROM `fn_tab`;
DELETE FROM `fn_function`;
DELETE FROM `fn_lu_alert_method`;
DELETE FROM `fn_lu_activity`;
DELETE FROM `fn_lu_menu_set`;
DELETE FROM `fn_lu_priority`;
DELETE FROM `fn_lu_tab_set`;

--
-- Dumping data for table `cr_raptor_action_img`
--
REPLACE INTO `cr_raptor_action_img` VALUES ('CALENDAR','/static/fusion/raptor/img/Calendar-16x16.png');
REPLACE INTO `cr_raptor_action_img` VALUES ('DELETE','/static/fusion/raptor/img/deleteicon.gif');

--
-- Dumping data for table `fn_app`
--
INSERT IGNORE INTO `fn_app` VALUES (1,'Default',null,'Some Default Description','Some Default Note',null,null,null,'ECPP','?',1,'okYTaDrhzibcbGVq5mjkVQ==','N','N',null,'Default',null,null,'ECOMP-PORTAL-INBOX');



--
-- Dumping data for table `fn_function`
--
INSERT INTO `fn_function` VALUES ('1','test role function') ON DUPLICATE KEY UPDATE FUNCTION_NAME='test role function';
INSERT INTO `fn_function` VALUES ('doclib','Document Library') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Document Library';
INSERT INTO `fn_function` VALUES ('doclib_admin','Document Library Admin') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Document Library Admin';
INSERT INTO `fn_function` VALUES ('login','Login') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Login';
INSERT INTO `fn_function` VALUES ('menu_admin','Admin Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Admin Menu';
INSERT INTO `fn_function` VALUES ('menu_ajax','Ajax Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Ajax Menu';
INSERT INTO `fn_function` VALUES ('menu_servicemodels','Browse SDC Service Instances') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Browse SDC Service Instances';
INSERT INTO `fn_function` VALUES ('menu_concept','CoNCEPT') ON DUPLICATE KEY UPDATE FUNCTION_NAME='CoNCEPT';
INSERT INTO `fn_function` VALUES ('menu_customer','Customer Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Customer Menu';
INSERT INTO `fn_function` VALUES ('menu_customer_create','Customer Create') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Customer Create';
INSERT INTO `fn_function` VALUES ('menu_doclib','Document Library Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Document Library Menu';
INSERT INTO `fn_function` VALUES ('menu_feedback','Feedback Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Feedback Menu';
INSERT INTO `fn_function` VALUES ('menu_help','Help Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Help Menu';
INSERT INTO `fn_function` VALUES ('menu_home','Home Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Home Menu';
INSERT INTO `fn_function` VALUES ('menu_itracker','iTracker Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='iTracker Menu';
INSERT INTO `fn_function` VALUES ('menu_itracker_admin','Itracker Admin/Support menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Itracker Admin/Support menu';
INSERT INTO `fn_function` VALUES ('menu_job','Job Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Job Menu';
INSERT INTO `fn_function` VALUES ('menu_job_create','Job Create') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Job Create';
INSERT INTO `fn_function` VALUES ('menu_job_designer','Process in Designer view') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Process in Designer view';
INSERT INTO `fn_function` VALUES ('menu_logout','Logout Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Logout Menu';
INSERT INTO `fn_function` VALUES ('menu_map','Map Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Map Menu';
INSERT INTO `fn_function` VALUES ('menu_newserinstance','Create New Service Instance') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Create New Service Instance';
INSERT INTO `fn_function` VALUES ('menu_notes','Notes Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Notes Menu';
INSERT INTO `fn_function` VALUES ('menu_process','Process List') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Process List';
INSERT INTO `fn_function` VALUES ('menu_profile','Profile Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Profile Menu';
INSERT INTO `fn_function` VALUES ('menu_profile_create','Profile Create') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Profile Create';
INSERT INTO `fn_function` VALUES ('menu_profile_import','Profile Import') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Profile Import';
INSERT INTO `fn_function` VALUES ('menu_reports','Reports Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Reports Menu';
INSERT INTO `fn_function` VALUES ('menu_sample','Sample Pages Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Sample Pages Menu';
INSERT INTO `fn_function` VALUES ('menu_tab','Sample Tab Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Sample Tab Menu';
INSERT INTO `fn_function` VALUES ('menu_task','Task Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Task Menu';
INSERT INTO `fn_function` VALUES ('menu_task_search','Task Search') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Task Search';
INSERT INTO `fn_function` VALUES ('menu_test','Test Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Test Menu';
INSERT INTO `fn_function` VALUES ('menu_viewlog','Log Menu') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Log Menu';
INSERT INTO `fn_function` VALUES ('view_reports','View Raptor reports') ON DUPLICATE KEY UPDATE FUNCTION_NAME='View Raptor reports';
INSERT INTO `fn_function` VALUES ('menu_searchexisting', 'Search for Existing Service Instances') ON DUPLICATE KEY UPDATE FUNCTION_NAME='Search for Existing Service Instances';

--
-- Dumping data for table `fn_lu_activity`
--
REPLACE INTO `fn_lu_activity` VALUES ('add_child_role','add_child_role');
REPLACE INTO `fn_lu_activity` VALUES ('add_role','add_role');
REPLACE INTO `fn_lu_activity` VALUES ('add_role_function','add_role_function');
REPLACE INTO `fn_lu_activity` VALUES ('add_user_role','add_user_role');
REPLACE INTO `fn_lu_activity` VALUES ('login','Login');
REPLACE INTO `fn_lu_activity` VALUES ('logout','Logout');
REPLACE INTO `fn_lu_activity` VALUES ('mobile_login','Mobile Login');
REPLACE INTO `fn_lu_activity` VALUES ('mobile_logout','Mobile Logout');
REPLACE INTO `fn_lu_activity` VALUES ('remove_child_role','remove_child_role');
REPLACE INTO `fn_lu_activity` VALUES ('remove_role','remove_role');
REPLACE INTO `fn_lu_activity` VALUES ('remove_role_function','remove_role_function');
REPLACE INTO `fn_lu_activity` VALUES ('remove_user_role','remove_user_role');

--
-- Dumping data for table `fn_lu_alert_method`
--
REPLACE INTO `fn_lu_alert_method` VALUES ('EMAIL','Email');
REPLACE INTO `fn_lu_alert_method` VALUES ('FAX','Fax');
REPLACE INTO `fn_lu_alert_method` VALUES ('PAGER','Pager');
REPLACE INTO `fn_lu_alert_method` VALUES ('PHONE','Phone');
REPLACE INTO `fn_lu_alert_method` VALUES ('SMS','SMS');

--
-- Dumping data for table `fn_lu_menu_set`
--
INSERT INTO `fn_lu_menu_set` VALUES ('APP','Application Menu') ON DUPLICATE KEY UPDATE MENU_SET_NAME='Application Menu';

--
-- Dumping data for table `fn_lu_priority`
--
REPLACE INTO `fn_lu_priority` VALUES (10,'Low','Y',10);
REPLACE INTO `fn_lu_priority` VALUES (20,'Normal','Y',20);
REPLACE INTO `fn_lu_priority` VALUES (30,'High','Y',30);
REPLACE INTO `fn_lu_priority` VALUES (40,'Urgent','Y',40);
REPLACE INTO `fn_lu_priority` VALUES (50,'Fatal','Y',50);


--
-- Dumping data for table `fn_lu_tab_set`
--
INSERT INTO `fn_lu_tab_set` VALUES ('APP','Application Tabs') ON DUPLICATE KEY UPDATE TAB_SET_NAME='Application Tabs';

--
-- Dumping data for table `fn_lu_timezone`
--
INSERT INTO `fn_lu_timezone` VALUES (10,'US/Eastern','US/Eastern') ON DUPLICATE KEY UPDATE TIMEZONE_NAME='US/Eastern', TIMEZONE_VALUE='US/Eastern';
INSERT INTO `fn_lu_timezone` VALUES (20,'US/Central','US/Central') ON DUPLICATE KEY UPDATE TIMEZONE_NAME='US/Central', TIMEZONE_VALUE='US/Central';
INSERT INTO `fn_lu_timezone` VALUES (30,'US/Mountain','US/Mountain') ON DUPLICATE KEY UPDATE TIMEZONE_NAME='US/Mountain', TIMEZONE_VALUE='US/Mountain';
INSERT INTO `fn_lu_timezone` VALUES (40,'US/Arizona','America/Phoenix') ON DUPLICATE KEY UPDATE TIMEZONE_NAME='US/Arizona', TIMEZONE_VALUE='America/Phoenix';
INSERT INTO `fn_lu_timezone` VALUES (50,'US/Pacific','US/Pacific') ON DUPLICATE KEY UPDATE TIMEZONE_NAME='US/Pacific', TIMEZONE_VALUE='US/Pacific';
INSERT INTO `fn_lu_timezone` VALUES (60,'US/Alaska','US/Alaska') ON DUPLICATE KEY UPDATE TIMEZONE_NAME='US/Alaska', TIMEZONE_VALUE='US/Alaska';
INSERT INTO `fn_lu_timezone` VALUES (70,'US/Hawaii','US/Hawaii') ON DUPLICATE KEY UPDATE TIMEZONE_NAME='US/Hawaii', TIMEZONE_VALUE='US/Hawaii';

--
-- Dumping data for table `fn_menu`
--
INSERT INTO `fn_menu` VALUES (1,'Root',NULL,10,NULL,'menu_home','N',NULL,NULL,NULL,NULL,'APP','N',NULL) ON DUPLICATE KEY UPDATE LABEL='Root', PARENT_ID=NULL, SORT_ORDER=10, ACTION=NULL, FUNCTION_CD='menu_home', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC=NULL;
INSERT INTO `fn_menu` VALUES (2,'VID Home',1,10,'welcome.htm','menu_home','Y',NULL,NULL,NULL,NULL,'APP','N','ion-home') ON DUPLICATE KEY UPDATE LABEL='VID Home', PARENT_ID=1, SORT_ORDER=10, ACTION='welcome.htm', FUNCTION_CD='menu_home', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='ion-home';
INSERT INTO `fn_menu` VALUES (3,'Search for Existing Service Instances',1,10,'serviceModels.htm#/instances/services','menu_searchexisting','Y',NULL,NULL,NULL,NULL,'APP','N','ion-android-search') ON DUPLICATE KEY UPDATE LABEL='Search for Existing Service Instances', PARENT_ID=1, SORT_ORDER=10, ACTION='searchexistingsi.htm', FUNCTION_CD='menu_searchexisting', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='ion-android-search';INSERT INTO `fn_menu` VALUES (8,'Reports',1,40,'report.htm','menu_reports','N',NULL,NULL,NULL,NULL,'APP','N','ion-ios-paper') ON DUPLICATE KEY UPDATE LABEL='Reports', PARENT_ID=1, SORT_ORDER=40, ACTION='report.htm', FUNCTION_CD='menu_reports', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='ion-ios-paper';
INSERT INTO `fn_menu` VALUES (9,'Profile',1,90,'userProfile','menu_profile','N',NULL,NULL,NULL,NULL,'APP','N','ion-person') ON DUPLICATE KEY UPDATE LABEL='Profile', PARENT_ID=1, SORT_ORDER=90, ACTION='userProfile', FUNCTION_CD='menu_profile', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='ion-person';
INSERT INTO `fn_menu` VALUES (10,'Admin',1,110,'role_list.htm','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','ion-gear-a') ON DUPLICATE KEY UPDATE LABEL='Admin', PARENT_ID=1, SORT_ORDER=110, ACTION='role_list.htm', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='ion-gear-a';
INSERT INTO `fn_menu` VALUES (13,'VID Logout',1,130,'app_logout.htm','menu_logout','N',NULL,NULL,NULL,NULL,'APP','N','ion-android-exit') ON DUPLICATE KEY UPDATE LABEL='VID Logout', PARENT_ID=1, SORT_ORDER=130, ACTION='app_logout.htm', FUNCTION_CD='menu_logout', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='Y', IMAGE_SRC='ion-android-exit';
INSERT INTO `fn_menu` VALUES (42,'Browse SDC Service Models',1,10,'serviceModels.htm','menu_servicemodels','Y',NULL,NULL,NULL,NULL,'APP','N','ion-android-navigate') ON DUPLICATE KEY UPDATE LABEL='Browse SDC Service Instances', PARENT_ID=1, SORT_ORDER=10, ACTION='serviceModels.htm', FUNCTION_CD='menu_servicemodels', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='ion-android-navigate';INSERT INTO `fn_menu` VALUES (43,'View Log',1,10,'viewlog.htm','menu_viewlog','N',NULL,NULL,NULL,NULL,'APP','N','icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='View Log', PARENT_ID=1, SORT_ORDER=10, ACTION='viewlog.htm', FUNCTION_CD='menu_viewlog', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
INSERT INTO `fn_menu` VALUES (84,'All Reports',8,50,'report','menu_reports','N',null,null,null,null,'APP','N','/static/fusion/images/reports.png') ON DUPLICATE KEY UPDATE LABEL='All Reports', PARENT_ID=8, SORT_ORDER=50, ACTION='report', FUNCTION_CD='menu_reports', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/reports.png';
INSERT INTO `fn_menu` VALUES (87,'Create Reports',8,120,'report_wizard.htm?r_action=report.create','menu_reports','N',null,'r_action=report.create',null,null,'APP','N',null) ON DUPLICATE KEY UPDATE LABEL='Create Reports', PARENT_ID=8, SORT_ORDER=120, ACTION='report_wizard.htm?r_action=report.create', FUNCTION_CD='menu_reports', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING='r_action=report.create', EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC=NULL;
INSERT INTO `fn_menu` VALUES (88,'Sample Dashboard',8,130,'report_sample','menu_reports','N',null,null,null,null,'APP','N',null) ON DUPLICATE KEY UPDATE LABEL='Sample Dashboard', PARENT_ID=8, SORT_ORDER=130, ACTION='report_sample', FUNCTION_CD='menu_reports', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC=NULL;
INSERT INTO `fn_menu` VALUES (92,'Import User',9,30,'userProfile#/post_search','menu_profile_import','Y',null,null,null,null,'APP','N',NULL) ON DUPLICATE KEY UPDATE LABEL='Import User', PARENT_ID=9, SORT_ORDER=30, ACTION='userProfile#/post_search', FUNCTION_CD='menu_profile_import', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC=NULL;
INSERT INTO `fn_menu` VALUES (94,'Self',9,40,'userProfile#/self_profile','menu_profile','Y',null,null,null,null,'APP','N','/static/fusion/images/profile.png') ON DUPLICATE KEY UPDATE LABEL='Self', PARENT_ID=9, SORT_ORDER=40, ACTION='userProfile#/self_profile', FUNCTION_CD='menu_profile', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/profile.png';
INSERT INTO `fn_menu` VALUES (101,'Roles',10,20,'admin','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/users.png') ON DUPLICATE KEY UPDATE LABEL='Roles', PARENT_ID=10, SORT_ORDER=20, ACTION='admin', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/users.png';
INSERT INTO `fn_menu` VALUES (102,'Role Functions',10,30,'admin#/role_function_list','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N',NULL) ON DUPLICATE KEY UPDATE LABEL='Role Functions', PARENT_ID=10, SORT_ORDER=30, ACTION='admin#/role_function_list', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC=NULL;
INSERT INTO `fn_menu` VALUES (103,'Broadcast Messages',10,50,'admin#/broadcast_list','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/bubble.png') ON DUPLICATE KEY UPDATE LABEL='Broadcast Messages', PARENT_ID=10, SORT_ORDER=50, ACTION='admin#/broadcast_list', FUNCTION_CD='menu_admin', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/bubble.png';
INSERT INTO `fn_menu` VALUES (105,'Cache Admin',10,40,'admin#/jcs_admin','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/cache.png') ON DUPLICATE KEY UPDATE LABEL='Cache Admin', PARENT_ID=10, SORT_ORDER=40, ACTION='admin#/jcs_admin', FUNCTION_CD='menu_admin', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/cache.png';
-- INSERT INTO `fn_menu` VALUES (106,'Lock/Unlock Application',10,60,'application_lockout.htm','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/decrypted.png') ON DUPLICATE KEY UPDATE LABEL='Lock/Unlock Application', PARENT_ID=10, SORT_ORDER=60, ACTION='application_lockout.htm', FUNCTION_CD='menu_admin', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/decrypted.png';
INSERT INTO `fn_menu` VALUES (108,'Usage',10,80,'admin#/usage_list','menu_admin','Y',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/users.png') ON DUPLICATE KEY UPDATE LABEL='Usage', PARENT_ID=10, SORT_ORDER=80, ACTION='admin#/usage_list', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/users.png';
INSERT INTO `fn_menu` VALUES (930,'Search',9,15,'userProfile','menu_admin','Y',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/search_profile.png') ON DUPLICATE KEY UPDATE LABEL='Search', PARENT_ID=9, SORT_ORDER=15, ACTION='userProfile', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/search_profile.png';
INSERT INTO `fn_menu` VALUES (41,'Create New Service Instance',1,10,'serviceModels.htm#/instances/subscribers','menu_newserinstance','Y',NULL,NULL,NULL,NULL,'APP','N','icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='Create New Service Instance', PARENT_ID=1, SORT_ORDER=10, ACTION='searchexistingsi.htm', FUNCTION_CD='menu_newserinstance', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';

--
-- Dumping data for table `fn_restricted_url`
--
REPLACE INTO `fn_restricted_url` VALUES ('attachment.htm','menu_admin');
REPLACE INTO `fn_restricted_url` VALUES ('broadcast.htm','menu_admin');
REPLACE INTO `fn_restricted_url` VALUES ('file_upload.htm','menu_admin');
REPLACE INTO `fn_restricted_url` VALUES ('job.htm','menu_admin');
REPLACE INTO `fn_restricted_url` VALUES ('role.htm','menu_admin');
REPLACE INTO `fn_restricted_url` VALUES ('role_function.htm','menu_admin');
REPLACE INTO `fn_restricted_url` VALUES ('test.htm','menu_admin');
REPLACE INTO `fn_restricted_url` VALUES ('serviceModels.htm','menu_servicemodels');
REPLACE INTO `fn_restricted_url` VALUES ('async_test.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('chatWindow.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('contact_list.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('customer_dynamic_list.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('event.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('event_list.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('mobile_welcome.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('sample_map.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('template.jsp','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('welcome.htm','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('zkau','menu_home');
REPLACE INTO `fn_restricted_url` VALUES ('itracker_assign.htm','menu_itracker');
REPLACE INTO `fn_restricted_url` VALUES ('itracker_byassignee.htm','menu_itracker');
REPLACE INTO `fn_restricted_url` VALUES ('itracker_create.htm','menu_itracker');
REPLACE INTO `fn_restricted_url` VALUES ('itracker_update.htm','menu_itracker');
REPLACE INTO `fn_restricted_url` VALUES ('manage_license.htm','menu_itracker');
REPLACE INTO `fn_restricted_url` VALUES ('support_ticket.htm','menu_itracker');
REPLACE INTO `fn_restricted_url` VALUES ('jbpm_designer.htm','menu_job_create');
REPLACE INTO `fn_restricted_url` VALUES ('jbpm_drools.htm','menu_job_create');
REPLACE INTO `fn_restricted_url` VALUES ('process_job.htm','menu_job_create');
-- REPLACE INTO `fn_restricted_url` VALUES ('novamap_controller.htm','menu_map');
REPLACE INTO `fn_restricted_url` VALUES ('createnewserviceinstance.htm','menu_newserinstance');
REPLACE INTO `fn_restricted_url` VALUES ('profile.htm','menu_profile_create');
REPLACE INTO `fn_restricted_url` VALUES ('raptor.htm','menu_reports');
REPLACE INTO `fn_restricted_url` VALUES ('raptor2.htm','menu_reports');
REPLACE INTO `fn_restricted_url` VALUES ('raptor_blob_extract.htm','menu_reports');
REPLACE INTO `fn_restricted_url` VALUES ('raptor_email_attachment.htm','menu_reports');
REPLACE INTO `fn_restricted_url` VALUES ('raptor_search.htm','menu_reports');
REPLACE INTO `fn_restricted_url` VALUES ('report_list.htm','menu_reports');
REPLACE INTO `fn_restricted_url` VALUES ('gauge.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('gmap_controller.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('gmap_frame.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('map.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('map_download.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('map_grid_search.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('sample_animated_map.htm','menu_tab');
-- REPLACE INTO `fn_restricted_url` VALUES ('sample_heat_map.htm','menu_tab');
-- REPLACE INTO `fn_restricted_url` VALUES ('sample_heat_map_no_header.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('sample_map_2.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('sample_map_3.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub1.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub2_link1.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub2_link2.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub3.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab3.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab4.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('viewlog.htm','menu_viewlog');
-- REPLACE INTO `fn_restricted_url` VALUES ('bd_optima.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('bd_optima_interactive.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('bd_p2t.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('grid_heatmap.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('hive.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('hiveconfig.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('hiveconfig_popup.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('hive_search.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('hive_search_popup.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('jbpmTestProcess.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('job_progress.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('mapreduce.htm','quantum_bd');
-- REPLACE INTO `fn_restricted_url` VALUES ('mapreduce_search.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('raptor.htm','view_reports');
REPLACE INTO `fn_restricted_url` VALUES ('raptor_blob_extract.htm','view_reports');

--
-- Dumping data for table `fn_role`
--
INSERT INTO `fn_role` VALUES (1,'System Administrator','Y',1) ON DUPLICATE KEY UPDATE ROLE_NAME='System Administrator', ACTIVE_YN='Y', PRIORITY=1;
INSERT INTO `fn_role` VALUES (16,'Standard User','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Standard User', ACTIVE_YN='Y', PRIORITY=5;

--
-- Dumping data for table `fn_role_composite`
--
REPLACE INTO `fn_role_composite` VALUES (1,16);

--
-- Dumping data for table `fn_role_function`
--
REPLACE INTO `fn_role_function` VALUES (1,'doclib');
REPLACE INTO `fn_role_function` VALUES (1,'doclib_admin');
REPLACE INTO `fn_role_function` VALUES (1,'login');
REPLACE INTO `fn_role_function` VALUES (1,'menu_admin');
REPLACE INTO `fn_role_function` VALUES (1,'menu_ajax');
REPLACE INTO `fn_role_function` VALUES (1,'menu_servicemodels');
REPLACE INTO `fn_role_function` VALUES (1,'menu_customer');
REPLACE INTO `fn_role_function` VALUES (1,'menu_customer_create');
REPLACE INTO `fn_role_function` VALUES (1,'menu_feedback');
REPLACE INTO `fn_role_function` VALUES (1,'menu_help');
-- REPLACE INTO `fn_role_function` VALUES (1,'menu_hiveconfig');
-- REPLACE INTO `fn_role_function` VALUES (1,'menu_hiveconfig_create');
-- REPLACE INTO `fn_role_function` VALUES (1,'menu_hiveconfig_search');
REPLACE INTO `fn_role_function` VALUES (1,'menu_home');
REPLACE INTO `fn_role_function` VALUES (1,'menu_itracker');
REPLACE INTO `fn_role_function` VALUES (1,'menu_itracker_admin');
REPLACE INTO `fn_role_function` VALUES (1,'menu_job');
REPLACE INTO `fn_role_function` VALUES (1,'menu_job_create');
REPLACE INTO `fn_role_function` VALUES (1,'menu_logout');
-- REPLACE INTO `fn_role_function` VALUES (1,'menu_mapreduce');
-- REPLACE INTO `fn_role_function` VALUES (1,'menu_mapreduce_create');
-- REPLACE INTO `fn_role_function` VALUES (1,'menu_mapreduce_search');
REPLACE INTO `fn_role_function` VALUES (1,'menu_newserinstance');
REPLACE INTO `fn_role_function` VALUES (1,'menu_notes');
REPLACE INTO `fn_role_function` VALUES (1,'menu_process');
REPLACE INTO `fn_role_function` VALUES (1,'menu_profile');
REPLACE INTO `fn_role_function` VALUES (1,'menu_profile_create');
REPLACE INTO `fn_role_function` VALUES (1,'menu_profile_import');
REPLACE INTO `fn_role_function` VALUES (1,'menu_reports');
REPLACE INTO `fn_role_function` VALUES (1,'menu_sample');
REPLACE INTO `fn_role_function` VALUES (1,'menu_tab');
REPLACE INTO `fn_role_function` VALUES (1,'menu_test');
REPLACE INTO `fn_role_function` VALUES (1,'menu_viewlog');
-- REPLACE INTO `fn_role_function` VALUES (1,'quantum_bd');
REPLACE INTO `fn_role_function` VALUES (16,'login');
REPLACE INTO `fn_role_function` VALUES (16,'menu_ajax');
REPLACE INTO `fn_role_function` VALUES (16,'menu_servicemodels');
REPLACE INTO `fn_role_function` VALUES (16,'menu_customer');
REPLACE INTO `fn_role_function` VALUES (16,'menu_customer_create');
REPLACE INTO `fn_role_function` VALUES (16,'menu_home');
REPLACE INTO `fn_role_function` VALUES (16,'menu_itracker');
REPLACE INTO `fn_role_function` VALUES (16,'menu_logout');
REPLACE INTO `fn_role_function` VALUES (16,'menu_map');
REPLACE INTO `fn_role_function` VALUES (16,'menu_newserinstance');
REPLACE INTO `fn_role_function` VALUES (16,'menu_profile');
REPLACE INTO `fn_role_function` VALUES (16,'menu_reports');
REPLACE INTO `fn_role_function` VALUES (16,'menu_tab');
REPLACE INTO `fn_role_function` VALUES (16,'menu_viewlog');
-- REPLACE INTO `fn_role_function` VALUES (16,'quantum_bd');
REPLACE INTO `fn_role_function` VALUES ('1', 'menu_searchexisting');
REPLACE INTO `fn_role_function` VALUES ('16', 'menu_searchexisting');


--
-- Dumping data for table `fn_tab`
--
INSERT INTO `fn_tab` VALUES ('TAB1','Tab 1','Tab 1 Information','tab1.htm','menu_tab','Y',10,NULL,'APP') ON DUPLICATE KEY UPDATE TAB_NAME='Tab 1', TAB_DESCR='Tab 1 Information', ACTION='tab1.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=10, PARENT_TAB_CD=NULL, TAB_SET_CD='APP';
INSERT INTO `fn_tab` VALUES ('TAB2','Tab 2','Tab 2 Information','tab2_sub1.htm','menu_tab','Y',20,NULL,'APP') ON DUPLICATE KEY UPDATE TAB_NAME='Tab 2', TAB_DESCR='Tab 2 Information', ACTION='tab2_sub1.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=20, PARENT_TAB_CD=NULL, TAB_SET_CD='APP';
INSERT INTO `fn_tab` VALUES ('TAB2_SUB1','Sub Tab 1','Sub Tab 1 Information','tab2_sub1.htm','menu_tab','Y',10,'TAB2','APP') ON DUPLICATE KEY UPDATE TAB_NAME='Sub Tab 1', TAB_DESCR='Sub Tab 1 Information', ACTION='tab2_sub1.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=10, PARENT_TAB_CD='TAB2', TAB_SET_CD='APP';
INSERT INTO `fn_tab` VALUES ('TAB2_SUB1_S1','Left Tab 1','Sub - Sub Tab 1 Information','tab2_sub1.htm','menu_tab','Y',10,'TAB2_SUB1','APP') ON DUPLICATE KEY UPDATE TAB_NAME='Left Tab 1', TAB_DESCR='Sub - Sub Tab 1 Information', ACTION='tab2_sub1.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=10, PARENT_TAB_CD='TAB2_SUB1', TAB_SET_CD='APP';
INSERT INTO `fn_tab` VALUES ('TAB2_SUB2','Sub Tab 2','Sub Tab 2 Information','tab2_sub2.htm','menu_tab','Y',20,'TAB2','APP') ON DUPLICATE KEY UPDATE TAB_NAME='Sub Tab 2', TAB_DESCR='Sub Tab 2 Information', ACTION='tab2_sub2.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=20, PARENT_TAB_CD='TAB2', TAB_SET_CD='APP';
INSERT INTO `fn_tab` VALUES ('TAB2_SUB3','Sub Tab 3','Sub Tab 3 Information','tab2_sub3.htm','menu_tab','Y',30,'TAB2','APP') ON DUPLICATE KEY UPDATE TAB_NAME='Sub Tab 3', TAB_DESCR='Sub Tab 3 Information', ACTION='tab2_sub3.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=30, PARENT_TAB_CD='TAB2', TAB_SET_CD='APP';
INSERT INTO `fn_tab` VALUES ('TAB3','Tab 3','Tab 3 Information','tab3.htm','menu_tab','Y',30,NULL,'APP') ON DUPLICATE KEY UPDATE TAB_NAME='Tab 3', TAB_DESCR='Tab 3 Information', ACTION='tab3.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=30, PARENT_TAB_CD=NULL, TAB_SET_CD='APP';
INSERT INTO `fn_tab` VALUES ('TAB4','Tab 4','Tab 4 Information','tab4.htm','menu_tab','Y',40,NULL,'APP') ON DUPLICATE KEY UPDATE TAB_NAME='Tab 4', TAB_DESCR='Tab 4 Information', ACTION='tab4.htm', FUNCTION_CD='menu_tab', ACTIVE_YN='Y', SORT_ORDER=40, PARENT_TAB_CD=NULL, TAB_SET_CD='APP';

--
-- Dumping data for table `fn_tab_selected`
--
REPLACE INTO `fn_tab_selected` VALUES ('TAB1','tab1');
REPLACE INTO `fn_tab_selected` VALUES ('TAB2','tab2_sub1');
REPLACE INTO `fn_tab_selected` VALUES ('TAB2','tab2_sub2');
REPLACE INTO `fn_tab_selected` VALUES ('TAB2','tab2_sub3');
REPLACE INTO `fn_tab_selected` VALUES ('TAB2_SUB1','tab2_sub1');
REPLACE INTO `fn_tab_selected` VALUES ('TAB2_SUB1_S1','tab2_sub1');
REPLACE INTO `fn_tab_selected` VALUES ('TAB2_SUB2','tab2_sub2');
REPLACE INTO `fn_tab_selected` VALUES ('TAB2_SUB3','tab2_sub3');
REPLACE INTO `fn_tab_selected` VALUES ('TAB3','tab3');
REPLACE INTO `fn_tab_selected` VALUES ('TAB4','tab4');


--
-- Dumping data for table `fn_user`
--
INSERT IGNORE `fn_user` VALUES (1,null,null,'Demo',null,'User',null,null,null,null,null,null,null,'demo',null,'demo','Kp8bJ4SXszM0WX','2016-11-14 13:24:07','Y',null,'2016-10-17 00:00:00',1,'2016-11-14 13:24:07','N',null,null,null,'NJ',null,'US',null,null,null,null,null,10,null,null,null,null,null,null);
INSERT IGNORE `fn_user` VALUES (2,null,null,'vid1',null,'User',null,null,null,null,null,null,null,'vid1',null,'vid1','Kp8bJ4SXszM0WX','2016-11-14 13:24:07','Y',null,'2016-10-17 00:00:00',1,'2016-11-14 13:24:07','N',null,null,null,'NJ',null,'US',null,null,null,null,null,10,null,null,null,null,null,null);
INSERT IGNORE `fn_user` VALUES (3,null,null,'vid2',null,'User',null,null,null,null,null,null,null,'vid2',null,'vid2','Kp8bJ4SXszM0WX','2016-11-14 13:24:07','Y',null,'2016-10-17 00:00:00',1,'2016-11-14 13:24:07','N',null,null,null,'NJ',null,'US',null,null,null,null,null,10,null,null,null,null,null,null);
INSERT IGNORE `fn_user` VALUES (4,null,null,'vid3',null,'User',null,null,null,null,null,null,null,'vid3',null,'vid3','Kp8bJ4SXszM0WX','2016-11-14 13:24:07','Y',null,'2016-10-17 00:00:00',1,'2016-11-14 13:24:07','N',null,null,null,'NJ',null,'US',null,null,null,null,null,10,null,null,null,null,null,null);
INSERT IGNORE `fn_user` VALUES (5,null,null,'vid4',null,'User',null,null,null,null,null,null,null,'vid4',null,'vid4','Kp8bJ4SXszM0WX','2016-11-14 13:24:07','Y',null,'2016-10-17 00:00:00',1,'2016-11-14 13:24:07','N',null,null,null,'NJ',null,'US',null,null,null,null,null,10,null,null,null,null,null,null);

INSERT IGNORE `fn_user_role` VALUES (1,1,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (2,1,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (3,1,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (4,1,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (5,1,NULL,1);
