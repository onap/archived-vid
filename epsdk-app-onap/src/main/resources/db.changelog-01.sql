--liquibase formatted sql

--changeset da797d:1
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`vid_service_info` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
   `JOB_ID` BINARY(16) NOT NULL,
  `USER_ID` INT(11),
  `CREATED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `JOB_STATUS` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `PAUSE` TINYINT(1) NULL,
  `OWNING_ENTITY_ID` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `OWNING_ENTITY_NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `PROJECT` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `AIC_ZONE_ID` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `AIC_ZONE_NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `TENANT_ID` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `TENANT_NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `REGION_ID` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `REGION_NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SERVICE_TYPE` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SUBSCRIBER_NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SERVICE_INSTANCE_ID` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SERVICE_INSTANCE_NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SERVICE_MODEL_ID` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SERVICE_MODEL_NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SERVICE_MODEL_VERSION` VARCHAR(20) NULL COLLATE 'utf8_bin',
  PRIMARY KEY (`ID`),
  CONSTRAINT `vid_service_info_vid_job_JOB_ID_fk` FOREIGN KEY (`JOB_ID`) REFERENCES `vid_openecomp_epsdk`.`vid_job` (`JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--rollback drop table `vid_openecomp_epsdk`.`vid_service_info`;

--changeset em088y:2
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
ADD COLUMN CREATED_BULK_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IS_HIDDEN BIT,
ADD COLUMN IS_PAUSE BIT,
ADD COLUMN STATUS_MODIFIED_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
DROP COLUMN PAUSE;
--rollback alter `vid_openecomp_epsdk`.`vid_service_info` drop CREATED_BULK_DATE, drop IS_HIDDEN, drop IS_PAUSE, drop STATUS_MODIFIED_DATE;

--changeset em088y:3
delete from `vid_openecomp_epsdk`.`vid_service_info`;
delete from `vid_openecomp_epsdk`.`vid_job`;

alter table `vid_openecomp_epsdk`.`vid_service_info` drop foreign key vid_service_info_vid_job_JOB_ID_fk;

alter table `vid_openecomp_epsdk`.`vid_job`
drop index `FK_OWNING_VID_JOB_PARENT`,
drop FOREIGN KEY `FK_OWNING_VID_JOB_PARENT`,
drop primary key,
modify JOB_ID CHAR(36),
modify PARENT_JOB_ID CHAR(36),
add PRIMARY KEY (`JOB_ID`);

alter table `vid_openecomp_epsdk`.`vid_job`
add CONSTRAINT `FK_OWNING_VID_JOB_PARENT` FOREIGN KEY (`PARENT_JOB_ID`) REFERENCES `vid_job` (`JOB_ID`);

alter table `vid_openecomp_epsdk`.`vid_service_info`
modify JOB_ID CHAR(36),
add FOREIGN KEY `FK_SERVICE_INFO_VID_JOB_ID`(`JOB_ID`) REFERENCES `vid_job` (`JOB_ID`);

--changeset rr155p:4
ALTER TABLE `vid_openecomp_epsdk`.`vid_job`
DROP FOREIGN KEY  `FK_OWNING_VID_JOB_PARENT`;

ALTER TABLE `vid_openecomp_epsdk`.`vid_job`
 CHANGE COLUMN `PARENT_JOB_ID` `TEMPLATE_ID` CHAR(36) NULL DEFAULT NULL COLLATE 'utf8_bin' AFTER `JOB_DATA`;

--changeset is9613:5
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
ADD COLUMN `TEMPLATE_ID` CHAR(36) NULL DEFAULT NULL;

--changeset rr155p:6
ALTER TABLE `vid_openecomp_epsdk`.`vid_job`
ADD COLUMN `USER_ID` VARCHAR(50) NULL COLLATE 'utf8_bin';

--changeset is9613:7
ALTER TABLE `vid_openecomp_epsdk`.`vid_job`
  ADD COLUMN `AGE` INT NOT NULL default '0';

--changeset rr155p:8
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
 CHANGE COLUMN `USER_ID` `USER_ID` VARCHAR(50) NULL COLLATE 'utf8_bin';

--changeset cl0627:9
ALTER TABLE `vid_openecomp_epsdk`.`vid_job`
  ADD COLUMN DELETED_AT TIMESTAMP NULL DEFAULT NULL;

ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  ADD COLUMN DELETED_AT TIMESTAMP NULL DEFAULT NULL;

