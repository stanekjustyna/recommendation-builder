package com.justynastanek.recommendationbuilder.configuration;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RecommenderConfig {

    private DataSource dataSource;

    @Autowired
    public RecommenderConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DataModel getDataModel() throws TasteException {
         return new ReloadFromJDBCDataModel(new MySQLJDBCDataModel(dataSource, "rating",
                "user_id", "movie_id", "rating", "rating_timestamp"));
    }

    @Bean("userSimilarity")
    public UserSimilarity getUserSimilarity(DataModel dataModel) throws TasteException{
        return new PearsonCorrelationSimilarity(dataModel);
    }

    @Bean
    public UserNeighborhood getUserNeighborhood(DataModel dataModel, UserSimilarity similarity) throws TasteException{
        return new NearestNUserNeighborhood(10, similarity, dataModel);
    }

    @Bean
    public UserBasedRecommender getUserBasedRecommender(DataModel dataModel, UserNeighborhood neighborhood,
                                                        @Qualifier("userSimilarity")UserSimilarity similarity) {
        return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
    }

    @Bean("itemSimilarity")
    public ItemSimilarity getItemSimilarity(DataModel dataModel) throws TasteException{
        return new PearsonCorrelationSimilarity(dataModel);
    }

    @Bean
    public ItemBasedRecommender getItemBasedRecommender(DataModel dataModel,
                                                        @Qualifier("itemSimilarity") ItemSimilarity similarity) {
        return new GenericItemBasedRecommender(dataModel, similarity);
    }
}
