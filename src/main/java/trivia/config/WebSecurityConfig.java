package trivia.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import trivia.model.User;
import trivia.repository.UserRepository;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserRepository userRepository;

	private class CustomAuthProvider implements AuthenticationProvider {
		private final Logger logger = LoggerFactory.getLogger(CustomAuthProvider.class);
		
		@Override
		public Authentication authenticate(Authentication authentication)
				throws AuthenticationException {
			
			User user = userRepository.findByName(authentication.getName());
			logger.debug("Username: " + authentication.getName() + ", Token: " + authentication.getCredentials().toString());
			if (user == null) {
				// add the user
				user = new User();
				user.setName(authentication.getName());
				user.setToken(authentication.getCredentials().toString());
				user = userRepository.save(user);
			} else {
				// validate token
				if (!user.getToken().equals(authentication.getCredentials().toString())) {
					logger.debug(authentication.getName() + " tried to login, but the token didn't match.");
					return null;
				} else {
					logger.debug(authentication.getName() + " has logged back in.");
				}
			}
			List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getToken(), grantedAuths);
			
            return auth;
		}

		@Override
		public boolean supports(Class<?> authentication) {
			return authentication.equals(UsernamePasswordAuthenticationToken.class);
		}
		
	}
	
	@Bean
	protected AuthenticationProvider authenticationProvider() {
		return new CustomAuthProvider();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/webjars/**", "/js/**").permitAll()
			.anyRequest().authenticated();
		
		http
        .authorizeRequests()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .permitAll();
		
	}
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        auth.authenticationProvider(authenticationProvider());
    }
}
