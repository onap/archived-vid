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
-- ----------------------------------------------------------------------------
-- MySQL Workbench Migration
-- Migrated Schemata: vid_openecomp_epsdk
-- Source Schemata: ecomp_sd
-- Created: Sun Nov 13 08:58:53 2016
-- Workbench Version: 6.3.6
-- ----------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------------------------------------
-- Schema vid_openecomp_epsdk
-- ----------------------------------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vid_openecomp_epsdk` ;

USE vid_openecomp_epsdk;


CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`schema_info` (
  `SCHEMA_ID` VARCHAR(25) NOT NULL,
  `SCHEMA_DESC` VARCHAR(75) NOT NULL,
  `DATASOURCE_TYPE` VARCHAR(100) NULL DEFAULT NULL,
  `CONNECTION_URL` VARCHAR(200) NOT NULL,
  `USER_NAME` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(45) NULL DEFAULT NULL,
  `DRIVER_CLASS` VARCHAR(100) NOT NULL,
  `MIN_POOL_SIZE` INT(11) NOT NULL,
  `MAX_POOL_SIZE` INT(11) NOT NULL,
  `IDLE_CONNECTION_TEST_PERIOD` INT(11) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_favorite_reports
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_favorite_reports` (
  `USER_ID` INT(11) NOT NULL,
  `REP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`USER_ID`, `REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_filehist_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_filehist_log` (
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `URL` VARCHAR(4000) NULL DEFAULT NULL,
  `NOTES` VARCHAR(3500) NULL DEFAULT NULL,
  `RUN_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_folder
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_folder` (
  `FOLDER_ID` INT(11) NOT NULL,
  `FOLDER_NAME` VARCHAR(50) NOT NULL,
  `DESCR` VARCHAR(500) NULL DEFAULT NULL,
  `CREATE_ID` INT(11) NOT NULL,
  `CREATE_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PARENT_FOLDER_ID` INT(11) NULL DEFAULT NULL,
  `PUBLIC_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`FOLDER_ID`),
  INDEX `FK_PARENT_KEY_CR_FOLDER` (`PARENT_FOLDER_ID` ASC),
  CONSTRAINT `FK_PARENT_KEY_CR_FOLDER`
    FOREIGN KEY (`PARENT_FOLDER_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_folder` (`FOLDER_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_folder_access
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_folder_access` (
  `FOLDER_ACCESS_ID` DECIMAL(11,0) NOT NULL,
  `FOLDER_ID` DECIMAL(11,0) NOT NULL,
  `ORDER_NO` DECIMAL(11,0) NOT NULL,
  `ROLE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `USER_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `READ_ONLY_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`FOLDER_ACCESS_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_hist_user_map
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_hist_user_map` (
  `HIST_ID` INT(11) NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  PRIMARY KEY (`HIST_ID`, `USER_ID`),
  INDEX `SYS_C0014617` (`USER_ID` ASC),
  CONSTRAINT `SYS_C0014616`
    FOREIGN KEY (`HIST_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_report_file_history` (`HIST_ID`),
  CONSTRAINT `SYS_C0014617`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_user` (`USER_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_lu_file_type
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_lu_file_type` (
  `LOOKUP_ID` DECIMAL(2,0) NOT NULL,
  `LOOKUP_DESCR` VARCHAR(255) NOT NULL,
  `ACTIVE_YN` CHAR(1) NULL DEFAULT 'Y',
  `ERROR_CODE` DECIMAL(11,0) NULL DEFAULT NULL,
  PRIMARY KEY (`LOOKUP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_raptor_action_img
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_raptor_action_img` (
  `IMAGE_ID` VARCHAR(100) NOT NULL,
  `IMAGE_LOC` VARCHAR(400) NULL DEFAULT NULL,
  PRIMARY KEY (`IMAGE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_raptor_pdf_img
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_raptor_pdf_img` (
  `IMAGE_ID` VARCHAR(100) NOT NULL,
  `IMAGE_LOC` VARCHAR(400) NULL DEFAULT NULL,
  PRIMARY KEY (`IMAGE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_remote_schema_info
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_remote_schema_info` (
  `SCHEMA_PREFIX` VARCHAR(5) NOT NULL,
  `SCHEMA_DESC` VARCHAR(75) NOT NULL,
  `DATASOURCE_TYPE` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`SCHEMA_PREFIX`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report` (
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `TITLE` VARCHAR(100) NOT NULL,
  `DESCR` VARCHAR(255) NULL DEFAULT NULL,
  `PUBLIC_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  `REPORT_XML` TEXT NULL DEFAULT NULL,
  `CREATE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `CREATE_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MAINT_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `MAINT_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MENU_ID` VARCHAR(500) NULL DEFAULT NULL,
  `MENU_APPROVED_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  `OWNER_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `FOLDER_ID` INT(11) NULL DEFAULT '0',
  `DASHBOARD_TYPE_YN` VARCHAR(1) NULL DEFAULT 'N',
  `DASHBOARD_YN` VARCHAR(1) NULL DEFAULT 'N',
  PRIMARY KEY (`REP_ID`),
  INDEX `CR_REPORT_CREATE_IDPUBLIC_YNTITLE` (`CREATE_ID` ASC, `PUBLIC_YN` ASC, `TITLE` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_access
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_access` (
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `ORDER_NO` DECIMAL(11,0) NOT NULL,
  `ROLE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `USER_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `READ_ONLY_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`REP_ID`, `ORDER_NO`),
  CONSTRAINT `FK_CR_REPOR_REF_8550_CR_REPOR`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_dwnld_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_dwnld_log` (
  `USER_ID` DECIMAL(11,0) NOT NULL,
  `REP_ID` INT(11) NOT NULL,
  `FILE_NAME` VARCHAR(100) NOT NULL,
  `DWNLD_START_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `RECORD_READY_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `FILTER_PARAMS` VARCHAR(2000) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_email_sent_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_email_sent_log` (
  `LOG_ID` INT(11) NOT NULL,
  `SCHEDULE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `GEN_KEY` VARCHAR(25) NOT NULL,
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `USER_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `SENT_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ACCESS_FLAG` VARCHAR(1) NOT NULL DEFAULT 'Y',
  `TOUCH_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`LOG_ID`),
  INDEX `FK_CR_REPORT_REP_ID` (`REP_ID` ASC),
  CONSTRAINT `FK_CR_REPORT_REP_ID`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_file_history
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_file_history` (
  `HIST_ID` INT(11) NOT NULL,
  `SCHED_USER_ID` DECIMAL(11,0) NOT NULL,
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `USER_ID` DECIMAL(11,0) NOT NULL,
  `REP_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `RUN_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `RECURRENCE` VARCHAR(50) NULL DEFAULT NULL,
  `FILE_TYPE_ID` DECIMAL(2,0) NULL DEFAULT NULL,
  `FILE_NAME` VARCHAR(80) NULL DEFAULT NULL,
  `FILE_BLOB` BLOB NULL DEFAULT NULL,
  `FILE_SIZE` DECIMAL(11,0) NULL DEFAULT NULL,
  `RAPTOR_URL` VARCHAR(4000) NULL DEFAULT NULL,
  `ERROR_YN` CHAR(1) NULL DEFAULT 'N',
  `ERROR_CODE` DECIMAL(11,0) NULL DEFAULT NULL,
  `DELETED_YN` CHAR(1) NULL DEFAULT 'N',
  `DELETED_BY` DECIMAL(38,0) NULL DEFAULT NULL,
  PRIMARY KEY (`HIST_ID`),
  INDEX `SYS_C0014614` (`FILE_TYPE_ID` ASC),
  INDEX `SYS_C0014615` (`REP_ID` ASC),
  CONSTRAINT `SYS_C0014614`
    FOREIGN KEY (`FILE_TYPE_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_lu_file_type` (`LOOKUP_ID`),
  CONSTRAINT `SYS_C0014615`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_log` (
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `LOG_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `USER_ID` DECIMAL(11,0) NOT NULL,
  `ACTION` VARCHAR(2000) NOT NULL,
  `ACTION_VALUE` VARCHAR(50) NULL DEFAULT NULL,
  `FORM_FIELDS` VARCHAR(4000) NULL DEFAULT NULL,
  INDEX `FK_CR_REPOR_REF_17645_CR_REPOR` (`REP_ID` ASC),
  CONSTRAINT `FK_CR_REPOR_REF_17645_CR_REPOR`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_schedule
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_schedule` (
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `SCHED_USER_ID` DECIMAL(11,0) NOT NULL,
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `ENABLED_YN` VARCHAR(1) NOT NULL,
  `START_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `END_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `RUN_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `RECURRENCE` VARCHAR(50) NULL DEFAULT NULL,
  `CONDITIONAL_YN` VARCHAR(1) NOT NULL,
  `CONDITION_SQL` VARCHAR(4000) NULL DEFAULT NULL,
  `NOTIFY_TYPE` INT(11) NULL DEFAULT '0',
  `MAX_ROW` INT(11) NULL DEFAULT '1000',
  `INITIAL_FORMFIELDS` VARCHAR(3500) NULL DEFAULT NULL,
  `PROCESSED_FORMFIELDS` VARCHAR(3500) NULL DEFAULT NULL,
  `FORMFIELDS` VARCHAR(3500) NULL DEFAULT NULL,
  `CONDITION_LARGE_SQL` TEXT NULL DEFAULT NULL,
  `ENCRYPT_YN` CHAR(1) NULL DEFAULT 'N',
  `ATTACHMENT_YN` CHAR(1) NULL DEFAULT 'Y',
  PRIMARY KEY (`SCHEDULE_ID`),
  INDEX `FK_CR_REPOR_REF_14707_CR_REPOR` (`REP_ID` ASC),
  CONSTRAINT `FK_CR_REPOR_REF_14707_CR_REPOR`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_schedule_users
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_schedule_users` (
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `USER_ID` DECIMAL(11,0) NOT NULL,
  `ROLE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `ORDER_NO` DECIMAL(11,0) NOT NULL,
  PRIMARY KEY (`SCHEDULE_ID`, `REP_ID`, `USER_ID`, `ORDER_NO`),
  CONSTRAINT `FK_CR_REPOR_REF_14716_CR_REPOR`
    FOREIGN KEY (`SCHEDULE_ID`)
    REFERENCES `vid_openecomp_epsdk`.`cr_report_schedule` (`SCHEDULE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_report_template_map
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_report_template_map` (
  `REPORT_ID` INT(11) NOT NULL,
  `TEMPLATE_FILE` VARCHAR(200) NULL DEFAULT NULL,
  PRIMARY KEY (`REPORT_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_schedule_activity_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_schedule_activity_log` (
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `URL` VARCHAR(4000) NULL DEFAULT NULL,
  `NOTES` VARCHAR(2000) NULL DEFAULT NULL,
  `RUN_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_table_join
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_table_join` (
  `SRC_TABLE_NAME` VARCHAR(30) NOT NULL,
  `DEST_TABLE_NAME` VARCHAR(30) NOT NULL,
  `JOIN_EXPR` VARCHAR(500) NOT NULL,
  INDEX `CR_TABLE_JOIN_DEST_TABLE_NAME` (`DEST_TABLE_NAME` ASC),
  INDEX `CR_TABLE_JOIN_SRC_TABLE_NAME` (`SRC_TABLE_NAME` ASC),
  CONSTRAINT `FK_CR_TABLE_REF_311_CR_TAB`
    FOREIGN KEY (`SRC_TABLE_NAME`)
    REFERENCES `vid_openecomp_epsdk`.`cr_table_source` (`TABLE_NAME`),
  CONSTRAINT `FK_CR_TABLE_REF_315_CR_TAB`
    FOREIGN KEY (`DEST_TABLE_NAME`)
    REFERENCES `vid_openecomp_epsdk`.`cr_table_source` (`TABLE_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_table_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_table_role` (
  `TABLE_NAME` VARCHAR(30) NOT NULL,
  `ROLE_ID` DECIMAL(11,0) NOT NULL,
  PRIMARY KEY (`TABLE_NAME`, `ROLE_ID`),
  CONSTRAINT `FK_CR_TABLE_REF_32384_CR_TABLE`
    FOREIGN KEY (`TABLE_NAME`)
    REFERENCES `vid_openecomp_epsdk`.`cr_table_source` (`TABLE_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.cr_table_source
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`cr_table_source` (
  `TABLE_NAME` VARCHAR(30) NOT NULL,
  `DISPLAY_NAME` VARCHAR(30) NOT NULL,
  `PK_FIELDS` VARCHAR(200) NULL DEFAULT NULL,
  `WEB_VIEW_ACTION` VARCHAR(50) NULL DEFAULT NULL,
  `LARGE_DATA_SOURCE_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  `FILTER_SQL` VARCHAR(4000) NULL DEFAULT NULL,
  `SOURCE_DB` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`TABLE_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_portal.fn_app
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_app` (
  `APP_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `APP_NAME` VARCHAR(100) NOT NULL DEFAULT '?',
  `APP_IMAGE_URL` VARCHAR(256) NULL DEFAULT NULL,
  `APP_DESCRIPTION` VARCHAR(512) NULL DEFAULT NULL,
  `APP_NOTES` VARCHAR(4096) NULL DEFAULT NULL,
  `APP_URL` VARCHAR(256) NULL DEFAULT NULL,
  `APP_ALTERNATE_URL` VARCHAR(256) NULL DEFAULT NULL,
  `APP_REST_ENDPOINT` VARCHAR(2000) NULL DEFAULT NULL,
  `ML_APP_NAME` VARCHAR(50) NOT NULL DEFAULT '?',
  `ML_APP_ADMIN_ID` VARCHAR(7) NOT NULL DEFAULT '?',
  `MOTS_ID` INT(11) NULL DEFAULT NULL,
  `APP_PASSWORD` VARCHAR(256) NOT NULL DEFAULT '?',
  `OPEN` CHAR(1) NULL DEFAULT 'N',
  `ENABLED` CHAR(1) NULL DEFAULT 'Y',
  `THUMBNAIL` MEDIUMBLOB NULL DEFAULT NULL,
  `APP_USERNAME` VARCHAR(50) NULL DEFAULT NULL,
  `UEB_KEY` VARCHAR(256) NULL DEFAULT NULL,
  `UEB_SECRET` VARCHAR(256) NULL DEFAULT NULL,
  `UEB_TOPIC_NAME` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`APP_ID`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_app_mme_cpu
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_app_mme_cpu` (
  `MME` VARCHAR(200) NULL DEFAULT NULL,
  `YEARMONTH` INT(11) NULL DEFAULT NULL,
  `SCTP_CPU` INT(11) NULL DEFAULT NULL,
  `AP_CPU` INT(11) NULL DEFAULT NULL,
  `DP_CPU` INT(11) NULL DEFAULT NULL,
  `ROUTER_CPU` INT(11) NULL DEFAULT NULL,
  `PEB_CPU` INT(11) NULL DEFAULT NULL,
  `SAU` INT(11) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_audit_action
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_audit_action` (
  `AUDIT_ACTION_ID` INT(11) NOT NULL,
  `CLASS_NAME` VARCHAR(500) NOT NULL,
  `METHOD_NAME` VARCHAR(50) NOT NULL,
  `AUDIT_ACTION_CD` VARCHAR(20) NOT NULL,
  `AUDIT_ACTION_DESC` VARCHAR(200) NULL DEFAULT NULL,
  `ACTIVE_YN` VARCHAR(1) NULL DEFAULT NULL,
  PRIMARY KEY (`AUDIT_ACTION_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_audit_action_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_audit_action_log` (
  `AUDIT_LOG_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `AUDIT_ACTION_CD` VARCHAR(200) NULL DEFAULT NULL,
  `ACTION_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `USER_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `CLASS_NAME` VARCHAR(100) NULL DEFAULT NULL,
  `METHOD_NAME` VARCHAR(50) NULL DEFAULT NULL,
  `SUCCESS_MSG` VARCHAR(20) NULL DEFAULT NULL,
  `ERROR_MSG` VARCHAR(500) NULL DEFAULT NULL,
  PRIMARY KEY (`AUDIT_LOG_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_audit_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_audit_log` (
  `LOG_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(11) NOT NULL,
  `ACTIVITY_CD` VARCHAR(50) NOT NULL,
  `AUDIT_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `COMMENTS` VARCHAR(1000) NULL DEFAULT NULL,
  `AFFECTED_RECORD_ID_BK` VARCHAR(500) NULL DEFAULT NULL,
  `AFFECTED_RECORD_ID` VARCHAR(4000) NULL DEFAULT NULL,
  PRIMARY KEY (`LOG_ID`),
  INDEX `FN_AUDIT_LOG_ACTIVITY_CD` (`ACTIVITY_CD` ASC),
  INDEX `FN_AUDIT_LOG_USER_ID` (`USER_ID` ASC),
  CONSTRAINT `FK_FN_AUDIT_REF_205_FN_LU_AC`
    FOREIGN KEY (`ACTIVITY_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_lu_activity` (`ACTIVITY_CD`),
  CONSTRAINT `FK_FN_AUDIT_REF_209_FN_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_user` (`USER_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_broadcast_message
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_broadcast_message` (
  `MESSAGE_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MESSAGE_TEXT` VARCHAR(1000) NOT NULL,
  `MESSAGE_LOCATION_ID` DECIMAL(11,0) NOT NULL,
  `BROADCAST_START_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `BROADCAST_END_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ACTIVE_YN` CHAR(1) NOT NULL DEFAULT 'Y',
  `SORT_ORDER` DECIMAL(4,0) NOT NULL,
  `BROADCAST_SITE_CD` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`MESSAGE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_chat_logs
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_chat_logs` (
  `CHAT_LOG_ID` INT(11) NOT NULL,
  `CHAT_ROOM_ID` INT(11) NULL DEFAULT NULL,
  `USER_ID` INT(11) NULL DEFAULT NULL,
  `MESSAGE` VARCHAR(1000) NULL DEFAULT NULL,
  `MESSAGE_DATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CHAT_LOG_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_chat_room
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_chat_room` (
  `CHAT_ROOM_ID` INT(11) NOT NULL,
  `NAME` VARCHAR(50) NOT NULL,
  `DESCRIPTION` VARCHAR(500) NULL DEFAULT NULL,
  `OWNER_ID` INT(11) NULL DEFAULT NULL,
  `CREATED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CHAT_ROOM_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_chat_users
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_chat_users` (
  `CHAT_ROOM_ID` INT(11) NULL DEFAULT NULL,
  `USER_ID` INT(11) NULL DEFAULT NULL,
  `LAST_ACTIVITY_DATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CHAT_STATUS` VARCHAR(20) NULL DEFAULT NULL,
  `ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_datasource
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_datasource` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(50) NULL DEFAULT NULL,
  `DRIVER_NAME` VARCHAR(256) NULL DEFAULT NULL,
  `SERVER` VARCHAR(256) NULL DEFAULT NULL,
  `PORT` INT(11) NULL DEFAULT NULL,
  `USER_NAME` VARCHAR(256) NULL DEFAULT NULL,
  `PASSWORD` VARCHAR(256) NULL DEFAULT NULL,
  `URL` VARCHAR(256) NULL DEFAULT NULL,
  `MIN_POOL_SIZE` INT(11) NULL DEFAULT NULL,
  `MAX_POOL_SIZE` INT(11) NULL DEFAULT NULL,
  `ADAPTER_ID` INT(11) NULL DEFAULT NULL,
  `DS_TYPE` VARCHAR(20) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_function
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_function` (
  `FUNCTION_CD` VARCHAR(30) NOT NULL,
  `FUNCTION_NAME` VARCHAR(50) NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  `action` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`FUNCTION_CD`),
  CONSTRAINT `function` UNIQUE (FUNCTION_CD,TYPE,ACTION))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_license
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_license` (
  `id` DECIMAL(11,0) NOT NULL,
  `app_id` DECIMAL(11,0) NOT NULL,
  `ip_address` VARCHAR(100) NOT NULL,
  `quantum_version_id` DECIMAL(11,0) NOT NULL,
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_id` DECIMAL(11,0) NULL DEFAULT NULL,
  `modified_id` DECIMAL(11,0) NULL DEFAULT NULL,
  `end_date` TIMESTAMP NOT NULL DEFAULT '2036-01-19 03:14:07',
  PRIMARY KEY (`id`),
  INDEX `fn_license_r02` (`quantum_version_id` ASC),
  CONSTRAINT `fn_license_r02`
    FOREIGN KEY (`quantum_version_id`)
    REFERENCES `vid_openecomp_epsdk`.`fn_license_version` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_license_app
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_license_app` (
  `id` DECIMAL(11,0) NOT NULL,
  `app_name` VARCHAR(100) NOT NULL,
  `ctxt_name` VARCHAR(100) NULL DEFAULT NULL,
  INDEX `fn_license_app_ID` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_license_contact
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_license_contact` (
  `id` INT(11) NOT NULL,
  `license_id` INT(11) NULL DEFAULT NULL,
  `sbcid` VARCHAR(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_license_history
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_license_history` (
  `license_id` DECIMAL(11,0) NULL DEFAULT NULL,
  `app_id` DECIMAL(11,0) NULL DEFAULT NULL,
  `ip_address` VARCHAR(100) NULL DEFAULT NULL,
  `quantum_version_id` DECIMAL(11,0) NULL DEFAULT NULL,
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_id` DECIMAL(11,0) NULL DEFAULT NULL,
  `modified_id` DECIMAL(11,0) NULL DEFAULT NULL,
  `id` DECIMAL(11,0) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_license_version
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_license_version` (
  `id` DECIMAL(11,0) NOT NULL,
  `quantum_version` VARCHAR(25) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_activity
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_activity` (
  `ACTIVITY_CD` VARCHAR(50) NOT NULL,
  `ACTIVITY` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ACTIVITY_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_alert_method
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_alert_method` (
  `ALERT_METHOD_CD` VARCHAR(10) NOT NULL,
  `ALERT_METHOD` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ALERT_METHOD_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_broadcast_site
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_broadcast_site` (
  `BROADCAST_SITE_CD` VARCHAR(50) NOT NULL,
  `BROADCAST_SITE_DESCR` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`BROADCAST_SITE_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_call_times
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_call_times` (
  `CALL_TIME_ID` DECIMAL(10,0) NOT NULL,
  `CALL_TIME_AMOUNT` DECIMAL(10,0) NOT NULL,
  `CALL_TIME_DISPLAY` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`CALL_TIME_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_city
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_city` (
  `CITY_CD` VARCHAR(2) NOT NULL,
  `CITY` VARCHAR(100) NOT NULL,
  `STATE_CD` VARCHAR(2) NOT NULL,
  PRIMARY KEY (`CITY_CD`, `STATE_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_country
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_country` (
  `COUNTRY_CD` VARCHAR(3) NOT NULL,
  `COUNTRY` VARCHAR(100) NOT NULL,
  `FULL_NAME` VARCHAR(100) NULL DEFAULT NULL,
  `WEBPHONE_COUNTRY_LABEL` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`COUNTRY_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_menu_set
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_menu_set` (
  `MENU_SET_CD` VARCHAR(10) NOT NULL,
  `MENU_SET_NAME` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`MENU_SET_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_priority
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_priority` (
  `PRIORITY_ID` DECIMAL(11,0) NOT NULL,
  `PRIORITY` VARCHAR(50) NOT NULL,
  `ACTIVE_YN` CHAR(1) NOT NULL,
  `SORT_ORDER` DECIMAL(5,0) NULL DEFAULT NULL,
  PRIMARY KEY (`PRIORITY_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_role_type
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_role_type` (
  `ROLE_TYPE_ID` DECIMAL(11,0) NOT NULL,
  `ROLE_TYPE` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ROLE_TYPE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_state
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_state` (
  `STATE_CD` VARCHAR(2) NOT NULL,
  `STATE` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`STATE_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_tab_set
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_tab_set` (
  `TAB_SET_CD` VARCHAR(30) NOT NULL,
  `TAB_SET_NAME` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`TAB_SET_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_lu_timezone
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_lu_timezone` (
  `TIMEZONE_ID` INT(11) NOT NULL,
  `TIMEZONE_NAME` VARCHAR(100) NOT NULL,
  `TIMEZONE_VALUE` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`TIMEZONE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_menu
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_menu` (
  `MENU_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `LABEL` VARCHAR(100) NULL DEFAULT NULL,
  `PARENT_ID` INT(11) NULL DEFAULT NULL,
  `SORT_ORDER` DECIMAL(4,0) NULL DEFAULT NULL,
  `ACTION` VARCHAR(200) NULL DEFAULT NULL,
  `FUNCTION_CD` VARCHAR(30) NULL DEFAULT NULL,
  `ACTIVE_YN` VARCHAR(1) NOT NULL DEFAULT 'Y',
  `SERVLET` VARCHAR(50) NULL DEFAULT NULL,
  `QUERY_STRING` VARCHAR(200) NULL DEFAULT NULL,
  `EXTERNAL_URL` VARCHAR(200) NULL DEFAULT NULL,
  `TARGET` VARCHAR(25) NULL DEFAULT NULL,
  `MENU_SET_CD` VARCHAR(10) NULL DEFAULT 'APP',
  `SEPARATOR_YN` CHAR(1) NULL DEFAULT 'N',
  `IMAGE_SRC` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`),
  INDEX `FK_FN_MENU_REF_196_FN_MENU` (`PARENT_ID` ASC),
  INDEX `FK_FN_MENU_MENU_SET_CD` (`MENU_SET_CD` ASC),
  INDEX `FN_MENU_FUNCTION_CD` (`FUNCTION_CD` ASC),
  CONSTRAINT `FK_FN_MENU_MENU_SET_CD`
    FOREIGN KEY (`MENU_SET_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_lu_menu_set` (`MENU_SET_CD`),
  CONSTRAINT `FK_FN_MENU_REF_196_FN_MENU`
    FOREIGN KEY (`PARENT_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_menu` (`MENU_ID`),
  CONSTRAINT `FK_FN_MENU_REF_223_FN_FUNCT`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_function` (`FUNCTION_CD`))
ENGINE = InnoDB
AUTO_INCREMENT = 150029
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_org
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_org` (
  `ORG_ID` INT(11) NOT NULL,
  `ORG_NAME` VARCHAR(50) NOT NULL,
  `ACCESS_CD` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`ORG_ID`),
  INDEX `FN_ORG_ACCESS_CD` (`ACCESS_CD` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_blob_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_blob_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `BLOB_DATA` BLOB NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  INDEX `SCHED_NAME` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC),
  CONSTRAINT `fn_qz_blob_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`)
    REFERENCES `vid_openecomp_epsdk`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_calendars
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_calendars` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `CALENDAR_NAME` VARCHAR(200) NOT NULL,
  `CALENDAR` BLOB NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_cron_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_cron_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `CRON_EXPRESSION` VARCHAR(120) NOT NULL,
  `TIME_ZONE_ID` VARCHAR(80) NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `fn_qz_cron_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`)
    REFERENCES `vid_openecomp_epsdk`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_fired_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_fired_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `ENTRY_ID` VARCHAR(95) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `INSTANCE_NAME` VARCHAR(200) NOT NULL,
  `FIRED_TIME` BIGINT(13) NOT NULL,
  `SCHED_TIME` BIGINT(13) NOT NULL,
  `PRIORITY` INT(11) NOT NULL,
  `STATE` VARCHAR(16) NOT NULL,
  `JOB_NAME` VARCHAR(200) NULL DEFAULT NULL,
  `JOB_GROUP` VARCHAR(200) NULL DEFAULT NULL,
  `IS_NONCONCURRENT` VARCHAR(1) NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1) NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`),
  INDEX `IDX_FN_QZ_FT_TRIG_INST_NAME` (`SCHED_NAME` ASC, `INSTANCE_NAME` ASC),
  INDEX `IDX_FN_QZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME` ASC, `INSTANCE_NAME` ASC, `REQUESTS_RECOVERY` ASC),
  INDEX `IDX_FN_QZ_FT_J_G` (`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `IDX_FN_QZ_FT_JG` (`SCHED_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `IDX_FN_QZ_FT_T_G` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC),
  INDEX `IDX_FN_QZ_FT_TG` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_job_details
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_job_details` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `JOB_NAME` VARCHAR(200) NOT NULL,
  `JOB_GROUP` VARCHAR(200) NOT NULL,
  `DESCRIPTION` VARCHAR(250) NULL DEFAULT NULL,
  `JOB_CLASS_NAME` VARCHAR(250) NOT NULL,
  `IS_DURABLE` VARCHAR(1) NOT NULL,
  `IS_NONCONCURRENT` VARCHAR(1) NOT NULL,
  `IS_UPDATE_DATA` VARCHAR(1) NOT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1) NOT NULL,
  `JOB_DATA` BLOB NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`),
  INDEX `IDX_FN_QZ_J_REQ_RECOVERY` (`SCHED_NAME` ASC, `REQUESTS_RECOVERY` ASC),
  INDEX `IDX_FN_QZ_J_GRP` (`SCHED_NAME` ASC, `JOB_GROUP` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_locks
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_locks` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `LOCK_NAME` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_paused_trigger_grps
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_paused_trigger_grps` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_scheduler_state
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_scheduler_state` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `INSTANCE_NAME` VARCHAR(200) NOT NULL,
  `LAST_CHECKIN_TIME` BIGINT(13) NOT NULL,
  `CHECKIN_INTERVAL` BIGINT(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_simple_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_simple_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `REPEAT_COUNT` BIGINT(7) NOT NULL,
  `REPEAT_INTERVAL` BIGINT(12) NOT NULL,
  `TIMES_TRIGGERED` BIGINT(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `fn_qz_simple_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`)
    REFERENCES `vid_openecomp_epsdk`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_simprop_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_simprop_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `STR_PROP_1` VARCHAR(512) NULL DEFAULT NULL,
  `STR_PROP_2` VARCHAR(512) NULL DEFAULT NULL,
  `STR_PROP_3` VARCHAR(512) NULL DEFAULT NULL,
  `INT_PROP_1` INT(11) NULL DEFAULT NULL,
  `INT_PROP_2` INT(11) NULL DEFAULT NULL,
  `LONG_PROP_1` BIGINT(20) NULL DEFAULT NULL,
  `LONG_PROP_2` BIGINT(20) NULL DEFAULT NULL,
  `DEC_PROP_1` DECIMAL(13,4) NULL DEFAULT NULL,
  `DEC_PROP_2` DECIMAL(13,4) NULL DEFAULT NULL,
  `BOOL_PROP_1` VARCHAR(1) NULL DEFAULT NULL,
  `BOOL_PROP_2` VARCHAR(1) NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `fn_qz_simprop_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`)
    REFERENCES `vid_openecomp_epsdk`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_qz_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_qz_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `JOB_NAME` VARCHAR(200) NOT NULL,
  `JOB_GROUP` VARCHAR(200) NOT NULL,
  `DESCRIPTION` VARCHAR(250) NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` BIGINT(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` BIGINT(13) NULL DEFAULT NULL,
  `PRIORITY` INT(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` VARCHAR(16) NOT NULL,
  `TRIGGER_TYPE` VARCHAR(8) NOT NULL,
  `START_TIME` BIGINT(13) NOT NULL,
  `END_TIME` BIGINT(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` VARCHAR(200) NULL DEFAULT NULL,
  `MISFIRE_INSTR` SMALLINT(2) NULL DEFAULT NULL,
  `JOB_DATA` BLOB NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  INDEX `IDX_FN_QZ_T_J` (`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `IDX_FN_QZ_T_JG` (`SCHED_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `IDX_FN_QZ_T_C` (`SCHED_NAME` ASC, `CALENDAR_NAME` ASC),
  INDEX `IDX_FN_QZ_T_G` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC),
  INDEX `IDX_FN_QZ_T_STATE` (`SCHED_NAME` ASC, `TRIGGER_STATE` ASC),
  INDEX `IDX_FN_QZ_T_N_STATE` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC),
  INDEX `IDX_FN_QZ_T_N_G_STATE` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC),
  INDEX `IDX_FN_QZ_T_NEXT_FIRE_TIME` (`SCHED_NAME` ASC, `NEXT_FIRE_TIME` ASC),
  INDEX `IDX_FN_QZ_T_NFT_ST` (`SCHED_NAME` ASC, `TRIGGER_STATE` ASC, `NEXT_FIRE_TIME` ASC),
  INDEX `IDX_FN_QZ_T_NFT_MISFIRE` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC),
  INDEX `IDX_FN_QZ_T_NFT_ST_MISFIRE` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_STATE` ASC),
  INDEX `IDX_FN_QZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC),
  CONSTRAINT `fn_qz_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `JOB_NAME` , `JOB_GROUP`)
    REFERENCES `vid_openecomp_epsdk`.`fn_qz_job_details` (`SCHED_NAME` , `JOB_NAME` , `JOB_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_restricted_url
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_restricted_url` (
  `RESTRICTED_URL` VARCHAR(250) NOT NULL,
  `FUNCTION_CD` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`RESTRICTED_URL`, `FUNCTION_CD`),
  INDEX `FK_RESTRICTED_URL_FUNCTION_CD` (`FUNCTION_CD` ASC),
  CONSTRAINT `FK_RESTRICTED_URL_FUNCTION_CD`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_function` (`FUNCTION_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_role` (
  `ROLE_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` VARCHAR(255) NOT NULL,
  `ACTIVE_YN` VARCHAR(1) NOT NULL DEFAULT 'Y',
  `PRIORITY` DECIMAL(4,0) NULL DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`))
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `vid_openecomp_epsdk`.`fn_role` MODIFY `ROLE_NAME` VARCHAR (255);


-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_role_composite
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_role_composite` (
  `PARENT_ROLE_ID` INT(11) NOT NULL,
  `CHILD_ROLE_ID` INT(11) NOT NULL,
  PRIMARY KEY (`PARENT_ROLE_ID`, `CHILD_ROLE_ID`),
  INDEX `FK_FN_ROLE_COMPOSITE_CHILD` (`CHILD_ROLE_ID` ASC),
  CONSTRAINT `FK_FN_ROLE_COMPOSITE_CHILD`
    FOREIGN KEY (`CHILD_ROLE_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `FK_FN_ROLE_COMPOSITE_PARENT`
    FOREIGN KEY (`PARENT_ROLE_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_role` (`ROLE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_role_function
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_role_function` (
  `ROLE_ID` INT(11) NOT NULL,
  `FUNCTION_CD` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`ROLE_ID`, `FUNCTION_CD`),
  INDEX `FN_ROLE_FUNCTION_FUNCTION_CD` (`FUNCTION_CD` ASC),
  INDEX `FN_ROLE_FUNCTION_ROLE_ID` (`ROLE_ID` ASC),
  CONSTRAINT `FK_FN_ROLE__REF_198_FN_ROLE`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `FK_FN_ROLE__REF_201_FN_FUNCT`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_function` (`FUNCTION_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_schedule_workflows
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_schedule_workflows` (
  `id_schedule_workflows` BIGINT(25) NOT NULL AUTO_INCREMENT,
  `workflow_server_url` VARCHAR(45) NULL DEFAULT NULL,
  `workflow_key` VARCHAR(45) NOT NULL,
  `workflow_arguments` VARCHAR(45) NULL DEFAULT NULL,
  `startDateTimeCron` VARCHAR(45) NULL DEFAULT NULL,
  `endDateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `start_date_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `recurrence` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_schedule_workflows`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_tab
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_tab` (
  `TAB_CD` VARCHAR(30) NOT NULL,
  `TAB_NAME` VARCHAR(50) NOT NULL,
  `TAB_DESCR` VARCHAR(100) NULL DEFAULT NULL,
  `ACTION` VARCHAR(100) NOT NULL,
  `FUNCTION_CD` VARCHAR(30) NOT NULL,
  `ACTIVE_YN` CHAR(1) NOT NULL,
  `SORT_ORDER` DECIMAL(11,0) NOT NULL,
  `PARENT_TAB_CD` VARCHAR(30) NULL DEFAULT NULL,
  `TAB_SET_CD` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`TAB_CD`),
  INDEX `FK_FN_TAB_FUNCTION_CD` (`FUNCTION_CD` ASC),
  INDEX `FK_FN_TAB_SET_CD` (`TAB_SET_CD` ASC),
  CONSTRAINT `FK_FN_TAB_FUNCTION_CD`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_function` (`FUNCTION_CD`),
  CONSTRAINT `FK_FN_TAB_SET_CD`
    FOREIGN KEY (`TAB_SET_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_lu_tab_set` (`TAB_SET_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_tab_selected
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_tab_selected` (
  `SELECTED_TAB_CD` VARCHAR(30) NOT NULL,
  `TAB_URI` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`SELECTED_TAB_CD`, `TAB_URI`),
  CONSTRAINT `FK_FN_TAB_SELECTED_TAB_CD`
    FOREIGN KEY (`SELECTED_TAB_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_tab` (`TAB_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_user
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_user` (
  `USER_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` INT(11) NULL DEFAULT NULL,
  `MANAGER_ID` INT(11) NULL DEFAULT NULL,
  `FIRST_NAME` VARCHAR(50) NULL DEFAULT NULL,
  `MIDDLE_NAME` VARCHAR(50) NULL DEFAULT NULL,
  `LAST_NAME` VARCHAR(50) NULL DEFAULT NULL,
  `PHONE` VARCHAR(25) NULL DEFAULT NULL,
  `FAX` VARCHAR(25) NULL DEFAULT NULL,
  `CELLULAR` VARCHAR(25) NULL DEFAULT NULL,
  `EMAIL` VARCHAR(50) NULL DEFAULT NULL,
  `ADDRESS_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `ALERT_METHOD_CD` VARCHAR(10) NULL DEFAULT NULL,
  `HRID` VARCHAR(20) NULL DEFAULT NULL,
  `ORG_USER_ID` VARCHAR(20) NULL DEFAULT NULL,
  `ORG_CODE` VARCHAR(30) NULL DEFAULT NULL,
  `LOGIN_ID` VARCHAR(25) NULL DEFAULT NULL,
  `LOGIN_PWD` VARCHAR(25) NULL DEFAULT NULL,
  `LAST_LOGIN_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ACTIVE_YN` VARCHAR(1) NOT NULL DEFAULT 'Y',
  `CREATED_ID` INT(11) NULL DEFAULT NULL,
  `CREATED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_ID` INT(11) NULL DEFAULT NULL,
  `MODIFIED_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `IS_INTERNAL_YN` CHAR(1) NOT NULL DEFAULT 'N',
  `ADDRESS_LINE_1` VARCHAR(100) NULL DEFAULT NULL,
  `ADDRESS_LINE_2` VARCHAR(100) NULL DEFAULT NULL,
  `CITY` VARCHAR(50) NULL DEFAULT NULL,
  `STATE_CD` VARCHAR(3) NULL DEFAULT NULL,
  `ZIP_CODE` VARCHAR(11) NULL DEFAULT NULL,
  `COUNTRY_CD` VARCHAR(3) NULL DEFAULT NULL,
  `LOCATION_CLLI` VARCHAR(8) NULL DEFAULT NULL,
  `ORG_MANAGER_USERID` VARCHAR(20) NULL DEFAULT NULL,
  `COMPANY` VARCHAR(100) NULL DEFAULT NULL,
  `DEPARTMENT_NAME` VARCHAR(100) NULL DEFAULT NULL,
  `JOB_TITLE` VARCHAR(100) NULL DEFAULT NULL,
  `TIMEZONE` INT(11) NULL DEFAULT NULL,
  `DEPARTMENT` VARCHAR(25) NULL DEFAULT NULL,
  `BUSINESS_UNIT` VARCHAR(25) NULL DEFAULT NULL,
  `BUSINESS_UNIT_NAME` VARCHAR(100) NULL DEFAULT NULL,
  `COST_CENTER` VARCHAR(25) NULL DEFAULT NULL,
  `FIN_LOC_CODE` VARCHAR(10) NULL DEFAULT NULL,
  `SILO_STATUS` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE INDEX `FN_USER_HRID` (`HRID` ASC),
  UNIQUE INDEX `FN_USER_LOGIN_ID` (`LOGIN_ID` ASC),
  INDEX `FN_USER_ADDRESS_ID` (`ADDRESS_ID` ASC),
  INDEX `FN_USER_ALERT_METHOD_CD` (`ALERT_METHOD_CD` ASC),
  INDEX `FN_USER_ORG_ID` (`ORG_ID` ASC),
  INDEX `FK_FN_USER_REF_197_FN_USER` (`MANAGER_ID` ASC),
  INDEX `FK_FN_USER_REF_198_FN_USER` (`CREATED_ID` ASC),
  INDEX `FK_FN_USER_REF_199_FN_USER` (`MODIFIED_ID` ASC),
  INDEX `FK_TIMEZONE` (`TIMEZONE` ASC),
  CONSTRAINT `FK_FN_USER_REF_110_FN_ORG`
    FOREIGN KEY (`ORG_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_org` (`ORG_ID`),
  CONSTRAINT `FK_FN_USER_REF_123_FN_LU_AL`
    FOREIGN KEY (`ALERT_METHOD_CD`)
    REFERENCES `vid_openecomp_epsdk`.`fn_lu_alert_method` (`ALERT_METHOD_CD`),
  CONSTRAINT `FK_FN_USER_REF_197_FN_USER`
    FOREIGN KEY (`MANAGER_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_user` (`USER_ID`),
  CONSTRAINT `FK_FN_USER_REF_198_FN_USER`
    FOREIGN KEY (`CREATED_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_user` (`USER_ID`),
  CONSTRAINT `FK_FN_USER_REF_199_FN_USER`
    FOREIGN KEY (`MODIFIED_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_user` (`USER_ID`),
  CONSTRAINT `FK_TIMEZONE`
    FOREIGN KEY (`TIMEZONE`)
    REFERENCES `vid_openecomp_epsdk`.`fn_lu_timezone` (`TIMEZONE_ID`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_user_pseudo_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_user_pseudo_role` (
  `PSEUDO_ROLE_ID` INT(11) NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  PRIMARY KEY (`PSEUDO_ROLE_ID`, `USER_ID`),
  INDEX `FK_PSEUDO_ROLE_USER_ID` (`USER_ID` ASC),
  CONSTRAINT `FK_PSEUDO_ROLE_PSEUDO_ROLE_ID`
    FOREIGN KEY (`PSEUDO_ROLE_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `FK_PSEUDO_ROLE_USER_ID`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_user` (`USER_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_user_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_user_role` (
  `USER_ID` INT(10) NOT NULL,
  `ROLE_ID` INT(10) NOT NULL,
  `PRIORITY` DECIMAL(4,0) NULL DEFAULT NULL,
  `APP_ID` INT(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`USER_ID`, `ROLE_ID`, `APP_ID`),
  INDEX `FN_USER_ROLE_ROLE_ID` (`ROLE_ID` ASC),
  INDEX `FN_USER_ROLE_USER_ID` (`USER_ID` ASC),
  INDEX `FK_FN_USER__REF_178_FN_APP_idx` (`APP_ID` ASC),
  CONSTRAINT `FK_FN_USER__REF_172_FN_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_user` (`USER_ID`),
  CONSTRAINT `FK_FN_USER__REF_175_FN_ROLE`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `FK_FN_USER__REF_178_FN_APP`
    FOREIGN KEY (`APP_ID`)
    REFERENCES `vid_openecomp_epsdk`.`fn_app` (`APP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_workflow
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_workflow` (
  `id` MEDIUMINT(9) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `description` VARCHAR(500) NULL DEFAULT NULL,
  `run_link` VARCHAR(300) NULL DEFAULT NULL,
  `suspend_link` VARCHAR(300) NULL DEFAULT NULL,
  `modified_link` VARCHAR(300) NULL DEFAULT NULL,
  `active_yn` VARCHAR(300) NULL DEFAULT NULL,
  `created` VARCHAR(300) NULL DEFAULT NULL,
  `created_by` INT(11) NULL DEFAULT NULL,
  `modified` VARCHAR(300) NULL DEFAULT NULL,
  `modified_by` INT(11) NULL DEFAULT NULL,
  `workflow_key` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name` (`name` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.fn_xmltype
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`fn_xmltype` (
  `ID` DECIMAL(10,0) NOT NULL,
  `XML_DOCUMENT` TEXT NULL DEFAULT NULL,
  UNIQUE INDEX `FN_XMLTYPE_ID` (`ID` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.schema_info
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`schema_info` (
  `SCHEMA_ID` VARCHAR(25) NOT NULL,
  `SCHEMA_DESC` VARCHAR(75) NOT NULL,
  `DATASOURCE_TYPE` VARCHAR(100) NULL DEFAULT NULL,
  `CONNECTION_URL` VARCHAR(200) NOT NULL,
  `USER_NAME` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(45) NULL DEFAULT NULL,
  `DRIVER_CLASS` VARCHAR(100) NOT NULL,
  `MIN_POOL_SIZE` INT(11) NOT NULL,
  `MAX_POOL_SIZE` INT(11) NOT NULL,
  `IDLE_CONNECTION_TEST_PERIOD` INT(11) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.vid_vnf
-- ----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`vid_vnf` (
  `VNF_DB_ID` int(11) NOT NULL AUTO_INCREMENT,
  `VNF_APP_UUID` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `VNF_APP_INVARIANT_UUID` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`VNF_DB_ID`),
  UNIQUE KEY `vid_vnf_VNF_ID_uindex` (`VNF_APP_UUID`,`VNF_APP_INVARIANT_UUID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.vid_workflow
-- ----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`vid_workflow` (
  `WORKFLOW_DB_ID` int(11) NOT NULL AUTO_INCREMENT,
  `WORKFLOW_APP_NAME` varchar(50) COLLATE utf8_bin NOT NULL,
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`WORKFLOW_DB_ID`),
  UNIQUE KEY `vid_workflow_workflow_uuid_uindex` (`WORKFLOW_APP_NAME`),
  UNIQUE KEY `vid_workflow_WORKFLOW_ID_uindex` (`WORKFLOW_DB_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.vid_vnf_workflow
-- ----------------------------------------------------------------------------


CREATE TABLE IF NOT EXISTS `vid_openecomp_epsdk`.`vid_vnf_workflow` (
  `VNF_DB_ID` int(11) NOT NULL,
  `WORKFLOW_DB_ID` int(11) NOT NULL,
  KEY `vid_vnf_workflow_vid_vnf_VND_ID_fk` (`VNF_DB_ID`),
  KEY `vid_vnf_workflow_vid_workflow_WORKFLOW_ID_fk` (`WORKFLOW_DB_ID`),
  CONSTRAINT `vid_vnf_workflow_vid_vnf_VND_ID_fk` FOREIGN KEY (`VNF_DB_ID`) REFERENCES `vid_vnf` (`VNF_DB_ID`),
  CONSTRAINT `vid_vnf_workflow_vid_workflow_WORKFLOW_ID_fk` FOREIGN KEY (`WORKFLOW_DB_ID`) REFERENCES `vid_openecomp_epsdk`.`vid_workflow` (`WORKFLOW_DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.vid_category_parameter
-- ----------------------------------------------------------------------------


CREATE TABLE IF NOT EXISTS `vid_category_parameter` (
	`CATEGORY_ID` INT(11) NOT NULL AUTO_INCREMENT,
	`NAME` VARCHAR(255) NULL COLLATE 'utf8_bin',
	`ID_SUPPORTED` TINYINT(1) NOT NULL DEFAULT '0',
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`CATEGORY_ID`)
) COLLATE='utf8_bin' ENGINE=InnoDB AUTO_INCREMENT=5;

-------------------------------------------------------------------------------

ALTER TABLE `vid_category_parameter`
	ADD COLUMN if not exists `FAMILY` ENUM('PARAMETER_STANDARDIZATION','TENANT_ISOLATION') NOT NULL DEFAULT 'PARAMETER_STANDARDIZATION' AFTER `ID_SUPPORTED`;
-- ----------------------------------------------------------------------------
-- Table vid_openecomp_epsdk.vid_category_parameter
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_category_parameter_option` (
  `CATEGORY_OPT_DB_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `CATEGORY_OPT_APP_ID` VARCHAR(50) NOT NULL COLLATE 'utf8_bin',
  `NAME` VARCHAR(50) NULL COLLATE 'utf8_bin',
  `CATEGORY_ID` INT(11) NOT NULL DEFAULT '0',
  `CREATED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CATEGORY_OPT_DB_ID`),
  UNIQUE INDEX `APP_ID_CATEGORY_UNIQUE` (`CATEGORY_ID`, `CATEGORY_OPT_APP_ID`),
  UNIQUE INDEX `NAME_CATEGORY_UNIQUE` (`CATEGORY_ID`, `NAME`),
  CONSTRAINT `FK_OWNING_ENTITY_OPTIONS_TO_OE` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `vid_openecomp_epsdk`.`vid_category_parameter` (`CATEGORY_ID`)
) COLLATE='utf8_bin' ENGINE=InnoDB AUTO_INCREMENT=25;

CREATE TABLE IF NOT EXISTS `vid_job` (
  `JOB_ID`        BINARY(16)   NOT NULL PRIMARY KEY,
  `CREATED_DATE`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `JOB_STATUS`    VARCHAR(50)  NULL COLLATE 'utf8_bin',
  `JOB_TYPE`      VARCHAR(50)  NULL COLLATE 'utf8_bin',
  `JOB_DATA`      MEDIUMTEXT   NULL COLLATE 'utf8_bin',
  `PARENT_JOB_ID` BINARY(16)   NULL,
  `TAKEN_BY`      VARCHAR(100) NULL COLLATE 'utf8_bin',
  CONSTRAINT `FK_OWNING_VID_JOB_PARENT` FOREIGN KEY (`PARENT_JOB_ID`) REFERENCES `vid_openecomp_epsdk`.`vid_job` (`JOB_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `vid_job`
  ADD COLUMN if NOT EXISTS `TAKEN_BY` VARCHAR (100) COLLATE 'utf8_bin';
-- ----------------------------------------------------------------------------
-- View vid_openecomp_epsdk.v_url_access
-- ----------------------------------------------------------------------------
CREATE OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_url_access` AS select distinct `m`.`ACTION` AS `URL`,`m`.`FUNCTION_CD` AS `FUNCTION_CD` from `fn_menu` `m` where (`m`.`ACTION` is not null) union select distinct `t`.`ACTION` AS `URL`,`t`.`FUNCTION_CD` AS `FUNCTION_CD` from `fn_tab` `t` where (`t`.`ACTION` is not null) union select `r`.`RESTRICTED_URL` AS `URL`,`r`.`FUNCTION_CD` AS `FUNCTION_CD` from `fn_restricted_url` `r`;
SET FOREIGN_KEY_CHECKS = 1;