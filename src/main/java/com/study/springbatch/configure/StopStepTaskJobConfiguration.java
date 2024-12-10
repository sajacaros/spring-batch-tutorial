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
public class StopStepTaskJobConfiguration {

    public static final String STOP_STEP_TASK = "STOP_STEP_TASK";

    @Autowired
    PlatformTransactionManager transactionManager;


    @Bean
    public Job stopStepJob(Step stopStep01, Step stopStep02, JobRepository jobRepository) {
        log.info("------------------ Init myJob -----------------");
        return new JobBuilder(STOP_STEP_TASK, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stopStep01).on("FAILED").stop()
                .from(stopStep01).on("COMPLETED").to(stopStep02)
                .end()
                .build();
    }

    @Bean(name = "stopStep01")
    @JobScope
    public Step stepStop01(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");

        return new StepBuilder("stopStep01", jobRepository)
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

    @Bean(name = "stopStep02")
    @JobScope
    public Step stepStop02(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");

        return new StepBuilder("stopStep02", jobRepository)
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
