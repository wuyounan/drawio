/*
 Navicat Premium Data Transfer

 Source Server         : 公司测试库
 Source Server Type    : SQL Server
 Source Server Version : 11002100
 Source Host           : 192.168.3.63:1433
 Source Catalog        : topsun
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 11002100
 File Encoding         : 65001

 Date: 13/01/2020 15:23:34
*/


-- ----------------------------
-- Table structure for ACT_GE_BYTEARRAY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_GE_BYTEARRAY]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_GE_BYTEARRAY]
GO

CREATE TABLE [dbo].[ACT_GE_BYTEARRAY] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOYMENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BYTES_] varbinary(max)  NULL,
  [GENERATED_] decimal(1)  NULL
)
GO

ALTER TABLE [dbo].[ACT_GE_BYTEARRAY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_GE_PROPERTY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_GE_PROPERTY]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_GE_PROPERTY]
GO

CREATE TABLE [dbo].[ACT_GE_PROPERTY] (
  [NAME_] nvarchar(256) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VALUE_] nvarchar(1200) COLLATE Chinese_PRC_CI_AS  NULL,
  [REV_] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[ACT_GE_PROPERTY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_HI_ACTINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_ACTINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_ACTINST]
GO

CREATE TABLE [dbo].[ACT_HI_ACTINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CALL_PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ASSIGNEE_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime2(7)  NOT NULL,
  [END_TIME_] datetime2(7)  NULL,
  [DURATION_] decimal(19)  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_ACTINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_HI_COMMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_COMMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_COMMENT]
GO

CREATE TABLE [dbo].[ACT_HI_COMMENT] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [TIME_] datetime2(7)  NOT NULL,
  [USER_ID_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACTION_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [MESSAGE_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_MSG_] varbinary(max)  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_COMMENT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_HI_IDENTITYLINK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_IDENTITYLINK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_IDENTITYLINK]
GO

CREATE TABLE [dbo].[ACT_HI_IDENTITYLINK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [GROUP_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [USER_ID_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_IDENTITYLINK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_HI_PROCINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_PROCINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_PROCINST]
GO

CREATE TABLE [dbo].[ACT_HI_PROCINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BUSINESS_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [START_TIME_] datetime2(7)  NOT NULL,
  [END_TIME_] datetime2(7)  NULL,
  [DURATION_] decimal(19)  NULL,
  [START_USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [END_ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUPER_PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DELETE_REASON_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_PROCINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_HI_PROCINST_EXTENSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_PROCINST_EXTENSION]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_PROCINST_EXTENSION]
GO

CREATE TABLE [dbo].[ACT_HI_PROCINST_EXTENSION] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BUSINESS_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_POS_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_POS_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_DEPT_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_DEPT_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_ORG_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_ORG_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_FULL_ID_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_FULL_NAME_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION_] decimal(38,30)  NULL,
  [DESCRIPTION_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_NAME_] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime2(7)  NULL,
  [END_TIME_] datetime2(7)  NULL,
  [DURATION_] decimal(19)  NULL,
  [START_USER_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [END_ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUPER_PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DELETE_REASON_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_PROCINST_EXTENSION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_HI_TASKINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_TASKINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_TASKINST]
GO

CREATE TABLE [dbo].[ACT_HI_TASKINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [ASSIGNEE_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime2(7)  NOT NULL,
  [CLAIM_TIME_] datetime2(7)  NULL,
  [END_TIME_] datetime2(7)  NULL,
  [DURATION_] decimal(19)  NULL,
  [DELETE_REASON_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRIORITY_] decimal(38,30)  NULL,
  [DUE_DATE_] datetime2(7)  NULL,
  [FORM_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_TASKINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_HI_TASKINST_EXTENSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_TASKINST_EXTENSION]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_TASKINST_EXTENSION]
GO

CREATE TABLE [dbo].[ACT_HI_TASKINST_EXTENSION] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CATALOG_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_URL_] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_URL_] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_POS_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_POS_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_DEPT_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_DEPT_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_OGN_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_OGN_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_FULL_ID_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_FULL_NAME_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_POS_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_POS_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_DEPT_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_DEPT_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_OGN_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_OGN_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_FULL_ID_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_FULL_NAME_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_NAME_] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_NAMES_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_POS_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_POS_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_DEPT_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_DEPT_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_OGN_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_OGN_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_FULL_ID_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_FULL_NAME_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [PREVIOUS_ID_] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_HANDLER_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_CODE_] nvarchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [NEED_TIMING_] decimal(38,30)  NULL,
  [OPINION_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESULT_] decimal(38,30)  NULL,
  [SLEEPED_DATE_] datetime2(7)  NULL,
  [SIGN_PUNISH_] decimal(38,30)  NULL,
  [SIGN_NEED_TIMING_] decimal(38,30)  NULL,
  [VERSION_] decimal(38,30)  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [COOPERATION_MODEL_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LIMIT_TIME_] decimal(38,30)  NULL,
  [APPLICANT_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_DEFINITION_KEY_] varchar(510) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_PERSON_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_PERSON_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME_] datetime2(7)  NULL,
  [CLAIM_TIME_] datetime2(7)  NULL,
  [END_TIME_] datetime2(7)  NULL,
  [DURATION_] decimal(19)  NULL,
  [DELETE_REASON_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRIORITY_] decimal(38,30)  NULL,
  [DUE_DATE_] datetime2(7)  NULL,
  [FORM_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [GENERATE_REASON_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_PROC_UNIT_NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_TASKINST_EXTENSION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务的大类标识 process为流程任务，task为协同任务',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CATALOG_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务的类型细分标识，processInstance为流程实例，task为任务，executor为执行者',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'KIND_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建此任务的activity的URL',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_URL_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理此任务的activity的URL',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_URL_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_POS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人岗位',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_POS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人部门ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_DEPT_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人部门',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_DEPT_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人机构ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_OGN_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人机构',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_OGN_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_FULL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_FULL_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_POS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人岗位',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_POS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人部门ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_DEPT_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人部门',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_DEPT_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人机构ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_OGN_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人机构',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_OGN_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_FULL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_FULL_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务状态ID ，取值范围是ready(尚未处理)，executing(正在处理)，sleeping(暂缓处理)，canceled(已取消)，aborted(已终止)，completed(已完成)，returned(已回退)，trainsmited(已转发)，paused(已暂停)',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'STATUS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务状态',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'STATUS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人名称',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_NAMES_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_POS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人岗位',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_POS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人部门ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_DEPT_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人部门',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_DEPT_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人机构ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_OGN_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人机构',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_OGN_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_FULL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OWNER_FULL_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'前任务ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'PREVIOUS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节处理人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'PROC_UNIT_HANDLER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务组件ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'BUSINESS_KEY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务组件编码',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'BUSINESS_CODE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'需要计时',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'NEED_TIMING_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理意见',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'OPINION_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理结果',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'RESULT_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'暂缓时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'SLEEPED_DATE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人为标记是否处罚',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'SIGN_PUNISH_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人为标记是否计时',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'SIGN_NEED_TIMING_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'PROC_INST_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'协助模式',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'COOPERATION_MODEL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'限制时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'LIMIT_TIME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'申请人员成员ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'APPLICANT_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'申请人员成员',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'APPLICANT_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程定义ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'PROCESS_DEFINITION_KEY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'CREATOR_PERSON_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'EXECUTOR_PERSON_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'产生原因',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_EXTENSION',
'COLUMN', N'GENERATE_REASON_'
GO


-- ----------------------------
-- Table structure for ACT_HI_TASKINST_RELATION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_TASKINST_RELATION]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_TASKINST_RELATION]
GO

CREATE TABLE [dbo].[ACT_HI_TASKINST_RELATION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_KEY] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID] varchar(36) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [RELATED_TASK_ID] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_TASKINST_RELATION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'BUSINESS_KEY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'PROC_INST_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'TASK_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'关联任务ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION',
'COLUMN', N'RELATED_TASK_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务关联',
'SCHEMA', N'dbo',
'TABLE', N'ACT_HI_TASKINST_RELATION'
GO


-- ----------------------------
-- Table structure for ACT_HI_VARINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_HI_VARINST]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_HI_VARINST]
GO

CREATE TABLE [dbo].[ACT_HI_VARINST] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VAR_TYPE_] nvarchar(400) COLLATE Chinese_PRC_CI_AS  NULL,
  [REV_] decimal(38,30)  NULL,
  [BYTEARRAY_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DOUBLE_] decimal(10,10)  NULL,
  [LONG_] decimal(19)  NULL,
  [TEXT_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEXT2_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATE_TIME_] datetime2(7)  NULL,
  [LAST_UPDATED_TIME_] datetime2(7)  NULL
)
GO

ALTER TABLE [dbo].[ACT_HI_VARINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_RE_DEPLOYMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RE_DEPLOYMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RE_DEPLOYMENT]
GO

CREATE TABLE [dbo].[ACT_RE_DEPLOYMENT] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [CATEGORY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPLOY_TIME_] datetime2(7)  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RE_DEPLOYMENT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_RE_PROCDEF
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RE_PROCDEF]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RE_PROCDEF]
GO

CREATE TABLE [dbo].[ACT_RE_PROCDEF] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [CATEGORY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VERSION_] decimal(38,30)  NOT NULL,
  [DEPLOYMENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_NAME_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DGRM_RESOURCE_NAME_] varchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [HAS_START_FORM_KEY_] decimal(1)  NULL,
  [SUSPENSION_STATE_] decimal(38,30)  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [HAS_GRAPHICAL_NOTATION_] decimal(1)  NULL
)
GO

ALTER TABLE [dbo].[ACT_RE_PROCDEF] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_RE_PROCDEF_TREE_DEL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RE_PROCDEF_TREE_DEL]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RE_PROCDEF_TREE_DEL]
GO

CREATE TABLE [dbo].[ACT_RE_PROCDEF_TREE_DEL] (
  [RE_PROCDEF_TREE_ID] decimal(38,30)  NOT NULL,
  [CODE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [PATH] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [DESCRIPTION] varchar(1200) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [KEY] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPROVAL_RULE_PROC_DEF_KEY] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NEED_TIMING] decimal(38,30)  NULL,
  [APP_MODEL_ID] decimal(38,30)  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SHOW_QUERY_HANDLERS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[ACT_RE_PROCDEF_TREE_DEL] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'RE_PROCDEF_TREE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'PATH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程KEY',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'KEY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则流程ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'APPROVAL_RULE_PROC_DEF_KEY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类型 folder 文件夹 proc 流程 procUnit 流程环节',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'需要计时',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'NEED_TIMING'
GO

EXEC sp_addextendedproperty
'MS_Description', N'APP模型ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'APP_MODEL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'显示询问处理人对话框 1 是 0 否',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL',
'COLUMN', N'SHOW_QUERY_HANDLERS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程定义树',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RE_PROCDEF_TREE_DEL'
GO


-- ----------------------------
-- Table structure for ACT_RU_EVENT_SUBSCR
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_EVENT_SUBSCR]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_EVENT_SUBSCR]
GO

CREATE TABLE [dbo].[ACT_RU_EVENT_SUBSCR] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [EVENT_TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EVENT_NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACTIVITY_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONFIGURATION_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_] datetime2(7)  NOT NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_EVENT_SUBSCR] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_RU_EXECUTION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_EXECUTION]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_EXECUTION]
GO

CREATE TABLE [dbo].[ACT_RU_EXECUTION] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUPER_EXEC_] nvarchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ACT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_ACTIVE_] decimal(1)  NULL,
  [IS_CONCURRENT_] decimal(1)  NULL,
  [IS_SCOPE_] decimal(1)  NULL,
  [IS_EVENT_SCOPE_] decimal(1)  NULL,
  [SUSPENSION_STATE_] decimal(38,30)  NULL,
  [CACHED_ENT_STATE_] decimal(38,30)  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOCK_TIME_] datetime2(7)  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_EXECUTION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_RU_IDENTITYLINK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_IDENTITYLINK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_IDENTITYLINK]
GO

CREATE TABLE [dbo].[ACT_RU_IDENTITYLINK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [GROUP_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [USER_ID_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_IDENTITYLINK] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_RU_JOB
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_JOB]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_JOB]
GO

CREATE TABLE [dbo].[ACT_RU_JOB] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LOCK_EXP_TIME_] datetime2(7)  NULL,
  [LOCK_OWNER_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCLUSIVE_] decimal(1)  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_INSTANCE_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [RETRIES_] decimal(38,30)  NULL,
  [EXCEPTION_STACK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEPTION_MSG_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [DUEDATE_] datetime2(7)  NULL,
  [REPEAT_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_CFG_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_JOB] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for ACT_RU_TASK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_TASK]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_TASK]
GO

CREATE TABLE [dbo].[ACT_RU_TASK] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [ASSIGNEE_] nvarchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [DELEGATION_] nvarchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRIORITY_] decimal(38,30)  NULL,
  [CREATE_TIME_] datetime2(7)  NULL,
  [DUE_DATE_] datetime2(7)  NULL,
  [SUSPENSION_STATE_] decimal(38,30)  NULL,
  [CATEGORY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [FORM_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_TASK] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'REV_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'EXECUTION_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'PROC_INST_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程定义ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'PROC_DEF_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点定义名称',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'PARENT_TASK_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点定义描述',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'DESCRIPTION_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点定义的KEY',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'TASK_DEF_KEY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'实际签收人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'OWNER_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'签收人或委托人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'ASSIGNEE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'委托类型',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'DELEGATION_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'优先级别',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'PRIORITY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'CREATE_TIME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'过期时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'DUE_DATE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否挂起',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK',
'COLUMN', N'SUSPENSION_STATE_'
GO


-- ----------------------------
-- Table structure for ACT_RU_TASK_EXTENSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_TASK_EXTENSION]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_TASK_EXTENSION]
GO

