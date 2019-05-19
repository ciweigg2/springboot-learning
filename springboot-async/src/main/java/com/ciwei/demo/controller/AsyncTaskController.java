package com.ciwei.demo.controller;

import com.ciwei.demo.manager.AsyncTaskManager;
import com.ciwei.demo.entity.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Ciwei
 * @Date 2019/5/19/019
 */
@RestController
@RequestMapping(value = "/api/v1/asynctask")
public class AsyncTaskController {

    //注入异步任务管理器
    @Autowired
    AsyncTaskManager asyncTaskManager;

    @RequestMapping(value = "/startTask", method = RequestMethod.GET)
    public ResponseEntity startAsyncTask() {
        //调用任务管理器中的submit去提交一个异步任务
        TaskInfo taskInfo = asyncTaskManager.submit(() -> {
            System.out.println("__________");
            try {
                //模拟异步，睡眠6秒
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("__________");
        });

        return ResponseEntity.ok(taskInfo);
    }

    @RequestMapping(value = "/getTaskStatus", method = RequestMethod.GET)
    public ResponseEntity getTaskStatus( @RequestParam("taskId") String taskId) {
        return ResponseEntity.ok(asyncTaskManager.getTaskInfo(taskId));
    }

}
