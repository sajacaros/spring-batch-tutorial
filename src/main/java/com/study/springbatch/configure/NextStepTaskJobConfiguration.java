package com.study.springbatch.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class NextStepTaskJobConfiguration {

    public static final String NEXT_STEP_TASK = "NEXT_STEP_TASK";
    @Autowired
    PlatformTransactionManager transactionManager;

    @Bean
    public Job nextStepJob(Step nextStep01, Step nextStep02, JobRepository jobRepository) {
        log.info("------------------ Init myJob -----------------");
        return new JobBuilder(NEXT_STEP_TASK, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(nextStep01)
                .next(nextStep02)
                .build();
    }

    @Bean(name = "nextStep01")
    @JobScope
    public Step step01(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");
        return new StepBuilder("step01", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("------------------\nExecute Step 01 Tasklet ...\n------------------");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean(name = "nextStep02")
    @JobScope
    public Step step02(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");

        return new StepBuilder("step02", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("------------------\nExecute Step 02 Tasklet ...\n------------------");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
