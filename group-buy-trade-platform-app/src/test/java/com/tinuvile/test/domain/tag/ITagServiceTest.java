package com.tinuvile.test.domain.tag;


import com.tinuvile.domain.tag.service.TagService;
import com.tinuvile.infrastructure.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBitSet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/6
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITagServiceTest {

    @Resource
    private TagService tagService;

    @Resource
    private IRedisService redisService;

    @Test
    public void test_01_tag_job() {
        tagService.execTagBatchJob("RQ_KJHKL98UU78H66554GFDV", "10001");
    }

    @Test
    public void test_02_tag_job_bitmap() {
        RBitSet bitSet = redisService.getBitSet("RQ_KJHKL98UU78H66554GFDV");
        log.info("Tinuvile存在，预期结果为true，测试结果：{}", bitSet.get(redisService.getIndexFromUserId("Tinuvile")));
        log.info("Erechamion不存在，预期结果为false，测试结果：{}", bitSet.get(redisService.getIndexFromUserId("Erechamion")));
    }

    @Test
    public void test_03_null_tag_bitmap() {
        RBitSet bitSet = redisService.getBitSet("null");
        log.info("测试结果:{}", bitSet.isExists());
    }
}
