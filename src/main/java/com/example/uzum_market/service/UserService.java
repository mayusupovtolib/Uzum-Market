package com.example.uzum_market.service;

import com.example.uzum_market.entity.Role;
import com.example.uzum_market.entity.User;
import com.example.uzum_market.entity.VerificationUrl;
import com.example.uzum_market.entity.enums.RoleName;
import com.example.uzum_market.payload.dto.LoginDto;
import com.example.uzum_market.payload.dto.PasswordDto;
import com.example.uzum_market.payload.dto.ResendVerificationDto;
import com.example.uzum_market.payload.dto.UserDto;
import com.example.uzum_market.repository.RoleRepository;
import com.example.uzum_market.repository.UserRepository;
import com.example.uzum_market.repository.VerificationUrlRepository;
import com.example.uzum_market.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {


    //    public Boolean isValidGmail(String email) {
//        String gmailRegex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
//        Pattern pattern = Pattern.compile(gmailRegex);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();
//    }
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    VerificationUrl verificationUrl = new VerificationUrl();
    String token = UUID.randomUUID().toString();
    Timestamp expired = Timestamp.from(Instant.now().plusMillis(3600000));

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationUrlRepository verificationUrlRepository;

    public static boolean isValidGmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    //    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUserName(username).orElse(null);
//    }
//
//    public Optional<User> findByUserName(String userName) {
//        return userRepository.findByUserName(userName);
//    }
    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public HttpEntity<?> singUp(UserDto userDto) {


        Optional<User> byUserName = findByUserName(userDto.getUserName());
        if (userDto.getUserName().isEmpty()) {
            return ResponseEntity.status(400).body("Username is empty. Pleace write your username");
        }
        if (byUserName.isPresent()) {
            return ResponseEntity.status(401).body("Bunday username mavjud");
        }


        verificationUrl.setEmail(userDto.getEmail());
        verificationUrl.setToken(token);
        verificationUrl.setExpiryDate(expired);
        verificationUrlRepository.save(verificationUrl);
        emailService.sendVerificationEmail(userDto.getEmail(), token);


        String password = passwordEncoder.encode(userDto.getPassword());
        User user = new User();
        user.setName(userDto.getName());
        user.setFirstName(userDto.getFirstName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setUserName(userDto.getUserName());
        user.setPassword(password);
        user.setEmail(userDto.getEmail());
        Optional<Role> optionalRole = roleRepository.findByName(RoleName.USER.name());
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
        }


        userRepository.save(user);
        return ResponseEntity.status(201).body("User Saved");
    }

    public HttpEntity<?> verifyEmail(String token, HttpServletResponse response) {
        try {
            Optional<VerificationUrl> verificationOpt = verificationUrlRepository.findByToken(token);
            if (verificationOpt.isPresent()) {
                verificationUrl = verificationOpt.get();
                if (verificationUrl.getExpiryDate().toInstant().isBefore(Instant.now())) {
                    return ResponseEntity.status(401).body("Verification link has expired. Please sign up again.");
                }
                verificationUrlRepository.delete(verificationUrl);
                Optional<User> byEmail = userRepository.findByEmail(verificationUrl.getEmail());
                User user = byEmail.get();
                user.setVerified(true);
                userRepository.save(user);

                response.sendRedirect("https://www.google.co.uk/");

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(201).body("Invalid verification code!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());

        }
    }

    public HttpEntity loginPage(LoginDto loginDto) {


        if (isValidGmail(loginDto.getEmailOrUsername())) {
            Optional<User> byUserEmail = userRepository.findByEmail(loginDto.getEmailOrUsername());
            if (byUserEmail.isEmpty()) {
                return ResponseEntity.status(400).body("Username is empty. Pleace write your username");
            }

            User exestingUser = byUserEmail.get();
            if (exestingUser.isVerified()) {
                if (passwordEncoder
                        .matches(loginDto.getPassword(), exestingUser.getPassword())) {
                    String token = jwtProvider.generateToken(exestingUser);
                    return ResponseEntity.status(200).body(token);
                }
            } else {
                return ResponseEntity.status(400).body("Verifikatsiya tasdiqlanmagan ");

            }

        } else {
            Optional<User> byUserName = userRepository.findByUserName(loginDto.getEmailOrUsername());

            if (byUserName.isEmpty()) {
                return ResponseEntity.status(400).body("Username is empty. Pleace write your username");
            }
            User NewbyUseName = byUserName.get();
            if (NewbyUseName.isVerified()) {
                try {
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmailOrUsername(), loginDto.getPassword()));
                } catch (Exception e) {
                    throw new RuntimeException(e + " " +
                            " Avval ruyxatdan uting");
                }

                User exestingUser = byUserName.get();
                if (passwordEncoder
                        .matches(loginDto.getPassword(), exestingUser.getPassword())) {
                    String token = jwtProvider.generateToken(exestingUser);
                    return ResponseEntity.status(200).body(token);
                }
            } else {
                return ResponseEntity.status(400).body("Verifikatsiya tasdiqlanmagan ");

            }
        }

        return ResponseEntity.status(400).body("no user");
    }

    public HttpEntity<?> getAll() {
        List allUsersList = new ArrayList<>();
        if (!allUsersList.isEmpty()) {
            userRepository.findAll().forEach(user -> {
                user.getId();
                user.getUsername();
                user.getPhoneNumber();
                user.getFirstName();
                user.getName();
                user.getPassword();
                allUsersList.add(user);
            });
            return ResponseEntity.status(200).body(allUsersList);
        }
        return null;
    }

    public HttpEntity<?> aboutMe(User currentUser) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(200).body(currentUser);
    }

    public HttpEntity<?> editMe(UserDto userDto, User currentUser) {
        try {
            String password = passwordEncoder.encode(userDto.getPassword());
            Optional<User> optionalUser = userRepository.findById(currentUser.getId());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body("Not found");
            }

            User user = optionalUser.get();

            user.setUserName(userDto.getUserName());
            user.setName(userDto.getName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setPassword(password);
            user.setFirstName(userDto.getFirstName());
            userRepository.save(user);

            return ResponseEntity.status(200).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    public HttpEntity<?> deleteAccount(User currentUser) {
        try {
            Optional<User> optionalUser = userRepository.findById(currentUser.getId());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body("Not found");
            }
            User user = optionalUser.get();
            userRepository.delete(user);

            return ResponseEntity.status(200).body("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    public HttpEntity<?> resendVerification(ResendVerificationDto verificationDto) {
        try {
            Optional<User> byEmail = userRepository.findByEmail(verificationDto.getEmail());
            User user = byEmail.get();
            if (byEmail.isPresent() && !user.isVerified()) {

                String token = UUID.randomUUID().toString();
                verificationUrl.setEmail(verificationDto.getEmail());
                verificationUrl.setToken(token);
                verificationUrl.setExpiryDate(expired);
                verificationUrlRepository.save(verificationUrl);
                emailService.sendVerificationEmail(verificationDto.getEmail(), token);
                return ResponseEntity.status(201).body("Email sended");

            }

            return ResponseEntity.status(201).body("E");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());

        }
    }


    public HttpEntity<?> forgotPassword(ResendVerificationDto email) {
        try {
            Optional<User> byEmail = userRepository.findByEmail(email.getEmail());
            User user = byEmail.get();
            if (byEmail.isPresent() && user.isVerified()) {

                String token = UUID.randomUUID().toString();
                verificationUrl.setEmail(email.getEmail());
                verificationUrl.setToken(token);
                verificationUrl.setExpiryDate(expired);
                verificationUrlRepository.save(verificationUrl);
                emailService.sendVerificationEmailPassword(email.getEmail(), token);
                return ResponseEntity.status(201).body("Email sended");

            }
            return ResponseEntity.status(401).body("Din't verifyred");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());

        }

    }


    public HttpEntity<?> forgotPasswordWriteNewPassword(PasswordDto passwordDto, String token, HttpServletResponse response) {
        try {
            Optional<VerificationUrl> verificationOpt = verificationUrlRepository.findByToken(token);
            if (verificationOpt.isPresent()) {
                verificationUrl = verificationOpt.get();
//                if (verificationUrl.getExpiryDate().toInstant().isBefore(Instant.now())) {
//                    return ResponseEntity.status(402).body("Verification link has expired. Please sign up again.");
//                }
                Optional<User> byEmail = userRepository.findByEmail(verificationUrl.getEmail());
                User user = byEmail.get();
                String password = passwordEncoder.encode(passwordDto.getNewPassword());
                user.setPassword(password);
                userRepository.save(user);
                verificationUrlRepository.delete(verificationUrl);
                userRepository.save(user);

                response.sendRedirect("https://www.google.co.uk/");

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(401).body("Invalid verification url!");
            }


        } catch (Exception e) {
            return ResponseEntity.status(400).body("error");
        }
    }


    public HttpEntity<?> verifyPassword(String token, HttpServletResponse response) {
        try {


            Optional<VerificationUrl> verificationOpt = verificationUrlRepository.findByToken(token);
            if (verificationOpt.isPresent()) {
                verificationUrl = verificationOpt.get();
                if (verificationUrl.getExpiryDate().toInstant().isBefore(Instant.now())) {
                    return ResponseEntity.status(400).body("Verification link has expired. Please sign up again.");
                }

                response.sendRedirect("https://uzum.uz/");

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(400).body("Invalid verification url!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Invalid verification url!");
        }
    }
}


//    @Transactional
//    public HttpEntity<?> aboutMeWithToken(String token) {
//        User userFromToken = jwtProvider.getUserFromToken(token);
//        return ResponseEntity.status(201).body(userFromToken);
//    }



