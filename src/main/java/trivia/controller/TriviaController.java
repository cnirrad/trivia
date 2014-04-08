package trivia.controller;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import trivia.model.User;
import trivia.repository.UserRepository;
import trivia.service.TriviaService;

/**
 * Controller to handle user interactions for the trivia game.
 */
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
    public String trivia(Principal p) {
        logger.debug(p.getName() + " has hit the home page.");

        messaging.convertAndSend("/topic/joined", p.getName());

        return "home";
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
    @RequestMapping(value = "/guess/{questionId}/{answer}", method = RequestMethod.POST)
    public @ResponseBody
    String guess(Principal p, @PathVariable Long questionId, @PathVariable String answer) {
        logger.debug(p.getName() + " has guessed " + answer + " for question #" + questionId);

        User user = userRepository.findByName(p.getName());

        triviaService.guess(user, questionId, answer);

        return "OK";
    }

}
