package org.neo4j.cineasts.domain;

import java.util.*;

public class TV extends Item {

    private Date firstAirDate;
    private List<Integer> episodeRuntime;
    private String status;

    public TV() {
    }

    public TV(String id, String title) {
        super(id, title);
    }

    public int getYear() {
        if (firstAirDate == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(firstAirDate);
        return cal.get(Calendar.YEAR);
    }

    public int getStars() {
        Iterable<Rating> allRatings = ratings;

        if (allRatings == null) {
            return 0;
        }
        int stars = 0, count = 0;
        for (Rating rating : allRatings) {
            stars += rating.getStars();
            count++;
        }
        return count == 0 ? 0 : stars / count;
    }

    public Date getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(Date firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<Integer> getEpisodeRuntime() {
        return episodeRuntime;
    }

    public void setEpisodeRuntime(List<Integer> episodeRuntime) {
        this.episodeRuntime = episodeRuntime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYoutubeId() {
        String trailerUrl = trailer;
        if (trailerUrl == null || !trailerUrl.contains("youtu")) {
            return null;
        }
        String[] parts = trailerUrl.split("[=/]");
        int numberOfParts = parts.length;
        return numberOfParts > 0 ? parts[numberOfParts - 1] : null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", title, getYear(), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TV)) {
            return false;
        }

        TV tv = (TV) o;

        if (id != null ? !id.equals(tv.id) : tv.id != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
