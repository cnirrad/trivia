package trivia.controller;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import trivia.messages.GuessRequestMessage;
import trivia.messages.GuessResponseMessage;
import trivia.model.User;
import trivia.repository.UserRepository;
import trivia.service.TriviaService;

@Controller
public class TriviaController {

    private static final Logger logger = LoggerFactory.getLogger(TriviaController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private SimpMessagingTemplate messaging;

    /**
     * The login page.
     * 
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * The trivia page.
     * 
     * @param p
     *            the current user.
     * @return the page to display
     */
    @Secured("ROLE_USER")
    @RequestMapping("/")
    public ModelAndView trivia(Principal p) {
        logger.debug(p.getName() + " has hit the home page.");

        User u = userRepository.findByName(p.getName());

        messaging.convertAndSend("/topic/joined", p.getName());

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", u);
        mav.addObject("game", triviaService.getGame());

        return mav;
    }

    /**
     * Log a guess.
     * 
     * @param p
     *            the principal of the user making the guess.
     * @param questionId
     *            the ID of the question they are guessing on.
     * @param answer
     *            the guess they have made (should be "A", "B", "C", or "D").
     * @return the status
     */
    @Secured("ROLE_USER")
    @RequestMapping(value = "/guess", method = RequestMethod.POST)
    public @ResponseBody
    GuessResponseMessage guess(Principal p, @RequestBody GuessRequestMessage msg) {
        logger.debug(p.getName() + " has guessed " + msg.getGuess() + " for question #" + msg.getQuestionNumber());

        User user = userRepository.findByName(p.getName());

        GuessResponseMessage response = triviaService.guess(user, msg.getQuestionNumber(), msg.getGuess());

        return response;
    }

}
