package com.netflix;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestApi {
    Api api;
    @Before
    public void createApi() throws Exception {
        api = new ApiImpl();
    }

    @Test
    public void test_movie_rating() {
        Assert.assertEquals(api.getRating("tt0806877"), (Double)8.3);
    }


    /**
     * tests the season average based on episodes algorithm for a movie:
     * tt7450814    tvSeries        Koombiyo        Koombiyo        0       2017    2018    20      Crime,Drama,Thriller
     */
    @Test
    public void test_tv_series_rating() {
        Assert.assertEquals(api.getRatingBySeason("tt7450814", 1), (Double)9.32280701754386);
        Assert.assertEquals(api.getRatingBySeason("tt7450814", 2), (Double)9.9);
        Assert.assertEquals(api.getRatingBySeason("tt7450814", 0), (Double)9.9);
        Assert.assertEquals(api.getRating("tt7450814"), (Double)9.9);
        api.updateRatingTitleId("tt7481646", 0.0);
        Assert.assertNotEquals(api.getRatingBySeason("tt7450814", 1), (Double)9.32280701754386);
    }
}

