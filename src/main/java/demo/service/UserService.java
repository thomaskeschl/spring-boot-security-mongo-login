package demo.service;

import demo.domain.User;
import demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/u")
public class UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    User createUser(@RequestBody User user) {
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsAccountNonExpired(true);
        user.setIsEnabled(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("USER")));
        return repository.save(user);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    @ResponseBody
    User getUserByUsername(@PathVariable String username) {
        User user = repository.findByUsername(username.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
