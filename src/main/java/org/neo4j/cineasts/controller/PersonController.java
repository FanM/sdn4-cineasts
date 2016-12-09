package org.neo4j.cineasts.controller;

import org.neo4j.cineasts.domain.Actor;
import org.neo4j.cineasts.domain.Director;
import org.neo4j.ogm.cypher.Filter;
import org.springframework.data.neo4j.util.IterableUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PersonController extends BaseController {

    @RequestMapping(value = "/actors/{id}", method = RequestMethod.GET, headers = "Accept=text/html")
    protected String singleActorView(Model model, @PathVariable String id) {
        Actor actor = IterableUtils.getFirstOrNull(findActorByProperty("id", id));
        model.addAttribute("actor", actor);
        model.addAttribute("id", id);
        model.addAttribute("roles", actor.getRoles());
        addUser(model);
        return "/actors/show";
    }

    @RequestMapping(value = "/directors/{id}", method = RequestMethod.GET, headers = "Accept=text/html")
    protected String singleDirectorView(Model model, @PathVariable String id) {
        Director director = IterableUtils.getFirstOrNull(findDirectorByProperty("id", id));
        model.addAttribute("director", director);
        model.addAttribute("id", id);
        model.addAttribute("movies", director.getDirectedMovies());
        addUser(model);
        return "/directors/show";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        addUser(model);
        return "index";
    }

    Iterable<Actor> findActorByProperty(String propertyName, Object propertyValue) {
        return session.loadAll(Actor.class, new Filter(propertyName, propertyValue));
    }

    Iterable<Director> findDirectorByProperty(String propertyName, Object propertyValue) {
        return session.loadAll(Director.class, new Filter(propertyName, propertyValue));
    }
}
