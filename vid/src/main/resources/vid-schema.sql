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
-- Migrated Schemata: vid_openecomp
-- Source Schemata: ecomp_sd
-- Created: Sun Nov 13 08:58:53 2016
-- Workbench Version: 6.3.6
-- ----------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------------------------------------
-- Schema vid_openecomp
-- ----------------------------------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vid_openecomp` ;

USE vid_openecomp;
-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_favorite_reports
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_favorite_reports` (
  `USER_ID` INT(11) NOT NULL,
  `REP_ID` INT(11) NOT NULL,
  PRIMARY KEY (`USER_ID`, `REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_filehist_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_filehist_log` (
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `URL` VARCHAR(4000) NULL DEFAULT NULL,
  `NOTES` VARCHAR(3500) NULL DEFAULT NULL,
  `RUN_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_folder
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_folder` (
  `FOLDER_ID` INT(11) NOT NULL,
  `FOLDER_NAME` VARCHAR(50) NOT NULL,
  `DESCR` VARCHAR(500) NULL DEFAULT NULL,
  `CREATE_ID` INT(11) NOT NULL,
  `CREATE_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PARENT_FOLDER_ID` INT(11) NULL DEFAULT NULL,
  `PUBLIC_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`FOLDER_ID`),
  INDEX `fk_parent_key_cr_folder` (`PARENT_FOLDER_ID` ASC),
  CONSTRAINT `fk_parent_key_cr_folder`
    FOREIGN KEY (`PARENT_FOLDER_ID`)
    REFERENCES `vid_openecomp`.`cr_folder` (`FOLDER_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_folder_access
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_folder_access` (
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
-- Table vid_openecomp.cr_hist_user_map
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_hist_user_map` (
  `HIST_ID` INT(11) NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  PRIMARY KEY (`HIST_ID`, `USER_ID`),
  INDEX `sys_c0014617` (`USER_ID` ASC),
  CONSTRAINT `sys_c0014616`
    FOREIGN KEY (`HIST_ID`)
    REFERENCES `vid_openecomp`.`cr_report_file_history` (`HIST_ID`),
  CONSTRAINT `sys_c0014617`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp`.`fn_user` (`USER_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_lu_file_type
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_lu_file_type` (
  `LOOKUP_ID` DECIMAL(2,0) NOT NULL,
  `LOOKUP_DESCR` VARCHAR(255) NOT NULL,
  `ACTIVE_YN` CHAR(1) NULL DEFAULT 'Y',
  `ERROR_CODE` DECIMAL(11,0) NULL DEFAULT NULL,
  PRIMARY KEY (`LOOKUP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_raptor_action_img
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_raptor_action_img` (
  `IMAGE_ID` VARCHAR(100) NOT NULL,
  `IMAGE_LOC` VARCHAR(400) NULL DEFAULT NULL,
  PRIMARY KEY (`IMAGE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_raptor_pdf_img
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_raptor_pdf_img` (
  `IMAGE_ID` VARCHAR(100) NOT NULL,
  `IMAGE_LOC` VARCHAR(400) NULL DEFAULT NULL,
  PRIMARY KEY (`IMAGE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_remote_schema_info
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_remote_schema_info` (
  `SCHEMA_PREFIX` VARCHAR(5) NOT NULL,
  `SCHEMA_DESC` VARCHAR(75) NOT NULL,
  `DATASOURCE_TYPE` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`SCHEMA_PREFIX`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report` (
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
  INDEX `cr_report_create_idpublic_yntitle` (`CREATE_ID` ASC, `PUBLIC_YN` ASC, `TITLE` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_access
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_access` (
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `ORDER_NO` DECIMAL(11,0) NOT NULL,
  `ROLE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `USER_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `READ_ONLY_YN` VARCHAR(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`REP_ID`, `ORDER_NO`),
  CONSTRAINT `fk_cr_repor_ref_8550_cr_repor`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_dwnld_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_dwnld_log` (
  `USER_ID` DECIMAL(11,0) NOT NULL,
  `REP_ID` INT(11) NOT NULL,
  `FILE_NAME` VARCHAR(100) NOT NULL,
  `DWNLD_START_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `RECORD_READY_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `FILTER_PARAMS` VARCHAR(2000) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_email_sent_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_email_sent_log` (
  `LOG_ID` INT(11) NOT NULL,
  `SCHEDULE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `GEN_KEY` VARCHAR(25) NOT NULL,
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `USER_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `SENT_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ACCESS_FLAG` VARCHAR(1) NOT NULL DEFAULT 'Y',
  `TOUCH_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`LOG_ID`),
  INDEX `fk_cr_report_rep_id` (`REP_ID` ASC),
  CONSTRAINT `fk_cr_report_rep_id`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_file_history
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_file_history` (
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
  INDEX `sys_c0014614` (`FILE_TYPE_ID` ASC),
  INDEX `sys_c0014615` (`REP_ID` ASC),
  CONSTRAINT `sys_c0014614`
    FOREIGN KEY (`FILE_TYPE_ID`)
    REFERENCES `vid_openecomp`.`cr_lu_file_type` (`LOOKUP_ID`),
  CONSTRAINT `sys_c0014615`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_log` (
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `LOG_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `USER_ID` DECIMAL(11,0) NOT NULL,
  `ACTION` VARCHAR(2000) NOT NULL,
  `ACTION_VALUE` VARCHAR(50) NULL DEFAULT NULL,
  `FORM_FIELDS` VARCHAR(4000) NULL DEFAULT NULL,
  INDEX `fk_cr_repor_ref_17645_cr_repor` (`REP_ID` ASC),
  CONSTRAINT `fk_cr_repor_ref_17645_cr_repor`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_schedule
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_schedule` (
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
  INDEX `fk_cr_repor_ref_14707_cr_repor` (`REP_ID` ASC),
  CONSTRAINT `fk_cr_repor_ref_14707_cr_repor`
    FOREIGN KEY (`REP_ID`)
    REFERENCES `vid_openecomp`.`cr_report` (`REP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_schedule_users
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_schedule_users` (
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `REP_ID` DECIMAL(11,0) NOT NULL,
  `USER_ID` DECIMAL(11,0) NOT NULL,
  `ROLE_ID` DECIMAL(11,0) NULL DEFAULT NULL,
  `ORDER_NO` DECIMAL(11,0) NOT NULL,
  PRIMARY KEY (`SCHEDULE_ID`, `REP_ID`, `USER_ID`, `ORDER_NO`),
  CONSTRAINT `fk_cr_repor_ref_14716_cr_repor`
    FOREIGN KEY (`SCHEDULE_ID`)
    REFERENCES `vid_openecomp`.`cr_report_schedule` (`SCHEDULE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_report_template_map
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_report_template_map` (
  `REPORT_ID` INT(11) NOT NULL,
  `TEMPLATE_FILE` VARCHAR(200) NULL DEFAULT NULL,
  PRIMARY KEY (`REPORT_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_schedule_activity_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_schedule_activity_log` (
  `SCHEDULE_ID` DECIMAL(11,0) NOT NULL,
  `URL` VARCHAR(4000) NULL DEFAULT NULL,
  `NOTES` VARCHAR(2000) NULL DEFAULT NULL,
  `RUN_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_table_join
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_table_join` (
  `SRC_TABLE_NAME` VARCHAR(30) NOT NULL,
  `DEST_TABLE_NAME` VARCHAR(30) NOT NULL,
  `JOIN_EXPR` VARCHAR(500) NOT NULL,
  INDEX `cr_table_join_dest_table_name` (`DEST_TABLE_NAME` ASC),
  INDEX `cr_table_join_src_table_name` (`SRC_TABLE_NAME` ASC),
  CONSTRAINT `fk_cr_table_ref_311_cr_tab`
    FOREIGN KEY (`SRC_TABLE_NAME`)
    REFERENCES `vid_openecomp`.`cr_table_source` (`TABLE_NAME`),
  CONSTRAINT `fk_cr_table_ref_315_cr_tab`
    FOREIGN KEY (`DEST_TABLE_NAME`)
    REFERENCES `vid_openecomp`.`cr_table_source` (`TABLE_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_table_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_table_role` (
  `TABLE_NAME` VARCHAR(30) NOT NULL,
  `ROLE_ID` DECIMAL(11,0) NOT NULL,
  PRIMARY KEY (`TABLE_NAME`, `ROLE_ID`),
  CONSTRAINT `fk_cr_table_ref_32384_cr_table`
    FOREIGN KEY (`TABLE_NAME`)
    REFERENCES `vid_openecomp`.`cr_table_source` (`TABLE_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.cr_table_source
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`cr_table_source` (
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
-- Table vid_openecomp.demo_bar_chart
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`demo_bar_chart` (
  `label` VARCHAR(20) NULL DEFAULT NULL,
  `value` DECIMAL(25,15) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.demo_bar_chart_inter
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`demo_bar_chart_inter` (
  `spam_date` DATE NULL DEFAULT NULL,
  `num_rpt_sources` DECIMAL(10,0) NULL DEFAULT NULL,
  `num_det_sources` DECIMAL(10,0) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.demo_line_chart
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`demo_line_chart` (
  `series` VARCHAR(20) NULL DEFAULT NULL,
  `log_date` DATE NULL DEFAULT NULL,
  `data_value` DECIMAL(10,5) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.demo_pie_chart
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`demo_pie_chart` (
  `legend` VARCHAR(20) NULL DEFAULT NULL,
  `data_value` DECIMAL(10,5) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.demo_scatter_chart
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`demo_scatter_chart` (
  `rainfall` DECIMAL(10,2) NULL DEFAULT NULL,
  `key_value` VARCHAR(20) NULL DEFAULT NULL,
  `measurements` DECIMAL(10,2) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.demo_scatter_plot
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`demo_scatter_plot` (
  `SERIES` VARCHAR(20) NULL DEFAULT NULL,
  `VALUEX` DECIMAL(25,15) NULL DEFAULT NULL,
  `VALUEY` DECIMAL(25,15) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.demo_util_chart
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`demo_util_chart` (
  `traffic_date` DATE NULL DEFAULT NULL,
  `util_perc` DECIMAL(10,5) NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_app
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_app` (
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
-- Table vid_openecomp.fn_audit_action
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_audit_action` (
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
-- Table vid_openecomp.fn_audit_action_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_audit_action_log` (
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
-- Table vid_openecomp.fn_audit_log
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_audit_log` (
  `LOG_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` INT(11) NOT NULL,
  `ACTIVITY_CD` VARCHAR(50) NOT NULL,
  `AUDIT_DATE` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `COMMENTS` VARCHAR(1000) NULL DEFAULT NULL,
  `AFFECTED_RECORD_ID_BK` VARCHAR(500) NULL DEFAULT NULL,
  `AFFECTED_RECORD_ID` VARCHAR(4000) NULL DEFAULT NULL,
  PRIMARY KEY (`LOG_ID`),
  INDEX `fn_audit_log_activity_cd` (`ACTIVITY_CD` ASC),
  INDEX `fn_audit_log_user_id` (`USER_ID` ASC),
  CONSTRAINT `FK_FN_AUDIT_REF_209_FN_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp`.`fn_user` (`USER_ID`),
  CONSTRAINT `fk_fn_audit_ref_205_fn_lu_ac`
    FOREIGN KEY (`ACTIVITY_CD`)
    REFERENCES `vid_openecomp`.`fn_lu_activity` (`ACTIVITY_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_broadcast_message
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_broadcast_message` (
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
-- Table vid_openecomp.fn_chat_logs
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_chat_logs` (
  `CHAT_LOG_ID` INT(11) NOT NULL,
  `CHAT_ROOM_ID` INT(11) NULL DEFAULT NULL,
  `USER_ID` INT(11) NULL DEFAULT NULL,
  `MESSAGE` VARCHAR(1000) NULL DEFAULT NULL,
  `MESSAGE_DATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CHAT_LOG_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_chat_room
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_chat_room` (
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
-- Table vid_openecomp.fn_chat_users
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_chat_users` (
  `CHAT_ROOM_ID` INT(11) NULL DEFAULT NULL,
  `USER_ID` INT(11) NULL DEFAULT NULL,
  `LAST_ACTIVITY_DATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CHAT_STATUS` VARCHAR(20) NULL DEFAULT NULL,
  `ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_datasource
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_datasource` (
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
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_function
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_function` (
  `FUNCTION_CD` VARCHAR(30) NOT NULL,
  `FUNCTION_NAME` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`FUNCTION_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_activity
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_activity` (
  `ACTIVITY_CD` VARCHAR(50) NOT NULL,
  `ACTIVITY` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ACTIVITY_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_alert_method
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_alert_method` (
  `ALERT_METHOD_CD` VARCHAR(10) NOT NULL,
  `ALERT_METHOD` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ALERT_METHOD_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_broadcast_site
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_broadcast_site` (
  `BROADCAST_SITE_CD` VARCHAR(50) NOT NULL,
  `BROADCAST_SITE_DESCR` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`BROADCAST_SITE_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_menu_set
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_menu_set` (
  `MENU_SET_CD` VARCHAR(10) NOT NULL,
  `MENU_SET_NAME` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`MENU_SET_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_message_location
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_message_location` (
  `message_location_id` DECIMAL(11,0) NOT NULL,
  `message_location_descr` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`message_location_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_priority
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_priority` (
  `PRIORITY_ID` DECIMAL(11,0) NOT NULL,
  `PRIORITY` VARCHAR(50) NOT NULL,
  `ACTIVE_YN` CHAR(1) NOT NULL,
  `SORT_ORDER` DECIMAL(5,0) NULL DEFAULT NULL,
  PRIMARY KEY (`PRIORITY_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_role_type
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_role_type` (
  `ROLE_TYPE_ID` DECIMAL(11,0) NOT NULL,
  `ROLE_TYPE` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ROLE_TYPE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_tab_set
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_tab_set` (
  `TAB_SET_CD` VARCHAR(30) NOT NULL,
  `TAB_SET_NAME` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`TAB_SET_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_lu_timezone
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_lu_timezone` (
  `TIMEZONE_ID` INT(11) NOT NULL,
  `TIMEZONE_NAME` VARCHAR(100) NOT NULL,
  `TIMEZONE_VALUE` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`TIMEZONE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_menu
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_menu` (
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
  INDEX `fn_menu_function_cd` (`FUNCTION_CD` ASC),
  CONSTRAINT `FK_FN_MENU_MENU_SET_CD`
    FOREIGN KEY (`MENU_SET_CD`)
    REFERENCES `vid_openecomp`.`fn_lu_menu_set` (`MENU_SET_CD`),
  CONSTRAINT `FK_FN_MENU_REF_196_FN_MENU`
    FOREIGN KEY (`PARENT_ID`)
    REFERENCES `vid_openecomp`.`fn_menu` (`MENU_ID`),
  CONSTRAINT `FK_FN_MENU_REF_223_FN_FUNCT`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp`.`fn_function` (`FUNCTION_CD`))
ENGINE = InnoDB
AUTO_INCREMENT = 150039
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_org
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_org` (
  `ORG_ID` INT(11) NOT NULL,
  `ORG_NAME` VARCHAR(50) NOT NULL,
  `ACCESS_CD` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`ORG_ID`),
  INDEX `fn_org_access_cd` (`ACCESS_CD` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_blob_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_blob_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `BLOB_DATA` BLOB NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  INDEX `SCHED_NAME` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC),
  CONSTRAINT `fn_qz_blob_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`)
    REFERENCES `vid_openecomp`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_calendars
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_calendars` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `CALENDAR_NAME` VARCHAR(200) NOT NULL,
  `CALENDAR` BLOB NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_cron_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_cron_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `CRON_EXPRESSION` VARCHAR(120) NOT NULL,
  `TIME_ZONE_ID` VARCHAR(80) NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `fn_qz_cron_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`)
    REFERENCES `vid_openecomp`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_fired_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_fired_triggers` (
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
  INDEX `idx_fn_qz_ft_trig_inst_name` (`SCHED_NAME` ASC, `INSTANCE_NAME` ASC),
  INDEX `idx_fn_qz_ft_inst_job_req_rcvry` (`SCHED_NAME` ASC, `INSTANCE_NAME` ASC, `REQUESTS_RECOVERY` ASC),
  INDEX `idx_fn_qz_ft_j_g` (`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `idx_fn_qz_ft_jg` (`SCHED_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `idx_fn_qz_ft_t_g` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC),
  INDEX `idx_fn_qz_ft_tg` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_job_details
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_job_details` (
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
  INDEX `idx_fn_qz_j_req_recovery` (`SCHED_NAME` ASC, `REQUESTS_RECOVERY` ASC),
  INDEX `idx_fn_qz_j_grp` (`SCHED_NAME` ASC, `JOB_GROUP` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_locks
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_locks` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `LOCK_NAME` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_paused_trigger_grps
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_paused_trigger_grps` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_scheduler_state
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_scheduler_state` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `INSTANCE_NAME` VARCHAR(200) NOT NULL,
  `LAST_CHECKIN_TIME` BIGINT(13) NOT NULL,
  `CHECKIN_INTERVAL` BIGINT(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_simple_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_simple_triggers` (
  `SCHED_NAME` VARCHAR(120) NOT NULL,
  `TRIGGER_NAME` VARCHAR(200) NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) NOT NULL,
  `REPEAT_COUNT` BIGINT(7) NOT NULL,
  `REPEAT_INTERVAL` BIGINT(12) NOT NULL,
  `TIMES_TRIGGERED` BIGINT(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`),
  CONSTRAINT `fn_qz_simple_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`)
    REFERENCES `vid_openecomp`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_simprop_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_simprop_triggers` (
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
    REFERENCES `vid_openecomp`.`fn_qz_triggers` (`SCHED_NAME` , `TRIGGER_NAME` , `TRIGGER_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_qz_triggers
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_qz_triggers` (
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
  INDEX `idx_fn_qz_t_j` (`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `idx_fn_qz_t_jg` (`SCHED_NAME` ASC, `JOB_GROUP` ASC),
  INDEX `idx_fn_qz_t_c` (`SCHED_NAME` ASC, `CALENDAR_NAME` ASC),
  INDEX `idx_fn_qz_t_g` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC),
  INDEX `idx_fn_qz_t_state` (`SCHED_NAME` ASC, `TRIGGER_STATE` ASC),
  INDEX `idx_fn_qz_t_n_state` (`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC),
  INDEX `idx_fn_qz_t_n_g_state` (`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC),
  INDEX `idx_fn_qz_t_next_fire_time` (`SCHED_NAME` ASC, `NEXT_FIRE_TIME` ASC),
  INDEX `idx_fn_qz_t_nft_st` (`SCHED_NAME` ASC, `TRIGGER_STATE` ASC, `NEXT_FIRE_TIME` ASC),
  INDEX `idx_fn_qz_t_nft_misfire` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC),
  INDEX `idx_fn_qz_t_nft_st_misfire` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_STATE` ASC),
  INDEX `idx_fn_qz_t_nft_st_misfire_grp` (`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_GROUP` ASC, 

`TRIGGER_STATE` ASC),
  CONSTRAINT `fn_qz_triggers_ibfk_1`
    FOREIGN KEY (`SCHED_NAME` , `JOB_NAME` , `JOB_GROUP`)
    REFERENCES `vid_openecomp`.`fn_qz_job_details` (`SCHED_NAME` , `JOB_NAME` , `JOB_GROUP`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_restricted_url
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_restricted_url` (
  `RESTRICTED_URL` VARCHAR(250) NOT NULL,
  `FUNCTION_CD` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`RESTRICTED_URL`, `FUNCTION_CD`),
  INDEX `fk_restricted_url_function_cd` (`FUNCTION_CD` ASC),
  CONSTRAINT `fk_restricted_url_function_cd`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp`.`fn_function` (`FUNCTION_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_role` (
  `ROLE_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ROLE_NAME` VARCHAR(50) NOT NULL,
  `ACTIVE_YN` VARCHAR(1) NOT NULL DEFAULT 'Y',
  `PRIORITY` DECIMAL(4,0) NULL DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`))
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_role_composite
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_role_composite` (
  `PARENT_ROLE_ID` INT(11) NOT NULL,
  `CHILD_ROLE_ID` INT(11) NOT NULL,
  PRIMARY KEY (`PARENT_ROLE_ID`, `CHILD_ROLE_ID`),
  INDEX `FK_FN_ROLE_COMPOSITE_CHILD` (`CHILD_ROLE_ID` ASC),
  CONSTRAINT `FK_FN_ROLE_COMPOSITE_CHILD`
    FOREIGN KEY (`CHILD_ROLE_ID`)
    REFERENCES `vid_openecomp`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `FK_FN_ROLE_COMPOSITE_PARENT`
    FOREIGN KEY (`PARENT_ROLE_ID`)
    REFERENCES `vid_openecomp`.`fn_role` (`ROLE_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_role_function
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_role_function` (
  `ROLE_ID` INT(11) NOT NULL,
  `FUNCTION_CD` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`ROLE_ID`, `FUNCTION_CD`),
  INDEX `fn_role_function_function_cd` (`FUNCTION_CD` ASC),
  INDEX `fn_role_function_role_id` (`ROLE_ID` ASC),
  CONSTRAINT `FK_FN_ROLE__REF_198_FN_ROLE`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `vid_openecomp`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `fk_fn_role__ref_201_fn_funct`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp`.`fn_function` (`FUNCTION_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_schedule_workflows
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_schedule_workflows` (
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
-- Table vid_openecomp.fn_tab
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_tab` (
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
  INDEX `fk_fn_tab_function_cd` (`FUNCTION_CD` ASC),
  INDEX `fk_fn_tab_set_cd` (`TAB_SET_CD` ASC),
  CONSTRAINT `fk_fn_tab_function_cd`
    FOREIGN KEY (`FUNCTION_CD`)
    REFERENCES `vid_openecomp`.`fn_function` (`FUNCTION_CD`),
  CONSTRAINT `fk_fn_tab_set_cd`
    FOREIGN KEY (`TAB_SET_CD`)
    REFERENCES `vid_openecomp`.`fn_lu_tab_set` (`TAB_SET_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_tab_selected
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_tab_selected` (
  `SELECTED_TAB_CD` VARCHAR(30) NOT NULL,
  `TAB_URI` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`SELECTED_TAB_CD`, `TAB_URI`),
  CONSTRAINT `fk_fn_tab_selected_tab_cd`
    FOREIGN KEY (`SELECTED_TAB_CD`)
    REFERENCES `vid_openecomp`.`fn_tab` (`TAB_CD`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_user
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_user` (
  `USER_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` INT(11) NULL DEFAULT NULL,
  `MANAGER_ID` INT(11) NULL DEFAULT NULL,
  `FIRST_NAME` VARCHAR(25) NULL DEFAULT NULL,
  `MIDDLE_NAME` VARCHAR(25) NULL DEFAULT NULL,
  `LAST_NAME` VARCHAR(25) NULL DEFAULT NULL,
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
  UNIQUE INDEX `fn_user_hrid` (`HRID` ASC),
  UNIQUE INDEX `fn_user_login_id` (`LOGIN_ID` ASC),
  INDEX `fn_user_address_id` (`ADDRESS_ID` ASC),
  INDEX `fn_user_alert_method_cd` (`ALERT_METHOD_CD` ASC),
  INDEX `fn_user_org_id` (`ORG_ID` ASC),
  INDEX `fk_fn_user_ref_197_fn_user` (`MANAGER_ID` ASC),
  INDEX `fk_fn_user_ref_198_fn_user` (`CREATED_ID` ASC),
  INDEX `fk_fn_user_ref_199_fn_user` (`MODIFIED_ID` ASC),
  INDEX `fk_timezone` (`TIMEZONE` ASC),
  CONSTRAINT `fk_fn_user_ref_110_fn_org`
    FOREIGN KEY (`ORG_ID`)
    REFERENCES `vid_openecomp`.`fn_org` (`ORG_ID`),
  CONSTRAINT `fk_fn_user_ref_123_fn_lu_al`
    FOREIGN KEY (`ALERT_METHOD_CD`)
    REFERENCES `vid_openecomp`.`fn_lu_alert_method` (`ALERT_METHOD_CD`),
  CONSTRAINT `fk_fn_user_ref_197_fn_user`
    FOREIGN KEY (`MANAGER_ID`)
    REFERENCES `vid_openecomp`.`fn_user` (`USER_ID`),
  CONSTRAINT `fk_fn_user_ref_198_fn_user`
    FOREIGN KEY (`CREATED_ID`)
    REFERENCES `vid_openecomp`.`fn_user` (`USER_ID`),
  CONSTRAINT `fk_fn_user_ref_199_fn_user`
    FOREIGN KEY (`MODIFIED_ID`)
    REFERENCES `vid_openecomp`.`fn_user` (`USER_ID`),
  CONSTRAINT `fk_timezone`
    FOREIGN KEY (`TIMEZONE`)
    REFERENCES `vid_openecomp`.`fn_lu_timezone` (`TIMEZONE_ID`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_user_pseudo_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_user_pseudo_role` (
  `PSEUDO_ROLE_ID` INT(11) NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  PRIMARY KEY (`PSEUDO_ROLE_ID`, `USER_ID`),
  INDEX `fk_pseudo_role_user_id` (`USER_ID` ASC),
  CONSTRAINT `fk_pseudo_role_pseudo_role_id`
    FOREIGN KEY (`PSEUDO_ROLE_ID`)
    REFERENCES `vid_openecomp`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `fk_pseudo_role_user_id`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp`.`fn_user` (`USER_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_user_role
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_user_role` (
  `USER_ID` INT(10) NOT NULL,
  `ROLE_ID` INT(10) NOT NULL,
  `PRIORITY` DECIMAL(4,0) NULL DEFAULT NULL,
  `APP_ID` INT(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`USER_ID`, `ROLE_ID`, `APP_ID`),
  INDEX `fn_user_role_role_id` (`ROLE_ID` ASC),
  INDEX `fn_user_role_user_id` (`USER_ID` ASC),
  INDEX `fk_fn_user__ref_178_fn_app_IDX` (`APP_ID` ASC),
  CONSTRAINT `FK_FN_USER__REF_172_FN_USER`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `vid_openecomp`.`fn_user` (`USER_ID`),
  CONSTRAINT `FK_FN_USER__REF_175_FN_ROLE`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `vid_openecomp`.`fn_role` (`ROLE_ID`),
  CONSTRAINT `fk_fn_user__ref_178_fn_app`
    FOREIGN KEY (`APP_ID`)
    REFERENCES `vid_openecomp`.`fn_app` (`APP_ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.fn_workflow
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`fn_workflow` (
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
-- Table vid_openecomp.rcloudinvocation
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`rcloudinvocation` (
  `id` VARCHAR(128) NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userinfo` VARCHAR(2048) NOT NULL,
  `notebookid` VARCHAR(128) NOT NULL,
  `parameters` VARCHAR(2048) NULL DEFAULT NULL,
  `tokenreaddate` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.rcloudnotebook
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`rcloudnotebook` (
  `notebookname` VARCHAR(128) NOT NULL,
  `notebookid` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`notebookname`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

-- ----------------------------------------------------------------------------
-- Table vid_openecomp.schema_info
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `vid_openecomp`.`schema_info` (
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
-- View vid_openecomp.v_url_access
-- ----------------------------------------------------------------------------
CREATE OR REPLACE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_url_access` AS select distinct `m`.`ACTION` AS 

`URL`,`m`.`FUNCTION_CD` AS `FUNCTION_CD` from `fn_menu` `m` where (`m`.`ACTION` is not null) union select distinct 

`t`.`ACTION` AS `URL`,`t`.`FUNCTION_CD` AS `FUNCTION_CD` from `fn_tab` `t` where (`t`.`ACTION` is not null) union select 

`r`.`RESTRICTED_URL` AS `URL`,`r`.`FUNCTION_CD` AS `FUNCTION_CD` from `fn_restricted_url` `r`;
SET FOREIGN_KEY_CHECKS = 1;

