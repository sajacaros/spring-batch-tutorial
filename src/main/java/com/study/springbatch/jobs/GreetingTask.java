package com.study.springbatch.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class GreetingTask implements Tasklet, InitializingBean {
    @Override
    public RepeatStatus execute(
            StepContribution contribution,
            ChunkContext chunkContext
    ) throws Exception {
        log.info("------------------ Task Execute -----------------");
        log.info("GreetingTask: {}, {}", contribution, chunkContext);
        Thread.sleep(10000);

        return RepeatStatus.FINISHED;
//        return RepeatStatus.CONTINUABLE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("----------------- After Properites Sets() --------------");
    }
}