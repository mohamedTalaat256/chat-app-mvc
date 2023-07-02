package com.mido.utility;

import com.mido.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthData {

    public static Long getCurrentUserId(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ((AppUserDetails) authentication.getPrincipal()).getId();
    }
}
