package trivia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import trivia.model.GameEntity;
import trivia.model.User;
import trivia.repository.GameRepository;
import trivia.repository.UserRepository;
import trivia.service.TriviaService;

@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public ModelAndView dashboard() {
        ModelAndView mav = new ModelAndView("dashboard");

        GameEntity game = gameRepository.findOne(1L);
        Iterable<User> users = userRepository.findAll();

        mav.addObject("game", game);
        mav.addObject("users", users);

        return mav;
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public @ResponseBody
    GameEntity getGame() {
        return triviaService.getGame().getEntity();
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public @ResponseBody
    GameEntity startGame() {
        return triviaService.startGame().getEntity();
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public @ResponseBody
    GameEntity resetGame() {
        return triviaService.reset();
    }

}