CREATE TABLE [dbo].[ACT_RU_TASK_EXTENSION] (
  [ID_] varchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CATALOG_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_URL_] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_URL_] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_POS_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_POS_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_DEPT_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_DEPT_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_OGN_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_OGN_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_FULL_ID_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_FULL_NAME_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_POS_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_POS_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_DEPT_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_DEPT_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_OGN_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_OGN_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_FULL_ID_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_FULL_NAME_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_NAME_] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_NAMES_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_POS_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_POS_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_DEPT_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_DEPT_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_OGN_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_OGN_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_FULL_ID_] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_FULL_NAME_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [PREVIOUS_ID_] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_HANDLER_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_CODE_] nvarchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [NEED_TIMING_] decimal(38,30)  NULL,
  [OPINION_] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESULT_] decimal(38,30)  NULL,
  [SLEEPED_DATE_] datetime2(7)  NULL,
  [VERSION_] decimal(38,30)  NULL,
  [PROC_INST_ID_] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [COOPERATION_MODEL_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LIMIT_TIME_] decimal(38,30)  NULL,
  [APPLICANT_PERSON_MEMBER_ID_] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPLICANT_PERSON_MEMBER_NAME_] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_DEFINITION_KEY_] varchar(510) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATOR_PERSON_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTOR_PERSON_ID_] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_DEF_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_DEF_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [DELEGATION_] nvarchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRIORITY_] decimal(38,30)  NULL,
  [START_TIME_] datetime2(7)  NULL,
  [DUE_DATE_] datetime2(7)  NULL,
  [SUSPENSION_STATE_] decimal(38,30)  NULL,
  [CATEGORY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID_] nvarchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [FORM_KEY_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [GENERATE_REASON_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_PROC_UNIT_NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_TASK_EXTENSION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务的大类标识 process为流程任务，task为协同任务',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CATALOG_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务的类型细分标识，task为任务，notice 任务抄送',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'KIND_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建此任务的activity的URL',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_URL_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理此任务的activity的URL',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_URL_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_POS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人岗位',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_POS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人部门ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_DEPT_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人部门',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_DEPT_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人机构ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_OGN_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人机构',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_OGN_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_FULL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_FULL_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_POS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人岗位',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_POS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人部门ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_DEPT_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人部门',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_DEPT_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人机构ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_OGN_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人机构',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_OGN_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_FULL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_FULL_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务状态ID ，取值范围是ready(尚未处理)，executing(正在处理)，sleeping(暂缓处理)，canceled(已取消)，aborted(已终止)，completed(已完成)，returned(已回退)，trainsmited(已转发)，paused(已暂停)',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'STATUS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务状态',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'STATUS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人名称',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_NAMES_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_POS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人岗位',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_POS_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人部门ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_DEPT_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人部门',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_DEPT_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人机构ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_OGN_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人机构',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_OGN_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_FULL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'责任人名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OWNER_FULL_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'前任务ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'PREVIOUS_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节处理人ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'PROC_UNIT_HANDLER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务组件ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'BUSINESS_KEY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务组件编码',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'BUSINESS_CODE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'需要计时',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'NEED_TIMING_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理意见',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'OPINION_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理结果',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'RESULT_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'暂缓时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'SLEEPED_DATE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'PROC_INST_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'协助模式',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'COOPERATION_MODEL_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'限制时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'LIMIT_TIME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'申请人员成员ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'APPLICANT_PERSON_MEMBER_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'申请人员成员',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'APPLICANT_PERSON_MEMBER_NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程定义ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'PROCESS_DEFINITION_KEY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'CREATOR_PERSON_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行人',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTOR_PERSON_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'EXECUTION_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程定义ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'PROC_DEF_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点定义名称',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'NAME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点实例ID',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'PARENT_TASK_ID_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点定义描述',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'DESCRIPTION_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点定义的KEY',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'TASK_DEF_KEY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'委托类型',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'DELEGATION_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'优先级别',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'PRIORITY_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'START_TIME_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'过期时间',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'DUE_DATE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否挂起',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'SUSPENSION_STATE_'
GO

EXEC sp_addextendedproperty
'MS_Description', N'产生原因',
'SCHEMA', N'dbo',
'TABLE', N'ACT_RU_TASK_EXTENSION',
'COLUMN', N'GENERATE_REASON_'
GO


-- ----------------------------
-- Table structure for ACT_RU_VARIABLE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ACT_RU_VARIABLE]') AND type IN ('U'))
	DROP TABLE [dbo].[ACT_RU_VARIABLE]
GO

CREATE TABLE [dbo].[ACT_RU_VARIABLE] (
  [ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [REV_] decimal(38,30)  NULL,
  [TYPE_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME_] nvarchar(1020) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXECUTION_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_INST_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BYTEARRAY_ID_] nvarchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DOUBLE_] decimal(10,10)  NULL,
  [LONG_] decimal(19)  NULL,
  [TEXT_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEXT2_] nvarchar(2000) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[ACT_RU_VARIABLE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for AVIC_IMP_EMPLOYEE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_IMP_EMPLOYEE]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_IMP_EMPLOYEE]
GO

CREATE TABLE [dbo].[AVIC_IMP_EMPLOYEE] (
  [LEVEL1] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LEVEL2] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LEVEL3] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LEVEL4] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LEVEL5] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LEVEL6] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LEVEL7] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [EMP_NO] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [REAL_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BATCH_NUMBER] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [MESSAGE] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38)  NULL,
  [TMP_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LAST_NAME] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIRST_NAME] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TELEPHONE1] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TELEPHONE2] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MOBILE_PHONE1] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MOBILE_PHONE2] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EMAIL1] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [EMAIL2] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_TYPE] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[AVIC_IMP_EMPLOYEE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'一级部门名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LEVEL1'
GO

EXEC sp_addextendedproperty
'MS_Description', N'二级部门名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LEVEL2'
GO

EXEC sp_addextendedproperty
'MS_Description', N'三级部门名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LEVEL3'
GO

EXEC sp_addextendedproperty
'MS_Description', N'四级部门名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LEVEL4'
GO

EXEC sp_addextendedproperty
'MS_Description', N'五级部门名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LEVEL5'
GO

EXEC sp_addextendedproperty
'MS_Description', N'6级部门名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LEVEL6'
GO

EXEC sp_addextendedproperty
'MS_Description', N'7级部门名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LEVEL7'
GO

EXEC sp_addextendedproperty
'MS_Description', N'工号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'EMP_NO'
GO

EXEC sp_addextendedproperty
'MS_Description', N'姓名',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'REAL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'职务',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'POSITION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'导入批次号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'BATCH_NUMBER'
GO

EXEC sp_addextendedproperty
'MS_Description', N'导入状态消息',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'MESSAGE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'导入状态',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'临时数据id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'TMP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'姓',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'LAST_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'FIRST_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'电话号码1',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'TELEPHONE1'
GO

EXEC sp_addextendedproperty
'MS_Description', N'电话号码2',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'TELEPHONE2'
GO

EXEC sp_addextendedproperty
'MS_Description', N'移动号码1',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'MOBILE_PHONE1'
GO

EXEC sp_addextendedproperty
'MS_Description', N'移动号码2',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'MOBILE_PHONE2'
GO

EXEC sp_addextendedproperty
'MS_Description', N'邮箱1',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'EMAIL1'
GO

EXEC sp_addextendedproperty
'MS_Description', N'邮箱2',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'EMAIL2'
GO

EXEC sp_addextendedproperty
'MS_Description', N'职位类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'POSITION_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'员工导入临时表',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_IMP_EMPLOYEE'
GO


-- ----------------------------
-- Table structure for AVIC_OAUTH2_CLIENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_OAUTH2_CLIENT]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_OAUTH2_CLIENT]
GO

CREATE TABLE [dbo].[AVIC_OAUTH2_CLIENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VERSION] decimal(38)  NULL,
  [NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [SECRET_KEY] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[AVIC_OAUTH2_CLIENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'客户端id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OAUTH2_CLIENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'客户端名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OAUTH2_CLIENT',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'客户端key',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OAUTH2_CLIENT',
'COLUMN', N'SECRET_KEY'
GO


-- ----------------------------
-- Table structure for AVIC_OBJECT_AUTHORIZE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_OBJECT_AUTHORIZE]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_OBJECT_AUTHORIZE]
GO

CREATE TABLE [dbo].[AVIC_OBJECT_AUTHORIZE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BIZ_CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [AUTHORIZER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [AUTHORIZE_TIME] datetime2(7)  NOT NULL,
  [AUTHORIZER_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ORG_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ORG_FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [AUTHORIZE_TYPE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[AVIC_OBJECT_AUTHORIZE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务编码',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'BIZ_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'BIZ_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'授权人id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'AUTHORIZER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'授权时间',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'AUTHORIZE_TIME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'授权人名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'AUTHORIZER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'使用者orgId',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'使用者名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'ORG_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'使用者全id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'ORG_FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'授权类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'AUTHORIZE_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'实体版本号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'对象授权',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_OBJECT_AUTHORIZE'
GO


-- ----------------------------
-- Table structure for AVIC_REPORT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_REPORT]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_REPORT]
GO

CREATE TABLE [dbo].[AVIC_REPORT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VERSION] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NOT NULL,
  [NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CREATED_DATE] datetime2(7)  NOT NULL,
  [LAST_MODIFIED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LAST_MODIFIED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NOT NULL,
  [KIND_ID] decimal(38,30)  NOT NULL,
  [TEMPLATE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEMPLATE_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SHARE_COUNT] decimal(38,30)  NOT NULL,
  [CREATED_BY_FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[AVIC_REPORT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'实体版本',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'-1  逻辑删除，0 未发布， 1 已发布',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'报告名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建者id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建者名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'修改者id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'LAST_MODIFIED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'修改者名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'LAST_MODIFIED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'修改时间',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'LAST_MODIFIED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 - 在线，2 - 本地上传',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'TEMPLATE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'TEMPLATE_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'有效分享次数',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'SHARE_COUNT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建者全id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'CREATED_BY_FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务编码',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'BIZ_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件夹id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'生成的报告',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT'
GO


-- ----------------------------
-- Table structure for AVIC_REPORT_FORM
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_REPORT_FORM]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_REPORT_FORM]
GO

CREATE TABLE [dbo].[AVIC_REPORT_FORM] (
  [ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEMPLATE_PATH] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38)  NULL,
  [NODE_KIND_ID] decimal(2)  NULL,
  [FILE_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] decimal(2)  NULL,
  [URL] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[AVIC_REPORT_FORM] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板路径',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'TEMPLATE_PATH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'实体版本号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'FILE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'访问路径',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM',
'COLUMN', N'URL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'报表',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM'
GO


-- ----------------------------
-- Table structure for AVIC_REPORT_FORM_PARAMETER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_REPORT_FORM_PARAMETER]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_REPORT_FORM_PARAMETER]
GO

CREATE TABLE [dbo].[AVIC_REPORT_FORM_PARAMETER] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VERSION] decimal(38)  NULL,
  [CODE] varchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [NAME] varchar(20) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [STATUS] decimal(2)  NULL,
  [DEFAULT_VALUE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [REPORT_FORM_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CONTROL_TYPE] decimal(2)  NULL,
  [DATA_SOURCE_KIND] decimal(2)  NULL,
  [DATA_SOURCE] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[AVIC_REPORT_FORM_PARAMETER] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'默认值',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER',
'COLUMN', N'DEFAULT_VALUE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'报表id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER',
'COLUMN', N'REPORT_FORM_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'控件类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER',
'COLUMN', N'CONTROL_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据源类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER',
'COLUMN', N'DATA_SOURCE_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据源',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER',
'COLUMN', N'DATA_SOURCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'报表参数',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FORM_PARAMETER'
GO


-- ----------------------------
-- Table structure for AVIC_REPORT_FROM_AUTHORIZE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_REPORT_FROM_AUTHORIZE]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_REPORT_FROM_AUTHORIZE]
GO

CREATE TABLE [dbo].[AVIC_REPORT_FROM_AUTHORIZE] (
  [ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FORM_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38)  NULL
)
GO

ALTER TABLE [dbo].[AVIC_REPORT_FROM_AUTHORIZE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'报表授权',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_FROM_AUTHORIZE'
GO


-- ----------------------------
-- Table structure for AVIC_REPORT_TEMPLATE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_REPORT_TEMPLATE]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_REPORT_TEMPLATE]
GO

CREATE TABLE [dbo].[AVIC_REPORT_TEMPLATE] (
  [ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE] decimal(38,30)  NULL,
  [FILE_TYPE] decimal(38,30)  NULL,
  [NODE_KIND] decimal(38,30)  NULL,
  [PARENT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FOLDER_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [FILE_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PUB_TEMPLATE_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[AVIC_REPORT_TEMPLATE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板类型（字典），0-公共模板，1-个人模板',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板文件类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'FILE_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类型',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'NODE_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'实体版本号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件夹id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板文件id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'FILE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'关联公共模板id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE',
'COLUMN', N'PUB_TEMPLATE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'在线报告模板',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE'
GO


-- ----------------------------
-- Table structure for AVIC_REPORT_TEMPLATE_AUTHORIZE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[AVIC_REPORT_TEMPLATE_AUTHORIZE]') AND type IN ('U'))
	DROP TABLE [dbo].[AVIC_REPORT_TEMPLATE_AUTHORIZE]
GO

CREATE TABLE [dbo].[AVIC_REPORT_TEMPLATE_AUTHORIZE] (
  [ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEMPLATE_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [AUTHORIZE_TYPE] decimal(38,30)  NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38)  NULL
)
GO

ALTER TABLE [dbo].[AVIC_REPORT_TEMPLATE_AUTHORIZE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'模板id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE',
'COLUMN', N'TEMPLATE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'0 - 公共模板授权
1 - 个人
2 - 分享
',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE',
'COLUMN', N'AUTHORIZE_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'使用者id',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'使用者名称',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE',
'COLUMN', N'ORG_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'使用者全路径',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE',
'COLUMN', N'ORG_FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'实体版本号',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'报告模板授权',
'SCHEMA', N'dbo',
'TABLE', N'AVIC_REPORT_TEMPLATE_AUTHORIZE'
GO


-- ----------------------------
-- Table structure for BPM_BUSINESS_PROCESS
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_BUSINESS_PROCESS]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_BUSINESS_PROCESS]
GO

CREATE TABLE [dbo].[BPM_BUSINESS_PROCESS] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CODE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCESS_KIND] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_FINAL] decimal(38,30)  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [OWNER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL,
  [USER_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [OBJECT_KIND_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_ELECTRONIZATION] decimal(38,30)  NULL,
  [FLOW_LIST] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLOW_AIM] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLOW_RANGE] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLOW_OBJECT] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLOW_DUTY] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLOW_INPUT_CONDITION] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLOW_OUTPUT] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLOW_INDEX] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_FLOW_CHART] decimal(38,30)  NULL,
  [CHART_DIRECTION] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[BPM_BUSINESS_PROCESS] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'code',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'name',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'remark',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'parent_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程类型',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'PROCESS_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否末级',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'IS_FINAL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_name',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'sequence',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'status',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'owner_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'OWNER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'owner_name',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'OWNER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'object_kind_code',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'OBJECT_KIND_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'E化标志',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'IS_ELECTRONIZATION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程清单',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_LIST'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程目的',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_AIM'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程范围',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_RANGE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程对象',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_OBJECT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程职责',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_DUTY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程输入条件',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_INPUT_CONDITION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程输出',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_OUTPUT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程指标',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'FLOW_INDEX'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否绘图方向包含流程图',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS',
'COLUMN', N'IS_FLOW_CHART'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程定义',
'SCHEMA', N'dbo',
'TABLE', N'BPM_BUSINESS_PROCESS'
GO


-- ----------------------------
-- Table structure for BPM_FUNCTIONS
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_FUNCTIONS]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_FUNCTIONS]
GO

CREATE TABLE [dbo].[BPM_FUNCTIONS] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [CHECK_BEAN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [IS_TO_HIDE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[BPM_FUNCTIONS] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'无权限时隐藏',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS',
'COLUMN', N'IS_TO_HIDE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务功能',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS'
GO


-- ----------------------------
-- Table structure for BPM_FUNCTIONS_DETAILS
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_FUNCTIONS_DETAILS]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_FUNCTIONS_DETAILS]
GO

CREATE TABLE [dbo].[BPM_FUNCTIONS_DETAILS] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BPM_FUNCTIONS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FUNCTIONS_GROUP_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_ZH] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_EN] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [COLOR] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [URL] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[BPM_FUNCTIONS_DETAILS] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务功能ID',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS',
'COLUMN', N'BPM_FUNCTIONS_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组ID',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS',
'COLUMN', N'FUNCTIONS_GROUP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'对应功能编码',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'中文名',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS',
'COLUMN', N'NAME_ZH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'英文名',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS',
'COLUMN', N'NAME_EN'
GO

EXEC sp_addextendedproperty
'MS_Description', N'颜色',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS',
'COLUMN', N'COLOR'
GO

EXEC sp_addextendedproperty
'MS_Description', N'序号',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务功能对应连接',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_DETAILS'
GO


-- ----------------------------
-- Table structure for BPM_FUNCTIONS_GROUP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_FUNCTIONS_GROUP]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_FUNCTIONS_GROUP]
GO

CREATE TABLE [dbo].[BPM_FUNCTIONS_GROUP] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BPM_FUNCTIONS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_ZH] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME_EN] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [COLOR] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[BPM_FUNCTIONS_GROUP] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'中文名称',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_GROUP',
'COLUMN', N'NAME_ZH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'英文名称',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_GROUP',
'COLUMN', N'NAME_EN'
GO

EXEC sp_addextendedproperty
'MS_Description', N'颜色',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_GROUP',
'COLUMN', N'COLOR'
GO

EXEC sp_addextendedproperty
'MS_Description', N'序号',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_GROUP',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务功能分组',
'SCHEMA', N'dbo',
'TABLE', N'BPM_FUNCTIONS_GROUP'
GO


