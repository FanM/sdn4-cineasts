/*
 * Copyright [2011-2016] "Neo Technology"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */
package org.neo4j.cineasts.movieimport;

import java.text.SimpleDateFormat;
import java.util.*;

import org.neo4j.cineasts.domain.Movie;
import org.neo4j.cineasts.domain.Person;
import org.neo4j.cineasts.domain.Roles;
import org.neo4j.cineasts.domain.TV;
import org.springframework.stereotype.Component;

@Component
public class MovieDbJsonMapper {

    public static void mapToMovie(Map data, Movie movie, String baseImageUrl) {
        try {
            movie.setTitle((String) data.get("title"));
            movie.setLanguage((String) data.get("original_language"));
            movie.setImdbId((String) data.get("imdb_id"));
            movie.setTagline((String) data.get("tagline"));
            movie.setDescription(limit((String) data.get("overview"), 500));
            movie.setReleaseDate(toDate(data, "release_date", "yyyy-MM-dd"));
            movie.setRuntime((Integer) data.get("runtime"));
            movie.setHomepage((String) data.get("homepage"));
            //movie.setTrailer((String) data.get("trailer")); //TODO missing
            movie.setGenre(extractFirst(data, "genres", "name"));
            movie.setStudio(extractFirst(data, "production_companies", "name"));
            movie.setImageUrl(baseImageUrl + data.get("poster_path"));
        } catch (Exception e) {
            throw new MovieDbException("Failed to map json for movie", e);
        }
    }

    public static void mapToTV(Map data, TV tv, String baseImageUrl) {
        try {
            tv.setTitle((String) data.get("name"));
            tv.setLanguage((String) data.get("original_language"));
            tv.setFirstAirDate(toDate(data, "first_air_date", "yyyy-MM-dd"));
            tv.setStatus((String) data.get("status"));
            tv.setDescription(limit((String) data.get("overview"), 500));
            tv.setEpisodeRuntime(((List<Integer>)data.get("episode_run_time")));
            tv.setHomepage((String) data.get("homepage"));
            tv.setGenre(extractFirst(data, "genres", "name"));
            tv.setStudio(extractFirst(data, "production_companies", "name"));
            tv.setImageUrl(baseImageUrl + data.get("poster_path"));
        } catch (Exception e) {
            throw new MovieDbException("Failed to map json for TV", e);
        }
    }

    private static String extractFirst(Map data, String field, String property) {
        List<Map> inner = (List<Map>) data.get(field);
        if (inner == null || inner.isEmpty()) {
            return null;
        }
        return (String) inner.get(0).get(property);
    }

    private static List<String> extractFirstNImages(Map data, String field, String property, int n, String baseImageUrl) {
        List<Map> inner = (List<Map>) data.get(field);
        if (inner == null || inner.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < n && i < inner.size(); i++) {
            result.add(baseImageUrl + inner.get(i).get(property));
        }
        return result;
    }


    private static Date toDate(Map data, String field, final String pattern) {
        try {
            String dateString = (String) data.get(field);
            if (dateString == null || dateString.isEmpty()) {
                return null;
            }
            return new SimpleDateFormat(pattern).parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }


    public static void mapToPerson(Map data, Person person, String baseImageUrl) {
        try {
            person.setName((String) data.get("name"));
            person.setBirthday(toDate(data, "birthday", "yyyy-MM-dd"));
            person.setBirthplace((String) data.get("place_of_birth"));
            String biography = (String) data.get("biography");
            person.setBiography(limit(biography, 500));
            person.setVersion((Integer) data.get("version"));
            if(data.get("profile_path")!=null) {
                person.setProfileImageUrl(baseImageUrl + data.get("profile_path"));
            }
            Map images = (Map) data.get("images");
            if(images != null) {
                person.setImageUrls(extractFirstNImages(images, "profiles", "file_path", 3, baseImageUrl));
            }
        } catch (Exception e) {
            throw new MovieDbException("Failed to map json for person", e);
        }
    }

    private static String limit(String text, int limit) {
        if (text == null || text.length() < limit) {
            return text;
        }
        return text.substring(0, limit);
    }


    public static Roles mapToRole(String roleString) {
        if (roleString.equals("Actor")) {
            return Roles.ACTS_IN;
        }
        if (roleString.equals("Director")) {
            return Roles.DIRECTED;
        }
        return null;
    }
}
