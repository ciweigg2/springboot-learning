package com.ciwei.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步任务执行器
 *
 * @Author Ciwei
 * @Date 2019/5/19/019
 */
@Component
@Slf4j
public class AsyncTaskExecutor {

    @Async
    public void executor(AsyncTaskConstructor asyncTaskGenerator, String taskInfo) {
        log.info("AsyncTaskExecutor is executing async task:{}", taskInfo);
        asyncTaskGenerator.async();
    }

}