-- ----------------------------
-- Table structure for BPM_PROCESS_AREAS
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_PROCESS_AREAS]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_PROCESS_AREAS]
GO

CREATE TABLE [dbo].[BPM_PROCESS_AREAS] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BUSINESS_PROCESS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [LEFT] decimal(38,30)  NULL,
  [TOP] decimal(38,30)  NULL,
  [COLOR] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [WIDTH] decimal(38,30)  NULL,
  [HEIGHT] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[BPM_PROCESS_AREAS] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'business_process_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'BUSINESS_PROCESS_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'NAME',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'LEFT',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'LEFT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'TOP',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'TOP'
GO

EXEC sp_addextendedproperty
'MS_Description', N'COLOR',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'COLOR'
GO

EXEC sp_addextendedproperty
'MS_Description', N'WIDTH',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'WIDTH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'HEIGHT',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'HEIGHT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'VERSION',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程图区域组织划分框',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_AREAS'
GO


-- ----------------------------
-- Table structure for BPM_PROCESS_NODE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_PROCESS_NODE]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_PROCESS_NODE]
GO

CREATE TABLE [dbo].[BPM_PROCESS_NODE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BUSINESS_PROCESS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [OBJECT_KIND_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [XAXIS] decimal(38,30)  NULL,
  [YAXIS] decimal(38,30)  NULL,
  [RULE_KIND] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [OWNER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [LINK_KIND_CODES] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VIEW_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [INTERFACE_KIND] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [QUOTE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL,
  [REMARK] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FUNCTION_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[BPM_PROCESS_NODE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'business_process_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'BUSINESS_PROCESS_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'code',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'name',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'node_kind',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'OBJECT_KIND_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'xaxis',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'XAXIS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'yaxis',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'YAXIS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'rule_kind',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'RULE_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'owner_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'OWNER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'owner_name',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'OWNER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'包含对象',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'LINK_KIND_CODES'
GO

EXEC sp_addextendedproperty
'MS_Description', N'接口类型',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'INTERFACE_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'引用节点ID(影子)',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'QUOTE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'对应功能编号',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'FUNCTION_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点编码',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE',
'COLUMN', N'NODE_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'活动节点',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE'
GO


-- ----------------------------
-- Table structure for BPM_PROCESS_NODE_FUNCTION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_PROCESS_NODE_FUNCTION]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_PROCESS_NODE_FUNCTION]
GO

CREATE TABLE [dbo].[BPM_PROCESS_NODE_FUNCTION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VIEW_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BUSINESS_PROCESS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ICON] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [URL] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARAM] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[BPM_PROCESS_NODE_FUNCTION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程ID',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION',
'COLUMN', N'BUSINESS_PROCESS_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'图标',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION',
'COLUMN', N'ICON'
GO

EXEC sp_addextendedproperty
'MS_Description', N'连接',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION',
'COLUMN', N'URL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION',
'COLUMN', N'PARAM'
GO

EXEC sp_addextendedproperty
'MS_Description', N'序号',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'活动节点功能',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_FUNCTION'
GO


-- ----------------------------
-- Table structure for BPM_PROCESS_NODE_LINE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_PROCESS_NODE_LINE]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_PROCESS_NODE_LINE]
GO

CREATE TABLE [dbo].[BPM_PROCESS_NODE_LINE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BUSINESS_PROCESS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LINE_TYPE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FROM_NODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TO_NODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[BPM_PROCESS_NODE_LINE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_LINE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'business_process_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_LINE',
'COLUMN', N'BUSINESS_PROCESS_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'line_type',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_LINE',
'COLUMN', N'LINE_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'name',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_LINE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'from_node',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_LINE',
'COLUMN', N'FROM_NODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'to_node',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_LINE',
'COLUMN', N'TO_NODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点关联关系',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_LINE'
GO


-- ----------------------------
-- Table structure for BPM_PROCESS_NODE_TEMP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[BPM_PROCESS_NODE_TEMP]') AND type IN ('U'))
	DROP TABLE [dbo].[BPM_PROCESS_NODE_TEMP]
GO

CREATE TABLE [dbo].[BPM_PROCESS_NODE_TEMP] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BUSINESS_PROCESS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [LINK_KIND_CODES] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VIEW_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OWNER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [OBJECT_KIND_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FUNCTION_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[BPM_PROCESS_NODE_TEMP] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'owner_id',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_TEMP',
'COLUMN', N'OWNER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'owner_name',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_TEMP',
'COLUMN', N'OWNER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'node_kind',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_TEMP',
'COLUMN', N'OBJECT_KIND_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'对应功能编号',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_TEMP',
'COLUMN', N'FUNCTION_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点编码',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_TEMP',
'COLUMN', N'NODE_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程节点临时存放',
'SCHEMA', N'dbo',
'TABLE', N'BPM_PROCESS_NODE_TEMP'
GO


-- ----------------------------
-- Table structure for DEMO_LEAVE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[DEMO_LEAVE]') AND type IN ('U'))
	DROP TABLE [dbo].[DEMO_LEAVE]
GO

CREATE TABLE [dbo].[DEMO_LEAVE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [FILLIN_DATE] datetime2(7)  NULL,
  [BILL_CODE] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(32)  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_NAME] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_NAME] varchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(32)  NULL,
  [DAYS] decimal(32)  NULL
)
GO

ALTER TABLE [dbo].[DEMO_LEAVE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for DISTRICT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[DISTRICT]') AND type IN ('U'))
	DROP TABLE [dbo].[DISTRICT]
GO

CREATE TABLE [dbo].[DISTRICT] (
  [ID] decimal(10)  NOT NULL,
  [PARENT_ID] decimal(10)  NULL,
  [NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL
)
GO

ALTER TABLE [dbo].[DISTRICT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for PM_EXERCISE_LIST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[PM_EXERCISE_LIST]') AND type IN ('U'))
	DROP TABLE [dbo].[PM_EXERCISE_LIST]
GO

CREATE TABLE [dbo].[PM_EXERCISE_LIST] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EXERCISE_NAME] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXERCISE_TYPE] decimal(38,30)  NULL,
  [EXERCISE_STOCK] decimal(8)  NULL,
  [EXERCISE_PRICE] decimal(10,2)  NULL,
  [VERSION] decimal(19)  NULL,
  [CREATED_BY_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL,
  [CODE] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(10)  NULL,
  [FOLDER_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[PM_EXERCISE_LIST] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'商品id',
'SCHEMA', N'dbo',
'TABLE', N'PM_EXERCISE_LIST',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'商品名称',
'SCHEMA', N'dbo',
'TABLE', N'PM_EXERCISE_LIST',
'COLUMN', N'EXERCISE_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'商品类型',
'SCHEMA', N'dbo',
'TABLE', N'PM_EXERCISE_LIST',
'COLUMN', N'EXERCISE_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'库存',
'SCHEMA', N'dbo',
'TABLE', N'PM_EXERCISE_LIST',
'COLUMN', N'EXERCISE_STOCK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'价格',
'SCHEMA', N'dbo',
'TABLE', N'PM_EXERCISE_LIST',
'COLUMN', N'EXERCISE_PRICE'
GO


-- ----------------------------
-- Table structure for PM_EXERCISEDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[PM_EXERCISEDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[PM_EXERCISEDETAIL]
GO

CREATE TABLE [dbo].[PM_EXERCISEDETAIL] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [EID] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [CPU] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [RAM] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROM] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [REAR_CAMERA] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [FRONT_CAMERA] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCREEN_SIZE] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(19)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[PM_EXERCISEDETAIL] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for PM_PRODUCT_CONFIG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[PM_PRODUCT_CONFIG]') AND type IN ('U'))
	DROP TABLE [dbo].[PM_PRODUCT_CONFIG]
GO

CREATE TABLE [dbo].[PM_PRODUCT_CONFIG] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CPU] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [RAM] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROM] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [DESCRIBE] varchar(1000) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[PM_PRODUCT_CONFIG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for PM_TREE_PRODUCT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[PM_TREE_PRODUCT]') AND type IN ('U'))
	DROP TABLE [dbo].[PM_TREE_PRODUCT]
GO

CREATE TABLE [dbo].[PM_TREE_PRODUCT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [SID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [P_NAME] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [P_TYPE] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [P_PRICE] decimal(10,2)  NULL,
  [P_STOCK] decimal(8)  NULL,
  [VERSION] decimal(19)  NULL,
  [CREATED_BY_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL,
  [FULL_ID] varchar(1000) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[PM_TREE_PRODUCT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for PM_TREE_SHAPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[PM_TREE_SHAPE]') AND type IN ('U'))
	DROP TABLE [dbo].[PM_TREE_SHAPE]
GO

CREATE TABLE [dbo].[PM_TREE_SHAPE] (
  [ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CODE] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(10)  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(10)  NULL,
  [VERSION] decimal(19)  NULL,
  [CREATED_BY_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL
)
GO

ALTER TABLE [dbo].[PM_TREE_SHAPE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_APPLICATIONSYSTEM
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_APPLICATIONSYSTEM]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_APPLICATIONSYSTEM]
GO

CREATE TABLE [dbo].[SA_APPLICATIONSYSTEM] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLASS_PREFIX] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_APPLICATIONSYSTEM] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_APPROVAL_REJECTED_REASON
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_APPROVAL_REJECTED_REASON]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_APPROVAL_REJECTED_REASON]
GO

CREATE TABLE [dbo].[SA_APPROVAL_REJECTED_REASON] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONTENT] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [REJECTED_REASON_KIND] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_APPROVAL_REJECTED_REASON] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分类ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'content',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON',
'COLUMN', N'CONTENT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'sequence',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'status',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON',
'COLUMN', N'REJECTED_REASON_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批驳回理由维护',
'SCHEMA', N'dbo',
'TABLE', N'SA_APPROVAL_REJECTED_REASON'
GO


-- ----------------------------
-- Table structure for SA_ATTACHMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_ATTACHMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_ATTACHMENT]
GO

CREATE TABLE [dbo].[SA_ATTACHMENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_SUB_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PATH] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FILE_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [FILE_SIZE] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FILE_KIND] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [CREATED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLEAR_CACHE] decimal(38,30)  NULL,
  [UPLOAD_KIND] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_ATTACHMENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务类别ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'BIZ_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'BIZ_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务子类别ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'BIZ_SUB_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'PATH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'FILE_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件大小',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'FILE_SIZE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'FILE_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建日期',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'清楚缓存',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'CLEAR_CACHE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'上传类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'UPLOAD_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_ATTACHMENT',
'COLUMN', N'SEQUENCE'
GO


-- ----------------------------
-- Table structure for SA_ATTACHMENTCONFIG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_ATTACHMENTCONFIG]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_ATTACHMENTCONFIG]
GO

CREATE TABLE [dbo].[SA_ATTACHMENTCONFIG] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ALLOW_DELETE] decimal(38,30)  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_ATTACHMENTCONFIG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_ATTACHMENTCONFIGDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_ATTACHMENTCONFIGDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_ATTACHMENTCONFIGDETAIL]
GO

CREATE TABLE [dbo].[SA_ATTACHMENTCONFIGDETAIL] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ATTACHMENTCONFIG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ALLOW_MULTIPLE] decimal(38,30)  NULL,
  [FILE_KIND] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [COL_SPAN] decimal(38,30)  NULL,
  [IS_REQUIRED] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_ATTACHMENTCONFIGDETAIL] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_BIZCLASSIFICATION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_BIZCLASSIFICATION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_BIZCLASSIFICATION]
GO

CREATE TABLE [dbo].[SA_BIZCLASSIFICATION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_BIZCLASSIFICATION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全路径ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务分类配置',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATION'
GO


-- ----------------------------
-- Table structure for SA_BIZCLASSIFICATIONDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_BIZCLASSIFICATIONDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_BIZCLASSIFICATIONDETAIL]
GO

CREATE TABLE [dbo].[SA_BIZCLASSIFICATIONDETAIL] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_CLASSIFICATION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_TYPE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_PROPERTY_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [ENTITY_CLASS_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DIALOG_WIDTH] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_BIZCLASSIFICATIONDETAIL] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务属性配置ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'BIZ_CLASSIFICATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务参数;业务数据',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'BIZ_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务属性ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'BIZ_PROPERTY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'对应实体资源类名',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'ENTITY_CLASS_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'BIZ_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'对话框宽度',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL',
'COLUMN', N'DIALOG_WIDTH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务分类配置明细',
'SCHEMA', N'dbo',
'TABLE', N'SA_BIZCLASSIFICATIONDETAIL'
GO


-- ----------------------------
-- Table structure for SA_CODEBUILDRULE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_CODEBUILDRULE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_CODEBUILDRULE]
GO

CREATE TABLE [dbo].[SA_CODEBUILDRULE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [RULE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CURRENT_VALUE] decimal(38,30)  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL
)
GO

ALTER TABLE [dbo].[SA_CODEBUILDRULE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_COMMONHANDLER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_COMMONHANDLER]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_COMMONHANDLER]
GO

CREATE TABLE [dbo].[SA_COMMONHANDLER] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_UNIT_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_UNIT_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [GROUP_ID] decimal(38,30)  NULL,
  [SUB_PROC_UNIT_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_COMMONHANDLER] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'BIZ_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'receiver 接收人 feedbacker 反馈人 feedbackViewer 反馈查看人',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'机构ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'ORG_UNIT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织单元名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'ORG_UNIT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织单元类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'ORG_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'GROUP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节描述',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER',
'COLUMN', N'SUB_PROC_UNIT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'通用处理人设置',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONHANDLER'
GO


-- ----------------------------
-- Table structure for SA_COMMONTREE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_COMMONTREE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_COMMONTREE]
GO

CREATE TABLE [dbo].[SA_COMMONTREE] (
  [KIND_ID] decimal(38,30)  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SHORT_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] varchar(6) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [FULL_ID] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_COMMONTREE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'简码',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'SHORT_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'floder文件夹 2 left',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'通用树',
'SCHEMA', N'dbo',
'TABLE', N'SA_COMMONTREE'
GO


-- ----------------------------
-- Table structure for SA_DICTIONARY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_DICTIONARY]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_DICTIONARY]
GO

CREATE TABLE [dbo].[SA_DICTIONARY] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_DICTIONARY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_DICTIONARYDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_DICTIONARYDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_DICTIONARYDETAIL]
GO

CREATE TABLE [dbo].[SA_DICTIONARYDETAIL] (
  [DICTIONARY_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [TYPE_ID] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30) DEFAULT NULL NULL
)
GO

ALTER TABLE [dbo].[SA_DICTIONARYDETAIL] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_EXCELIMPORTLOG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_EXCELIMPORTLOG]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_EXCELIMPORTLOG]
GO

CREATE TABLE [dbo].[SA_EXCELIMPORTLOG] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TEMPLATE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BATCH_NUMBER] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [FILE_NAME] varchar(400) COLLATE Chinese_PRC_CI_AS  NULL,
  [ERROR_CODE] decimal(10)  NULL,
  [ERROR_MESSAGE] varchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [ERROR_COUNT] decimal(38,30)  NULL,
  [SUCCESS_COUNT] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_EXCELIMPORTLOG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_EXCELIMPORTTEMPLATE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_EXCELIMPORTTEMPLATE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_EXCELIMPORTTEMPLATE]
GO

CREATE TABLE [dbo].[SA_EXCELIMPORTTEMPLATE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [TABLE_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROCEDURE_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [REMARK] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_EXCELIMPORTTEMPLATE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_EXCELIMPORTTEMPLATEDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_EXCELIMPORTTEMPLATEDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_EXCELIMPORTTEMPLATEDETAIL]
GO

CREATE TABLE [dbo].[SA_EXCELIMPORTTEMPLATEDETAIL] (
  [TEMPLATE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXCEL_COLUMN_NUMBER] decimal(38,30)  NULL,
  [EXCEL_COLUMN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [COLUMN_NAME] varchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
  [COLUMN_DESCRIPTION] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [LENGTH] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_EXCELIMPORTTEMPLATEDETAIL] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_FLEXFIELDBIZGROUP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_FLEXFIELDBIZGROUP]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_FLEXFIELDBIZGROUP]