--changeset em088y:10
UPDATE `vid_openecomp_epsdk`.`fn_menu` SET `SORT_ORDER` = 13 where MENU_ID=110;
UPDATE `vid_openecomp_epsdk`.`fn_menu` SET `SORT_ORDER` = 12 where MENU_ID=109;
INSERT INTO `vid_openecomp_epsdk`.`fn_menu` (`MENU_ID`, `LABEL`, `PARENT_ID`, `SORT_ORDER`, `ACTION`, `FUNCTION_CD`, `ACTIVE_YN`, `SERVLET`, `QUERY_STRING`, `EXTERNAL_URL`, `TARGET`, `MENU_SET_CD`, `SEPARATOR_YN`, `IMAGE_SRC`) VALUES
(111, 'Macro Instantiation Status', 1, 11, 'serviceModels.htm#/instantiationStatus', 'menu_searchexisting', 'Y', NULL, NULL, NULL, NULL, 'APP', 'N', 'icon-location-pin');

--changeset is9613:job_index_in_bulk
ALTER TABLE `vid_openecomp_epsdk`.`vid_job`
  ADD COLUMN `INDEX_IN_BULK` INT NOT NULL default '0';


--changeset rr155:vid_job_audit_status
CREATE TABLE `vid_openecomp_epsdk`.`vid_job_audit_status` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `JOB_ID` CHAR(36) NOT NULL,
  `JOB_STATUS` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `SOURCE` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `REQUEST_ID` CHAR(36) NULL,
  `ADDITIONAL_INFO` VARCHAR (255) NULL,
  `CREATED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--changeset em088y:name_counter
CREATE TABLE `vid_openecomp_epsdk`.`vid_name_counter` (
  `NAME` VARCHAR(100) NOT NULL COLLATE 'utf8_bin',
  `COUNTER` INT NOT NULL,
  PRIMARY KEY (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--changeset em088y:increase_names_maxsize_500
alter table `vid_openecomp_epsdk`.`vid_service_info`
modify `OWNING_ENTITY_NAME` VARCHAR(500) NULL COLLATE 'utf8_bin',
modify `AIC_ZONE_NAME` VARCHAR(500) NULL COLLATE 'utf8_bin',
modify `TENANT_NAME` VARCHAR(500) NULL COLLATE 'utf8_bin',
modify `REGION_NAME` VARCHAR(500) NULL COLLATE 'utf8_bin',
modify `SUBSCRIBER_NAME` VARCHAR(500) NULL COLLATE 'utf8_bin',
modify `SERVICE_INSTANCE_NAME` VARCHAR(500) NULL COLLATE 'utf8_bin',
modify `SERVICE_MODEL_NAME` VARCHAR(500) NULL COLLATE 'utf8_bin';

alter table `vid_openecomp_epsdk`.`vid_name_counter`
modify `NAME` VARCHAR(255) NOT NULL COLLATE 'utf8_bin';

--changeset is9613:add_SUBSCRIBER_ID_to_vid_service_info
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  ADD COLUMN `SUBSCRIBER_ID` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_bin' AFTER `SUBSCRIBER_NAME`;

--changeset em088y:additional_info_maxsize_2000
alter table `vid_openecomp_epsdk`.`vid_job_audit_status`
modify `ADDITIONAL_INFO` VARCHAR(2000) NULL COLLATE 'utf8_bin';

--changeset er767y:change_'Macro Instantiation Status'_menu_item
UPDATE `vid_openecomp_epsdk`.`fn_menu` SET `LABEL` = 'Instantiation Status' WHERE `MENU_ID`=111;

--changeset cl0627:add_MSO_REQUEST_ID_and_IS_A_LA_CARTE_to_vid_service_info
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  ADD COLUMN `MSO_REQUEST_ID` CHAR(36) NULL AFTER `USER_ID`,
  ADD COLUMN `IS_A_LA_CARTE` BIT NULL AFTER `MSO_REQUEST_ID`;

--changeset cl0627:change_IS_A_LA_CARTE_to_not_null_and_default_false
UPDATE `vid_openecomp_epsdk`.`vid_service_info` SET `IS_A_LA_CARTE` = 0 WHERE ISNULL(`IS_A_LA_CARTE`);
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  MODIFY `IS_A_LA_CARTE` bit(1) NOT NULL DEFAULT 0;


--changeset vid:add_Action_and_Type_to_fn_function
ALTER TABLE `vid_openecomp_epsdk`.`fn_function`
  ADD COLUMN IF NOT EXISTS `type` VARCHAR(50) NULL AFTER `function_name`,
  ADD COLUMN IF NOT EXISTS `action` VARCHAR(100) NULL AFTER `type`;

--changeset em088y:increase_ids_maxsize_500
alter table `vid_openecomp_epsdk`.`vid_service_info`
modify `USER_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `MSO_REQUEST_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `OWNING_ENTITY_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `PROJECT` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `AIC_ZONE_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `TENANT_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `REGION_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `SERVICE_TYPE` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `SUBSCRIBER_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `SERVICE_INSTANCE_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `SERVICE_MODEL_ID` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin',
modify `SERVICE_MODEL_VERSION` VARCHAR(500) NULL DEFAULT NULL COLLATE 'utf8_bin';

--changeset ah0398:restrict_url_for_role_management
INSERT IGNORE INTO fn_restricted_url (restricted_url, function_cd) VALUES ('get_user_roles','menu_admin');
INSERT IGNORE INTO fn_restricted_url (restricted_url, function_cd) VALUES ('addRoleFunction','menu_admin');

--changeset rs282j:add_ACTION_to_vid_service_info
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  ADD COLUMN `ACTION` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_bin';

UPDATE `vid_openecomp_epsdk`.`vid_service_info` SET `ACTION` = 'INSTANTIATE' where ISNULL(ACTION);
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  MODIFY `ACTION` VARCHAR(50) NOT NULL;

--changeset ag137v:welcome_page_used_by_epsdk_2_5_0
INSERT IGNORE INTO fn_restricted_url (restricted_url, function_cd) VALUES ('welcome','menu_home');

--changeset ag137v:welcome_page_used_by_epsdk_2_5_0_fn_menu
UPDATE `vid_openecomp_epsdk`.`fn_menu` SET `ACTION` = 'welcome' WHERE `ACTION` = 'welcome.htm';

--changeset em088y:remove_all_foreign_keys_from_fn_user
alter table `vid_openecomp_epsdk`.`fn_user`
drop foreign key FK_FN_USER_REF_110_FN_ORG,
drop foreign key FK_FN_USER_REF_123_FN_LU_AL,
drop foreign key FK_FN_USER_REF_197_FN_USER,
drop foreign key FK_FN_USER_REF_198_FN_USER,
drop foreign key FK_FN_USER_REF_199_FN_USER,
drop foreign key FK_TIMEZONE;

--changeset em088y:add_IS_RETRY_ENABLED_to_vid_service_info
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  ADD COLUMN `IS_RETRY_ENABLED` bit(1) NOT NULL DEFAULT 0 AFTER `IS_PAUSE`;

--changeset em088y:create_table_vid_job_request
CREATE TABLE `vid_openecomp_epsdk`.`vid_job_request` (
  `JOB_ID` CHAR(36) NOT NULL COLLATE 'utf8_bin',
  `REQUEST` MEDIUMTEXT NOT NULL COLLATE 'utf8_bin',
  `CREATED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`JOB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--changeset cp2122:vid_resource_info
CREATE TABLE `vid_openecomp_epsdk`.`vid_resource_info` (
  `ROOT_JOB_ID` CHAR(36) NOT NULL COLLATE 'utf8_bin',
  `TRACK_BY_ID` CHAR(36) NOT NULL COLLATE 'utf8_bin',
  `JOB_STATUS` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `INSTANCE_ID` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `CREATED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`TRACK_BY_ID`),
  INDEX `ROOT_JOB_ID_IND` (`ROOT_JOB_ID` )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--changeset ag137v:welcome_page_used_by_epsdk_2_5_0
