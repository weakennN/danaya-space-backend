package danayaspace.controller;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import danayaspace.api.AuthRequest;
import danayaspace.users.UserEntity;
import danayaspace.users.UserRepository;
import danayaspace.util.JwtUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // TODO: Include userId in the jwt
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/register")
    public void register(@RequestBody AuthRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            throw new IllegalStateException("Email is alreay taken");
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }
}
