package com.netflix;

/**
 * tconst  averageRating   numVotes
 * from title.ratings.tsv
 */
public class TitleRating {
    private String id;
    private Double averageRating;
    private int numVotes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    public TitleRating(String [] comps) {
        for (int i = 0; i < comps.length; i++) {
            switch (i) {
                case 0: id = comps[i]; break;
                case 1: averageRating = Double.parseDouble(comps[i]); break;
                case 2: numVotes = Integer.parseInt(comps[i]); break;
            }
        }
    }
}