INSERT IGNORE INTO fn_restricted_url (restricted_url, function_cd) VALUES ('welcome','menu_home');

--changeset er767y:add_error_message_to_vid_resource_info
ALTER TABLE `vid_openecomp_epsdk`.`vid_resource_info`
  ADD COLUMN `ERROR_MESSAGE` TEXT NULL DEFAULT NULL COLLATE 'utf8_bin' AFTER `INSTANCE_ID`;

--changeset em088y:drop_foreign_key_from_service_info_to_jobs
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  DROP FOREIGN KEY `FK_SERVICE_INFO_VID_JOB_ID`;

--changeset em088y:add_BUILD_to_vid_job
ALTER TABLE `vid_openecomp_epsdk`.`vid_job`
  ADD COLUMN `BUILD` VARCHAR(100) NULL DEFAULT NULL COLLATE 'utf8_bin';

--changeset eb7504:add_category_parameter_release
INSERT IGNORE INTO vid_category_parameter (CATEGORY_ID, NAME, ID_SUPPORTED,FAMILY) VALUES (7,'release', 0, 'TENANT_ISOLATION');

--changeset ah0398:restrict_url_for_role_functions
INSERT IGNORE INTO fn_restricted_url (restricted_url, function_cd) VALUES('get_role_functions','menu_admin');

