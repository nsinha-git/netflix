package com.netflix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * //const  titleType       primaryTitle    originalTitle   isAdult startYear       endYear runtimeMinutes  genres
 *
 * The fields are as they happen in title.basic.tsv
 */
public class TitleBasicInfo {
    private String id;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private String isAdult = "0";
    private int startYear = 2017;
    private int endYear = 2017;
    private Double runtimeMinutes = 0.0;
    private List<String> genres = new ArrayList<String>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(String isAdult) {
        this.isAdult = isAdult;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public Double getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Double runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }


    public TitleBasicInfo(String [] comps) {
        for (int i = 0; i < comps.length; i++) {
            switch (i) {
                case 0: id = comps[i]; break;
                case 1: titleType = comps[i]; break;
                case 2: primaryTitle = comps[i]; break;
                case 3: originalTitle = comps[i]; break;
                case 4: isAdult = comps[i]; break;
                case 5: try {
                    startYear = Integer.parseInt(comps[i]);
                } catch (Exception e){
                    startYear = 2017;
                }
               break;

                case 6: try {
                    endYear = Integer.parseInt(comps[i]);
                } catch (Exception e) {
                    endYear = 2017;
                }

                break;
                case 7: try {
                    runtimeMinutes = Double.parseDouble(comps[i]);
                } catch (Exception e) {}
                break;
                case 8: try {
                    genres = Arrays.asList(comps[i].split(","));
                } catch(Exception e){

                }
                break;
            }
        }
    }
}
