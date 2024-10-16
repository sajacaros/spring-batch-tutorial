package com.study.springbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLauncherController {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    Job job;

    @RequestMapping("/run")
    public void handle() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(job) // This uses the incrementer
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }
}
