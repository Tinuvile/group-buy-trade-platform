package com.tinuvile.types.design.framework.link.model1;


/**
 * @description 提供受理业务逻辑的方法
 * @author Tinuvile
 * @since 2025/11/11
 */
public interface ILogicLink<T, D, R> extends ILogicChainArmory<T, D, R> {

    R apply(T requestParameter, D dynamicParameter) throws Exception;

}
