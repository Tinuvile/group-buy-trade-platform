package com.tinuvile.test.infrastructure.dao;

import com.alibaba.fastjson2.JSON;
import com.tinuvile.infrastructure.dao.IGroupBuyActivityDao;
import com.tinuvile.infrastructure.dao.po.GroupBuyActivity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Tinuvile
 * @since 2025-10-30
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupBuyActivityDaoTest {
    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;

    @Test
    public void test_queryGroupBuyActivityList() {
        List<GroupBuyActivity> groupBuyActivities = groupBuyActivityDao.queryGroupBuyActivityList();
        log.info("test result:{}", JSON.toJSONString(groupBuyActivities));
    }
}
