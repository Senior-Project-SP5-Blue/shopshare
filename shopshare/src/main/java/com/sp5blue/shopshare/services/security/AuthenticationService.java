package com.sp5blue.shopshare.services.security;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.repositories.ShopperRepository;
import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.security.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final ShopperRepository shopperRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthenticationService(ShopperRepository shopperRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.shopperRepository = shopperRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationResponse signUp(SignUpRequest request) {
        if (shopperRepository.existsByUsername(request.username())) throw new UserAlreadyExistsException("An account with entered username already exists - " + request.username());
        if (shopperRepository.existsByEmail(request.email())) throw new UserAlreadyExistsException("An account with entered email already exists - " + request.email());
        Shopper user = new Shopper(request.firstName(), request.lastName(), request.username(), request.email(), passwordEncoder.encode(request.password()));
        shopperRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest request) throws UserNotFoundException {
        Optional<Shopper> user = shopperRepository.findByEmail(request.email());
        if (user.isEmpty()) throw new UserNotFoundException("User with email does not exist - " + request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        final String jwt = jwtService.generateToken(user.get());
        return new AuthenticationResponse(jwt);
    }
}
