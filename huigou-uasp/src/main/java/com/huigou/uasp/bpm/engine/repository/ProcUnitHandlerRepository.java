package com.huigou.uasp.bpm.engine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.huigou.uasp.bpm.engine.domain.model.ProcUnitHandler;

public interface ProcUnitHandlerRepository extends JpaRepository<ProcUnitHandler, String>, JpaSpecificationExecutor<ProcUnitHandler> {

	List<ProcUnitHandler> findByBizId(String bizId);

	List<ProcUnitHandler> findByBizIdAndProcUnitId(String bizId, String procUnitId);

	List<ProcUnitHandler> findByBizCode(String bizCode);

	ProcUnitHandler findFirstByBizCode(String bizCode);

	ProcUnitHandler findFirstByBizIdAndProcUnitIdAndGroupId(String bizId, String procUnitId, Integer groupId);

	@Query(name = "procUnitHandler.findAllNextProcUnitHandlers", value = "select t from ProcUnitHandler t where t.bizId = :bizId  and t.groupId > :groupId)")
	List<ProcUnitHandler> findAllNextProcUnitHandlers(@Param("bizId") String bizId, @Param("groupId") Integer groupId);

	@Query(name = "procUnitHandler.findNextGroupProcUnitHandlers", value = "select t  from ProcUnitHandler t where t.bizId = :bizId\n"
			+ "   and t.procUnitId = :procUnitId\n" + "   and status = 0\n" + "   and coalesce(result, 0) != 4\n" + "   and cooperationModelId != 'cc'\n"
			+ "   and groupId = (select min(o.groupId)\n" + "                    from ProcUnitHandler o\n" + "                   where o.bizId = :bizId\n"
			+ "                     and cooperationModelId != 'cc'\n" + "                     and o.procUnitId = :procUnitId\n"
			+ "                     and o.status = 0\n" + "                     and o.groupId > :groupId)")
	List<ProcUnitHandler> findNextGroupProcUnitHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId, @Param("groupId") Integer groupId);

	@Query(name = "procUnitHandler.findProcUnitHandlerIds", value = "select id from ProcUnitHandler where  bizId = :bizId and  procUnitId = :procUnitId and groupId = :groupId and status = 0")
	List<String> findProcUnitHandlerIds(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId, @Param("groupId") Integer groupId);

	@Query(name = "procUnitHandler.findProcUnitHandlers", value = "select o from ProcUnitHandler o where  bizId = :bizId and  procUnitId = :procUnitId and groupId = :groupId and status = 0")
	List<ProcUnitHandler> findProcUnitHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId, @Param("groupId") Integer groupId);

	@Query(name = "procUnitHandler.findCompletedProcUnitHandlers", value = "select o from ProcUnitHandler o where  bizId = :bizId and  procUnitId = :procUnitId and status > 0 order by groupId")
	List<ProcUnitHandler> findCompletedProcUnitHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId);

	@Query(name = "procUnitHandler.findCompletedProcUnitHandlersWithGroupId", value = "select o from ProcUnitHandler o where  bizId = :bizId and  procUnitId = :procUnitId and groupId = :groupId and status > 0 order by groupId")
	List<ProcUnitHandler> findCompletedProcUnitHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId, @Param("groupId") Integer groupId);

	@Query(name = "procUnitHandler.findCCProcUnitHandlers", value = "select o from ProcUnitHandler o  where o.bizId = :bizId and o.procUnitId = :procUnitId  and o.chiefId = :chiefId and o.cooperationModelId = 'cc'")
	List<ProcUnitHandler> findCCProcUnitHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId, @Param("chiefId") String chiefId);

	@Query(name = "procUnitHandler.findCCProcUnitHandlersByBizId", value = "select o from ProcUnitHandler o  where o.bizId = :bizId and o.cooperationModelId = 'cc'")
	List<ProcUnitHandler> findCCProcUnitHandlersByBizId(@Param("bizId") String bizId);

	@Query(name = "procUnitHandler.findInitialMendProcUnitHandlers", value = "select o  from ProcUnitHandler o  where o.bizId = :bizId  and o.procUnitId = :procUnitId\n"
			+ "   and o.cooperationModelId = 'mend'\n" + "   and o.status = -2")
	List<ProcUnitHandler> findInitialMendProcUnitHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId);

	@Query(name = "procUnitHandler.countProcUnitHandlers", value = "select count(1) from ProcUnitHandler o where o.bizId = :bizId and o.procUnitId = :procUnitId and o.cooperationModelId != 'cc' and o.handlerId = :handlerId")
	long countProcUnitHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId, @Param("handlerId") String handlerId);

	@Query(name = "procUnitHandler.queryAssistantHandlers", value = "select o from ProcUnitHandler o where o.bizId = :bizId and o.procUnitId = :procUnitId and o.status >= 0 and o.chiefId = :chiefId and o.cooperationModelId = 'assistant'")
	List<ProcUnitHandler> findAssistantHandlers(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId, @Param("chiefId") String chiefId);

	@Query(name = "procUnitHandler.findChiefHandler", value = "select o from ProcUnitHandler o where o.id = ( select i.chiefId from ProcUnitHandler i where i.id = :id) ")
    ProcUnitHandler findChiefHandler (@Param("id") String assistantProcUnitHandlerId);
	
	@Query(name = "procUnitHandler.findSucceedingProcUnitHandlers", value = "select o from ProcUnitHandler o where o.bizId = :bizId and o.status = 1 and o.groupId > :groupId and o.result != 4")
	List<ProcUnitHandler> findSucceedingProcUnitHandlers(@Param("bizId") String bizId, @Param("groupId") Integer groupId);

	@Query(name = "procUnitHandler.countByBizIdAndprocUnitId", value = "select count(1) from ProcUnitHandler o where o.bizId = :bizId and o.procUnitId = :procUnitId and o.status = 0 and cooperationModelId = 'chief'")
	Integer countChiefNotApproveByBizIdAndprocUnitId(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId);

	
	@Query(name = "procUnitHandler.countAssistantNotApproveByChiefId", value = "select count(1) from ProcUnitHandler o where o.bizId = :bizId and chiefId = :chiefId and o.status = 0 and cooperationModelId = 'assistant'")
	Integer countAssistantNotApproveByChiefId(@Param("bizId") String bizId, @Param("chiefId") String chiefId);
	
	@Query(name = "procUnitHandler.countCurrentGroupChiefNotApproveByBizIdAndprocUnitId", value = "select count(1) from ProcUnitHandler o where o.bizId = :bizId and o.procUnitId = :procUnitId and groupId = :groupId and o.status = 0 and cooperationModelId = 'chief'")
	long countCurrentGroupChiefNotApproveByBizIdAndprocUnitId(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId,
			@Param("groupId") Integer groupId);

	long deleteByBizCode(String bizCode);

	@Query(name = "procUnitHandler.getMaxGrouId", value = "select max(groupId) from ProcUnitHandler o where o.bizId = :bizId and o.procUnitId = :procUnitId and cooperationModelId = 'chief'")
	Integer getMaxGrouId(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId);

	@Query(name = "procUnitHandler.getExecutionTimes", value = "select coalesce(max(executionTimes), 0) from ProcUnitHandler o where o.bizId = :bizId and o.procUnitId = :procUnitId and cooperationModelId = 'chief'")
	Integer getExecutionTimes(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId);

	@Query(name = "procUnitHandler.getMaxSequence", value = "select coalesce(max(sequence), 0) from ProcUnitHandler o where o.bizId = :bizId and o.procUnitId = :procUnitId and cooperationModelId = 'chief'")
	Integer getMaxSequence(@Param("bizId") String bizId, @Param("procUnitId") String procUnitId);

	/**
	 * 删除回收后继处理人
	 */
	@Modifying
	@Query(name = "procUnitHandler.deleteWithdrawSucceedingHandlers", value = "delete from ProcUnitHandler t where t.id in (select i.procUnitHandlerId from HistoricTaskInstExtension i  where i.businessKey = :bizId and i.previousId = :taskId)")
	void deleteWithdrawSucceedingHandlers(@Param("bizId") String bizId, @Param("taskId") String taskId);
}
