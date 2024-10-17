package com.study.springbatch.configure;

import com.study.springbatch.jobs.GreetingTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class BasicTaskJobConfiguration {
    @Autowired
    PlatformTransactionManager transactionManager;

    @Bean
    public JobLauncher myJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Tasklet greetingTasklet() {
        return new GreetingTask();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init myStep -----------------");
        return new StepBuilder("myStep", jobRepository)
                .tasklet(greetingTasklet(), transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job myJob(@Qualifier("myStep") Step step, JobRepository jobRepository) {
        log.info("------------------ Init myJob -----------------");
        return new JobBuilder("myJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
