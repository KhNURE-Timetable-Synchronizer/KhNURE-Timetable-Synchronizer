package com.khnure.timetable.synchronizer.service;

import com.khnure.timetable.synchronizer.model.CustomUserDetails;
import com.khnure.timetable.synchronizer.model.User;
import com.khnure.timetable.synchronizer.model.Role;
import com.khnure.timetable.synchronizer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmailOrCreateUser(String email, String refreshToken) {
        Optional<User> userFromDatabase = findByEmail(email);
        User user;
        if (userFromDatabase.isPresent()) {
            user = userFromDatabase.get();
            user.setGoogleRefreshToken(refreshToken);
            save(user);
            return user;
        }
        user = User.builder()
                .email(email)
                .role(Role.USER)
                .googleRefreshToken(refreshToken)
                .build();

        return save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username).map(user -> new CustomUserDetails(
                new SimpleGrantedAuthority(user.getRole().name()),
                user)
        ).orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}
