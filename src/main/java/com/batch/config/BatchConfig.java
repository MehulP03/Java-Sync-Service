package com.batch.config;

import com.batch.model.User;
import com.opencsv.CSVWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.beans.JavaBean;
import java.io.File;
import java.io.FileWriter;

@Configuration
@EnableBatchProcessing
//@PropertySource("classpath:scheduler.properties")
public class BatchConfig {
    private static final String filePath = "./resource/Assignment Sheet.csv";


    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<User> reader(){
        FlatFileItemReader<User> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("Assignment Sheet.csv"));
        reader.setLineMapper(getLineMapper());
        reader.setLinesToSkip(1);
        return reader;
    }

    //This function is used to read data from csv file
    private LineMapper<User> getLineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        //This is used for fetching specific columns which is required
        lineTokenizer.setNames(new String[]{"ver","product_family","country","os"});
        //This is used for the specifying the column number
        lineTokenizer.setIncludedFields(new int[]{1,2,3,6});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    //This UserItemProcessor process the data
    public UserItemProcessor processor(){
        return new UserItemProcessor();
    }

    @Bean
    //This function is used to write data which is read by above function to store in our Database
    public JdbcBatchItemWriter<User> writer(){

        JdbcBatchItemWriter<User> writer =new JdbcBatchItemWriter<User>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
        writer.setSql("insert into  udata(ver,product_family,country,os) values (:ver, " +
                ":product_family, :country, :os)");
        writer.setDataSource(this.dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(){

        return this.jobBuilderFactory.get("USER-IMPORT-JOB")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
                .<User, User>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Autowired
    public static void writeDataLineByLine(String filePath){
        File file = new File(filePath);
        try{
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            String[] addData = {"2018-05-21 04:25:29","ECOM_200","Clothes","IND","Mobile","iOS","3447.309","1559.7594",
                    "12194.1727","15875.2994"};
            writer.writeNext(addData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
