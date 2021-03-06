package com.netflix;

public interface Api {
    /**
     * if id is movie return rating. if series return rating
     * @param id id of movie or tvseries or video
     * @return rating
     */
    Double getRating(String id);

    /**
     * if name is movie return rating. if series return rating for all seasons together
     * @param name name of movie or tvseries or video
     * @return rating
     */
    Double getRatingByName(String name);
    /**
     * if id is movie return rating. if its series return rating for season by accumulating on the season only
     * @param id: tv series id
     * @return : rating
     */
    Double getRatingBySeason(String id, int season);

    /**
     * if name is movie return rating. if its series return rating for season by accumulating on the season only
     * @param name: tv series name
     * @return : rating
     */
    Double getRatingBySeasonAndName(String name, int season);

    /**
     * update the Raing of an episode by id
     *
     */
    void updateRatingTitleId(String id, double rating);
}
