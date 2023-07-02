package com.mido.registration.token;

import com.mido.model.User;

import java.util.Optional;

/**
 * @author Sampson Alfred
 */

public interface IVerificationTokenService {
    String validateToken(String token);
    void saveVerificationTokenForUser(User user, String token);
    Optional<VerificationToken> findByToken(String token);


    void deleteUserToken(Long id);
}