GO

CREATE TABLE [dbo].[SA_FLEXFIELDBIZGROUP] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [VISIBLE] decimal(38,30)  NULL,
  [BIZ_CODE] varchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_DETAIL_TABLE] decimal(38,30)  NULL,
  [UI_STYLE] decimal(38,30)  NULL,
  [COLS] decimal(38,30)  NULL,
  [REMARK] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [MODEL_FILE_PATH] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ENTITY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SHOW_MODEL] decimal(38,30)  NULL,
  [TABLE_LAYOUT] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_FLEXFIELDBIZGROUP] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_FLEXFIELDBIZGROUPFIELD
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_FLEXFIELDBIZGROUPFIELD]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_FLEXFIELDBIZGROUPFIELD]
GO

CREATE TABLE [dbo].[SA_FLEXFIELDBIZGROUPFIELD] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLEXFIELDBIZGROUP_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLEXFIELDDEFINITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [NULLABLE] decimal(38,30)  NULL,
  [CONTROL_WIDTH] decimal(38,30)  NULL,
  [CONTROL_HEIGHT] decimal(38,30)  NULL,
  [READ_ONLY] decimal(38,30)  NULL,
  [VISIBLE] decimal(38,30)  NULL,
  [NEW_LINE] decimal(38,30)  NULL,
  [LABEL_WIDTH] decimal(38,30)  NULL,
  [COL_SPAN] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_FLEXFIELDBIZGROUPFIELD] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_FLEXFIELDDEFINITION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_FLEXFIELDDEFINITION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_FLEXFIELDDEFINITION]
GO

CREATE TABLE [dbo].[SA_FLEXFIELDDEFINITION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_NAME] varchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_TYPE] varchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_LENGTH] decimal(38,30)  NULL,
  [FIELD_PRECISION] decimal(38,30)  NULL,
  [NULLABLE] decimal(38,30)  NULL,
  [DEFAULT_VALUE] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [MIN_VALUE] decimal(18,2)  NULL,
  [MAX_VALUE] decimal(18,2)  NULL,
  [REMARK] varchar(2400) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONTROL_TYPE] decimal(38,30)  NULL,
  [CONTROL_WIDTH] decimal(38,30)  NULL,
  [CONTROL_HEIGHT] decimal(38,30)  NULL,
  [DATA_SOURCE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_SOURCE] varchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [READ_ONLY] decimal(38,30)  NULL,
  [VISIBLE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [NEW_LINE] decimal(38,30)  NULL,
  [LABEL_WIDTH] decimal(38,30)  NULL,
  [COL_SPAN] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_FLEXFIELDDEFINITION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_FLEXFIELDSTORAGE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_FLEXFIELDSTORAGE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_FLEXFIELDSTORAGE]
GO

CREATE TABLE [dbo].[SA_FLEXFIELDSTORAGE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DETAIL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLEXFIELDDEFINITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_VALUE] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FLEXFIELDGROUP_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOOK_UP_VALUE] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FIELD_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_FLEXFIELDSTORAGE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_HISTORICSESSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_HISTORICSESSION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_HISTORICSESSION]
GO

CREATE TABLE [dbo].[SA_HISTORICSESSION] (
  [ID] varchar(36) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLIENT_IP] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SERVER_IP] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGIN_DATE] datetime2(7)  NULL,
  [LOGOUT_DATE] datetime2(7)  NULL,
  [LOGOUT_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGOUT_PERSON_MEMEBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGOUT_PERSON_MEMEBER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [SESSION_ID] varchar(36) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGIN_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ERROR_MESSAGE] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_HISTORICSESSION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_I18NPROPERTIES
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_I18NPROPERTIES]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_I18NPROPERTIES]
GO

CREATE TABLE [dbo].[SA_I18NPROPERTIES] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE1] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE2] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE3] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE4] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE5] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_KIND] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_I18NPROPERTIES] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'i18n 编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'语言值1(如中文显示数据)',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'VALUE1'
GO

EXEC sp_addextendedproperty
'MS_Description', N'语言值2(如英文显示数据)',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'VALUE2'
GO

EXEC sp_addextendedproperty
'MS_Description', N'语言值3',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'VALUE3'
GO

EXEC sp_addextendedproperty
'MS_Description', N'语言值4',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'VALUE4'
GO

EXEC sp_addextendedproperty
'MS_Description', N'语言值5',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'VALUE5'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源类型  数据字典i18nResourceKind',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES',
'COLUMN', N'RESOURCE_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据库数据国际化支持',
'SCHEMA', N'dbo',
'TABLE', N'SA_I18NPROPERTIES'
GO


-- ----------------------------
-- Table structure for SA_MACHINE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_MACHINE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_MACHINE]
GO

CREATE TABLE [dbo].[SA_MACHINE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [IP] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MAC] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SECURITY_GRADE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_MACHINE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_MESSAGE_REMIND
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_MESSAGE_REMIND]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_MESSAGE_REMIND]
GO

CREATE TABLE [dbo].[SA_MESSAGE_REMIND] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [REMIND_TITLE] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMIND_URL] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTE_FUNC] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [REPLACE_KIND] decimal(38,30)  NULL,
  [OPEN_KIND] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_MESSAGE_REMIND] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件夹ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'提示文本',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'REMIND_TITLE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'连接地址',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'REMIND_URL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'函数',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'EXECUTE_FUNC'
GO

EXEC sp_addextendedproperty
'MS_Description', N'替换类别  0 顺序替换 1名称替换 2存在则显示',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'REPLACE_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'页面打开方式 0 新窗口 1 弹出',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND',
'COLUMN', N'OPEN_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统消息提醒配置表',
'SCHEMA', N'dbo',
'TABLE', N'SA_MESSAGE_REMIND'
GO


-- ----------------------------
-- Table structure for SA_ONLINESESSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_ONLINESESSION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_ONLINESESSION]
GO

CREATE TABLE [dbo].[SA_ONLINESESSION] (
  [ID] varchar(36) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLIENT_IP] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SERVER_IP] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGIN_DATE] datetime2(7)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [SESSION_ID] varchar(36) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGIN_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_ONLINESESSION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_OPAGENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPAGENT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPAGENT]
GO

CREATE TABLE [dbo].[SA_OPAGENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLIENT_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [AGENT_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_DATE] datetime2(7)  NULL,
  [END_DATE] datetime2(7)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [PROC_AGENT_KIND_ID] decimal(38,30)  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [CAN_TRAN_AGENT] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [REMARK] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPAGENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'委托人ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'CLIENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'代理人ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'AGENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'开始时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'START_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'结束时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'END_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'代理方式ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'PROC_AGENT_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否允许把代理工作转交给其他人',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'CAN_TRAN_AGENT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'代理',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAGENT'
GO


-- ----------------------------
-- Table structure for SA_OPAGENTPROC
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPAGENTPROC]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPAGENTPROC]
GO

CREATE TABLE [dbo].[SA_OPAGENTPROC] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [AGENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPAGENTPROC] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_OPAUTHORIZE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPAUTHORIZE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPAUTHORIZE]
GO

CREATE TABLE [dbo].[SA_OPAUTHORIZE] (
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPAUTHORIZE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织机构ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAUTHORIZE',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAUTHORIZE',
'COLUMN', N'ROLE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'授权',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPAUTHORIZE'
GO


-- ----------------------------
-- Table structure for SA_OPBASEFUNCTIONTYPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPBASEFUNCTIONTYPE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPBASEFUNCTIONTYPE]
GO

CREATE TABLE [dbo].[SA_OPBASEFUNCTIONTYPE] (
  [ID] decimal(38,30)  NULL,
  [FOLDER_ID] decimal(38,30)  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPBASEFUNCTIONTYPE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'BaseFunctionTypeID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEFUNCTIONTYPE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件夹ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEFUNCTIONTYPE',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEFUNCTIONTYPE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEFUNCTIONTYPE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEFUNCTIONTYPE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEFUNCTIONTYPE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'基础职能角色
',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEFUNCTIONTYPE'
GO


-- ----------------------------
-- Table structure for SA_OPBASEMANAGEMENTTYPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPBASEMANAGEMENTTYPE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPBASEMANAGEMENTTYPE]
GO

CREATE TABLE [dbo].[SA_OPBASEMANAGEMENTTYPE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIZ_MANAGEMENT_TYPE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPBASEMANAGEMENTTYPE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务权限ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'BIZ_MANAGEMENT_TYPE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'目录ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理权限类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBASEMANAGEMENTTYPE'
GO


-- ----------------------------
-- Table structure for SA_OPBIZFUNCTION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPBIZFUNCTION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPBIZFUNCTION]
GO

CREATE TABLE [dbo].[SA_OPBIZFUNCTION] (
  [ID] decimal(38,30)  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPBIZFUNCTION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'BizFunctionTypeId',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTION',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务职能角色
',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTION'
GO


-- ----------------------------
-- Table structure for SA_OPBIZFUNCTIONOWNBASE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPBIZFUNCTIONOWNBASE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPBIZFUNCTIONOWNBASE]
GO

CREATE TABLE [dbo].[SA_OPBIZFUNCTIONOWNBASE] (
  [ID] decimal(38,30)  NULL,
  [BIZ_FUNCTION_ID] decimal(38,30)  NULL,
  [BASE_FUNCTION_TYPE_ID] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPBIZFUNCTIONOWNBASE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'BizFunctionOwnBaseID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTIONOWNBASE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'BizFunctionTypeId',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTIONOWNBASE',
'COLUMN', N'BIZ_FUNCTION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'BaseFunctionTypeID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTIONOWNBASE',
'COLUMN', N'BASE_FUNCTION_TYPE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务职能角色对应基础职能',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZFUNCTIONOWNBASE'
GO


-- ----------------------------
-- Table structure for SA_OPBIZMANAGEMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPBIZMANAGEMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPBIZMANAGEMENT]
GO

CREATE TABLE [dbo].[SA_OPBIZMANAGEMENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MANAGER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [MANAGE_TYPE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUBORDINATION_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPBIZMANAGEMENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理者ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'MANAGER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务权限类型ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'MANAGE_TYPE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'下属ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'SUBORDINATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理权限',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENT'
GO


-- ----------------------------
-- Table structure for SA_OPBIZMANAGEMENTTYPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPBIZMANAGEMENTTYPE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPBIZMANAGEMENTTYPE]
GO

CREATE TABLE [dbo].[SA_OPBIZMANAGEMENTTYPE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [MANAGE_ORG_KIND_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MANAGE_ORG_KIND_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] varchar(6) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] decimal(38,30)  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPBIZMANAGEMENTTYPE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理组织类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'MANAGE_ORG_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理组织类别名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'MANAGE_ORG_KIND_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'sys 系统 def 自定义',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类别ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务权限类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPBIZMANAGEMENTTYPE'
GO


-- ----------------------------
-- Table structure for SA_OPDATAKIND
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAKIND]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAKIND]
GO

CREATE TABLE [dbo].[SA_OPDATAKIND] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_KIND] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_SOURCE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAKIND] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'DATA_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据源',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'DATA_SOURCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'SEQUENCE',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'status',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'VERSION',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限维度定义',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAKIND'
GO


-- ----------------------------
-- Table structure for SA_OPDATAMANAGEBUSINESS
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAMANAGEBUSINESS]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAMANAGEBUSINESS]
GO

CREATE TABLE [dbo].[SA_OPDATAMANAGEBUSINESS] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_MANAGE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAMANAGEBUSINESS] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类型(分类，类型)',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_name',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'SEQUENCE',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'status',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'REMARK',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'DATA_MANAGE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限业务类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS'
GO


-- ----------------------------
-- Table structure for SA_OPDATAMANAGEBUSINESS_FIELD
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAMANAGEBUSINESS_FIELD]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAMANAGEBUSINESS_FIELD]
GO

CREATE TABLE [dbo].[SA_OPDATAMANAGEBUSINESS_FIELD] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATAMANAGEBUSINESS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_ORG_CONDITION] decimal(38,30)  NULL,
  [DATA_KIND_NAME] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_KIND_CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [TABLE_COLUMN] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [COLUMN_DATA_TYPE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [COLUMN_SYMBOL] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TABLE_ALIAS] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FORMULA] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_KIND] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MANAGE_TYPE] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAMANAGEBUSINESS_FIELD] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限业务类型ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'DATAMANAGEBUSINESS_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'特殊组织条件',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'IS_ORG_CONDITION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源维度名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'DATA_KIND_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源维度编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'DATA_KIND_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'column',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'TABLE_COLUMN'
GO

EXEC sp_addextendedproperty
'MS_Description', N'type',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'COLUMN_DATA_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'symbol',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'COLUMN_SYMBOL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'alias',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'TABLE_ALIAS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'Formula',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'FORMULA'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'DATA_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'MANAGE_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'SEQUENCE',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限业务过滤字段定义',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEBUSINESS_FIELD'
GO


-- ----------------------------
-- Table structure for SA_OPDATAMANAGEDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAMANAGEDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAMANAGEDETAIL]
GO

CREATE TABLE [dbo].[SA_OPDATAMANAGEDETAIL] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_MANAGE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAMANAGEDETAIL] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAIL',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAIL',
'COLUMN', N'DATA_MANAGE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAIL',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAIL',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'SEQUENCE',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAIL',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAIL',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限取值定义',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAIL'
GO


-- ----------------------------
-- Table structure for SA_OPDATAMANAGEDETAILRESOURCE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAMANAGEDETAILRESOURCE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAMANAGEDETAILRESOURCE]
GO

CREATE TABLE [dbo].[SA_OPDATAMANAGEDETAILRESOURCE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_MANAGEDETAL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_KIND] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_KEY] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_VALUE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [ORG_DATA_KIND] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAMANAGEDETAILRESOURCE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据取值定义ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'DATA_MANAGEDETAL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源维度ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'DATA_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'DATA_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源KEY',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'RESOURCE_KEY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源值',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'RESOURCE_VALUE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_name',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织权限类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE',
'COLUMN', N'ORG_DATA_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限包含维度资源',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEDETAILRESOURCE'
GO


-- ----------------------------
-- Table structure for SA_OPDATAMANAGEMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAMANAGEMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAMANAGEMENT]
GO

CREATE TABLE [dbo].[SA_OPDATAMANAGEMENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_MANAGE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_MANAGEDETAL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MANAGER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAMANAGEMENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'DATA_MANAGE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据取值定义ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'DATA_MANAGEDETAL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理者ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'MANAGER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限授权',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGEMENT'
GO


-- ----------------------------
-- Table structure for SA_OPDATAMANAGETYPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAMANAGETYPE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAMANAGETYPE]
GO

CREATE TABLE [dbo].[SA_OPDATAMANAGETYPE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAMANAGETYPE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类型(分类，数据权限)',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'full_name',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'SEQUENCE',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'REMARK',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'status',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPE'
GO


-- ----------------------------
-- Table structure for SA_OPDATAMANAGETYPEKIND
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPDATAMANAGETYPEKIND]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPDATAMANAGETYPEKIND]
GO

CREATE TABLE [dbo].[SA_OPDATAMANAGETYPEKIND] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_MANAGE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPDATAMANAGETYPEKIND] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPEKIND',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPEKIND',
'COLUMN', N'DATA_MANAGE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源维度ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPEKIND',
'COLUMN', N'DATA_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPEKIND',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据管理权限包含维度明细',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPDATAMANAGETYPEKIND'
GO


-- ----------------------------
-- Table structure for SA_OPERATIONLOG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPERATIONLOG]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPERATIONLOG]
GO