--changeset ah0398:restrict_all_required_urls_according_to_sdk_2_5_0
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('admin','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('addRole','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('get_role','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('profile/*','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('post_search/process','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('post_search/search','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('post_search/search','menu_profile');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('role/saveRole.htm','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('role_list/*','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('role_function_list/*','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('removeRole','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('removeRoleFunction','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('samplePage','menu_sample');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('workflows','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('workflows/list','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('workflows/addWorkflow','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('workflows/saveCronJob','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('workflows/editWorkflow','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('workflows/removeWorkflow','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('workflows/removeAllWorkflows','menu_admin');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('report/wizard/retrieve_def_tab_wise_data/*','menu_reports');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('report/wizard/retrieve_form_tab_wise_data/*','menu_reports');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('report/wizard/retrieve_sql_tab_wise_data/*','menu_reports');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('report/wizard/security/*','menu_reports');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('report/wizard/copy_report/*','menu_reports');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('report/wizard/save_def_tab_data/*','menu_reports');
INSERT IGNORE INTO fn_restricted_url  (restricted_url, function_cd) VALUES('report/wizard/retrieve_data/true','menu_reports');

--changeset is9613:add_ordinal_to_vid_job_audit_status
ALTER TABLE `vid_openecomp_epsdk`.`vid_job_audit_status`
  ADD COLUMN `ORDINAL` INT DEFAULT 0;

--changeset vid:align_changes_from_ecomp
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (6, 'VNF Scale Out') ON DUPLICATE KEY UPDATE WORKFLOW_DB_ID=6, WORKFLOW_APP_NAME='VNF Scale Out';
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (4, 'VNF Config Update') ON DUPLICATE KEY UPDATE WORKFLOW_DB_ID=4, WORKFLOW_APP_NAME='VNF Config Update';

UPDATE `vid_openecomp_epsdk`.`fn_function` SET `type` = 'menu', `action` = '*';
UPDATE `vid_openecomp_epsdk`.`fn_function` SET `type` = 'url' WHERE `FUNCTION_NAME`='Login';

UPDATE `vid_openecomp_epsdk`.`fn_menu` SET `LABEL`='Browse SDC Service Models' WHERE `LABEL`='Browse ASDC Service Models';

INSERT INTO `fn_role` VALUES (1,'System Administrator','Y',1) ON DUPLICATE KEY UPDATE ROLE_NAME='System Administrator', ACTIVE_YN='Y', PRIORITY=1;
INSERT INTO `fn_role` VALUES (16,'Standard User','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Standard User', ACTIVE_YN='Y', PRIORITY=5;
INSERT INTO `fn_role` VALUES (17,'Demonstration___vFWCL','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Demonstration___vFWCL', ACTIVE_YN='Y', PRIORITY=5;
INSERT INTO `fn_role` VALUES (18,'Demonstration___vFW','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Demonstration___vFW', ACTIVE_YN='Y', PRIORITY=5;
INSERT INTO `fn_role` VALUES (19,'Demonstration___vCPE','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Demonstration___vCPE', ACTIVE_YN='Y', PRIORITY=5;
INSERT INTO `fn_role` VALUES (20,'Demonstration___vIMS','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Demonstration___vIMS', ACTIVE_YN='Y', PRIORITY=5;
INSERT INTO `fn_role` VALUES (21,'Demonstration___vLB','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Demonstration___vLB', ACTIVE_YN='Y', PRIORITY=5;
INSERT INTO `fn_role` VALUES (22,'Demonstration___gNB','Y',5) ON DUPLICATE KEY UPDATE ROLE_NAME='Demonstration___gNB', ACTIVE_YN='Y', PRIORITY=5;

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
INSERT IGNORE `fn_user_role` VALUES (1,17,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (1,18,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (1,19,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (1,20,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (1,21,NULL,1);
INSERT IGNORE `fn_user_role` VALUES (1,22,NULL,1);
UPDATE `fn_app` SET `APP_IMAGE_URL`=null,`APP_URL`=null,`APP_ALTERNATE_URL`=null WHERE `APP_ID`=1;


--changeset vid:service_info_request_summary
ALTER TABLE `vid_openecomp_epsdk`.`vid_service_info`
  ADD COLUMN `REQUEST_SUMMARY` VARCHAR(400) NULL DEFAULT NULL COLLATE 'utf8_bin';

--changeset vid:add_workflow_pnf_software_upgrade
INSERT INTO `vid_workflow` (`WORKFLOW_DB_ID`, `WORKFLOW_APP_NAME`) VALUES (7, 'PNF Software Upgrade');
