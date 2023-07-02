package com.mido.controller;
import com.mido.event.listener.RegistrationCompleteEventListener;
import com.mido.dto.RegistrationRequest;
import com.mido.registration.password.IPasswordResetTokenService;
import com.mido.registration.token.VerificationToken;
import com.mido.registration.token.VerificationTokenService;
import com.mido.repository.FilesStorageService;
import com.mido.repository.IUserService;
import com.mido.model.User;
import com.mido.utility.StorageUtl;
import com.mido.utility.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;
    private final IPasswordResetTokenService passwordResetTokenService;
    private final RegistrationCompleteEventListener eventListener;
    private final FilesStorageService filesStorageService;





    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("title", "Signup");
        model.addAttribute("user", new RegistrationRequest());
        return "auth/registration";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registration, @RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) throws IOException {


        String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        registration.setImage(fileName);
        User user = userService.registerUser(registration);

        String uploadDirectory = "src/main/resources/static/photos/";
        StorageUtl.saveFile(uploadDirectory, fileName, imageFile);


        // publisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
        return "redirect:/registration/registration-form?success";
    }
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm(){
        return "auth/forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        Optional<User> user= userService.findByEmail(email);
        if (user.isEmpty()){
            return  "redirect:/registration/forgot-password-request?not_fond";
        }
        String passwordResetToken = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
        //send password reset verification email to the user
        String url = UrlUtil.getApplicationUrl(request)+"/registration/password-reset-form?token="+passwordResetToken;
        try {
            eventListener.sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/registration/forgot-password-request?success";
    }
  @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        return "auth/password-reset-form";
    }
    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")){
            return "redirect:/error?invalid_token";
        }
        Optional<User> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()){
            passwordResetTokenService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";
    }
}
