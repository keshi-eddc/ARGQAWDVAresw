package com.edmi.configs;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TaskExecutePoolTest {

    @Async("myTaskAsyncPool")
    public void produceTask(int i){
        System.out.println("任务生产"+i);
    }
    @Async("myTaskAsyncPool")
    public void comsumerTask(int i){
        System.out.println("任务消费..."+i);
    }
}
