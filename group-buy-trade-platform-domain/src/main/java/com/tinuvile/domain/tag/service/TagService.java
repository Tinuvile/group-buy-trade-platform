package com.tinuvile.domain.tag.service;

import com.tinuvile.domain.tag.adapter.repository.ITagRepository;
import com.tinuvile.domain.tag.model.entity.CrowdTagsJobEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/6
 */
@Slf4j
@Service
public class TagService implements ITagService {

    @Resource
    private ITagRepository repository;

    @Override
    public void execTagBatchJob(String tagId, String batchId) {
        log.info("人群标签批次任务 tagId: {}, batchId: {}", tagId, batchId);

        // 1. 查询批次任务
        CrowdTagsJobEntity crowdTagsJobEntity = repository.queryCrowdTagsJobEntity(tagId, batchId);

        // TODO 2. 采集用户数据

        // 3. 数据写入
        List<String> userIdList = new ArrayList<String>() {
            {
                add("Tinuvile");
                add("Erchamion");
            }
        };

        // 4. 写入数据库
        for (String userId : userIdList) {
            repository.addCrowdTagsUserId(tagId, userId);
        }

        // 5. 更新统计信息
        /* TODO 修复统计量错误更新
          目前TagRepository实现中忽略唯一索引冲突，即数据库中如果已经存在该用户ID，则该ID会被忽略，但统计信息仍会保留，统计量会误增加
         */
        repository.updateCrowdTagsStatistic(tagId, userIdList.size());
    }
}
