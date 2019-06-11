package com.example.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableTask
@EnableBatchProcessing
public class ErrorApplication implements JobParametersIncrementer {

	private static final Log logger = LogFactory.getLog(ErrorApplication.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	public static void main(String[] args) {
		SpringApplication.run(ErrorApplication.class, args);
	}

	public JobParameters getNext(JobParameters parameters) {
		if (parameters == null || parameters.isEmpty()) {
			logger.info("First time launching!");
			return new JobParametersBuilder().addLong("run.id", 1L).toJobParameters();
		}
		long id = parameters.getLong("run.id", 1L) + 1;
		logger.info("Incremented by 1; value = " + id);
		return new JobParametersBuilder().addLong("run.id", id).toJobParameters();
	}

	@Bean
	public Job job1() {
		return jobBuilderFactory.get("job1")
				.incrementer(new ErrorApplication())
				.start(stepBuilderFactory.get("job1step1")
						.tasklet(new Tasklet() {
							@Override
							public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
									throws Exception {
								logger.info("Job1 was run");
								return RepeatStatus.FINISHED;
							}
						})
						.build())
				.build();
	}

	@Bean
	public Job job2() {
		return jobBuilderFactory.get("job2")
				.incrementer(new ErrorApplication())
				.start(stepBuilderFactory.get("job2step1")
						.tasklet(new Tasklet() {
							@Override
							public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
									throws Exception {
								logger.info("Job2 was run");
								throw new IllegalStateException("NO LUCK!");
							}
						})
						.build())
				.build();
	}
}