CREATE TABLE [dbo].[SA_OPERATIONLOG] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [APP_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [APP_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [APP_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_KIND_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [BEGIN_DATE] datetime2(7)  NULL,
  [END_DATE] datetime2(7)  NULL,
  [IP] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MAC] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLASS_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [METHOD_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOG_TYPE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATE_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_ID] decimal(38,30)  NULL,
  [STATUS_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_SECURITY_LEVEL_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_SECURITY_LEVEL_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MACHINE_SECURITY_LEVEL_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_SECURITY_LEVEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_SECURITY_LEVEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MACHINE_SECURITY_LEVEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [DESCRIPTION] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPERATIONLOG] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作日志',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPERATIONLOG'
GO


-- ----------------------------
-- Table structure for SA_OPERATIONLOG_BAK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPERATIONLOG_BAK]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPERATIONLOG_BAK]
GO

CREATE TABLE [dbo].[SA_OPERATIONLOG_BAK] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [APP_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [APP_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [APP_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORGAN_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_KIND_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [BEGIN_DATE] datetime2(7)  NULL,
  [END_DATE] datetime2(7)  NULL,
  [IP] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MAC] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CLASS_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [METHOD_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOG_TYPE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATE_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS_ID] decimal(38,30)  NULL,
  [STATUS_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_SECURITY_LEVEL_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_SECURITY_LEVEL_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MACHINE_SECURITY_LEVEL_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_SECURITY_LEVEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_SECURITY_LEVEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MACHINE_SECURITY_LEVEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPERATIONLOG_BAK] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作日志',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPERATIONLOG_BAK'
GO


-- ----------------------------
-- Table structure for SA_OPERATIONLOGDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPERATIONLOGDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPERATIONLOGDETAIL]
GO

CREATE TABLE [dbo].[SA_OPERATIONLOGDETAIL] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATIONLOG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARAMS] varchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [ERROR_MESSAGE] varchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [BEFORE_IMAGE] varchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [AFTER_IMAGE] varchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPERATIONLOGDETAIL] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'日志明细',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPERATIONLOGDETAIL'
GO


-- ----------------------------
-- Table structure for SA_OPFUNCTION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPFUNCTION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPFUNCTION]
GO

CREATE TABLE [dbo].[SA_OPFUNCTION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] varchar(6) COLLATE Chinese_PRC_CI_AS  NULL,
  [KEY_CODE] decimal(38,30)  NULL,
  [URL] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ICON] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPTH] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [DESCRIPTION] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATION_MAP_ID] decimal(38,30)  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPFUNCTION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'功能ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'node 节点 left 叶子 ',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'快捷键码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'KEY_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'URL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'图标',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'ICON'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'深度',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'DEPTH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理业务导图ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION',
'COLUMN', N'OPERATION_MAP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'功能',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION'
GO


-- ----------------------------
-- Table structure for SA_OPFUNCTION_FIELD_GROUP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPFUNCTION_FIELD_GROUP]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPFUNCTION_FIELD_GROUP]
GO

CREATE TABLE [dbo].[SA_OPFUNCTION_FIELD_GROUP] (
  [FUNCTION_FIELD_GROUP_ID] decimal(38,30)  NULL,
  [FUNCTION_ID] decimal(38,30)  NULL,
  [NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPFUNCTION_FIELD_GROUP] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'function_field_group_id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_FIELD_GROUP',
'COLUMN', N'FUNCTION_FIELD_GROUP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'功能ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_FIELD_GROUP',
'COLUMN', N'FUNCTION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_FIELD_GROUP',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_FIELD_GROUP',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'功能字段权限设置表',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_FIELD_GROUP'
GO


-- ----------------------------
-- Table structure for SA_OPFUNCTION_PERMISSIONFIELD
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPFUNCTION_PERMISSIONFIELD]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPFUNCTION_PERMISSIONFIELD]
GO

CREATE TABLE [dbo].[SA_OPFUNCTION_PERMISSIONFIELD] (
  [PERMISSION_FIELD_ID] decimal(38,30)  NULL,
  [FUNCTION_FIELD_GROUP_ID] decimal(38,30)  NULL,
  [FIELD_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_TYPE] decimal(1)  NULL,
  [FIELD_AUTHORITY] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPFUNCTION_PERMISSIONFIELD] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'Permission_field_id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_PERMISSIONFIELD',
'COLUMN', N'PERMISSION_FIELD_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'function_field_group_id',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_PERMISSIONFIELD',
'COLUMN', N'FUNCTION_FIELD_GROUP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_PERMISSIONFIELD',
'COLUMN', N'FIELD_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_PERMISSIONFIELD',
'COLUMN', N'FIELD_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_PERMISSIONFIELD',
'COLUMN', N'FIELD_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'权限',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_PERMISSIONFIELD',
'COLUMN', N'FIELD_AUTHORITY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'功能字段权限对应表',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPFUNCTION_PERMISSIONFIELD'
GO


-- ----------------------------
-- Table structure for SA_OPOPERATION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPOPERATION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPOPERATION]
GO

CREATE TABLE [dbo].[SA_OPOPERATION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [IS_COMMON] decimal(38,30)  NULL,
  [RESOURCE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPOPERATION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_OPORG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPORG]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPORG]
GO

CREATE TABLE [dbo].[SA_OPORG] (
  [ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(360) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(360) COLLATE Chinese_PRC_CI_AS  NULL,
  [LONG_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_CODE] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_KIND_ID] varchar(5) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPTH] decimal(38,30)  NULL,
  [PERSON_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] varchar(6) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [ORG_CODE] varchar(360) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_CODE] varchar(360) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_CODE] varchar(360) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_CODE] varchar(360) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_CENTER] decimal(38,30)  NULL,
  [CENTER_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CENTER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_MEMBER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CENTER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_SEQUENCE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ORG_KIND_ID] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_VIRTUAL] decimal(38,30)  NULL,
  [DEPT_LEVEL] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPORG] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织类型ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'TYPE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'长名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'LONG_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'FULL_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'全名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ogn 机构 dpt 部门 pos 岗位 psm 人员成员',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'ORG_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'深度',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'DEPTH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'PERSON_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'node 节点 leaf 叶子 ',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'公司编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'ORG_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'DEPT_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'岗位编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'POSITION_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员成员编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'PERSON_MEMBER_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否中心',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'IS_CENTER'
GO

EXEC sp_addextendedproperty
'MS_Description', N'中心编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'CENTER_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'公司名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'DEPT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'POSITION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员成员ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'PERSON_MEMBER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'中心ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'CENTER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'公司名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'ORG_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'DEPT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'岗位名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'POSITION_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员成员名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'PERSON_MEMBER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'中心名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'CENTER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'FULL_SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否虚拟组织',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'IS_VIRTUAL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门级别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'DEPT_LEVEL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'租户ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG',
'COLUMN', N'TENANT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORG'
GO


-- ----------------------------
-- Table structure for SA_OPORGPROPERTY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPORGPROPERTY]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPORGPROPERTY]
GO

CREATE TABLE [dbo].[SA_OPORGPROPERTY] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROPERTY_DEFINITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROPERTY_VALUE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROPERTY_DISPLAY] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPORGPROPERTY] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGPROPERTY',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'机构ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGPROPERTY',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'属性定义ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGPROPERTY',
'COLUMN', N'PROPERTY_DEFINITION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'属性值',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGPROPERTY',
'COLUMN', N'PROPERTY_VALUE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGPROPERTY',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织机构属性',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGPROPERTY'
GO


-- ----------------------------
-- Table structure for SA_OPORGPROPERTYDEFINITION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPORGPROPERTYDEFINITION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPORGPROPERTYDEFINITION]
GO

CREATE TABLE [dbo].[SA_OPORGPROPERTYDEFINITION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_KIND_ID] varchar(6) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_SOURCE] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPORGPROPERTYDEFINITION] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_OPORGROLEAUTHORIZE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPORGROLEAUTHORIZE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPORGROLEAUTHORIZE]
GO

CREATE TABLE [dbo].[SA_OPORGROLEAUTHORIZE] (
  [ID] decimal(38,30)  NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_KIND_ID] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPORGROLEAUTHORIZE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGROLEAUTHORIZE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGROLEAUTHORIZE',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色类别ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGROLEAUTHORIZE',
'COLUMN', N'ROLE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGROLEAUTHORIZE',
'COLUMN', N'VERSION'
GO


-- ----------------------------
-- Table structure for SA_OPORGTEMPLATE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPORGTEMPLATE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPORGTEMPLATE]
GO

CREATE TABLE [dbo].[SA_OPORGTEMPLATE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TYPE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPORGTEMPLATE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTEMPLATE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织类型ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTEMPLATE',
'COLUMN', N'TYPE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTEMPLATE',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTEMPLATE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTEMPLATE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTEMPLATE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织机构模板',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTEMPLATE'
GO


-- ----------------------------
-- Table structure for SA_OPORGTYPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPORGTYPE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPORGTYPE]
GO

CREATE TABLE [dbo].[SA_OPORGTYPE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_KIND_ID] varchar(5) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPORGTYPE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件夹ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ogn 机构 dpt 部门 pos 岗位',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'ORG_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织机构类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPORGTYPE'
GO


-- ----------------------------
-- Table structure for SA_OPPERMISSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPPERMISSION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPPERMISSION]
GO

CREATE TABLE [dbo].[SA_OPPERMISSION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [NODE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPPERMISSION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源类型ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'RESOURCE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'RESOURCE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'OPERATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERMISSION',
'COLUMN', N'REMARK'
GO


-- ----------------------------
-- Table structure for SA_OPPERSON
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPPERSON]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPPERSON]
GO

CREATE TABLE [dbo].[SA_OPPERSON] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGIN_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PASSWORD] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PASSWORD_TIME_LIMIT] decimal(38,30)  NULL,
  [PASSWORD_MODIFY_TIME] datetime2(7)  NULL,
  [MAIN_ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [DESCRIPTION] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEX] varchar(12) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIRTHDAY] datetime2(7)  NULL,
  [JOIN_DATE] datetime2(7)  NULL,
  [HOME_PLACE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEGREE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [GRADUATE_SCHOOL] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [SPECIALITY] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCHOOL_LENGTH] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TITLE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [MARRIAGE] varchar(12) COLLATE Chinese_PRC_CI_AS  NULL,
  [CERTIFICATE_NO] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CERTIFICATE_KIND_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [FAMILY_ADDRESS] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [ZIP] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MSN] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [QQ] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [EMAIL] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [MOBILE_PHONE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FAMILY_PHONE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [OFFICE_PHONE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [PHOTO_FILE_ID] decimal(38,30)  NULL,
  [COUNTRY] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROVINCE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CITY] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ENGLISH_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PAY_PASSWORD] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_OPERATOR] decimal(38,30)  NULL,
  [IS_HIDDEN] decimal(38,30)  NULL,
  [WEIXIN] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NUM] decimal(38,30)  NULL,
  [SECURITY_GRADE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_SECURITY_GRADE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CA_STATUS] decimal(38,30)  NULL,
  [CA_NO] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [TENANT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_KIND] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPPERSON] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'登录名',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'LOGIN_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'密码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PASSWORD'
GO

EXEC sp_addextendedproperty
'MS_Description', N'密码时限',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PASSWORD_TIME_LIMIT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'密码修改时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PASSWORD_MODIFY_TIME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'所属部门',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'MAIN_ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'性别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'SEX'
GO

EXEC sp_addextendedproperty
'MS_Description', N'出生日期',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'BIRTHDAY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参加工作日期',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'JOIN_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'出生地',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'HOME_PLACE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'学历',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'DEGREE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'毕业院校',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'GRADUATE_SCHOOL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'专业',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'SPECIALITY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'学年制',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'SCHOOL_LENGTH'
GO

EXEC sp_addextendedproperty
'MS_Description', N'职称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'TITLE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'婚姻状况',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'MARRIAGE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'证件号码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'CERTIFICATE_NO'
GO

EXEC sp_addextendedproperty
'MS_Description', N'证件类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'CERTIFICATE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'家庭住址',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'FAMILY_ADDRESS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'邮编',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'ZIP'
GO

EXEC sp_addextendedproperty
'MS_Description', N'Msn',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'MSN'
GO

EXEC sp_addextendedproperty
'MS_Description', N'QQ',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'QQ'
GO

EXEC sp_addextendedproperty
'MS_Description', N'电子邮件',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'EMAIL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'移动电话',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'MOBILE_PHONE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'家庭电话',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'FAMILY_PHONE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'办公电话',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'OFFICE_PHONE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'照片',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PHOTO_FILE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'国家',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'COUNTRY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'省',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PROVINCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'市',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'CITY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'英文名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'ENGLISH_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'薪资密码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PAY_PASSWORD'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否操作员',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'IS_OPERATOR'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否隐藏',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'IS_HIDDEN'
GO

EXEC sp_addextendedproperty
'MS_Description', N'微信',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'WEIXIN'
GO

EXEC sp_addextendedproperty
'MS_Description', N'密级',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'SECURITY_GRADE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员涉密登记',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PERSON_SECURITY_GRADE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'租户ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'TENANT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON',
'COLUMN', N'PERSON_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPPERSON'
GO


-- ----------------------------
-- Table structure for SA_OPREMIND
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPREMIND]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPREMIND]
GO

CREATE TABLE [dbo].[SA_OPREMIND] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPREMIND] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色提醒授权',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPREMIND'
GO


-- ----------------------------
-- Table structure for SA_OPROLE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPROLE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPROLE]
GO

CREATE TABLE [dbo].[SA_OPROLE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [TENANT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] decimal(38,30)  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_PERSON_KIND] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_OPROLE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'fn 功能 data 数据',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'ROLE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'租户ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'TENANT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'FULL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称全路径',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'FULL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类型',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE',
'COLUMN', N'ROLE_PERSON_KIND'
GO

EXEC sp_addextendedproperty
'MS_Description', N'角色',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLE'
GO


-- ----------------------------
-- Table structure for SA_OPROLEPERMISSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPROLEPERMISSION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPROLEPERMISSION]
GO

CREATE TABLE [dbo].[SA_OPROLEPERMISSION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERMISSION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPROLEPERMISSION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLEPERMISSION',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLEPERMISSION',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLEPERMISSION',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPROLEPERMISSION',
'COLUMN', N'VERSION'
GO


-- ----------------------------
-- Table structure for SA_OPTENANT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPTENANT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPTENANT]
GO

CREATE TABLE [dbo].[SA_OPTENANT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [INDUSTRY_ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DESCRIPTION] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONTACTS] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CONTACT_NUMBER] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROOT_FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [IS_INDUSTRY] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPTENANT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_OPUIELEMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPUIELEMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPUIELEMENT]
GO

CREATE TABLE [dbo].[SA_OPUIELEMENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEFAULT_OPERATION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPUIELEMENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件夹ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'默认操作',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT',
'COLUMN', N'DEFAULT_OPERATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'序列号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'界面元素',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENT'
GO


-- ----------------------------
-- Table structure for SA_OPUIELEMENTPERMISSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_OPUIELEMENTPERMISSION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_OPUIELEMENTPERMISSION]
GO

CREATE TABLE [dbo].[SA_OPUIELEMENTPERMISSION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERMISSION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [RESOURCE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_OPUIELEMENTPERMISSION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'权限ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'PERMISSION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'资源ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'RESOURCE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'OPERATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'界面元素权限',
'SCHEMA', N'dbo',
'TABLE', N'SA_OPUIELEMENTPERMISSION'
GO


-- ----------------------------
-- Table structure for SA_ORGFUN
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_ORGFUN]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_ORGFUN]
GO

CREATE TABLE [dbo].[SA_ORGFUN] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [IS_LAST] decimal(38,30)  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_ORGFUN] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'code',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'name',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'parent_id',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'is_last',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'IS_LAST'
GO

