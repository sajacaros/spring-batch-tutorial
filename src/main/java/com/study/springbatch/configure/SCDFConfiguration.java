package com.study.springbatch.configure;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTask
@EnableBatchProcessing(tablePrefix = "boot3_batch_")
public class SCDFConfiguration {
}
