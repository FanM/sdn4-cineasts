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
package org.neo4j.cineasts.controller;

import java.util.Collections;

import org.neo4j.cineasts.domain.*;
import org.neo4j.cineasts.repository.MovieRepository;
import org.neo4j.ogm.cypher.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.util.IterableUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mh
 * @since 04.03.11
 */
@Controller
public class MovieController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieRepository movieRepository;

    @RequestMapping(value = "/movies/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    Movie getMovie(@PathVariable String id) {
        return IterableUtils.getFirstOrNull(findMovieByProperty("id", id));
    }


    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.GET, headers = "Accept=text/html")
    public String singleMovieView(final Model model, @PathVariable String movieId) {
        User user = addUser(model);
        Movie movie = IterableUtils.getFirstOrNull(findMovieByProperty("id", movieId));
        model.addAttribute("id", movieId);
        if (movie != null) {
            model.addAttribute("movie", movie);
            final int stars = movie.getStars();
            model.addAttribute("stars", stars);
            Rating rating = null;
            if (user != null) {
                for (Rating r : user.getRatings()) {
                    if (r.getItem().equals(movie)) {
                        rating = r;
                        break;
                    }
                }
            }
            if (rating == null) {
                rating = new Rating();
                rating.setItem(movie);
                rating.setUser(user);
                rating.setStars(stars);
            }
            model.addAttribute("userRating", rating);
        }
        return "/movies/show";
    }

    @RequestMapping(value = "/movies/{movieId}", method = RequestMethod.POST, headers = "Accept=text/html")
    public String updateMovie(Model model, @PathVariable String movieId, @RequestParam(value = "rated", required = false) Integer stars, @RequestParam(value = "comment", required = false) String comment) {
        Movie movie = IterableUtils.getFirstOrNull(findMovieByProperty("id", movieId));
        User user = userRepository.getUserFromSession();
        if (user != null && movie != null) {
            int stars1 = stars == null ? -1 : stars;
            String comment1 = comment != null ? comment.trim() : null;
            user.rate(movie, stars1, comment1);
            userRepository.save(user);
        }
        return singleMovieView(model, movieId);
    }



    @RequestMapping(value = "/movies", method = RequestMethod.GET, headers = "Accept=text/html")
    public String findMovies(Model model, @RequestParam("q") String query) {
        if (query != null && !query.isEmpty()) {
            //Page<Movie> movies = movieRepository.findByTitleLike(query, new PageRequest(0, 20));
            Iterable<Movie> movies = movieRepository.findByTitleLike("(?i).*" + query + ".*");
            model.addAttribute("movies", movies);
        } else {
            model.addAttribute("movies", Collections.emptyList());
        }
        model.addAttribute("query", query);
        addUser(model);
        return "/movies/list";
    }

    public Iterable<Movie> findMovieByProperty(String propertyName, Object propertyValue) {
        return session.loadAll(Movie.class, new Filter(propertyName, propertyValue));
    }
}
