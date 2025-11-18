package com.tinuvile.test.trigger;


import com.alibaba.fastjson.JSON;
import com.tinuvile.api.IDCCService;
import com.tinuvile.api.dto.GoodsMarketRequestDTO;
import com.tinuvile.api.dto.GoodsMarketResponseDTO;
import com.tinuvile.api.response.Response;
import com.tinuvile.trigger.http.MarketIndexController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketIndexControllerTest {

    @Resource
    private MarketIndexController marketIndexController;

    @Resource
    private IDCCService dccService;

    @Test
    public void test_queryGroupBuyMarketConfig() throws InterruptedException {
        dccService.updateConfig("downgradeSwitch", "0");
        dccService.updateConfig("cutRange", "100");
        dccService.updateConfig("whiteListSwitch", "0");

        Thread.sleep(1000);

        GoodsMarketRequestDTO requestDTO = new GoodsMarketRequestDTO();
        requestDTO.setSource("s01");
        requestDTO.setChannel("c01");
        requestDTO.setUserId("Tin01");
        requestDTO.setGoodsId("9890001");

        Response<GoodsMarketResponseDTO> response = marketIndexController.queryGroupBuyMarketConfig(requestDTO);

        log.info("请求参数:{}", JSON.toJSONString(requestDTO));
        log.info("应答结果:{}", JSON.toJSONString(response));
    }

}
