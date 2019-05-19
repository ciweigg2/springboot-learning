package com.ciwei.demo.manager;

import cn.hutool.core.date.DateUtil;
import com.ciwei.demo.entity.TaskStatusEnum;
import com.ciwei.demo.entity.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * 步任务监控
 *
 * @Author Ciwei
 * @Date 2019/5/19/019
 */
@Component
@Aspect
@Slf4j
public class AsyncTaskMonitor {

    @Autowired
    AsyncTaskManager manager;

    @Around("execution(* com.ciwei.demo.service.AsyncTaskExecutor.*(..))")
    public void taskHandle(ProceedingJoinPoint pjp) {
        //获取taskId
        String taskId = pjp.getArgs()[1].toString();

        //获取任务信息
        TaskInfo taskInfo = manager.getTaskInfo(taskId);
        log.info("AsyncTaskMonitor is monitoring async task:{}", taskId);
        taskInfo.setStatus(TaskStatusEnum.RUNNING);
        manager.setTaskInfo(taskInfo);
        TaskStatusEnum status = null;

        try {
            pjp.proceed();
            status = TaskStatusEnum.SUCCESS;
        } catch (Throwable throwable) {
            status = TaskStatusEnum.FAILED;
            log.error("AsyncTaskMonitor:async task {} is failed.Error info:{}", taskId, throwable.getMessage());
        }

        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime(String.valueOf(DateUtil.betweenMs(taskInfo.getStartTime() ,taskInfo.getEndTime())) + "ms");
        manager.setTaskInfo(taskInfo);
    }
}
