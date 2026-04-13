package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.config.TokenConfig;
import com.mechasystem.mapaintercambista.dto.request.LoginRequest;
import com.mechasystem.mapaintercambista.dto.request.RegisterUserRequest;
import com.mechasystem.mapaintercambista.dto.response.IntercambistaResponse;
import com.mechasystem.mapaintercambista.dto.response.LoginResponse;
import com.mechasystem.mapaintercambista.dto.response.RegisterUserResponse;
import com.mechasystem.mapaintercambista.enums.Role;
import com.mechasystem.mapaintercambista.model.Intercambista;
import com.mechasystem.mapaintercambista.model.User;
import com.mechasystem.mapaintercambista.repository.IntercambistaRepository;
import com.mechasystem.mapaintercambista.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final IntercambistaService intercambistaService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager, TokenConfig tokenConfig,
                        IntercambistaService intercambistaService) {
        this.userRepository = userRepository;
        this.intercambistaService = intercambistaService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public LoginResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);

        return new LoginResponse(token);
    }

    @Transactional
    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        if(userRepository.findUserByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já está em uso");
        }

        User nUser = new User();
        nUser.setRole(Role.ROLE_USER);
        nUser.setEmail(request.email());
        nUser.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(nUser);

        IntercambistaResponse intercambistaResponse = intercambistaService.saveIntercambista(request, nUser);

        return new RegisterUserResponse(
                nUser.getEmail(),
                intercambistaResponse.username()
        );
    }

    public RegisterUserResponse registerAgencia(RegisterUserRequest request) {
        return null;
    }
}
