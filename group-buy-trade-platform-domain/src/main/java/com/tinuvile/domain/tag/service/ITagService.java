package com.tinuvile.domain.tag.service;

/**
 * @author Tinuvile
 * @description 人群标签服务接口
 * @since 2025/11/6
 */
public interface ITagService {

    /**
     * 执行人群标签批次任务
     *
     * @param tagId 标签ID
     * @param batchId 批次ID
     */
    void execTagBatchJob(String tagId, String batchId);
}
