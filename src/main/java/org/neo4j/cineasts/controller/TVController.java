package org.neo4j.cineasts.controller;

import org.neo4j.cineasts.domain.Rating;
import org.neo4j.cineasts.domain.TV;
import org.neo4j.cineasts.domain.User;
import org.neo4j.cineasts.repository.TVRepository;
import org.neo4j.ogm.cypher.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.util.IterableUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class TVController extends BaseController {

    @Autowired
    private TVRepository tvRepository;

    @RequestMapping(value = "/tvs/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public
    @ResponseBody
    TV getTV(@PathVariable String id) {
        return IterableUtils.getFirstOrNull(findTVByProperty("id", id));
    }


    @RequestMapping(value = "/tvs/{tvId}", method = RequestMethod.GET, headers = "Accept=text/html")
    public String singleTVView(final Model model, @PathVariable String tvId) {
        User user = addUser(model);
        TV tv = IterableUtils.getFirstOrNull(findTVByProperty("id", tvId));
        model.addAttribute("id", tvId);
        if (tv != null) {
            model.addAttribute("tv", tv);
            final int stars = tv.getStars();
            model.addAttribute("stars", stars);
            Rating rating = null;
            if (user != null) {
                for (Rating r : user.getRatings()) {
                    if (r.getItem().equals(tv)) {
                        rating = r;
                        break;
                    }
                }
            }
            if (rating == null) {
                rating = new Rating();
                rating.setItem(tv);
                rating.setUser(user);
                rating.setStars(stars);
            }
            model.addAttribute("userRating", rating);
        }
        return "/tvs/show";
    }

    @RequestMapping(value = "/tvs", method = RequestMethod.GET, headers = "Accept=text/html")
    public String findTVs(Model model, @RequestParam("q") String query) {
        if (query != null && !query.isEmpty()) {
            Iterable<TV> tvs = tvRepository.findByTitleLike("(?i).*" + query + ".*");
            model.addAttribute("tvs", tvs);
        } else {
            model.addAttribute("tvs", Collections.emptyList());
        }
        model.addAttribute("query", query);
        addUser(model);
        return "/tvs/list";
    }

    public Iterable<TV> findTVByProperty(String propertyName, Object propertyValue) {
        return session.loadAll(TV.class, new Filter(propertyName, propertyValue));
    }

}
