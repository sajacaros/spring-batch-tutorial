package com.study.springbatch.controller;

import com.study.springbatch.dto.DatabaseConfig;
import com.study.springbatch.util.DynamicDataConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class JobLauncherController {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

//    @Autowired
//    @Qualifier("myJob")
//    private Job job;

    @Autowired
    @Qualifier("tableMetadataJob")
    private Job tableMetadataJob;

//    @GetMapping("/run")
//    public Long handle() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
//                .getNextJobParameters(job) // This uses the incrementer
//                .toJobParameters();
//
//        JobExecution run = jobLauncher.run(job, jobParameters);
//        return run.getJobId();
//    }

    @PostMapping(value = "/metadata", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String exportMetadata(@RequestBody DatabaseConfig config) {

        try {
            // Job을 실행하면서 동적으로 생성된 DataSource를 사용하도록 설정
            jobLauncher.run(tableMetadataJob, new JobParametersBuilder()
                    .addString("url", config.getUrl())
                    .addString("username", config.getUsername())
                    .addString("password", config.getPassword())
                    .addString("filePath", config.getFilePath())
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters());

            return "Batch job executed successfully!";
        } catch (Exception e) {
            return "Batch job failed: " + e.getMessage();
        }
    }
}
