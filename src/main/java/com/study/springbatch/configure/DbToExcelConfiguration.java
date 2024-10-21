package com.study.springbatch.configure;

import com.study.springbatch.QueryIdWithDB;
import com.study.springbatch.vo.TableMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static com.study.springbatch.util.DynamicDataConfig.*;

@Configuration
@Slf4j
public class DbToExcelConfiguration {

    @Bean
    public Job tableMetadataJob(JobRepository jobRepository, Step metadataToExcelStep) {
        log.info("===== tableMetadataJob =====");
        return new JobBuilder("tableMetadataJob", jobRepository)
                .start(metadataToExcelStep)
                .build();
    }

    @Bean
    @JobScope
    public Step metadataToExcelStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                    MyBatisCursorItemReader<TableMetadata> reader,
                                    ItemProcessor<TableMetadata, TableMetadata> processor,
                                    ItemWriter<TableMetadata> writer
    ) {
        log.info("===== metadataToExcelStep =====");
        return new StepBuilder("metadataToExcelStep", jobRepository)
                .<TableMetadata, TableMetadata>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public MyBatisCursorItemReader<TableMetadata> tableMetadataReader(
            @Value("#{jobParameters['url']}") String url,
            @Value("#{jobParameters['username']}") String username,
            @Value("#{jobParameters['password']}") String password,
            @Value("#{jobParameters['schema']}") String schema
    ) throws Exception {
        log.info("===== reader =====");
        MyBatisCursorItemReader<TableMetadata> reader = new MyBatisCursorItemReader<>();
        reader.setSqlSessionFactory(createSqlSessionFactory(createDataSource(url, username, password)));
        String queryId = QueryIdWithDB.fromDbType(extractDbType(url)).getQueryId();
        reader.setQueryId(queryId);
        reader.setParameterValues(Collections.singletonMap("schema", schema));
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<TableMetadata, TableMetadata> processor() {
        log.info("===== processor =====");
        final AtomicInteger counter = new AtomicInteger(0);
        return item -> {
            log.info("===== processor:{} =====", counter.incrementAndGet());
            return item;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<TableMetadata> excelWriter(@Value("#{jobParameters['filePath']}") String filePath) {
        log.info("===== writer =====");
        final AtomicInteger counter = new AtomicInteger(0);
        return items -> {
            log.info("===== writer:{} =====", counter.incrementAndGet());
            // 파일 경로 유효성 검증
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs(); // 디렉토리가 없으면 생성
            }

            try (Workbook workbook = new SXSSFWorkbook()) { // 메모리 효율을 위한 SXSSFWorkbook 사용
                Sheet sheet = workbook.createSheet("Table Metadata");

                // 헤더 생성
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Table/View Name");
                header.createCell(1).setCellValue("Type");

                // 데이터 작성
                int rowNum = 1;
                for (TableMetadata metadata : items) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(metadata.getName());
                    row.createCell(1).setCellValue(metadata.getType());
                }

                // 파일에 쓰기
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                }
            } catch (IOException e) {
                e.printStackTrace(); // 예외 처리 추가
            }
        };
    }

}
