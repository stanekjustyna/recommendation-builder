package com.justynastanek.recommendationbuilder.batch;

import com.justynastanek.recommendationbuilder.batch.mapper.MovieFieldSetMapper;
import com.justynastanek.recommendationbuilder.batch.mapper.RatingFieldSetMapper;
import com.justynastanek.recommendationbuilder.batch.mapper.UserFieldSetMapper;
import com.justynastanek.recommendationbuilder.batch.writer.MovieWriter;
import com.justynastanek.recommendationbuilder.batch.writer.RatingWriter;
import com.justynastanek.recommendationbuilder.batch.writer.UserWriter;
import com.justynastanek.recommendationbuilder.model.Item;
import com.justynastanek.recommendationbuilder.model.Rating;
import com.justynastanek.recommendationbuilder.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private MovieWriter movieWriter;

    private UserWriter userWriter;

    private RatingWriter ratingWriter;

    private RatingFieldSetMapper ratingFieldSetMapper;

    private MovieFieldSetMapper movieFieldSetMapper;

    private UserFieldSetMapper userFieldSetMapper;

    @Autowired
    public SpringBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                             MovieWriter movieWriter, UserWriter userWriter, RatingWriter ratingWriter,
                             RatingFieldSetMapper ratingFieldSetMapper, MovieFieldSetMapper movieFieldSetMapper,
                             UserFieldSetMapper userFieldSetMapper) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.movieWriter = movieWriter;
        this.userWriter = userWriter;
        this.ratingWriter = ratingWriter;
        this.ratingFieldSetMapper = ratingFieldSetMapper;
        this.movieFieldSetMapper = movieFieldSetMapper;
        this.userFieldSetMapper = userFieldSetMapper;
    }

    @Bean
    public Job job() throws MalformedURLException {
        return jobBuilderFactory.get("ETL-movies")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .next(step2())
                .next(step3())
                .end()
                .build();
    }

    @Bean
    public Step step1() throws MalformedURLException {
        return stepBuilderFactory.get("ETL-items")
                .<Item, Item>chunk(10000)
                .reader(itemReader())
                .writer(movieWriter)
                .build();
    }

    @Bean
    public Step step2() throws MalformedURLException {
        return stepBuilderFactory.get("ETL-users")
                .<User, User>chunk(10000)
                .reader(userReader())
                .writer(userWriter)
                .build();
    }


    @Bean
    public Step step3() throws MalformedURLException {
        return stepBuilderFactory.get("ETL-ratings")
                .<Rating, Rating>chunk(10000)
                .reader(ratingReader())
                .writer(ratingWriter)
                .build();
    }


    @Bean
    public FlatFileItemReader<Item> itemReader() throws MalformedURLException {
        FlatFileItemReader<Item> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(
                new UrlResource("https://raw.githubusercontent.com/sidooms/MovieTweetings/master/latest/movies.dat"));
        flatFileItemReader.setName("item-reader");
        flatFileItemReader.setLinesToSkip(0);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemReader<User> userReader() throws MalformedURLException {
        FlatFileItemReader<User> flatFileUserReader = new FlatFileItemReader<>();
        flatFileUserReader.setResource(
                new UrlResource("https://raw.githubusercontent.com/sidooms/MovieTweetings/master/latest/users.dat"));
        flatFileUserReader.setName("user-reader");
        flatFileUserReader.setLinesToSkip(0);
        flatFileUserReader.setLineMapper(lineUserMapper());
        return flatFileUserReader;
    }

    @Bean
    public FlatFileItemReader<Rating> ratingReader() throws MalformedURLException {
        FlatFileItemReader<Rating> flatFileRatingReader = new FlatFileItemReader<>();
        flatFileRatingReader.setResource(
                new UrlResource("https://raw.githubusercontent.com/sidooms/MovieTweetings/master/latest/ratings.dat"));
        flatFileRatingReader.setName("rating-reader");
        flatFileRatingReader.setLinesToSkip(0);
        flatFileRatingReader.setLineMapper(lineRatingMapper());
        return flatFileRatingReader;
    }

    @Bean
    public LineMapper<Item> lineMapper() {
        DefaultLineMapper<Item> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("::");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"movieId", "title", "genre"});

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(movieFieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public LineMapper<User> lineUserMapper() {
        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("::");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"userId", "twitterId"});

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(userFieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public LineMapper<Rating> lineRatingMapper() {
        DefaultLineMapper<Rating> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("::");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"userId", "movieId", "rating", "ratingTimestamp"});

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(ratingFieldSetMapper);

        return defaultLineMapper;
    }
}
