package com.tinuvile.domain.activity.service.trial.factory;


import com.tinuvile.domain.activity.model.entity.MarketProductEntity;
import com.tinuvile.domain.activity.model.entity.TrialBalanceEntity;
import com.tinuvile.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.tinuvile.domain.activity.model.valobj.SkuVO;
import com.tinuvile.domain.activity.service.trial.node.RootNode;
import com.tinuvile.types.design.framework.tree.StrategyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Tinuvile
 * @since 2025/11/1
 */
@Service
public class DefaultActivityStrategyFactory {
    private RootNode rootNode;

    public DefaultActivityStrategyFactory(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    public StrategyHandler<MarketProductEntity, DynamicContext, TrialBalanceEntity> strategyHandler() {
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        /* 拼团活动营销配置值对象 */
        private GroupBuyActivityDiscountVO groupBuyActivityDiscountVO;

        /* 商品信息 */
        private SkuVO skuVO;

        /** 折扣价格 */
        private BigDecimal deductionPrice;

        /** 是否可见 */
        private boolean visible;

        /** 是否可参与 */
        private boolean enable;
    }
}