EXEC sp_addextendedproperty
'MS_Description', N'remark',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'sequence',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'status',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'version',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统可用组织机构函数',
'SCHEMA', N'dbo',
'TABLE', N'SA_ORGFUN'
GO


-- ----------------------------
-- Table structure for SA_PARAMETER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_PARAMETER]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_PARAMETER]
GO

CREATE TABLE [dbo].[SA_PARAMETER] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [VALUE] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [REMARK] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_PARAMETER] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数值',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'VALUE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统参数',
'SCHEMA', N'dbo',
'TABLE', N'SA_PARAMETER'
GO


-- ----------------------------
-- Table structure for SA_PDSCREEN
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_PDSCREEN]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_PDSCREEN]
GO

CREATE TABLE [dbo].[SA_PDSCREEN] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_PDSCREEN] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户桌面分屏设置',
'SCHEMA', N'dbo',
'TABLE', N'SA_PDSCREEN'
GO


-- ----------------------------
-- Table structure for SA_PDSCREENFUNCTION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_PDSCREENFUNCTION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_PDSCREENFUNCTION]
GO

CREATE TABLE [dbo].[SA_PDSCREENFUNCTION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCREEN_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FUNCTION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_PDSCREENFUNCTION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'id',
'SCHEMA', N'dbo',
'TABLE', N'SA_PDSCREENFUNCTION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分屏ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_PDSCREENFUNCTION',
'COLUMN', N'SCREEN_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'功能ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_PDSCREENFUNCTION',
'COLUMN', N'FUNCTION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_PDSCREENFUNCTION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户分屏应用记录表',
'SCHEMA', N'dbo',
'TABLE', N'SA_PDSCREENFUNCTION'
GO


-- ----------------------------
-- Table structure for SA_PERSONACCOUNTMANAGEMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_PERSONACCOUNTMANAGEMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_PERSONACCOUNTMANAGEMENT]
GO

CREATE TABLE [dbo].[SA_PERSONACCOUNTMANAGEMENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGIN_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [LOCKED_DATE] datetime2(7)  NULL,
  [LAST_LOGIN_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_PASSWORD_DATE] datetime2(7)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_PERSONACCOUNTMANAGEMENT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_PERSONCALENDAR
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_PERSONCALENDAR]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_PERSONCALENDAR]
GO

CREATE TABLE [dbo].[SA_PERSONCALENDAR] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SUBJECT] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [START_TIME] datetime2(7)  NULL,
  [END_TIME] datetime2(7)  NULL,
  [IS_ALLDAYEVENT] decimal(38,30)  NULL,
  [PERSON_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL,
  [BUSINESS_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LINK_BILL_URL] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_PERSONCALENDAR] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'标题',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'SUBJECT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'开始时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'START_TIME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'结束时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'END_TIME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否全天',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'IS_ALLDAYEVENT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'PERSON_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务单据ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'BUSINESS_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务单据链接',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR',
'COLUMN', N'LINK_BILL_URL'
GO

EXEC sp_addextendedproperty
'MS_Description', N'日程安排',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONCALENDAR'
GO


-- ----------------------------
-- Table structure for SA_PERSONLOGINLIMIT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_PERSONLOGINLIMIT]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_PERSONLOGINLIMIT]
GO

CREATE TABLE [dbo].[SA_PERSONLOGINLIMIT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MACHINE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LOGIN_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_PERSONLOGINLIMIT] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_PERSONQUERYSCHEME
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_PERSONQUERYSCHEME]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_PERSONQUERYSCHEME]
GO

CREATE TABLE [dbo].[SA_PERSONQUERYSCHEME] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PERSON_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARAM] varchar(4000) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_PERSONQUERYSCHEME] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME',
'COLUMN', N'PERSON_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类别',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'参数',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME',
'COLUMN', N'PARAM'
GO

EXEC sp_addextendedproperty
'MS_Description', N'序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户查询方案',
'SCHEMA', N'dbo',
'TABLE', N'SA_PERSONQUERYSCHEME'
GO


-- ----------------------------
-- Table structure for SA_SCHEDULING_JOB
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_SCHEDULING_JOB]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_SCHEDULING_JOB]
GO

CREATE TABLE [dbo].[SA_SCHEDULING_JOB] (
  [JOB_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [JOB_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [JOB_GROUP] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [JOB_STATUS] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CRON_EXPRESSION] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [JOB_CLASS_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [JOB_DESC] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [JOB_PARAM] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [BEAN_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_EXECUTE_TIME] datetime2(7)  NULL
)
GO

ALTER TABLE [dbo].[SA_SCHEDULING_JOB] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_SCHEDULING_JOB_LOG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_SCHEDULING_JOB_LOG]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_SCHEDULING_JOB_LOG]
GO

CREATE TABLE [dbo].[SA_SCHEDULING_JOB_LOG] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [JOB_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTE_TIME] datetime2(7)  NULL,
  [MESSAGE] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_SCHEDULING_JOB_LOG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_SECURITYPOLICY
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_SECURITYPOLICY]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_SECURITYPOLICY]
GO

CREATE TABLE [dbo].[SA_SECURITYPOLICY] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ENABLE_INTERNET_LOGIN] decimal(38,30)  NULL,
  [INTRANET_SEGMENT] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PASSWORD_VALIDITY_INTERVAL] decimal(38,30)  NULL,
  [PASSWORD_EXPIRE_GIVE_DAYS] decimal(38,30)  NULL,
  [PASSWORD_MINIMUM_LENGTH] decimal(38,30)  NULL,
  [LOCK_USER_PASSWORD_ERROR_TIME] decimal(38,30)  NULL,
  [ENABLE_COMPLEXITY] decimal(38,30)  NULL,
  [AUTO_UNLOCK_TIME] decimal(38,30)  NULL,
  [SECURITY_GRADE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NUMBER_COUNT] decimal(38,30)  NULL,
  [UPPERCASE_COUNT] decimal(38,30)  NULL,
  [LOWERCASE_COUNT] decimal(38,30)  NULL,
  [SPECIAL_CHARACTER_COUNT] decimal(38,30)  NULL,
  [INIT_PASSWORD] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_SECURITYPOLICY] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_SYNBASEDATAVERSION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_SYNBASEDATAVERSION]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_SYNBASEDATAVERSION]
GO

CREATE TABLE [dbo].[SA_SYNBASEDATAVERSION] (
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MAX_VERSION] decimal(38,30)  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [MAX_TIMESTAMP] datetime2(7)  NULL,
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_SYNBASEDATAVERSION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'类别编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最大版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'MAX_VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'修改时间',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'MAX_TIMESTAMP'
GO

EXEC sp_addextendedproperty
'MS_Description', N'主键ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'类别名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'系统新信息同步',
'SCHEMA', N'dbo',
'TABLE', N'SA_SYNBASEDATAVERSION'
GO


-- ----------------------------
-- Table structure for SA_TMAUTHORIZE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_TMAUTHORIZE]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_TMAUTHORIZE]
GO

CREATE TABLE [dbo].[SA_TMAUTHORIZE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUBORDINATION_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUBORDINATION_FULL_ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SYSTEM_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ROLE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [MANAGER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [VERSION] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[SA_TMAUTHORIZE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for SA_USERGROUP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_USERGROUP]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_USERGROUP]
GO

CREATE TABLE [dbo].[SA_USERGROUP] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOLDER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] varchar(8) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [FUNCTION_NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_USERGROUP] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组编码',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组名称',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组类别(系统,个人)',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'函数名',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'FUNCTION_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'CREATEd_BY_ID',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'CREATEd_BY_name',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'created_date',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'序号',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户分组',
'SCHEMA', N'dbo',
'TABLE', N'SA_USERGROUP'
GO


-- ----------------------------
-- Table structure for SA_USERGROUPDETAIL
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SA_USERGROUPDETAIL]') AND type IN ('U'))
	DROP TABLE [dbo].[SA_USERGROUPDETAIL]
GO

CREATE TABLE [dbo].[SA_USERGROUPDETAIL] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [GROUP_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[SA_USERGROUPDETAIL] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for TEST_STAFF
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[TEST_STAFF]') AND type IN ('U'))
	DROP TABLE [dbo].[TEST_STAFF]
GO

CREATE TABLE [dbo].[TEST_STAFF] (
  [USER_NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [PASSWORD] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [AGE] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEX] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [BIRTHDAY] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [ID] varchar(255) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[TEST_STAFF] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for WF_APPROVALELEMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALELEMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALELEMENT]
GO

CREATE TABLE [dbo].[WF_APPROVALELEMENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [DATA_SOURCE_CONFIG] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALELEMENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALELEMENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALELEMENT',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALELEMENT',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1系统 2业务',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALELEMENT',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALELEMENT',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALELEMENT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批要素',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALELEMENT'
GO


-- ----------------------------
-- Table structure for WF_APPROVALHANDLERKIND
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALHANDLERKIND]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALHANDLERKIND]
GO

CREATE TABLE [dbo].[WF_APPROVALHANDLERKIND] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [DATA_SOURCE_ID] decimal(38,30)  NULL,
  [DATA_SOURCE_CONFIG] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALHANDLERKIND] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据源',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND',
'COLUMN', N'DATA_SOURCE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据源配置',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND',
'COLUMN', N'DATA_SOURCE_CONFIG'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人类别',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALHANDLERKIND'
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULE]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULE]
GO

CREATE TABLE [dbo].[WF_APPROVALRULE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] decimal(38,30)  NULL,
  [NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRIORITY] decimal(38,30)  NULL,
  [STATUS] decimal(38,30)  NULL,
  [REMARK] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [CREATED_BY_NAME] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [CREATED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_DATE] datetime2(7)  NULL,
  [LAST_MODIFIED_BY_NAME] varchar(100) COLLATE Chinese_PRC_CI_AS  NULL,
  [LAST_MODIFIED_BY_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [SCOPE_KIND_ID] decimal(38,30)  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'规则ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'父节点ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'PARENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'PROC_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'PROC_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'PROC_UNIT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'PROC_UNIT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'节点类别 1 分类 2 规则 ',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'NODE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'优先级',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'PRIORITY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'状态 1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'备注',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'REMARK'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人姓名',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'CREATED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'CREATED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后修改时间',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'LAST_MODIFIED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后修改人姓名',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'LAST_MODIFIED_BY_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'最后修改人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'LAST_MODIFIED_BY_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织ID（公司ID：organ_id，以后可扩展为组织ID）',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'适用范围',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE',
'COLUMN', N'SCOPE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULE'
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULEELEMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULEELEMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULEELEMENT]
GO

CREATE TABLE [dbo].[WF_APPROVALRULEELEMENT] (
  [APPROVAL_RULE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [ELEMENT_CODE] nvarchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FOPERATOR] varchar(20) COLLATE Chinese_PRC_CI_AS  NULL,
  [FVALUE_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [FVALUE] varchar(1000) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [ELEMENT_ID] nvarchar(32) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULEELEMENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ApprovalRule',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'APPROVAL_RULE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批要素编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'ELEMENT_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作符',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'FOPERATOR'
GO

EXEC sp_addextendedproperty
'MS_Description', N'值ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'FVALUE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'值',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'FVALUE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批要素ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT',
'COLUMN', N'ELEMENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则要素',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEELEMENT'
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULEHANDLER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULEHANDLER]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULEHANDLER]
GO

CREATE TABLE [dbo].[WF_APPROVALRULEHANDLER] (
  [APPROVAL_RULE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [DESCRIPTION] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_KIND_CODE] nvarchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_ID] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ALLOW_ADD] decimal(38,30)  NULL,
  [ALLOW_SUBTRACT] decimal(38,30)  NULL,
  [ALLOW_TRANSFER] decimal(38,30)  NULL,
  [ALLOW_ABORT] decimal(38,30)  NULL,
  [NEED_TIMING] decimal(38,30)  NULL,
  [HELP_SECTION] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [CHIEF_ID] decimal(38,30)  NULL,
  [GROUP_ID] decimal(38,30)  NULL,
  [BIZ_HANDLER_PARAM] varchar(60) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_KIND_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [MUST_PASS] decimal(38,30)  NULL,
  [APPROVAL_HANDLER_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [LIMIT_TIME] decimal(38,30)  NULL,
  [SEND_MESSAGE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULEHANDLER] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ApprovalRuleID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'APPROVAL_RULE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理权限、部门、岗位、人员成员、函数',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'HANDLER_KIND_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'HANDLER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N' chief 主审 assistant 协审 cc抄送',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'允许加签',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'ALLOW_ADD'
GO

EXEC sp_addextendedproperty
'MS_Description', N'允许被减签',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'ALLOW_SUBTRACT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'允许转交',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'ALLOW_TRANSFER'
GO

EXEC sp_addextendedproperty
'MS_Description', N'允许终止',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'ALLOW_ABORT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'需要计时',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'NEED_TIMING'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批要点',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'HELP_SECTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'主审环节ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'CHIEF_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'GROUP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务处理人参数',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'BIZ_HANDLER_PARAM'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'HANDLER_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'是否必经节点',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'MUST_PASS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'限制时间',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'LIMIT_TIME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'发送消息',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER',
'COLUMN', N'SEND_MESSAGE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则审批人',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER'
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULEHANDLER_AUTH
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULEHANDLER_AUTH]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULEHANDLER_AUTH]
GO

CREATE TABLE [dbo].[WF_APPROVALRULEHANDLER_AUTH] (
  [HANDLER_AUTH_ID] decimal(38,30)  NOT NULL,
  [APPROVAL_RULE_ID] decimal(38,30)  NULL,
  [HANDLER_ID] decimal(38,30)  NULL,
  [FIELD_CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_TYPE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [FIELD_AUTHORITY] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULEHANDLER_AUTH] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人字段权限主键',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH',
'COLUMN', N'HANDLER_AUTH_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH',
'COLUMN', N'APPROVAL_RULE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH',
'COLUMN', N'HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH',
'COLUMN', N'FIELD_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH',
'COLUMN', N'FIELD_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段类型',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH',
'COLUMN', N'FIELD_TYPE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段权限',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH',
'COLUMN', N'FIELD_AUTHORITY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人字段权限表',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLER_AUTH'
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULEHANDLERASSIST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULEHANDLERASSIST]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULEHANDLERASSIST]
GO

CREATE TABLE [dbo].[WF_APPROVALRULEHANDLERASSIST] (
  [APPROVAL_RULE_HANDLER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [DESCRIPTION] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_ID] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_KIND_CODE] nvarchar(128) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULEHANDLERASSIST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULEHANDLERGROUP
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULEHANDLERGROUP]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULEHANDLERGROUP]
GO

CREATE TABLE [dbo].[WF_APPROVALRULEHANDLERGROUP] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [APPROVAL_RULE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_EXECUTE_MODE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [GROUP_ID] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULEHANDLERGROUP] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULEHANDLERUIPERM
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULEHANDLERUIPERM]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULEHANDLERUIPERM]
GO

CREATE TABLE [dbo].[WF_APPROVALRULEHANDLERUIPERM] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [APPROVAL_RULE_HANDLER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [KIND_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATION_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULEHANDLERUIPERM] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人字段权限主键',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLERUIPERM',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLERUIPERM',
'COLUMN', N'APPROVAL_RULE_HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLERUIPERM',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLERUIPERM',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段类型',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLERUIPERM',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'字段权限',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLERUIPERM',
'COLUMN', N'OPERATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批人字段权限表',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULEHANDLERUIPERM'
GO


-- ----------------------------
-- Table structure for WF_APPROVALRULESCOPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_APPROVALRULESCOPE]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_APPROVALRULESCOPE]
GO

