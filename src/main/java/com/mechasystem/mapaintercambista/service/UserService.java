package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.config.TokenConfig;
import com.mechasystem.mapaintercambista.dto.request.CreateAgenciaRequest;
import com.mechasystem.mapaintercambista.dto.request.LoginRequest;
import com.mechasystem.mapaintercambista.dto.request.RegisterUserRequest;
import com.mechasystem.mapaintercambista.dto.response.AgenciaResponse;
import com.mechasystem.mapaintercambista.dto.response.IntercambistaResponse;
import com.mechasystem.mapaintercambista.dto.response.LoginResponse;
import com.mechasystem.mapaintercambista.dto.response.RegisterUserResponse;
import com.mechasystem.mapaintercambista.enums.Role;
import com.mechasystem.mapaintercambista.exception.ConflictException;
import com.mechasystem.mapaintercambista.model.User;
import com.mechasystem.mapaintercambista.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final IntercambistaService intercambistaService;
    private final AgenciaService agenciaService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager, TokenConfig tokenConfig,
                        IntercambistaService intercambistaService, AgenciaService agenciaService) {
        this.userRepository = userRepository;
        this.intercambistaService = intercambistaService;
        this.agenciaService = agenciaService;
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
    public RegisterUserResponse registerIntercambista(RegisterUserRequest request) {
        validarEmail(request.email());

        User u = registerUser(request.email(), request.password(), Role.ROLE_USER);
        IntercambistaResponse intercambistaResponse = intercambistaService.saveIntercambista(request, u);

        return new RegisterUserResponse(
                u.getEmail(),
                intercambistaResponse.username()
        );
    }

    @Transactional
    public AgenciaResponse registerAgencia(CreateAgenciaRequest request) {
        validarEmail(request.email());

        User u = registerUser(request.email(), request.password(), Role.ROLE_AGENCIA);

        return agenciaService.saveAgencia(request, u);
    }

    private User registerUser(String email, String password, Role r) {
        User nUser = new User();
        nUser.setRole(r);
        nUser.setEmail(email);
        nUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(nUser);

        return nUser;
    }

    private void validarEmail (String email) {
        if(userRepository.findUserByEmail(email).isPresent()) {
            throw new ConflictException("E-mail já está em uso");
        }
    }

}
