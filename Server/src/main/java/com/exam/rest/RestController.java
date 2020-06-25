package com.exam.rest;

import com.exam.domain.*;
import com.exam.repository.GameRepository;
import com.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/exam")
public class RestController {
    private final GameRepository gameRepo;

    @Autowired
    public RestController(GameRepository gameRepository) {
        this.gameRepo = gameRepository;
    }

    @GetMapping("/game/{id}")
    public R1DTO getGame(@PathVariable Integer id) {
        List<String> studentNames = new ArrayList<>();
        if (gameRepo.findByID(id) != null) {
            for (Round round : gameRepo.findByID(id).getRounds()) {
                for (var a : round.getWords())
                    studentNames.add(a.getStudent().getName());
                break;
            }
            return new R1DTO(gameRepo.findByID(id).getConfiguration().substring(1), studentNames);
        }
        return null;
    }

    @GetMapping("")
    public Iterable<R2DTO> getStatesAndPositions(@RequestParam("gameID") Integer gameID, @RequestParam("playerID") Integer playerID) {
        List<R2DTO> result = new ArrayList<>();
        if (gameRepo.findByID(gameID) != null)
            for (Round round : gameRepo.findByID(gameID).getRounds())
                for (var a : round.getWords())
                    if (a.getStudent().getId().equals(playerID))
                        result.add(new R2DTO(a.getConfiguration().substring(1), a.getGeneratedNumber()));
        return result;
    }

}
