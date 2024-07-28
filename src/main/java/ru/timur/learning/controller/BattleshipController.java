package ru.timur.learning.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class BattleshipController {

    private static final Logger logger =
            LoggerFactory.getLogger(BattleshipController.class);

    @GetMapping("/battleship")
    @ResponseBody
    public String printWelcome(ModelMap model) {

        logger.debug("printWelcome() is executed, value {}", BattleshipController.class);

        model.addAttribute("message", "Battleship game");
        return "battleship";
    }
}
