package com.tinuvile.test.domain.trade;


import com.alibaba.fastjson.JSON;
import com.tinuvile.domain.trade.adapter.repository.ITradeRepository;
import com.tinuvile.domain.trade.model.entity.GroupBuyTeamEntity;
import com.tinuvile.domain.trade.model.entity.MarketPayOrderEntity;
import com.tinuvile.domain.trade.model.entity.TradePaySettlementEntity;
import com.tinuvile.domain.trade.model.entity.TradePaySuccessEntity;
import com.tinuvile.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import com.tinuvile.domain.trade.service.ITradeSettlementOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/14
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeSettlementOrderServiceTest {

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;

    @MockBean
    private ITradeRepository tradeRepository;

    @Test
    public void test_settlementMarketPayOrder() throws Exception {

        // 准备Mock数据
        MarketPayOrderEntity mockOrder = new MarketPayOrderEntity();
        mockOrder.setTeamId("test-team-123");
        mockOrder.setOrderId("test-order-456");
        mockOrder.setDeductionPrice(BigDecimal.valueOf(100));
        mockOrder.setTradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE);

        GroupBuyTeamEntity mockTeam = new GroupBuyTeamEntity();
        mockTeam.setTeamId("test-team-123");
        mockTeam.setValidEndTime(new Date(System.currentTimeMillis() + 3600000)); // 1小时后过期

        // 设置Mock行为
        when(tradeRepository.queryMarketPayOrderEntityByOutTradeNo("tin02", "451517755304"))
                .thenReturn(mockOrder);
        when(tradeRepository.queryGroupBuyTeamByTeamId("test-team-123"))
                .thenReturn(mockTeam);

        TradePaySuccessEntity tradePaySuccessEntity = new TradePaySuccessEntity();
        tradePaySuccessEntity.setSource("s01");
        tradePaySuccessEntity.setChannel("c01");
        tradePaySuccessEntity.setUserId("tin02");
        tradePaySuccessEntity.setOutTradeNo("451517755304");
        tradePaySuccessEntity.setOutTradeTime(new Date());
        TradePaySettlementEntity tradePaySettlementEntity = tradeSettlementOrderService.settlementMarketPayOrder(tradePaySuccessEntity);
        log.info("请求参数:{}", JSON.toJSONString(tradePaySuccessEntity));
        log.info("响应参数:{}", JSON.toJSONString(tradePaySettlementEntity));
    }

}
