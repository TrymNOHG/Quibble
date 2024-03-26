package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.config.UserConfig;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(UserConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
