package com.netflix;

/***
 * tconst  parentTconst    seasonNumber    episodeNumber
 * from title.episode.tsv
 *
 */
public class TitleEpisode {
    private String id;
    private String parentId;
    private int seasonNumber = 0;
    private String episodeNumber = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public TitleEpisode(String [] comps) {
        for (int i = 0; i < comps.length; i++) {
            switch (i) {
                case 0: id = comps[i]; break;
                case 1: parentId = comps[i]; break;
                case 2: try {
                    seasonNumber = Integer.parseInt(comps[i]);
                } catch(Exception e) {
                    seasonNumber = 0;
                }
                break;
                case 3: episodeNumber = comps[i]; break;
            }
        }
    }


}
