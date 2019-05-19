package com.ciwei.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 任务信息
 *
 * @Author Ciwei
 * @Date 2019/5/19/019
 */
@Data
public class TaskInfo {

    private String taskId;

    private TaskStatusEnum status;

    private Date startTime;

    private Date endTime;

    private String totalTime;

}
