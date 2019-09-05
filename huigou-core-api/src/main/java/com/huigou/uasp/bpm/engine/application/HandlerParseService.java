package com.huigou.uasp.bpm.engine.application;

import java.util.List;

import com.huigou.context.OrgUnit;
import com.huigou.uasp.bpm.configuration.domain.model.HandlerKind;

/**
 * 处理人解析服务
 *
 * @author gongmm
 */
public interface HandlerParseService {

    String CURRENT_BIZ_SEGEMENTATION_ID = "currentBizSegementationId";

    /**
     * 构建处理人
     *
     * @param handlerKind 处理人类别
     * @param handlerId   处理人ID
     * @param orgUnits    组织机构单元
     */
    void buildHandler(HandlerKind handlerKind, String handlerId, List<OrgUnit> orgUnits);

    /**
     * 构建处理人
     *
     * @param handlerKind        处理人类别
     * @param bizSegementationId 业务段ID
     * @param handlerId          处理人ID
     * @param orgUnits           组织机构单元
     */
    void buildSegmentationHandler(HandlerKind handlerKind, Long bizSegementationId, String handlerId, List<OrgUnit> orgUnits);
}
