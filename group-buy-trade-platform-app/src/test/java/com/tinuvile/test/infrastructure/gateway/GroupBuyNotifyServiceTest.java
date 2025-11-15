package com.tinuvile.test.infrastructure.gateway;


import com.tinuvile.infrastructure.gateway.GroupBuyNotifyService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/15
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GroupBuyNotifyServiceTest {

    @Resource
    private GroupBuyNotifyService groupBuyNotifyService;

    @LocalServerPort
    private int port;

    @Test
    public void test_notify_api() throws Exception {

        String notifyRequestDTOJSON = "{\"teamId\":\"57199993\",\"outTradeNoList\":\"038426231487,652896391719,619401409195\"}";
        String notifyRequestDTOJSON2 = "{\"teamId\":\"57199993\",\"outTradeNoList\":[\"038426231487\",\"652896391719\",\"619401409195\"]}";

        String response = groupBuyNotifyService.groupBuyNotify("http://127.0.0.1:" + port + "/api/v1/test/group_buy_notify", notifyRequestDTOJSON2);

        log.info("测试结果:{}", response);
        assertEquals("success", response);
    }

    @Test
    public void test() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\"teamId\":\"57199993\",\"outTradeNoList\":\"038426231487,652896391719,619401409195\"}", mediaType);
        Request request = new Request.Builder()
                .url("http://127.0.0.1:" + port + "/api/v1/test/group_buy_notify")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        log.info("测试结果:{}", response);
    }

}
