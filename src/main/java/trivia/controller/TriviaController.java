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
	 * The home page.
	 * 
	 * @return
	 */
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping("/")
	public String trivia(Principal p) {
		logger.debug(p.getName() + " has hit the home page.");
		
		messaging.convertAndSend("/topic/joined", p.getName());
		
		return "home";
	}

    @Secured("ROLE_USER")
    @RequestMapping(value="/guess/{questionId}/{answer}",method=RequestMethod.POST)
    public String guess(Principal p, @PathVariable Long questionId, @PathVariable String answer) {
        logger.debug(p.getName() + " has guessed " + answer + " for question #" + questionId);

        User user = userRepository.findByName(p.getName());
        
        triviaService.guess(user, questionId, answer);

        return "home";
    }

}
