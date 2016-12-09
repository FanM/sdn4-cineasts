package org.neo4j.cineasts.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.*;

/**
 * Created by fanmao on 12/8/16.
 */
@NodeEntity
public abstract class Item {
    @GraphId
    Long nodeId;

    String id;
    String title;
    String description;

    @Relationship(type = "DIRECTED", direction = Relationship.INCOMING)
    protected Set<Director> directors = new HashSet<>();

    @Relationship(type = "ACTS_IN", direction = Relationship.INCOMING)
    protected Set<Role> roles = new HashSet<>();

    @Relationship(type = "RATED", direction = Relationship.INCOMING)
    protected Set<Rating> ratings = new HashSet<>();

    protected String language;
    protected String homepage;
    protected String trailer;
    protected String genre;
    protected String studio;
    protected String imageUrl;

    protected Item() {
    }

    protected Item(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Relationship(type = "ACTS_IN", direction = Relationship.INCOMING)
    public Collection<Role> getRoles() {
        return roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Relationship(type = "RATED", direction = Relationship.INCOMING)
    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Relationship(type = "DIRECTED", direction = Relationship.INCOMING)
    public Set<Director> getDirectors() {
        return directors;
    }

    public void addDirector(Director director) {
        directors.add(director);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
