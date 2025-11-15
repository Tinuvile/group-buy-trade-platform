package com.tinuvile.infrastructure.adapter.port;


import com.tinuvile.domain.trade.adapter.port.ITradePort;
import com.tinuvile.domain.trade.model.entity.NotifyTaskEntity;
import com.tinuvile.infrastructure.gateway.GroupBuyNotifyService;
import com.tinuvile.infrastructure.redis.IRedisService;
import com.tinuvile.types.enums.NotifyTaskHTTPEnumVO;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/15
 */
@Service
public class TradePort implements ITradePort {

    @Resource
    private GroupBuyNotifyService groupBuyNotifyService;

    @Resource
    private IRedisService redisService;

    @Override
    public String groupBuyNotify(NotifyTaskEntity notifyTask) throws Exception {

        RLock lock = redisService.getLock(notifyTask.lockKey());
        try {
            // group-buy-trade-platform 服务端需要部署到多台应用服务器上，会有很多任务一起执行，需要进行抢占以避免被执行多次
            if (lock.tryLock(3, 0, TimeUnit.SECONDS)) {
                try {
                    // 无效的 notifyUrl 直接返回成功
                    if (StringUtils.isBlank(notifyTask.getNotifyUrl()) || "暂无".equals(notifyTask.getNotifyUrl())) {
                        return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                    }
                    return groupBuyNotifyService.groupBuyNotify(notifyTask.getNotifyUrl(), notifyTask.getParameterJson());
                } finally {
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }

    }
}
