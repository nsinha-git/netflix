package com.netflix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemDataStore {
    Logger logger = LoggerFactory.getLogger(InMemDataStore.class);
    private final String TV_SERIES = "tvSeries";
    /**
     * mapTitleIdToTitleBasicInfo is baisc map of id to title.basic,
     * setTitleIds is set of setTitleIds that is in 2017.
     */
    private Map<String, TitleBasicInfo> mapTitleIdToTitleBasicInfo = new ConcurrentHashMap<String, TitleBasicInfo>();
    private Set<String> setTitleIds = new HashSet<String>();
    private Map<String, String> mapTitlesNamesToId = new  ConcurrentHashMap<String, String>();

    /**
     * mapIdToEpisode is map of episode id to TitleEpisode
     * mapTitleToEpisodes is map of a titleID to all it's episode ids. A materialized join operation
     */
    private Map<String, TitleEpisode> mapIdToEpisode = new ConcurrentHashMap<String, TitleEpisode>();
    private Map<String, List<String>> mapTitleToEpisodes = new ConcurrentHashMap<String, List<String>>();

    /**
     * mapTitleIdToRatings is map of id of title to its ratings
     */
    private Map<String, TitleRating> mapTitleIdToRatings = new ConcurrentHashMap<String, TitleRating>();


    /**
     * contains pre calculated  season scores for tvSeries
     */
    private Map<String,Map<Integer, Double>> mapTitleToSeasonToRating = new HashMap<String, Map<Integer, Double>>();


    public InMemDataStore() throws Exception {
        readTitleBasic();
        readTitleEpisode();
        readTitleRating();
        updateRatingBySeason();
    }


    public void readTitleBasic() throws Exception {
        String file = this.getClass().getClassLoader().getResource("title_2017.tsv").getFile();
        TsvReader tsvReader = new TsvReader(file);
        String[] components;

        while ((components = tsvReader.getNextLine()) != null) {
            TitleBasicInfo titleBasicInfo = new TitleBasicInfo(components);
            mapTitleIdToTitleBasicInfo.put(titleBasicInfo.getId(), titleBasicInfo);
            setTitleIds.add(titleBasicInfo.getId());
            mapTitlesNamesToId.put(titleBasicInfo.getOriginalTitle(), titleBasicInfo.getId());
        }
    }


    public void readTitleEpisode() throws Exception {
        String file = this.getClass().getClassLoader().getResource("title.episode.tsv").getFile();
        TsvReader tsvReader = new TsvReader(file);
        String[] components;

        while ((components = tsvReader.getNextLine()) != null) {
            TitleEpisode titleEpisode = new TitleEpisode(components);
            if (setTitleIds.contains(titleEpisode.getParentId())) {
                mapIdToEpisode.put(titleEpisode.getId(), titleEpisode);
                if (mapTitleToEpisodes.containsKey(titleEpisode.getParentId())) {
                    mapTitleToEpisodes.get(titleEpisode.getParentId()).add(titleEpisode.getId());
                } else {
                    mapTitleToEpisodes.put(titleEpisode.getParentId(), new ArrayList<String>());
                    mapTitleToEpisodes.get(titleEpisode.getParentId()).add(titleEpisode.getId());
                }
            }
        }
    }


    public void readTitleRating() throws Exception {
        String file = this.getClass().getClassLoader().getResource("title.ratings.tsv").getFile();
        TsvReader tsvReader = new TsvReader(file);
        String[] components;

        while ((components = tsvReader.getNextLine()) != null) {
            TitleRating titleRating = new TitleRating(components);
            mapTitleIdToRatings.put(titleRating.getId(), titleRating);
        }
    }


    public TitleRating getFromMapTitleIdToRatings(String id) {
        return mapTitleIdToRatings.get(id);
    }

    public TitleBasicInfo getFromMapTitleIdToTitleBasicInfo (String id) {
        return mapTitleIdToTitleBasicInfo.get(id);
    }

    public List<String> getFromMapTitleToEpisodes(String id) {
        return mapTitleToEpisodes.get(id);
    }

    public TitleEpisode getFromMapIdToEpisode (String id) {
        return    mapIdToEpisode.get(id);
    }

    public void updateMapTitleIdToRatings(String  id, Double rating) {
        try {
            mapTitleIdToRatings.get(id).setAverageRating(rating);
        } catch (Exception e) {
            logger.error("Exception e {}", e);
        }
    }


    public void updateRatingEpisodeId(String id, double rating) {
        TitleEpisode titleEpisode = mapIdToEpisode.get(id);
        String tvSeriesId = titleEpisode.getParentId();
        //update the rating of episode
        updateMapTitleIdToRatings(id, rating);
        //recalculate the tvSeries id season again.
        updateRatingBySeasonForId(tvSeriesId);
    }

    /**
     * this gets called during initialization to update tv series seasons with ratings
     */
    public void updateRatingBySeason() {
        for (String id : setTitleIds) {
            updateRatingBySeasonForId(id);
        }
    }

    /**
     * if title is tvseries it bunches it's episode as per seasons using map of map.
     *
     *
     * @param id of title
     */
    public void updateRatingBySeasonForId(String id) {
        TitleBasicInfo titleBasicInfo = getFromMapTitleIdToTitleBasicInfo(id);
        //only if tv series
        if (titleBasicInfo.getTitleType().equals(TV_SERIES)) {
            List<String> listOfEpisodes = getFromMapTitleToEpisodes(id);
            //no episodes ... exit
            if (listOfEpisodes == null) {
                return;
            }
            //this map keeps sum of all episodes belonging to same season for the title
            Map<Integer,Double> sumRatingsEpisodesMap = new HashMap<Integer, Double>();
            //this map keeps number of  episodes belonging to same season for the title
            Map<Integer, Integer> totalEpisodesForSeasonMap = new HashMap<Integer, Integer>();
            for (String episodeId: listOfEpisodes) {
                TitleEpisode titleEpisode = getFromMapIdToEpisode(episodeId);
                int curSeason = titleEpisode.getSeasonNumber();
                //create keys in map
                if (!sumRatingsEpisodesMap.containsKey(curSeason)) {
                    sumRatingsEpisodesMap.put(curSeason, 0.0);
                    totalEpisodesForSeasonMap.put(curSeason, 0);
                }
                //update keys
                totalEpisodesForSeasonMap.put(curSeason, totalEpisodesForSeasonMap.get(curSeason) + 1);
                double episodeRating  = 0.0;
                if (mapTitleIdToRatings.containsKey(episodeId)) {
                    episodeRating = getFromMapTitleIdToRatings(episodeId).getAverageRating();
                } else {
                    if (mapTitleIdToRatings.containsKey(id)) {
                        episodeRating = mapTitleIdToRatings.get(id).getAverageRating();
                    }
                }
                sumRatingsEpisodesMap.put(curSeason, sumRatingsEpisodesMap.get(curSeason) + episodeRating);
            }

            /**
             * we do season wise calculation of rating by going over each season and doing division of total ratings by
             * number of episodes.
             * in case of failure the default title rating is put .
             */

            for (int season: totalEpisodesForSeasonMap.keySet()) {
                int totalEpisodesForSeason = totalEpisodesForSeasonMap.get(season);
                double sumRatingsEpisodes = sumRatingsEpisodesMap.get(season);
                double res = 0.0;
                if (totalEpisodesForSeason > 0) {
                    res = sumRatingsEpisodes/totalEpisodesForSeason;
                } else {
                    res = getFromMapTitleIdToRatings(id).getAverageRating();
                }

                if (!mapTitleToSeasonToRating.containsKey(id)) {
                    mapTitleToSeasonToRating.put(id, new HashMap<Integer, Double>());
                }
                if (res != 0.0) {
                    mapTitleToSeasonToRating.get(id).put(season, res);
                } else if (mapTitleIdToRatings.containsKey(id)){
                    //put the default rating of the title onto season value.
                    mapTitleToSeasonToRating.get(id).put(season, mapTitleIdToRatings.get(id).getAverageRating());
                }
            }
        }
    }

    /**
     * try returning season rating. If the season does not exist return the overall tv series rating from imdb/
     * @param id of tv series/video
     * @param season season number
     * @return: rating
     */
    double getSeasonRating(String id, int season) {
        if (mapTitleToSeasonToRating.containsKey(id)) {
            Map<Integer, Double>  seasonMap = mapTitleToSeasonToRating.get(id);
            if (seasonMap.containsKey(season)) {
                return seasonMap.get(season);
            } else {
                return getFromMapTitleIdToRatings(id).getAverageRating();
            }
        } else {
            return getFromMapTitleIdToRatings(id).getAverageRating();
        }
    }
}





