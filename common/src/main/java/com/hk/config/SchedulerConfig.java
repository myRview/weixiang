package com.hk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    private final static int POOL_SIZE = 8;
    private final static String THREAD_NAME_PREFIX = "scheduled-task-";
    private final static int AWAIT_TERMINATION_SECONDS = 60;
    private final static boolean WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN = true;


    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(POOL_SIZE);
        scheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        //等待终止时间 s
        scheduler.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
        // 关闭时等待任务完成
        scheduler.setWaitForTasksToCompleteOnShutdown(WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN);
        return scheduler;
    }
}