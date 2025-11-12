package com.tinuvile.types.design.framework.link.model2;


import com.tinuvile.types.design.framework.link.model2.chain.BusinessLinkedList;
import com.tinuvile.types.design.framework.link.model2.handler.ILogicHandler;
import lombok.Getter;

/**
 * @author Tinuvile
 * @description 链路装配
 * @since 2025/11/11
 */
@Getter
public class LinkArmory<T, D, R> {

    private final BusinessLinkedList<T, D, R> logicLink;

    @SafeVarargs
    public LinkArmory(String LinkName, ILogicHandler<T, D, R>... logicHandlers) {
        logicLink = new BusinessLinkedList<>(LinkName);
        for (ILogicHandler<T, D, R> logicHandler : logicHandlers) {
            logicLink.add(logicHandler);
        }
    }

}
