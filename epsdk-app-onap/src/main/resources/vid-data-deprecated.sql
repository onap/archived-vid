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
DELETE FROM `fn_lu_call_times`;
DELETE FROM `fn_lu_country`;
DELETE FROM `fn_lu_menu_set`;
DELETE FROM `fn_lu_priority`;
DELETE FROM `fn_lu_state`;
DELETE FROM `fn_lu_tab_set`;

INSERT IGNORE INTO `fn_role` (`ROLE_ID`, `ROLE_NAME`, `ACTIVE_YN`, `PRIORITY`) VALUES
	(1, 'System Administrator', 'Y', 1),
	(16, 'Standard User', 'Y', 5);

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
-- Dumping data for table `vid_workflow`
--
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (1, 'Update') ON DUPLICATE KEY UPDATE WORKFLOW_APP_NAME='Update';
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (2, 'Replace') ON DUPLICATE KEY UPDATE WORKFLOW_APP_NAME='Replace';
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (3, 'VNF In Place Software Update') ON DUPLICATE KEY UPDATE WORKFLOW_APP_NAME='VNF In Place Software Update';
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (5, 'PNF Software Upgrade') ON DUPLICATE KEY UPDATE WORKFLOW_APP_NAME='PNF Software Upgrade';
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (6, 'VNF Scale Out') ON DUPLICATE KEY UPDATE WORKFLOW_DB_ID=6, WORKFLOW_APP_NAME='VNF Scale Out';
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (4, 'VNF Config Update') ON DUPLICATE KEY UPDATE WORKFLOW_DB_ID=4, WORKFLOW_APP_NAME='VNF Config Update';


--
-- Dumping data for table `vid_category_parameter`
--
INSERT INTO `vid_category_parameter` (`CATEGORY_ID`, `NAME`, `ID_SUPPORTED`, `FAMILY`,`CREATED_DATE`, `MODIFIED_DATE`) VALUES (1, 'platform', 0, 'PARAMETER_STANDARDIZATION','2017-09-12 17:01:13', '2017-09-12 17:01:13') ON DUPLICATE KEY UPDATE NAME='platform';
INSERT INTO `vid_category_parameter` (`CATEGORY_ID`, `NAME`, `ID_SUPPORTED`, `FAMILY`,`CREATED_DATE`, `MODIFIED_DATE`) VALUES (2, 'project', 0, 'PARAMETER_STANDARDIZATION','2017-09-12 18:23:54', '2017-09-13 15:05:25') ON DUPLICATE KEY UPDATE NAME='project';
INSERT INTO `vid_category_parameter` (`CATEGORY_ID`, `NAME`, `ID_SUPPORTED`, `FAMILY`,`CREATED_DATE`, `MODIFIED_DATE`) VALUES (3, 'lineOfBusiness', 0,'PARAMETER_STANDARDIZATION', '2017-09-12 18:24:14', '2017-09-12 18:24:15') ON DUPLICATE KEY UPDATE NAME='lineOfBusiness';
INSERT INTO `vid_category_parameter` (`CATEGORY_ID`, `NAME`, `ID_SUPPORTED`, `FAMILY`,`CREATED_DATE`, `MODIFIED_DATE`) VALUES (4, 'owningEntity', 1, 'PARAMETER_STANDARDIZATION', '2017-09-12 18:24:26', '2017-09-12 18:24:28') ON DUPLICATE KEY UPDATE NAME='owningEntity';
INSERT INTO `vid_category_parameter` (`CATEGORY_ID`, `NAME`, `ID_SUPPORTED`, `FAMILY`,`CREATED_DATE`, `MODIFIED_DATE`) VALUES (5,'operational-environment-type', 0,'TENANT_ISOLATION', '2017-11-12 18:24:26', '2017-11-12 18:24:28') ON DUPLICATE KEY UPDATE NAME='operational-environment-type';
INSERT INTO `vid_category_parameter` (`CATEGORY_ID`, `NAME`, `ID_SUPPORTED`, `FAMILY`,`CREATED_DATE`, `MODIFIED_DATE`) VALUES (6,'workload-context', 0, 'TENANT_ISOLATION','2017-11-12 18:24:26', '2017-11-12 18:24:28') ON DUPLICATE KEY UPDATE NAME='workload-context';

