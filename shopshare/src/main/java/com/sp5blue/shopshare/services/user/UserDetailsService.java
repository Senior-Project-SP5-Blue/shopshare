// package com.sp5blue.shopshare.services.user;
//
// import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
// import com.sp5blue.shopshare.repositories.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Primary;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Service;
//
// @Service
// @Primary
// public class UserDetailsService implements
// org.springframework.security.core.userdetails.UserDetailsService {
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
//        return userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new
// UserNotFoundException("Account with email does not exist - " +  email));
//    }
// }
