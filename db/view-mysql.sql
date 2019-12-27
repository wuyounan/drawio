 create or replace view `v_sa_oporg_exclude_pos` (`id`, `type_id`, `code`, `name`, `long_name`, `parent_id`, `full_id`, `full_code`, `full_name`, `org_kind_id`, `depth`, `person_id`, `node_kind_id`, `description`, `status`, `sequence`, `version`, `org_code`, `dept_code`, `position_code`, `person_member_code`, `is_center`, `center_code`, `org_id`, `dept_id`, `position_id`, `person_member_id`, `center_id`, `org_name`, `dept_name`, `position_name`, `person_member_name`, `center_name`, `full_sequence`, `full_org_kind_id`, `is_virtual`) as
  select id, type_id, code, name, long_name,
       (case org_kind_id when 'psm' then dept_id else parent_id end) parent_id,
      full_id,
       full_code, full_name, org_kind_id, depth, person_id, node_kind_id,
       description, status, sequence, version, org_code, dept_code,
       position_code, person_member_code, is_center, center_code, org_id,
       dept_id, position_id, person_member_id, center_id, org_name,
       dept_name, position_name, person_member_name, center_name,
       full_sequence, full_org_kind_id, is_virtual
  from sa_oporg
 where org_kind_id != 'pos';


CREATE
OR REPLACE VIEW V_ACT_HI_PROCINST (
	PROC_INST_ID_,
	BUSINESS_KEY_,
	PROC_DEF_ID_,
	PROCESS_DEFINITION_KEY_,
	PROC_FULL_ID,
	PROC_FULL_NAME,
	PROC_NAME,
	PROC_SYS_NAME,
	START_TIME_,
	END_TIME_,
	APPLICANT_PERSON_MEMBER_ID_,
	APPLICANT_PERSON_MEMBER_NAME_,
	APPLICANT_DEPT_NAME_,
	APPLICANT_ORG_NAME_,
	PROC_STATUS_ID_
) AS SELECT
	pe.id_ AS proc_inst_id_,
	pe.business_key_,
	pe.proc_def_id_,
	pe.key_ AS process_definition_key_,
	pdf.full_id proc_full_id,
	pdf.full_name proc_full_name,
	pdf. NAME proc_name,
	substr(
		pdf.full_name,
		2,
		instr(substr(pdf.full_name,3), '/') - 2
	) proc_sys_name,
	pe.start_time_,
	pe.end_time_,
	pe.applicant_person_member_id_,
	pe.applicant_person_member_name_,
	pe.applicant_dept_name_,
	pe.applicant_org_name_,
	pe.status_id_ proc_status_id_
FROM
	act_hi_procinst_extension pe
		join act_re_procdef pd on pe.proc_def_id_ = pd.id_
		left join wf_procdef pdf on pd.key_ = pdf.proc_id
WHERE
  pdf.node_kind_id = 'proc';