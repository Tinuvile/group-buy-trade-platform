package com.tinuvile.domain.trade.service.lock.filter;


import com.tinuvile.domain.trade.adapter.repository.ITradeRepository;
import com.tinuvile.domain.trade.model.entity.GroupBuyActivityEntity;
import com.tinuvile.domain.trade.model.entity.TradeLockRuleCommandEntity;
import com.tinuvile.domain.trade.model.entity.TradeLockRuleFilterBackEntity;
import com.tinuvile.domain.trade.service.lock.factory.TradeRuleFilterFactory;
import com.tinuvile.types.design.framework.link.model2.handler.ILogicHandler;
import com.tinuvile.types.enums.ActivityStatusEnumVO;
import com.tinuvile.types.enums.ResponseCode;
import com.tinuvile.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Tinuvile
 * @description 活动的可用性，规则过滤【状态、有效期】
 * @since 2025/11/13
 */
@Slf4j
@Service
public class ActivityUsabilityRuleFilter implements ILogicHandler<TradeLockRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeLockRuleFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeLockRuleFilterBackEntity apply(TradeLockRuleCommandEntity requestParameter, TradeRuleFilterFactory.DynamicContext dynamicParameter) throws Exception {

        log.info("交易规则过滤 - 活动的可用性校验{} activityId:{}", requestParameter.getUserId(), requestParameter.getActivityId());

        // 查询拼团活动
        GroupBuyActivityEntity groupBuyActivity = repository.queryGroupBuyActivityEntityByActivityId(requestParameter.getActivityId());

        // 校验： 活动状态 - 可以抛业务异常code，也可以把code写入到动态上下文dynamicContext中，最后获取
        if (!ActivityStatusEnumVO.EFFECTIVE.equals(ActivityStatusEnumVO.valueOf(groupBuyActivity.getStatus()))) {
            log.info("活动的可用性校验，非生效状态 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0101);
        }

        // 校验： 活动时间
        Date currentTime = new Date();
        if (currentTime.before(groupBuyActivity.getStartTime()) || currentTime.after(groupBuyActivity.getEndTime())) {
            log.info("活动的可用性校验，不在有效期内 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0102);
        }

        dynamicParameter.setGroupBuyActivity(groupBuyActivity);

        return next(requestParameter, dynamicParameter);
    }

}
