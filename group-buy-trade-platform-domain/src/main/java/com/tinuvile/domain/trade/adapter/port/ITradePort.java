package com.tinuvile.domain.trade.adapter.port;


import com.tinuvile.domain.trade.model.entity.NotifyTaskEntity;

/**
 * @author Tinuvile
 * @description 交易接口服务接口
 * @since 2025/11/15
 */
public interface ITradePort {

    String groupBuyNotify(NotifyTaskEntity notifyTask) throws Exception;

}
