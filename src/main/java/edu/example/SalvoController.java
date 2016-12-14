package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


/**
 * Created by louis on 12/14/2016.
 */
@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository games;

    @RequestMapping("/games")
    public List<Object> getAll() {
        //findAll() returns the list of games instances
        return
                games.findAll().stream()
                        .map(game -> game.getId())
                        .collect(toList());
    }
}
