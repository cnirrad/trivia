package trivia.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import trivia.model.GameEntity;
import trivia.model.Question;
import trivia.model.User;
import trivia.repository.AnswerRepository;
import trivia.repository.GameRepository;
import trivia.repository.QuestionRepository;
import trivia.repository.UserRepository;
import trivia.service.TriviaService;


@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    @Secured({"ROLE_ADMIN"})
    @RequestMapping("/")
    public ModelAndView dashboard(Principal p) {
        ModelAndView mav = new ModelAndView("dashboard");

//        GameEntity game = gameRepository.findOne(1L);
//        Iterable<User> users = userRepository.findAll();
//
//        mav.addObject("game", game);
//        mav.addObject("users", users);

        return mav;
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<User> getUsers() {
        return userRepository.findAll();
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/users/delete/{userId}", method = RequestMethod.POST)
    public @ResponseBody
    void deleteUser(@PathVariable Long userId) {
        answerRepository.deleteByUserId(userId);
        userRepository.delete(userId);
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody
    void updateUser(@RequestBody User user) {
        userRepository.save(user);
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public @ResponseBody
    void updateQuestion(@RequestBody Question question) {
        questionRepository.save(question);
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/users/scores/reset", method = RequestMethod.POST)
    public @ResponseBody
    void resetScores() {
        answerRepository.deleteAll();
        userRepository.resetAllScores();
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/users/delete/all", method = RequestMethod.POST)
    public @ResponseBody
    void deleteAllUsers() {
        answerRepository.deleteAll();
        userRepository.deleteAllExceptAdmin();
    }
    
    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public @ResponseBody
    GameEntity getGame() {
        return triviaService.getGame().getEntity();
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public @ResponseBody
    GameEntity startGame() {
        return triviaService.startGame().getEntity();
    }

    @Secured({"ROLE_ADMIN"})
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public @ResponseBody
    GameEntity resetGame() {
        return triviaService.reset();
    }

}