--
-- Dumping data for table `vid_category_parameter_option`
--
INSERT INTO `vid_category_parameter_option` (`CATEGORY_OPT_APP_ID`, `NAME`, `CATEGORY_ID`) VALUES ('ECOMP', 'ECOMP', '6') ON DUPLICATE KEY UPDATE NAME='ECOMP';
INSERT INTO `vid_category_parameter_option` (`CATEGORY_OPT_APP_ID`, `NAME`, `CATEGORY_ID`) VALUES ('DEV', 'DEV', '6') ON DUPLICATE KEY UPDATE NAME='DEV';
INSERT INTO `vid_category_parameter_option` (`CATEGORY_OPT_APP_ID`, `NAME`, `CATEGORY_ID`) VALUES ('TEST', 'TEST', '6') ON DUPLICATE KEY UPDATE NAME='TEST';
INSERT INTO `vid_category_parameter_option` (`CATEGORY_OPT_APP_ID`, `NAME`, `CATEGORY_ID`) VALUES ('VNF', 'VNF', '5') ON DUPLICATE KEY UPDATE NAME='VNF';

--
-- Dumping data for table `fn_function`
--
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('1','test role function');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('doclib','Document Library');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('doclib_admin','Document Library Admin');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('login','Login');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_admin','Admin Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_ajax','Ajax Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_servicemodels','Browse ASDC Service Instances');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_concept','CoNCEPT');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_customer','Customer Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_customer_create','Customer Create');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_doclib','Document Library Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_feedback','Feedback Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_help','Help Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_hiveconfig','Hive Configuration');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_hiveconfig_create','Hive Configuration Create');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_hiveconfig_search','Hive Configuration Search');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_home','Home Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_itracker','iTracker Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_itracker_admin','Itracker Admin/Support menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_job','Job Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_job_create','Job Create');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_job_designer','Process in Designer view');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_logout','Logout Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_map','Map Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_mapreduce','Map Reduce Configuration');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_mapreduce_create','Map Reduce Configuration Create');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_mapreduce_search','Map Reduce Configuration Search');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_newserinstance','Create New Service Instance');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_notes','Notes Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_process','Process List');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_profile','Profile Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_profile_create','Profile Create');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_profile_import','Profile Import');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_reports','Reports Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_sample','Sample Pages Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_tab','Sample Tab Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_task','Task Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_task_search','Task Search');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_test','Test Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_viewlog','Log Menu');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('quantum_bd','Big Data Function');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('view_reports','View Raptor reports');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_searchexisting', 'Search for Existing Service Instances');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_changemanagement','VNF Changes');
REPLACE INTO `fn_function` (`FUNCTION_CD`, `FUNCTION_NAME`) VALUES ('menu_testenvironment','Test Environments');

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
-- Dumping data for table `fn_lu_call_times`
--
REPLACE INTO `fn_lu_call_times` VALUES (1,20,'20 min');
REPLACE INTO `fn_lu_call_times` VALUES (2,40,'40 min');
REPLACE INTO `fn_lu_call_times` VALUES (3,60,'1 hr');
REPLACE INTO `fn_lu_call_times` VALUES (4,80,'1 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (5,100,'1 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (6,120,'2 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (7,140,'2 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (8,160,'2 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (9,180,'3 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (10,200,'3 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (11,220,'3 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (12,240,'4 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (13,260,'4 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (14,280,'4 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (15,300,'5 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (16,320,'5 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (17,340,'5 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (18,360,'6 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (19,380,'6 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (20,400,'6 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (21,420,'7 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (22,440,'7 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (23,460,'7 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (24,480,'8 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (25,500,'8 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (26,520,'8 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (27,540,'9 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (28,560,'9 hr 20 min');
REPLACE INTO `fn_lu_call_times` VALUES (29,580,'9 hr 40 min');
REPLACE INTO `fn_lu_call_times` VALUES (30,600,'10 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (31,10,'10 min');
REPLACE INTO `fn_lu_call_times` VALUES (32,5,'5 min');
REPLACE INTO `fn_lu_call_times` VALUES (33,1200,'20 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (34,1800,'30 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (35,2400,'40 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (36,3000,'50 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (37,4200,'70 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (38,4800,'80 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (39,5400,'90 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (40,6000,'100 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (41,7200,'120 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (42,9600,'160 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (43,10800,'180 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (44,12000,'200 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (45,18000,'300 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (46,24000,'400 hrs');
REPLACE INTO `fn_lu_call_times` VALUES (47,30000,'500 hrs');

--
-- Dumping data for table `fn_lu_country`
--
REPLACE INTO `fn_lu_country` VALUES ('AD','Andorra','Andorra',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AE','United Arab Emirates','United Arab Emirates',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AF','Afghanistan','Afghanistan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AG','Antigua and Barbuda','Antigua and Barbuda',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AI','Anguilla','Anguilla',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AL','Albania','Albania',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AM','Armenia','Armenia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AN','Netherlands Antilles','Netherlands Antilles',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AO','Angola','Angola',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AQ','Antarctica','Antarctica',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AR','Argentina','Argentina',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AS','American Samoa','American Samoa',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AT','Austria','Austria',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AU','Australia','Australia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AW','Aruba','Aruba',NULL);
REPLACE INTO `fn_lu_country` VALUES ('AZ','Azerbaidjan','Azerbaidjan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BA','Bosnia-Herzegovina','Bosnia-Herzegovina',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BB','Barbados','Barbados',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BD','Bangladesh','Bangladesh',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BE','Belgium','Belgium',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BF','Burkina Faso','Burkina Faso',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BG','Bulgaria','Bulgaria',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BH','Bahrain','Bahrain',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BI','Burundi','Burundi',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BJ','Benin','Benin',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BM','Bermuda','Bermuda',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BN','Brunei Darussalam','Brunei Darussalam',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BO','Bolivia','Bolivia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BR','Brazil','Brazil',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BS','Bahamas','Bahamas',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BT','Bhutan','Bhutan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BV','Bouvet Island','Bouvet Island',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BW','Botswana','Botswana',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BY','Belarus','Belarus',NULL);
REPLACE INTO `fn_lu_country` VALUES ('BZ','Belize','Belize',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CA','Canada','Canada',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CC','Cocos (Keeling) Islands','Cocos (Keeling) Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CF','Central African Republic','Central African Republic',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CG','Congo','Congo',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CH','Switzerland','Switzerland',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CI','Ivory Coast (Cote D\'Ivoire)','Ivory Coast (Cote D\'Ivoire)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CK','Cook Islands','Cook Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CL','Chile','Chile',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CM','Cameroon','Cameroon',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CN','China','China','China');
REPLACE INTO `fn_lu_country` VALUES ('CO','Colombia','Colombia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CR','Costa Rica','Costa Rica',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CS','Former Czechoslovakia','Former Czechoslovakia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CU','Cuba','Cuba',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CV','Cape Verde','Cape Verde',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CX','Christmas Island','Christmas Island',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CY','Cyprus','Cyprus',NULL);
REPLACE INTO `fn_lu_country` VALUES ('CZ','Czech Republic','Czech Republic',NULL);
REPLACE INTO `fn_lu_country` VALUES ('DE','Germany','Germany',NULL);
REPLACE INTO `fn_lu_country` VALUES ('DJ','Djibouti','Djibouti',NULL);
REPLACE INTO `fn_lu_country` VALUES ('DK','Denmark','Denmark',NULL);
REPLACE INTO `fn_lu_country` VALUES ('DM','Dominica','Dominica',NULL);
REPLACE INTO `fn_lu_country` VALUES ('DO','Dominican Republic','Dominican Republic',NULL);
REPLACE INTO `fn_lu_country` VALUES ('DZ','Algeria','Algeria',NULL);
REPLACE INTO `fn_lu_country` VALUES ('EC','Ecuador','Ecuador',NULL);
REPLACE INTO `fn_lu_country` VALUES ('EE','Estonia','Estonia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('EG','Egypt','Egypt',NULL);
REPLACE INTO `fn_lu_country` VALUES ('EH','Western Sahara','Western Sahara',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ER','Eritrea','Eritrea',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ES','Spain','Spain',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ET','Ethiopia','Ethiopia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('FI','Finland','Finland',NULL);
REPLACE INTO `fn_lu_country` VALUES ('FJ','Fiji','Fiji',NULL);
REPLACE INTO `fn_lu_country` VALUES ('FK','Falkland Islands','Falkland Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('FM','Micronesia','Micronesia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('FO','Faroe Islands','Faroe Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('FR','France','France',NULL);
REPLACE INTO `fn_lu_country` VALUES ('FX','France (European Territory)','France (European Territory)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GA','Gabon','Gabon',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GB','Great Britain','Great Britain',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GD','Grenada','Grenada',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GE','Georgia','Georgia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GF','French Guyana','French Guyana',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GH','Ghana','Ghana',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GI','Gibraltar','Gibraltar',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GL','Greenland','Greenland',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GM','Gambia','Gambia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GN','Guinea','Guinea',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GP','Guadeloupe (French)','Guadeloupe (French)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GQ','Equatorial Guinea','Equatorial Guinea',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GR','Greece','Greece',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GS','S. Georgia and S. Sandwich Isls.','S. Georgia and S. Sandwich Isls.',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GT','Guatemala','Guatemala',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GU','Guam (USA)','Guam (USA)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GW','Guinea Bissau','Guinea Bissau',NULL);
REPLACE INTO `fn_lu_country` VALUES ('GY','Guyana','Guyana',NULL);
REPLACE INTO `fn_lu_country` VALUES ('HK','Hong Kong','Hong Kong',NULL);
REPLACE INTO `fn_lu_country` VALUES ('HM','Heard and McDonald Islands','Heard and McDonald Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('HN','Honduras','Honduras',NULL);
REPLACE INTO `fn_lu_country` VALUES ('HR','Croatia','Croatia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('HT','Haiti','Haiti',NULL);
REPLACE INTO `fn_lu_country` VALUES ('HU','Hungary','Hungary',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ID','Indonesia','Indonesia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IE','Ireland','Ireland',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IL','Israel','Israel',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IN','India','India',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IO','British Indian Ocean Territory','British Indian Ocean Territory',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IQ','Iraq','Iraq',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IR','Iran','Iran',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IS','Iceland','Iceland',NULL);
REPLACE INTO `fn_lu_country` VALUES ('IT','Italy','Italy',NULL);
REPLACE INTO `fn_lu_country` VALUES ('JM','Jamaica','Jamaica',NULL);
REPLACE INTO `fn_lu_country` VALUES ('JO','Jordan','Jordan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('JP','Japan','Japan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KE','Kenya','Kenya',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KG','Kyrgyzstan','Kyrgyzstan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KH','Cambodia','Cambodia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KI','Kiribati','Kiribati',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KM','Comoros','Comoros',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KN','Saint Kitts and Nevis Anguilla','Saint Kitts and Nevis Anguilla',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KP','North Korea','North Korea',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KR','South Korea','South Korea',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KW','Kuwait','Kuwait',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KY','Cayman Islands','Cayman Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('KZ','Kazakhstan','Kazakhstan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LA','Laos','Laos',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LB','Lebanon','Lebanon',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LC','Saint Lucia','Saint Lucia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LI','Liechtenstein','Liechtenstein',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LK','Sri Lanka','Sri Lanka',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LR','Liberia','Liberia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LS','Lesotho','Lesotho',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LT','Lithuania','Lithuania',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LU','Luxembourg','Luxembourg',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LV','Latvia','Latvia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('LY','Libya','Libya',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MA','Morocco','Morocco',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MC','Monaco','Monaco',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MD','Moldavia','Moldavia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MG','Madagascar','Madagascar',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MH','Marshall Islands','Marshall Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MK','Macedonia','Macedonia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ML','Mali','Mali',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MM','Myanmar','Myanmar',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MN','Mongolia','Mongolia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MO','Macau','Macau',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MP','Northern Mariana Islands','Northern Mariana Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MQ','Martinique (French)','Martinique (French)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MR','Mauritania','Mauritania',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MS','Montserrat','Montserrat',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MT','Malta','Malta',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MU','Mauritius','Mauritius',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MV','Maldives','Maldives',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MW','Malawi','Malawi',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MX','Mexico','Mexico','Mexico');
REPLACE INTO `fn_lu_country` VALUES ('MY','Malaysia','Malaysia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('MZ','Mozambique','Mozambique',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NA','Namibia','Namibia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NC','New Caledonia (French)','New Caledonia (French)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NE','Niger','Niger',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NF','Norfolk Island','Norfolk Island',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NG','Nigeria','Nigeria',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NI','Nicaragua','Nicaragua',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NL','Netherlands','Netherlands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NO','Norway','Norway',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NP','Nepal','Nepal',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NR','Nauru','Nauru',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NU','Niue','Niue',NULL);
REPLACE INTO `fn_lu_country` VALUES ('NZ','New Zealand','New Zealand',NULL);
REPLACE INTO `fn_lu_country` VALUES ('OM','Oman','Oman',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PA','Panama','Panama',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PE','Peru','Peru',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PF','Polynesia (French)','Polynesia (French)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PG','Papua New Guinea','Papua New Guinea',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PH','Philippines','Philippines',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PK','Pakistan','Pakistan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PL','Poland','Poland',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PM','Saint Pierre and Miquelon','Saint Pierre and Miquelon',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PN','Pitcairn Island','Pitcairn Island',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PR','Puerto Rico','Puerto Rico',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PT','Portugal','Portugal',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PW','Palau','Palau',NULL);
REPLACE INTO `fn_lu_country` VALUES ('PY','Paraguay','Paraguay',NULL);
REPLACE INTO `fn_lu_country` VALUES ('QA','Qatar','Qatar',NULL);
REPLACE INTO `fn_lu_country` VALUES ('RE','Reunion (French)','Reunion (French)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('RO','Romania','Romania',NULL);
REPLACE INTO `fn_lu_country` VALUES ('RU','Russian Federation','Russian Federation',NULL);
REPLACE INTO `fn_lu_country` VALUES ('RW','Rwanda','Rwanda',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SA','Saudi Arabia','Saudi Arabia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SB','Solomon Islands','Solomon Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SC','Seychelles','Seychelles',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SD','Sudan','Sudan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SE','Sweden','Sweden',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SG','Singapore','Singapore',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SH','Saint Helena','Saint Helena',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SI','Slovenia','Slovenia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SJ','Svalbard and Jan Mayen Islands','Svalbard and Jan Mayen Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SK','Slovak Republic','Slovak Republic',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SL','Sierra Leone','Sierra Leone',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SM','San Marino','San Marino',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SN','Senegal','Senegal',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SO','Somalia','Somalia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SR','Suriname','Suriname',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ST','Saint Tome (Sao Tome) and Principe','Saint Tome (Sao Tome) and Principe',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SU','Former USSR','Former USSR',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SV','El Salvador','El Salvador',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SY','Syria','Syria',NULL);
REPLACE INTO `fn_lu_country` VALUES ('SZ','Swaziland','Swaziland',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TC','Turks and Caicos Islands','Turks and Caicos Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TD','Chad','Chad',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TF','French Southern Territories','French Southern Territories',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TG','Togo','Togo',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TH','Thailand','Thailand',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TJ','Tadjikistan','Tadjikistan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TK','Tokelau','Tokelau',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TM','Turkmenistan','Turkmenistan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TN','Tunisia','Tunisia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TO','Tonga','Tonga',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TP','East Timor','East Timor',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TR','Turkey','Turkey',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TT','Trinidad and Tobago','Trinidad and Tobago',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TV','Tuvalu','Tuvalu',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TW','Taiwan','Taiwan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('TZ','Tanzania','Tanzania',NULL);
REPLACE INTO `fn_lu_country` VALUES ('UA','Ukraine','Ukraine',NULL);
REPLACE INTO `fn_lu_country` VALUES ('UG','Uganda','Uganda',NULL);
REPLACE INTO `fn_lu_country` VALUES ('UK','United Kingdom','United Kingdom',NULL);
REPLACE INTO `fn_lu_country` VALUES ('UM','USA Minor Outlying Islands','USA Minor Outlying Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('US','United States','United States','USA');
REPLACE INTO `fn_lu_country` VALUES ('UY','Uruguay','Uruguay',NULL);
REPLACE INTO `fn_lu_country` VALUES ('UZ','Uzbekistan','Uzbekistan',NULL);
REPLACE INTO `fn_lu_country` VALUES ('VA','Vatican City State','Vatican City State',NULL);
REPLACE INTO `fn_lu_country` VALUES ('VC','Saint Vincent and Grenadines','Saint Vincent and Grenadines',NULL);
REPLACE INTO `fn_lu_country` VALUES ('VE','Venezuela','Venezuela',NULL);
REPLACE INTO `fn_lu_country` VALUES ('VG','Virgin Islands (British)','Virgin Islands (British)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('VI','Virgin Islands (USA)','Virgin Islands (USA)',NULL);
REPLACE INTO `fn_lu_country` VALUES ('VN','Vietnam','Vietnam',NULL);
REPLACE INTO `fn_lu_country` VALUES ('VU','Vanuatu','Vanuatu',NULL);
REPLACE INTO `fn_lu_country` VALUES ('WF','Wallis and Futuna Islands','Wallis and Futuna Islands',NULL);
REPLACE INTO `fn_lu_country` VALUES ('WS','Samoa','Samoa',NULL);
REPLACE INTO `fn_lu_country` VALUES ('YE','Yemen','Yemen',NULL);
REPLACE INTO `fn_lu_country` VALUES ('YT','Mayotte','Mayotte',NULL);
REPLACE INTO `fn_lu_country` VALUES ('YU','Yugoslavia','Yugoslavia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ZA','South Africa','South Africa',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ZM','Zambia','Zambia',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ZR','Zaire','Zaire',NULL);
REPLACE INTO `fn_lu_country` VALUES ('ZW','Zimbabwe','Zimbabwe',NULL);

--
-- Dumping data for table `fn_lu_menu_set`
--
REPLACE INTO `fn_lu_menu_set` VALUES ('APP','Application Menu');

--
-- Dumping data for table `fn_lu_priority`
--
REPLACE INTO `fn_lu_priority` VALUES (10,'Low','Y',10);
REPLACE INTO `fn_lu_priority` VALUES (20,'Normal','Y',20);
REPLACE INTO `fn_lu_priority` VALUES (30,'High','Y',30);
REPLACE INTO `fn_lu_priority` VALUES (40,'Urgent','Y',40);
REPLACE INTO `fn_lu_priority` VALUES (50,'Fatal','Y',50);

--
-- Dumping data for table `fn_lu_state`
--
REPLACE INTO `fn_lu_state` VALUES ('AK','AK - Alaska');
REPLACE INTO `fn_lu_state` VALUES ('AL','AL - Alabama');
REPLACE INTO `fn_lu_state` VALUES ('AR','AR - Arkansas');
REPLACE INTO `fn_lu_state` VALUES ('AZ','AZ - Arizona');
REPLACE INTO `fn_lu_state` VALUES ('CA','CA - California');
REPLACE INTO `fn_lu_state` VALUES ('CO','CO - Colorado');
REPLACE INTO `fn_lu_state` VALUES ('CT','CT - Connecticut');
REPLACE INTO `fn_lu_state` VALUES ('DC','DC - District Of Columbia');
REPLACE INTO `fn_lu_state` VALUES ('DE','DE - Delaware');
REPLACE INTO `fn_lu_state` VALUES ('FL','FL - Florida');
REPLACE INTO `fn_lu_state` VALUES ('GA','GA - Georgia');
REPLACE INTO `fn_lu_state` VALUES ('HI','HI - Hawaii');
REPLACE INTO `fn_lu_state` VALUES ('IA','IA - Iowa');
REPLACE INTO `fn_lu_state` VALUES ('ID','ID - Idaho');
REPLACE INTO `fn_lu_state` VALUES ('IL','IL - Illinois');
REPLACE INTO `fn_lu_state` VALUES ('IN','IN - Indiana');
REPLACE INTO `fn_lu_state` VALUES ('KS','KS - Kansas');
REPLACE INTO `fn_lu_state` VALUES ('KY','KY - Kentucky');
REPLACE INTO `fn_lu_state` VALUES ('LA','LA - Louisiana');
REPLACE INTO `fn_lu_state` VALUES ('MA','MA - Massachusetts');
REPLACE INTO `fn_lu_state` VALUES ('MD','MD - Maryland');
REPLACE INTO `fn_lu_state` VALUES ('ME','ME - Maine');
REPLACE INTO `fn_lu_state` VALUES ('MI','MI - Michigan');
REPLACE INTO `fn_lu_state` VALUES ('MN','MN - Minnesota');
REPLACE INTO `fn_lu_state` VALUES ('MO','MO - Missouri');
REPLACE INTO `fn_lu_state` VALUES ('MS','MS - Mississippi');
REPLACE INTO `fn_lu_state` VALUES ('MT','MT - Montana');
REPLACE INTO `fn_lu_state` VALUES ('NC','NC - North Carolina');
REPLACE INTO `fn_lu_state` VALUES ('ND','ND - North Dakota');
REPLACE INTO `fn_lu_state` VALUES ('NE','NE - Nebraska');
REPLACE INTO `fn_lu_state` VALUES ('NH','NH - New Hampshire');
REPLACE INTO `fn_lu_state` VALUES ('NJ','NJ - New Jersey');
REPLACE INTO `fn_lu_state` VALUES ('NM','NM - New Mexico');
REPLACE INTO `fn_lu_state` VALUES ('NV','NV - Nevada');
REPLACE INTO `fn_lu_state` VALUES ('NY','NY - New York');
REPLACE INTO `fn_lu_state` VALUES ('OH','OH - Ohio');
REPLACE INTO `fn_lu_state` VALUES ('OK','OK - Oklahoma');
REPLACE INTO `fn_lu_state` VALUES ('OR','OR - Oregon');
REPLACE INTO `fn_lu_state` VALUES ('PA','PA - Pennsylvania');
REPLACE INTO `fn_lu_state` VALUES ('PR','PR - Puerto Rico');
REPLACE INTO `fn_lu_state` VALUES ('RI','RI - Rhode Island');
REPLACE INTO `fn_lu_state` VALUES ('SC','SC - South Carolina');
REPLACE INTO `fn_lu_state` VALUES ('SD','SD - South Dakota');
REPLACE INTO `fn_lu_state` VALUES ('TN','TN - Tennessee');
REPLACE INTO `fn_lu_state` VALUES ('TX','TX - Texas');
REPLACE INTO `fn_lu_state` VALUES ('UT','UT - Utah');
REPLACE INTO `fn_lu_state` VALUES ('VA','VA - Virginia');
REPLACE INTO `fn_lu_state` VALUES ('VI','VI-Virgin Island');
REPLACE INTO `fn_lu_state` VALUES ('VT','VT - Vermont');
REPLACE INTO `fn_lu_state` VALUES ('WA','WA - Washington');
REPLACE INTO `fn_lu_state` VALUES ('WI','WI - Wisconsin');
REPLACE INTO `fn_lu_state` VALUES ('WV','WV - West Virginia');
REPLACE INTO `fn_lu_state` VALUES ('WY','WY - Wyoming');

--
-- Dumping data for table `fn_lu_tab_set`
--
REPLACE INTO `fn_lu_tab_set` VALUES ('APP','Application Tabs');

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
INSERT INTO `fn_menu` VALUES (2,'VID Home',1,10,'welcome.htm','menu_home','Y',NULL,NULL,NULL,NULL,'APP','N','icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='VID Home', PARENT_ID=1, SORT_ORDER=10, ACTION='welcome.htm', FUNCTION_CD='menu_home', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
INSERT INTO `fn_menu` VALUES (3,'Search for Existing Service Instances',1,10,'serviceModels.htm#/instances/services','menu_searchexisting','Y',NULL,NULL,NULL,NULL,'APP','N','icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='Search for Existing Service Instances', PARENT_ID=1, SORT_ORDER=10, ACTION='serviceModels.htm#/instances/services', FUNCTION_CD='menu_searchexisting', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
INSERT INTO `fn_menu` VALUES (10,'Admin',1,110,'role_list.htm','menu_admin','Y',NULL,NULL,NULL,NULL,'APP','N','icon-settings') ON DUPLICATE KEY UPDATE LABEL='Admin', PARENT_ID=1, SORT_ORDER=110, ACTION='admin', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-settings';
INSERT INTO `fn_menu` VALUES (13,'VID Logout',1,130,'app_logout.htm','menu_logout','N',NULL,NULL,NULL,NULL,'APP','N','icon-sign-out') ON DUPLICATE KEY UPDATE LABEL='VID Logout', PARENT_ID=1, SORT_ORDER=130, ACTION='app_logout.htm', FUNCTION_CD='menu_logout', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-sign-out';
INSERT INTO `fn_menu` VALUES (42,'Browse ASDC Service Models',1,10,'serviceModels.htm','menu_servicemodels','Y',NULL,NULL,NULL,NULL,'APP','N','icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='Browse ASDC Service Instances', PARENT_ID=1, SORT_ORDER=10, ACTION='serviceModels.htm', FUNCTION_CD='menu_servicemodels', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
INSERT INTO `fn_menu` VALUES (41,'Create New Service Instance',1,10,'serviceModels.htm#/instances/subscribers','menu_newserinstance','Y',NULL,NULL,NULL,NULL,'APP','N','icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='Create New Service Instance', PARENT_ID=1, SORT_ORDER=10, ACTION='serviceModels.htm#/instances/subscribers', FUNCTION_CD='menu_newserinstance', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
INSERT INTO `fn_menu` VALUES (43,'View Log',1,10,'viewlog.htm','menu_viewlog','N',NULL,NULL,NULL,NULL,'APP','N','icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='View Log', PARENT_ID=1, SORT_ORDER=10, ACTION='viewlog.htm', FUNCTION_CD='menu_viewlog', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
INSERT INTO `fn_menu` VALUES (101,'Roles',10,20,'admin','menu_admin','Y',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/users.png') ON DUPLICATE KEY UPDATE LABEL='Roles', PARENT_ID=10, SORT_ORDER=20, ACTION='admin', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/users.png';
INSERT INTO `fn_menu` VALUES (102,'Role Functions',10,30,'admin#/role_function_list','menu_admin','Y',NULL,NULL,NULL,NULL,'APP','N',NULL) ON DUPLICATE KEY UPDATE LABEL='Role Functions', PARENT_ID=10, SORT_ORDER=30, ACTION='admin#/role_function_list', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC=NULL;
INSERT INTO `fn_menu` VALUES (103,'Broadcast Messages',10,50,'admin#/broadcast_list','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/bubble.png') ON DUPLICATE KEY UPDATE LABEL='Broadcast Messages', PARENT_ID=10, SORT_ORDER=50, ACTION='admin#/broadcast_list', FUNCTION_CD='menu_admin', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/bubble.png';
INSERT INTO `fn_menu` VALUES (105,'Cache Admin',10,40,'admin#/jcs_admin','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/cache.png') ON DUPLICATE KEY UPDATE LABEL='Cache Admin', PARENT_ID=10, SORT_ORDER=40, ACTION='admin#/jcs_admin', FUNCTION_CD='menu_admin', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/cache.png';
INSERT INTO `fn_menu` VALUES (106,'Lock/Unlock Application',10,60,'application_lockout.htm','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/decrypted.png') ON DUPLICATE KEY UPDATE LABEL='Lock/Unlock Application', PARENT_ID=10, SORT_ORDER=60, ACTION='application_lockout.htm', FUNCTION_CD='menu_admin', ACTIVE_YN='N', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/decrypted.png';
INSERT INTO `fn_menu` VALUES (108,'Usage',10,80,'admin#/usage_list','menu_admin','N',NULL,NULL,NULL,NULL,'APP','N','/static/fusion/images/users.png') ON DUPLICATE KEY UPDATE LABEL='Usage', PARENT_ID=10, SORT_ORDER=80, ACTION='admin#/usage_list', FUNCTION_CD='menu_admin', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='/static/fusion/images/users.png';
INSERT INTO `fn_menu` VALUES (109, 'VNF Changes', 1, 11, 'serviceModels.htm#/change-management', 'menu_changemanagement', 'Y', NULL, NULL, NULL, NULL, 'APP', 'N', 'icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='VNF Changes', PARENT_ID=1, SORT_ORDER=11, ACTION='serviceModels.htm#/change-management', FUNCTION_CD='menu_changemanagement', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
INSERT INTO `fn_menu` VALUES (110, 'Test Environments', 1, 12, 'serviceModels.htm#/testEnvironments', 'menu_testenvironment', 'Y', NULL, NULL, NULL, NULL, 'APP', 'N', 'icon-location-pin') ON DUPLICATE KEY UPDATE LABEL='Test Environments', PARENT_ID=1, SORT_ORDER=12, ACTION='serviceModels.htm#/testEnvironments', FUNCTION_CD='menu_testenvironment', ACTIVE_YN='Y', SERVLET=NULL, QUERY_STRING=NULL, EXTERNAL_URL=NULL, TARGET=NULL, MENU_SET_CD='APP', SEPARATOR_YN='N', IMAGE_SRC='icon-location-pin';
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
REPLACE INTO `fn_restricted_url` VALUES ('novamap_controller.htm','menu_map');
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
REPLACE INTO `fn_restricted_url` VALUES ('sample_heat_map.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('sample_heat_map_no_header.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('sample_map_2.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('sample_map_3.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub1.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub2_link1.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub2_link2.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab2_sub3.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab3.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('tab4.htm','menu_tab');
REPLACE INTO `fn_restricted_url` VALUES ('viewlog.htm','menu_viewlog');
REPLACE INTO `fn_restricted_url` VALUES ('bd_optima.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('bd_optima_interactive.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('bd_p2t.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('grid_heatmap.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('hive.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('hiveconfig.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('hiveconfig_popup.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('hive_search.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('hive_search_popup.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('jbpmTestProcess.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('job_progress.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('mapreduce.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('mapreduce_search.htm','quantum_bd');
REPLACE INTO `fn_restricted_url` VALUES ('raptor.htm','view_reports');
REPLACE INTO `fn_restricted_url` VALUES ('raptor_blob_extract.htm','view_reports');
REPLACE INTO `fn_restricted_url` VALUES ('serviceModels.htm','menu_servicemodels');


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
REPLACE INTO `fn_role_function` VALUES (1,'menu_hiveconfig');
REPLACE INTO `fn_role_function` VALUES (1,'menu_hiveconfig_create');
REPLACE INTO `fn_role_function` VALUES (1,'menu_hiveconfig_search');
REPLACE INTO `fn_role_function` VALUES (1,'menu_home');
REPLACE INTO `fn_role_function` VALUES (1,'menu_itracker');
REPLACE INTO `fn_role_function` VALUES (1,'menu_itracker_admin');
REPLACE INTO `fn_role_function` VALUES (1,'menu_job');
REPLACE INTO `fn_role_function` VALUES (1,'menu_job_create');
REPLACE INTO `fn_role_function` VALUES (1,'menu_logout');
REPLACE INTO `fn_role_function` VALUES (1,'menu_mapreduce');
REPLACE INTO `fn_role_function` VALUES (1,'menu_mapreduce_create');
REPLACE INTO `fn_role_function` VALUES (1,'menu_mapreduce_search');
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
REPLACE INTO `fn_role_function` VALUES (1,'quantum_bd');
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
REPLACE INTO `fn_role_function` VALUES (16,'quantum_bd');
REPLACE INTO `fn_role_function` VALUES ('1', 'menu_searchexisting');
REPLACE INTO `fn_role_function` VALUES ('16', 'menu_searchexisting');
REPLACE INTO `fn_role_function` VALUES (1,'menu_changemanagement');
REPLACE INTO `fn_role_function` VALUES (16,'menu_changemanagement');
REPLACE INTO `fn_role_function` VALUES (1,'menu_testenvironment');
REPLACE INTO `fn_role_function` VALUES (16,'menu_testenvironment');

update fn_menu set active_yn = 'Y' where label = 'Admin';
update fn_menu set active_yn = 'Y' where label = 'Root';
update fn_menu set action = 'admin' where label = 'Admin';

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
