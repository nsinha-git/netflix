package com.netflix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiImpl implements Api {
    Logger logger = LoggerFactory.getLogger(ApiImpl.class);
    InMemDataStore readFilesAndInMemDataStore;
    private final String TV_SERIES = "tvSeries";

    public ApiImpl() throws Exception {
        readFilesAndInMemDataStore = new InMemDataStore();
    }

    /**
     * return -1 when in error
     * @param id
     * @return
     */
    public Double getRating(String id) {
        try {
            return readFilesAndInMemDataStore.getFromMapTitleIdToRatings(id).getAverageRating();
        } catch(Exception e) {
            logger.error("id {} not found in rating map e:{}", id, e);
            return -1.0;
        }
    }

    /**
     * The rating for a season of tv series is simply the average of ratings of all episodes of that series in the season.
     * In case of any error we return the rating of the series as recorded by IMDB data.
     * @param id
     * @param season
     * @return
     */

    public Double getRatingBySeason(String id, int season) {
        return readFilesAndInMemDataStore.getSeasonRating(id, season);
    }

    public void updateRatingTitleId(String id, double rating) {
        readFilesAndInMemDataStore.updateRatingEpisodeId(id, rating);
    }
}
