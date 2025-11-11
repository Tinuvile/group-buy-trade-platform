package com.tinuvile.test.types.rule01.logic;


import com.tinuvile.test.types.rule02.factory.Rule02TradeRuleFactory;
import com.tinuvile.types.design.framework.link.model1.AbstractLogicLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/11
 */
@Slf4j
@Service
public class RuleLogic102 extends AbstractLogicLink<String, Rule02TradeRuleFactory.DynamicContext, String> {

    @Override
    public String apply(String requestParameter, Rule02TradeRuleFactory.DynamicContext dynamicParameter) throws Exception {
        log.info("link model01 RuleLogic102");

        return "link model01 单实例链";
    }

}
