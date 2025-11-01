package com.tinuvile.types.design.framework.tree;


import lombok.Getter;
import lombok.Setter;

/**
 * @author Tinuvile
 * @since 2025/11/1
 */
public abstract class AbstractStrategyRouter<T, D, R> implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {
    @Getter
    @Setter
    protected StrategyHandler<T, D, R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter, D dynamicContext) throws Exception {
        StrategyHandler<T, D, R> strategyHandler = get(requestParameter, dynamicContext);
        if (null != strategyHandler) return strategyHandler.apply(requestParameter, dynamicContext);
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }
}
