package com.mido.service;

import com.mido.dto.ReturnUserDto;
import com.mido.model.Role;
import com.mido.model.User;
import com.mido.dto.RegistrationRequest;
import com.mido.registration.token.VerificationTokenService;
import com.mido.repository.IUserService;
import com.mido.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest registration) {
        var user = new User(registration.getFirstName(), registration.getLastName(),
                registration.getEmail(),
                passwordEncoder.encode(registration.getPassword()),
                registration.getImage(),
                Arrays.asList(new Role("ROLE_USER")));
       return userRepository.save(user);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public ReturnUserDto findReturnUserById(Long id) {

        Optional<User> user = findById(id);

        ReturnUserDto returnUserDto = new ReturnUserDto(
                user.get().getId(),
                user.get().getFirstName()+ " " +user.get().getLastName(),
                user.get().getEmail(),
                user.get().getEmail(),
                user.get().getImage(),
                0L
        );
        return returnUserDto;
    }

    @Transactional
    @Override
    public void updateUser(Long id, String firstName, String lastName, String email) {
        userRepository.update(firstName, lastName, email, id);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<User> theUser = userRepository.findById(id);
        theUser.ifPresent(user -> verificationTokenService.deleteUserToken(user.getId()));
        userRepository.deleteById(id);
    }
}
