package com.study.springbatch.configure;

import com.study.springbatch.economic.EconomicIndexAggregator;
import com.study.springbatch.economic.EconomicIndexHeaderHandler;
import com.study.springbatch.economic.processor.AggregateEconomicProcessor;
import com.study.springbatch.vo.EconomicIndex;
import com.study.springbatch.vo.EconomicIndexChangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class FlatFileConfiguration {
    /**
     * CHUNK 크기를 지정한다.
     */
    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String FLAT_FILE_WRITER_CHUNK_JOB = "FLAT_FILE_WRITER_CHUNK_JOB";

    private final ItemProcessor<EconomicIndex, EconomicIndexChangeRate> itemProcessor = new AggregateEconomicProcessor();

    @Bean
    public Job flatFileJob(Step flatFileStep, JobRepository jobRepository) {
        log.info("------------------ Init flatFileJob -----------------");
        return new JobBuilder(FLAT_FILE_WRITER_CHUNK_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep)
                .build();
    }


    @Bean
    public Step flatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------------ Init flatFileStep -----------------");

        return new StepBuilder("flatFileStep", jobRepository)
                .<EconomicIndex, EconomicIndexChangeRate>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader())
                .processor(itemProcessor)
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<EconomicIndex> flatFileItemReader() {

        return new FlatFileItemReaderBuilder<EconomicIndex>()
                .name("FlatFileItemReader")
                .resource(new ClassPathResource("./data/경기종합지수_2020100__구성지표_시계열__10차__20241029175241.csv"))
                .encoding(ENCODING)
                .delimited().delimiter(",")
                .names("name", "value1", "value2", "value3", "value4", "value5", "value6")
                .targetType(EconomicIndex.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public FlatFileItemWriter<EconomicIndexChangeRate> flatFileItemWriter() {
        return new FlatFileItemWriterBuilder<EconomicIndexChangeRate>()
                .name("flatFileItemWriter")
                .resource(new FileSystemResource("./output/economic_rate.csv"))
                .encoding(ENCODING)
                .delimited().delimiter("\t")
                .names("name", "value1", "value2", "value3", "value4", "value5")
                .append(false)
                .lineAggregator(new EconomicIndexAggregator())
                .headerCallback(new EconomicIndexHeaderHandler())
                .build();

    }

}
