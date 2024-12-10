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
public class FlowController {
    @Autowired
    @Qualifier("jobLauncher")
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    @Qualifier("nextStepJob")
    private Job nextStepJob;

    @Autowired
    @Qualifier("onStepJob")
    private Job onStepJob;

    @Autowired
    @Qualifier("stopStepJob")
    private Job stopStepJob;

    @GetMapping("/next")
    public Long next() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(nextStepJob) // This uses the incrementer
                .toJobParameters();

        JobExecution run = jobLauncher.run(nextStepJob, jobParameters);
        log.info("sync] next job id : {}, state : {}", run.getJobId(), run.getExitStatus());
        return run.getJobId();
    }

    @GetMapping("/on")
    public Long on() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(onStepJob) // This uses the incrementer
                .toJobParameters();

        JobExecution run = jobLauncher.run(onStepJob, jobParameters);
        log.info("sync] on job id : {}, state : {}", run.getJobId(), run.getExitStatus());
        return run.getJobId();
    }

    @GetMapping("/stop")
    public Long stop() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(stopStepJob) // This uses the incrementer
                .toJobParameters();

        JobExecution run = jobLauncher.run(stopStepJob, jobParameters);
        log.info("sync] stop job id : {}, state : {}", run.getJobId(), run.getExitStatus());
        return run.getJobId();
    }

}
