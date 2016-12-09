package org.neo4j.cineasts.controller;

import org.neo4j.cineasts.domain.User;
import org.neo4j.cineasts.repository.UserRepository;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

public abstract class BaseController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    Session session;

    User addUser(Model model) {
        User user = userRepository.getUserFromSession();
        model.addAttribute("user", user);
        return user;
    }
}
