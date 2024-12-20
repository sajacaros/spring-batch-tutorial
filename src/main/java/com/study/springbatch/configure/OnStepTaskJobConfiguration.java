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

import java.util.Random;

@Slf4j
@Configuration
public class OnStepTaskJobConfiguration {

    public static final String ON_STEP_TASK = "ON_STEP_TASK";

    @Autowired
    PlatformTransactionManager transactionManager;

    @Bean
    public Job onStepJob(Step onStep01, Step onStep02, Step onStep03, JobRepository jobRepository) {
        log.info("------------------ Init myJob -----------------");
        return new JobBuilder(ON_STEP_TASK, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(onStep01).on("FAILED").to(onStep03)
                .from(onStep01).on("COMPLETED").to(onStep02)
                .end()
                .build();
    }

    @Bean(name = "onStep01")
    @JobScope
    public Step onStep01(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");

        return new StepBuilder("onStep01", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("------------------\nExecute Step 01 Tasklet ...\n------------------");

                        Random random = new Random();
                        int randomValue = random.nextInt(1000);

                        if (randomValue % 2 == 0) {
                            return RepeatStatus.FINISHED;
                        } else {
                            throw new RuntimeException("Error This value is Odd: " + randomValue);
                        }
                    }
                }, transactionManager)
                .build();
    }

    @Bean(name = "onStep02")
    @JobScope
    public Step onStep02(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");

        return new StepBuilder("onStep02", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("------------------\nExecute Step 02 Tasklet ...\n------------------");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean(name = "onStep03")
    @JobScope
    public Step onStep03(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");

        return new StepBuilder("onStep03", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("------------------\nExecute Step 03 Tasklet ...\n------------------");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}