CREATE TABLE [dbo].[WF_APPROVALRULESCOPE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [APPROVAL_RULE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_APPROVALRULESCOPE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULESCOPE',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULESCOPE',
'COLUMN', N'APPROVAL_RULE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织机构ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULESCOPE',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULESCOPE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则适用范围',
'SCHEMA', N'dbo',
'TABLE', N'WF_APPROVALRULESCOPE'
GO


-- ----------------------------
-- Table structure for WF_BASESEGMENTATIONTYPE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_BASESEGMENTATIONTYPE]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_BASESEGMENTATIONTYPE]
GO

CREATE TABLE [dbo].[WF_BASESEGMENTATIONTYPE] (
  [BASE_SEGMENTATION_TYPE_ID] decimal(38,30)  NOT NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FOLDER_ID] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_BASESEGMENTATIONTYPE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'BaseSegmentationTypeID',
'SCHEMA', N'dbo',
'TABLE', N'WF_BASESEGMENTATIONTYPE',
'COLUMN', N'BASE_SEGMENTATION_TYPE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_BASESEGMENTATIONTYPE',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_BASESEGMENTATIONTYPE',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_BASESEGMENTATIONTYPE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_BASESEGMENTATIONTYPE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'文件夹ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_BASESEGMENTATIONTYPE',
'COLUMN', N'FOLDER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'基础分段类',
'SCHEMA', N'dbo',
'TABLE', N'WF_BASESEGMENTATIONTYPE'
GO


-- ----------------------------
-- Table structure for WF_BIZSEGMENTATION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_BIZSEGMENTATION]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_BIZSEGMENTATION]
GO

CREATE TABLE [dbo].[WF_BIZSEGMENTATION] (
  [BIZ_SEGMENTATION_ID] decimal(38,30)  NOT NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [KIND_ID] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_BIZSEGMENTATION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'BizSegmentationID',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION',
'COLUMN', N'BIZ_SEGMENTATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'Org_ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION',
'COLUMN', N'CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION',
'COLUMN', N'NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 私有段 2 公开段 3 私有公开段',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION',
'COLUMN', N'KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织业务段',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATION'
GO


-- ----------------------------
-- Table structure for WF_BIZSEGMENTATIONAUTHORIZE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_BIZSEGMENTATIONAUTHORIZE]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_BIZSEGMENTATIONAUTHORIZE]
GO

CREATE TABLE [dbo].[WF_BIZSEGMENTATIONAUTHORIZE] (
  [BIZ_SEGMENTATION_AUTHORIZE_ID] decimal(38,30)  NOT NULL,
  [BIZ_SEGMENTATION_ID] decimal(38,30)  NULL,
  [BASE_SEGMENTATION_TYPE_ID] decimal(38,30)  NULL,
  [ORG_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_BIZSEGMENTATIONAUTHORIZE] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'SegmentationAuthorizeID',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATIONAUTHORIZE',
'COLUMN', N'BIZ_SEGMENTATION_AUTHORIZE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'BizSegmentationTypeID',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATIONAUTHORIZE',
'COLUMN', N'BIZ_SEGMENTATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'BaseSegmentationTypeID',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATIONAUTHORIZE',
'COLUMN', N'BASE_SEGMENTATION_TYPE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织Id',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATIONAUTHORIZE',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATIONAUTHORIZE',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATIONAUTHORIZE',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织分段授权',
'SCHEMA', N'dbo',
'TABLE', N'WF_BIZSEGMENTATIONAUTHORIZE'
GO


-- ----------------------------
-- Table structure for WF_HI_PROCUNITHANDLER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_HI_PROCUNITHANDLER]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_HI_PROCUNITHANDLER]
GO

CREATE TABLE [dbo].[WF_HI_PROCUNITHANDLER] (
  [ID] decimal(38,30)  NOT NULL,
  [BIZ_ID] decimal(38,30)  NULL,
  [PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLE_KIND_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLE_KIND_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLE_TIME] datetime2(7)  NULL,
  [RESULT] decimal(38,30)  NULL,
  [OPINION] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [COOPERATION_MODEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_EXECUTE_MODE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [GROUP_ID] decimal(38,30)  NULL,
  [CHIEF_ID] decimal(38,30)  NULL,
  [APPROVAL_RULE_HANDLER_ID] decimal(38,30)  NULL,
  [APPROVAL_RULE_ID] decimal(38,30)  NULL,
  [ASSISTANT_SEQUENCE] decimal(38,30)  NULL,
  [BIZ_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPERATOR_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERS] decimal(38,30)  NULL,
  [OPERATOR_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[WF_HI_PROCUNITHANDLER] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'数据版本',
'SCHEMA', N'dbo',
'TABLE', N'WF_HI_PROCUNITHANDLER',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'操作类型 add delete ',
'SCHEMA', N'dbo',
'TABLE', N'WF_HI_PROCUNITHANDLER',
'COLUMN', N'OPERATE_KIND_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本',
'SCHEMA', N'dbo',
'TABLE', N'WF_HI_PROCUNITHANDLER',
'COLUMN', N'VERS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理人',
'SCHEMA', N'dbo',
'TABLE', N'WF_HI_PROCUNITHANDLER',
'COLUMN', N'OPERATOR_NAME'
GO


-- ----------------------------
-- Table structure for WF_HI_PROCUNITHANDLERINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_HI_PROCUNITHANDLERINST]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_HI_PROCUNITHANDLERINST]
GO

CREATE TABLE [dbo].[WF_HI_PROCUNITHANDLERINST] (
  [ID] decimal(38,30)  NOT NULL,
  [BIZ_ID] decimal(38,30)  NULL,
  [PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL,
  [VERS] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_HI_PROCUNITHANDLERINST] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for WF_ID_LOG
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_ID_LOG]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_ID_LOG]
GO

CREATE TABLE [dbo].[WF_ID_LOG] (
  [OLD_ID] decimal(38,30)  NULL,
  [NEW_ID] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_ID_LOG] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for WF_PROCAPPROVALELEMENT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_PROCAPPROVALELEMENT]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_PROCAPPROVALELEMENT]
GO

CREATE TABLE [dbo].[WF_PROCAPPROVALELEMENT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PROC_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [ELEMENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [PROC_KEY] varchar(510) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_PROCAPPROVALELEMENT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程审批要素ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'PROC_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'PROC_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'PROC_UNIT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'PROC_UNIT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批要素ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'ELEMENT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程Key',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT',
'COLUMN', N'PROC_KEY'
GO

EXEC sp_addextendedproperty
'MS_Description', N'流程审批要素',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCAPPROVALELEMENT'
GO


-- ----------------------------
-- Table structure for WF_PROCDEF
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_PROCDEF]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_PROCDEF]
GO

CREATE TABLE [dbo].[WF_PROCDEF] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CODE] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [NAME] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [PARENT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [DESCRIPTION] varchar(1200) COLLATE Chinese_PRC_CI_AS  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [APPROVAL_BRIDGE_PROC_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [NODE_KIND_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [NEED_TIMING] decimal(38,30)  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [SHOW_QUERY_HANDLERS] decimal(38,30)  NULL,
  [PROC_ID] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [LIMIT_TIME] decimal(38,30)  NULL,
  [ASSISTANT_MUST_APPROVE] decimal(38,30)  NULL,
  [MERGE_HANDLER_KIND] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_PROCDEF] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'限制时间',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCDEF',
'COLUMN', N'LIMIT_TIME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理人合并类型 0 不合并  1 相邻合并  2 向后合并',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCDEF',
'COLUMN', N'MERGE_HANDLER_KIND'
GO


-- ----------------------------
-- Table structure for WF_PROCUNITHANDLER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_PROCUNITHANDLER]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_PROCUNITHANDLER]
GO

CREATE TABLE [dbo].[WF_PROCUNITHANDLER] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_PROC_UNIT_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLED_DATE] datetime2(7)  NULL,
  [RESULT] decimal(38,30)  NULL,
  [OPINION] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [COOPERATION_MODEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_EXECUTE_MODE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [GROUP_ID] decimal(38,30)  NULL,
  [CHIEF_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPROVAL_RULE_HANDLER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPROVAL_RULE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ASSISTANT_SEQUENCE] decimal(38,30)  NULL,
  [BIZ_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRINCIPAL_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRINCIPAL_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [EXECUTION_TIMES] decimal(38,30)  NULL,
  [SEND_MESSAGE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_PROCUNITHANDLER] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'BIZ_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'PROC_UNIT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'PROC_UNIT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'子环节ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'SUB_PROC_UNIT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'子环节名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'SUB_PROC_UNIT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理人姓名',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'HANDLER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'岗位ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'POSITION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'岗位',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'POSITION_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'DEPT_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'部门',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'DEPT_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'机构ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'ORG_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'机构',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'ORG_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理时间',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'HANDLED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理结果',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'RESULT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理意见',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'OPINION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'1 启用 0 禁用',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'STATUS'
GO

EXEC sp_addextendedproperty
'MS_Description', N'主审序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'协作模式 chief 主审 assistant 协审',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'COOPERATION_MODEL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务执行模式 preempt 抢占模式 sequential 顺序执行 simultaneous 并行执行',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'TASK_EXECUTE_MODE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'GROUP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'主审人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'CHIEF_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则处理人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'APPROVAL_RULE_HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'审批规则ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'APPROVAL_RULE_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'协审序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'ASSISTANT_SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务编码',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'BIZ_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'委托人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'PRINCIPAL_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'委托人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'PRINCIPAL_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'执行次数',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'EXECUTION_TIMES'
GO

EXEC sp_addextendedproperty
'MS_Description', N'发送消息',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER',
'COLUMN', N'SEND_MESSAGE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节处理人',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLER'
GO


-- ----------------------------
-- Table structure for WF_PROCUNITHANDLER_CACHE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_PROCUNITHANDLER_CACHE]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_PROCUNITHANDLER_CACHE]
GO

