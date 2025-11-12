package com.tinuvile.test.types.rule02.logic;


import com.tinuvile.test.types.rule02.factory.Rule02TradeRuleFactory;
import com.tinuvile.types.design.framework.link.model2.handler.ILogicHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
@Slf4j
@Service
public class RuleLogic202 implements ILogicHandler<String, Rule02TradeRuleFactory.DynamicContext, XxxResponse> {

    @Override
    public XxxResponse apply(String requestParameter, Rule02TradeRuleFactory.DynamicContext dynamicContext) throws Exception {
        log.info("link model02 RuleLogic202");

        return new XxxResponse("hi! Tinuvile");
    }

}
