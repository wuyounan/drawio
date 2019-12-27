DROP VIEW "SYS_USERS_VIEW";  -- 不需要
DROP VIEW "V_ACT_HI_PROCINST";
DROP VIEW "V_ACT_HI_TASK"; -- 不需要
DROP VIEW "V_ACT_HI_TASK_FOR_DELETE"; -- 不需要
DROP VIEW "V_ACT_RU_TASK"; -- 不需要
DROP VIEW "V_SA_OPORG_EXCLUDE_POS";

  CREATE OR REPLACE VIEW "SYS_USERS_VIEW" ("EMPID", "EMPNAME", "USERID", "STATUS", "PASSWORD", "ORGID_ID") AS 
  select p.id EMPID,
       p.name EMPNAME,
       p.login_name USERID,
       o.status,
       p.password,
       listagg(o.dept_id, ',') within group(order by o.sequence) as ORGID_ID
  from sa_oporg o, sa_opperson p
 where o.person_id = p.id
   and o.org_kind_id = 'psm'
   and o.status=1
 group by p.id, p.name, p.login_name, p.password,o.status;

  CREATE OR REPLACE VIEW "V_ACT_HI_PROCINST" ("PROC_INST_ID_", "BUSINESS_KEY_", "PROC_DEF_ID_", "PROCESS_DEFINITION_KEY_", "PROC_FULL_ID", "PROC_FULL_NAME", "PROC_NAME", "PROC_SYS_NAME", "START_TIME_", "END_TIME_", "APPLICANT_PERSON_MEMBER_ID_", "APPLICANT_PERSON_MEMBER_NAME_", "APPLICANT_DEPT_NAME_", "APPLICANT_ORG_NAME_", "PROC_STATUS_ID_") AS 
  select pe.id_ as proc_inst_id_, pe.business_key_, pe.proc_def_id_,
       pe.key_ as process_definition_key_, pdf.full_id proc_full_id,
       pdf.full_name proc_full_name, pdf.name proc_name,
       substr(pdf.full_name, 2, instr(pdf.full_name, '/', 2) - 2) proc_sys_name,
       pe.start_time_, pe.end_time_, pe.applicant_person_member_id_,
       pe.applicant_person_member_name_, pe.applicant_dept_name_,
       pe.applicant_org_name_, pe.status_id_ proc_status_id_
  from act_hi_procinst_extension pe, act_re_procdef pd,
       wf_procdef pdf
 where pe.proc_def_id_ = pd.id_
   and pd.key_ = pdf.proc_id (+)
   and pdf.node_kind_id (+) = 'proc';

  CREATE OR REPLACE VIEW "V_ACT_HI_TASK" ("ID_", "PROC_INST_ID_", "PROC_DEF_ID_", "NAME_", "PARENT_TASK_ID_", "DESCRIPTION_", "CREATOR_OGN_NAME_", "CREATOR_DEPT_NAME_", "TASK_DEF_KEY_", "PROCESS_DEFINITION_KEY_", "PRIORITY_", "START_TIME_", "END_TIME_", "DUE_DATE_", "KIND_ID_", "CREATOR_URL_", "EXECUTOR_URL_", "CREATOR_PERSON_MEMBER_NAME_", "CREATOR_FULL_ID_", "EXECUTOR_FULL_ID_", "EXECUTOR_PERSON_MEMBER_NAME_", "EXECUTOR_NAMES_", "STATUS_ID_", "STATUS_NAME_", "CATALOG_ID_", "PREVIOUS_ID_", "BUSINESS_KEY_", "BUSINESS_CODE_", "PROC_UNIT_HANDLER_ID_", "EXECUTOR_PERSON_MEMBER_ID_", "CREATOR_PERSON_MEMBER_ID_", "NEED_TIMING_", "APPLICANT_PERSON_MEMBER_ID_", "APPLICANT_PERSON_MEMBER_NAME_") AS 
  select te.id_, te.proc_inst_id_, te.proc_def_id_, te.name_, te.parent_task_id_, te.description_,
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
  from act_hi_taskinst_extension te;

  CREATE OR REPLACE VIEW "V_ACT_HI_TASK_FOR_DELETE" ("ID_", "PROC_INST_ID_", "PROC_DEF_ID_", "NAME_", "PARENT_TASK_ID_", "DESCRIPTION_", "CREATOR_OGN_NAME_", "CREATOR_DEPT_NAME_", "TASK_DEF_KEY_", "PROCESS_DEFINITION_KEY_", "PRIORITY_", "START_TIME_", "END_TIME_", "DUE_DATE_", "KIND_ID_", "CREATOR_URL_", "EXECUTOR_URL_", "CREATOR_PERSON_MEMBER_NAME_", "CREATOR_FULL_ID_", "EXECUTOR_FULL_ID_", "EXECUTOR_PERSON_MEMBER_NAME_", "EXECUTOR_NAMES_", "STATUS_ID_", "STATUS_NAME_", "CATALOG_ID_", "PREVIOUS_ID_", "BUSINESS_KEY_", "BUSINESS_CODE_", "PROC_UNIT_HANDLER_ID_", "EXECUTOR_PERSON_MEMBER_ID_", "CREATOR_PERSON_MEMBER_ID_", "NEED_TIMING_", "APPLICANT_PERSON_MEMBER_ID_", "APPLICANT_PERSON_MEMBER_NAME_") AS 
  select te.id_, te.proc_inst_id_, te.proc_def_id_, te.name_, te.parent_task_id_, te.description_,
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
  from act_hi_taskinst_extension te;

  CREATE OR REPLACE VIEW "V_ACT_RU_TASK" ("ID_", "PROC_INST_ID_", "PROC_DEF_ID_", "NAME_", "PARENT_TASK_ID_", "DESCRIPTION_", "CREATOR_OGN_NAME_", "CREATOR_DEPT_NAME_", "TASK_DEF_KEY_", "PROCESS_DEFINITION_KEY_", "DELEGATION_", "PRIORITY_", "START_TIME_", "END_TIME_", "DUE_DATE_", "SUSPENSION_STATE_", "KIND_ID_", "CREATOR_URL_", "EXECUTOR_URL_", "CREATOR_PERSON_MEMBER_NAME_", "CREATOR_FULL_ID_", "EXECUTOR_FULL_ID_", "EXECUTOR_PERSON_MEMBER_NAME_", "EXECUTOR_NAMES_", "STATUS_ID_", "STATUS_NAME_", "CATALOG_ID_", "PREVIOUS_ID_", "BUSINESS_KEY_", "BUSINESS_CODE_", "PROC_UNIT_HANDLER_ID_", "EXECUTOR_PERSON_MEMBER_ID_", "CREATOR_PERSON_MEMBER_ID_", "NEED_TIMING_", "APPLICANT_PERSON_MEMBER_ID_", "APPLICANT_PERSON_MEMBER_NAME_") AS 
  select te.id_, te.proc_inst_id_, te.proc_def_id_, te.name_, te.parent_task_id_, te.description_,
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
  from  act_ru_task_extension te;

  CREATE OR REPLACE VIEW "V_SA_OPORG_EXCLUDE_POS" ("ID", "TYPE_ID", "CODE", "NAME", "LONG_NAME", "PARENT_ID", "FULL_ID", "FULL_CODE", "FULL_NAME", "ORG_KIND_ID", "DEPTH", "PERSON_ID", "NODE_KIND_ID", "DESCRIPTION", "STATUS", "SEQUENCE", "VERSION", "ORG_CODE", "DEPT_CODE", "POSITION_CODE", "PERSON_MEMBER_CODE", "IS_CENTER", "CENTER_CODE", "ORG_ID", "DEPT_ID", "POSITION_ID", "PERSON_MEMBER_ID", "CENTER_ID", "ORG_NAME", "DEPT_NAME", "POSITION_NAME", "PERSON_MEMBER_NAME", "CENTER_NAME", "FULL_SEQUENCE", "FULL_ORG_KIND_ID", "IS_VIRTUAL") AS 
  select id, type_id, code, name, long_name,
       decode(org_kind_id, 'psm', dept_id, parent_id) parent_id, full_id,
       full_code, full_name, org_kind_id, depth, person_id, node_kind_id,
       description, status, sequence, version, org_code, dept_code,
       position_code, person_member_code, is_center, center_code, org_id,
       dept_id, position_id, person_member_id, center_id, org_name,
       dept_name, position_name, person_member_name, center_name,
       full_sequence, full_org_kind_id, is_virtual
  from sa_oporg
 where org_kind_id != 'pos';