CREATE TABLE [dbo].[WF_PROCUNITHANDLER_CACHE] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_PROC_UNIT_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [SUB_PROC_UNIT_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [POSITION_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [DEPT_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ORG_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLED_DATE] datetime2(7)  NULL,
  [RESULT] decimal(38,30)  NULL,
  [OPINION] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [STATUS] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [FULL_NAME] varchar(2048) COLLATE Chinese_PRC_CI_AS  NULL,
  [FULL_ID] varchar(1024) COLLATE Chinese_PRC_CI_AS  NULL,
  [COOPERATION_MODEL_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [TASK_EXECUTE_MODE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [GROUP_ID] decimal(38,30)  NULL,
  [CHIEF_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPROVAL_RULE_HANDLER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [APPROVAL_RULE_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [ASSISTANT_SEQUENCE] decimal(38,30)  NULL,
  [BIZ_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRINCIPAL_ID] varchar(65) COLLATE Chinese_PRC_CI_AS  NULL,
  [PRINCIPAL_NAME] varchar(128) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[WF_PROCUNITHANDLER_CACHE] SET (LOCK_ESCALATION = TABLE)
GO


-- ----------------------------
-- Table structure for WF_PROCUNITHANDLERMANUSCRIPT
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_PROCUNITHANDLERMANUSCRIPT]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_PROCUNITHANDLERMANUSCRIPT]
GO

CREATE TABLE [dbo].[WF_PROCUNITHANDLERMANUSCRIPT] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [BIZ_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [PROC_UNIT_HANDLER_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NULL,
  [HEIGHT] decimal(38,30)  NULL,
  [OPINION_30] varchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [OPINION_64] varchar(max) COLLATE Chinese_PRC_CI_AS  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_PROCUNITHANDLERMANUSCRIPT] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLERMANUSCRIPT',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'业务ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLERMANUSCRIPT',
'COLUMN', N'BIZ_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'环节处理人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLERMANUSCRIPT',
'COLUMN', N'PROC_UNIT_HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'高度',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLERMANUSCRIPT',
'COLUMN', N'HEIGHT'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理意见',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLERMANUSCRIPT',
'COLUMN', N'OPINION_30'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理意见',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLERMANUSCRIPT',
'COLUMN', N'OPINION_64'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_PROCUNITHANDLERMANUSCRIPT',
'COLUMN', N'VERSION'
GO


-- ----------------------------
-- Table structure for WF_SEGMENTATIONHANDLER
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_SEGMENTATIONHANDLER]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_SEGMENTATIONHANDLER]
GO

CREATE TABLE [dbo].[WF_SEGMENTATIONHANDLER] (
  [SEGMENTATION_HANDLER_ID] decimal(38,30)  NOT NULL,
  [SEGMENTATION_ID] decimal(38,30)  NULL,
  [DESCRIPTION] varchar(512) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_KIND_CODE] varchar(64) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_ID] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [HANDLER_NAME] varchar(256) COLLATE Chinese_PRC_CI_AS  NULL,
  [GROUP_ID] decimal(38,30)  NULL,
  [VERSION] decimal(38,30)  NULL,
  [SEQUENCE] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_SEGMENTATIONHANDLER] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'SegmentationHandlerID',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'SEGMENTATION_HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'SegmentationID',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'SEGMENTATION_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'描述',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'DESCRIPTION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'管理权限、部门、岗位、人员成员、函数',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'HANDLER_KIND_CODE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理人ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'HANDLER_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'处理人名称',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'HANDLER_NAME'
GO

EXEC sp_addextendedproperty
'MS_Description', N'分组ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'GROUP_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'排序号',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER',
'COLUMN', N'SEQUENCE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'组织业务段处理人',
'SCHEMA', N'dbo',
'TABLE', N'WF_SEGMENTATIONHANDLER'
GO


-- ----------------------------
-- Table structure for WF_TASKCOLLECTION
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[WF_TASKCOLLECTION]') AND type IN ('U'))
	DROP TABLE [dbo].[WF_TASKCOLLECTION]
GO

CREATE TABLE [dbo].[WF_TASKCOLLECTION] (
  [ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [TASK_ID] varchar(64) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [PERSON_ID] varchar(32) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [CREATED_DATE] datetime2(7)  NULL,
  [VERSION] decimal(38,30)  NULL
)
GO

ALTER TABLE [dbo].[WF_TASKCOLLECTION] SET (LOCK_ESCALATION = TABLE)
GO

EXEC sp_addextendedproperty
'MS_Description', N'ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_TASKCOLLECTION',
'COLUMN', N'ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'任务ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_TASKCOLLECTION',
'COLUMN', N'TASK_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'人员ID',
'SCHEMA', N'dbo',
'TABLE', N'WF_TASKCOLLECTION',
'COLUMN', N'PERSON_ID'
GO

EXEC sp_addextendedproperty
'MS_Description', N'创建时间',
'SCHEMA', N'dbo',
'TABLE', N'WF_TASKCOLLECTION',
'COLUMN', N'CREATED_DATE'
GO

EXEC sp_addextendedproperty
'MS_Description', N'版本号',
'SCHEMA', N'dbo',
'TABLE', N'WF_TASKCOLLECTION',
'COLUMN', N'VERSION'
GO

EXEC sp_addextendedproperty
'MS_Description', N'用户收藏任务',
'SCHEMA', N'dbo',
'TABLE', N'WF_TASKCOLLECTION'
GO


-- ----------------------------
-- View structure for SYS_USERS_VIEW
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[SYS_USERS_VIEW]') AND type IN ('V'))
	DROP VIEW [dbo].[SYS_USERS_VIEW]
GO

CREATE VIEW [dbo].[SYS_USERS_VIEW] AS select o.person_id EMPID,
       p.name EMPNAME,
       p.login_name USERID,
       o.status,
       p.password,
       STUFF((select ','+so.dept_id from sa_oporg so where so.PERSON_ID= o.person_id for xml path ('') ), 1, 1, '') as ORGID_ID
  from sa_oporg o, sa_opperson p
 where o.person_id = p.id
   and o.org_kind_id = 'psm'
   and o.status=1
 group by o.person_id, p.name, p.login_name, p.password,o.status
GO


-- ----------------------------
-- View structure for V_ACT_HI_PROCINST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[V_ACT_HI_PROCINST]') AND type IN ('V'))
	DROP VIEW [dbo].[V_ACT_HI_PROCINST]
GO

CREATE VIEW [dbo].[V_ACT_HI_PROCINST] AS select pe.id_ as proc_inst_id_, pe.business_key_, pe.proc_def_id_,
       pe.key_ as process_definition_key_, pdf.full_id proc_full_id,
       pdf.full_name proc_full_name, pdf.name proc_name,
       case CHARINDEX('/', pdf.full_name,2) when 0 then null else substring(pdf.full_name, 2, CHARINDEX('/', pdf.full_name,2) - 2) end proc_sys_name,
       pe.start_time_, pe.end_time_, pe.applicant_person_member_id_,
       pe.applicant_person_member_name_, pe.applicant_dept_name_,
       pe.applicant_org_name_, pe.status_id_ proc_status_id_
  from act_hi_procinst_extension pe
	left join act_re_procdef pd on pe.proc_def_id_ = pd.id_
  left join wf_procdef pdf on pd.key_ = pdf.proc_id and pdf.node_kind_id  = 'proc'
GO


-- ----------------------------
-- View structure for V_ACT_HI_TASK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[V_ACT_HI_TASK]') AND type IN ('V'))
	DROP VIEW [dbo].[V_ACT_HI_TASK]
GO

CREATE VIEW [dbo].[V_ACT_HI_TASK] AS select te.id_, te.proc_inst_id_, te.proc_def_id_, te.name_, te.parent_task_id_, te.description_,
       te.creator_ogn_name_, te.creator_dept_name_, te.task_def_key_, te.process_definition_key_,
       te.priority_, te.start_time_,
       te.end_time_, te.due_date_,  te.kind_id_,
       te.creator_url_, te.executor_url_, te.creator_person_member_name_,
       te.creator_full_id_, te.executor_full_id_,
       te.executor_person_member_name_, te.executor_names_, te.status_id_,
       te.status_name_, te.catalog_id_, te.previous_id_, te.business_key_,
       te.business_code_, te.proc_unit_handler_id_,
       te.executor_person_member_id_, te.creator_person_member_id_,te.need_timing_,
       te.applicant_person_member_id_, te.applicant_person_member_name_
  from act_hi_taskinst_extension te
GO


-- ----------------------------
-- View structure for V_ACT_HI_TASK_FOR_DELETE
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[V_ACT_HI_TASK_FOR_DELETE]') AND type IN ('V'))
	DROP VIEW [dbo].[V_ACT_HI_TASK_FOR_DELETE]
GO

CREATE VIEW [dbo].[V_ACT_HI_TASK_FOR_DELETE] AS select te.id_, te.proc_inst_id_, te.proc_def_id_, te.name_, te.parent_task_id_, te.description_,
       te.creator_ogn_name_, te.creator_dept_name_, te.task_def_key_, te.process_definition_key_,
       te.priority_, te.start_time_,
       te.end_time_, te.due_date_,  te.kind_id_,
       te.creator_url_, te.executor_url_, te.creator_person_member_name_,
       te.creator_full_id_, te.executor_full_id_,
       te.executor_person_member_name_, te.executor_names_, te.status_id_,
       te.status_name_, te.catalog_id_, te.previous_id_, te.business_key_,
       te.business_code_, te.proc_unit_handler_id_,
       te.executor_person_member_id_, te.creator_person_member_id_,te.need_timing_,
       te.applicant_person_member_id_, te.applicant_person_member_name_
  from act_hi_taskinst_extension te
GO


-- ----------------------------
-- View structure for V_ACT_RU_TASK
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[V_ACT_RU_TASK]') AND type IN ('V'))
	DROP VIEW [dbo].[V_ACT_RU_TASK]
GO

CREATE VIEW [dbo].[V_ACT_RU_TASK] AS select te.id_, te.proc_inst_id_, te.proc_def_id_, te.name_, te.parent_task_id_, te.description_,
       te.creator_ogn_name_, te.creator_dept_name_, te.task_def_key_, te.process_definition_key_,
       te.delegation_, te.priority_, te.start_time_,
       '' end_time_, te.due_date_, te.suspension_state_, te.kind_id_,
       te.creator_url_, te.executor_url_, te.creator_person_member_name_,
       te.creator_full_id_, te.executor_full_id_,
       te.executor_person_member_name_, te.executor_names_, te.status_id_,
       te.status_name_, te.catalog_id_, te.previous_id_, te.business_key_,
       te.business_code_, te.proc_unit_handler_id_,
       te.executor_person_member_id_, te.creator_person_member_id_,te.need_timing_,
       te.applicant_person_member_id_, te.applicant_person_member_name_
  from  act_ru_task_extension te
GO


-- ----------------------------
-- View structure for V_SA_OPORG_EXCLUDE_POS
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[V_SA_OPORG_EXCLUDE_POS]') AND type IN ('V'))
	DROP VIEW [dbo].[V_SA_OPORG_EXCLUDE_POS]
GO

CREATE VIEW [dbo].[V_SA_OPORG_EXCLUDE_POS] AS select id, type_id, code, name, long_name,
       case org_kind_id when 'psm' then dept_id else parent_id end parent_id, full_id,
       full_code, full_name, org_kind_id, depth, person_id, node_kind_id,
       description, status, sequence, version, org_code, dept_code,
       position_code, person_member_code, is_center, center_code, org_id,
       dept_id, position_id, person_member_id, center_id, org_name,
       dept_name, position_name, person_member_name, center_name,
       full_sequence, full_org_kind_id, is_virtual
  from sa_oporg
 where org_kind_id != 'pos'
GO


-- ----------------------------
-- Primary Key structure for table ACT_GE_BYTEARRAY
-- ----------------------------
ALTER TABLE [dbo].[ACT_GE_BYTEARRAY] ADD CONSTRAINT [SYS_C0058291] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_GE_PROPERTY
-- ----------------------------
ALTER TABLE [dbo].[ACT_GE_PROPERTY] ADD CONSTRAINT [SYS_C0058295] PRIMARY KEY CLUSTERED ([NAME_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_ACTINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_ACTINST] ADD CONSTRAINT [SYS_C0058303] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_COMMENT
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_COMMENT] ADD CONSTRAINT [SYS_C0058306] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_IDENTITYLINK
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_IDENTITYLINK] ADD CONSTRAINT [SYS_C0058308] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_PROCINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_PROCINST] ADD CONSTRAINT [SYS_C0058313] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_PROCINST_EXTENSION
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_PROCINST_EXTENSION] ADD CONSTRAINT [PK_HI_PROCINST_EXTENSION] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_TASKINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_TASKINST] ADD CONSTRAINT [SYS_C0058319] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_TASKINST_EXTENSION
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_TASKINST_EXTENSION] ADD CONSTRAINT [PK_HI_TASKINST_EXTEND] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_TASKINST_RELATION
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_TASKINST_RELATION] ADD CONSTRAINT [PK_TASKINST_RELATION] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_HI_VARINST
-- ----------------------------
ALTER TABLE [dbo].[ACT_HI_VARINST] ADD CONSTRAINT [SYS_C0058326] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RE_DEPLOYMENT
-- ----------------------------
ALTER TABLE [dbo].[ACT_RE_DEPLOYMENT] ADD CONSTRAINT [SYS_C0058328] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RE_PROCDEF
-- ----------------------------
ALTER TABLE [dbo].[ACT_RE_PROCDEF] ADD CONSTRAINT [SYS_C0058339] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RE_PROCDEF_TREE_DEL
-- ----------------------------
ALTER TABLE [dbo].[ACT_RE_PROCDEF_TREE_DEL] ADD CONSTRAINT [PK_ACT_RE_PROCDEF_TREE] PRIMARY KEY CLUSTERED ([RE_PROCDEF_TREE_ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_EVENT_SUBSCR
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_EVENT_SUBSCR] ADD CONSTRAINT [PK_ACT_RU_EVENT_SUBSCR] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_EXECUTION
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_EXECUTION] ADD CONSTRAINT [SYS_C0058388] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_IDENTITYLINK
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_IDENTITYLINK] ADD CONSTRAINT [SYS_C0058415] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_JOB
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_JOB] ADD CONSTRAINT [SYS_C0058422] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_TASK
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_TASK] ADD CONSTRAINT [SYS_C0058424] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_TASK_EXTENSION
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_TASK_EXTENSION] ADD CONSTRAINT [PK_RU_TASK_EXTEND_ID] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table ACT_RU_VARIABLE
-- ----------------------------
ALTER TABLE [dbo].[ACT_RU_VARIABLE] ADD CONSTRAINT [SYS_C0058430] PRIMARY KEY CLUSTERED ([ID_])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table AVIC_IMP_EMPLOYEE
-- ----------------------------
ALTER TABLE [dbo].[AVIC_IMP_EMPLOYEE] ADD CONSTRAINT [SYS_C0058432] PRIMARY KEY CLUSTERED ([TMP_ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table AVIC_OAUTH2_CLIENT
-- ----------------------------
ALTER TABLE [dbo].[AVIC_OAUTH2_CLIENT] ADD CONSTRAINT [SYS_C0058439] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table AVIC_OBJECT_AUTHORIZE
-- ----------------------------
ALTER TABLE [dbo].[AVIC_OBJECT_AUTHORIZE] ADD CONSTRAINT [PK_AVIC_OBJECT_AUTHORIZE] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table AVIC_REPORT
-- ----------------------------
ALTER TABLE [dbo].[AVIC_REPORT] ADD CONSTRAINT [PK_AVIC_REPORT] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table AVIC_REPORT_FORM_PARAMETER
-- ----------------------------
ALTER TABLE [dbo].[AVIC_REPORT_FORM_PARAMETER] ADD CONSTRAINT [SYS_C0057996] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table DEMO_LEAVE
-- ----------------------------
ALTER TABLE [dbo].[DEMO_LEAVE] ADD CONSTRAINT [DEMO_LEAVE_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table DISTRICT
-- ----------------------------
ALTER TABLE [dbo].[DISTRICT] ADD CONSTRAINT [DISTRICT_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table PM_EXERCISE_LIST
-- ----------------------------
ALTER TABLE [dbo].[PM_EXERCISE_LIST] ADD CONSTRAINT [PK_PM_EXERCISE_LIST] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table PM_EXERCISEDETAIL
-- ----------------------------
ALTER TABLE [dbo].[PM_EXERCISEDETAIL] ADD CONSTRAINT [PK_EXERCISEDETAIL] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table PM_TREE_PRODUCT
-- ----------------------------
ALTER TABLE [dbo].[PM_TREE_PRODUCT] ADD CONSTRAINT [PK_TREE_PRODUCT] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table PM_TREE_SHAPE
-- ----------------------------
ALTER TABLE [dbo].[PM_TREE_SHAPE] ADD CONSTRAINT [PK_TREE_SHAPE] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table SA_OPERATIONLOG
-- ----------------------------
ALTER TABLE [dbo].[SA_OPERATIONLOG] ADD CONSTRAINT [PK_SA_OPERATIONLOG_NEW] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table SA_OPERATIONLOG_BAK
-- ----------------------------
ALTER TABLE [dbo].[SA_OPERATIONLOG_BAK] ADD CONSTRAINT [PK_SA_OPERATIONLOG] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table TEST_STAFF
-- ----------------------------
ALTER TABLE [dbo].[TEST_STAFF] ADD CONSTRAINT [SYS_C0057652] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALELEMENT
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALELEMENT] ADD CONSTRAINT [APPROVALELEMENT_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALHANDLERKIND
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALHANDLERKIND] ADD CONSTRAINT [PK_WF_APPROVALHANDLERKIND] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULE
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULE] ADD CONSTRAINT [APPROVALRULE_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULEELEMENT
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULEELEMENT] ADD CONSTRAINT [PK_WF_APPROVALRULEELEMENT] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULEHANDLER
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULEHANDLER] ADD CONSTRAINT [APPROVALRULEHANDLER_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULEHANDLER_AUTH
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULEHANDLER_AUTH] ADD CONSTRAINT [PK_HANDLER_AUTH_ID] PRIMARY KEY CLUSTERED ([HANDLER_AUTH_ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULEHANDLERASSIST
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULEHANDLERASSIST] ADD CONSTRAINT [PK_APPROVALRULEHANDLERASSIST] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULEHANDLERGROUP
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULEHANDLERGROUP] ADD CONSTRAINT [APPROVALRULEHANDLERGROUP_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULEHANDLERUIPERM
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULEHANDLERUIPERM] ADD CONSTRAINT [APPROVALRULEHANDLERUIPERM_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_APPROVALRULESCOPE
-- ----------------------------
ALTER TABLE [dbo].[WF_APPROVALRULESCOPE] ADD CONSTRAINT [PK_WF_APPROVALRULESCOPE] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_BASESEGMENTATIONTYPE
-- ----------------------------
ALTER TABLE [dbo].[WF_BASESEGMENTATIONTYPE] ADD CONSTRAINT [PK_WF_BASESEGMENTATIONTYPE] PRIMARY KEY CLUSTERED ([BASE_SEGMENTATION_TYPE_ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_BIZSEGMENTATION
-- ----------------------------
ALTER TABLE [dbo].[WF_BIZSEGMENTATION] ADD CONSTRAINT [PK_WF_BIZSEGMENTATION] PRIMARY KEY CLUSTERED ([BIZ_SEGMENTATION_ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_BIZSEGMENTATIONAUTHORIZE
-- ----------------------------
ALTER TABLE [dbo].[WF_BIZSEGMENTATIONAUTHORIZE] ADD CONSTRAINT [PK_BIZSEGMENTATIONAUTHORIZE] PRIMARY KEY CLUSTERED ([BIZ_SEGMENTATION_AUTHORIZE_ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_HI_PROCUNITHANDLER
-- ----------------------------
ALTER TABLE [dbo].[WF_HI_PROCUNITHANDLER] ADD CONSTRAINT [PK_HI_PROCUNITHANDLER] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_HI_PROCUNITHANDLERINST
-- ----------------------------
ALTER TABLE [dbo].[WF_HI_PROCUNITHANDLERINST] ADD CONSTRAINT [PK_HI_PROCUNITHANDLERINST] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_PROCAPPROVALELEMENT
-- ----------------------------
ALTER TABLE [dbo].[WF_PROCAPPROVALELEMENT] ADD CONSTRAINT [PK_WF_PROCAPPROVALELEMENT] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_PROCDEF
-- ----------------------------
ALTER TABLE [dbo].[WF_PROCDEF] ADD CONSTRAINT [PK_PROCDEF] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_PROCUNITHANDLER
-- ----------------------------
ALTER TABLE [dbo].[WF_PROCUNITHANDLER] ADD CONSTRAINT [PK_WF_PROCUNITHANDLER] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_PROCUNITHANDLER_CACHE
-- ----------------------------
ALTER TABLE [dbo].[WF_PROCUNITHANDLER_CACHE] ADD CONSTRAINT [PK_WF_PROCUNITHANDLER_CACHE] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_PROCUNITHANDLERMANUSCRIPT
-- ----------------------------
ALTER TABLE [dbo].[WF_PROCUNITHANDLERMANUSCRIPT] ADD CONSTRAINT [PROCUNITHANDLERMANUSCRIPT_PK] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_SEGMENTATIONHANDLER
-- ----------------------------
ALTER TABLE [dbo].[WF_SEGMENTATIONHANDLER] ADD CONSTRAINT [PK_WF_BIZSEGMENTATIONHANDLER] PRIMARY KEY CLUSTERED ([SEGMENTATION_HANDLER_ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO


-- ----------------------------
-- Primary Key structure for table WF_TASKCOLLECTION
-- ----------------------------
ALTER TABLE [dbo].[WF_TASKCOLLECTION] ADD CONSTRAINT [PK_SYS_PERSON_TASK_COLLECT] PRIMARY KEY CLUSTERED ([ID])
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
ON [PRIMARY]
GO