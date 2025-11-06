package com.tinuvile.domain.tag.adapter.repository;


import com.tinuvile.domain.tag.model.entity.CrowdTagsJobEntity;

/**
 * @author Tinuvile
 * @description
 * @since 2025/11/6
 */
public interface ITagRepository {

    CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId);

    void addCrowdTagsUserId(String tagId, String userId);

    void updateCrowdTagsStatistic(String tagId, int count);
}
