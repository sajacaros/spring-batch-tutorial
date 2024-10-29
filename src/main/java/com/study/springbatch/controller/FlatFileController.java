package com.study.springbatch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FlatFileController {
    @Autowired
    @Qualifier("jobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    @Qualifier("flatFileJob")
    private Job job;

    @GetMapping("/flatfile")
    public Long tasklet() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(job) // This uses the incrementer
                .toJobParameters();

        JobExecution run = jobLauncher.run(job, jobParameters);
        log.info("sync] job id : {}, state : {}", run.getJobId(), run.getExitStatus());
        return run.getJobId();
    }
}
