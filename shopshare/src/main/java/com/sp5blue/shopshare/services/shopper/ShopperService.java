package com.sp5blue.shopshare.services.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.repositories.ShopperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Primary
public class ShopperService implements UserDetailsService, IShopperService {

    private final ShopperRepository shopperRepository;

    private final Logger logger = LoggerFactory.getLogger(ShopperService.class);

    @Autowired
    public ShopperService(ShopperRepository shopperRepository) {
        this.shopperRepository = shopperRepository;
    }

    @Override
    @Transactional
    public Shopper create(Shopper shopper) throws UserAlreadyExistsException {
        if (shopperRepository.existsByEmail(shopper.getEmail())) throw new UserAlreadyExistsException("Shopper with email already exists - " + shopper.getEmail());
        if (shopperRepository.existsByUsername(shopper.getUsername())) throw new UserAlreadyExistsException("Shopper with username already exists - " + shopper.getUsername());
        return shopperRepository.save(shopper);
    }

    @Override
    @Transactional
    public Shopper create(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException {
        if (shopperRepository.existsByEmail(email)) throw new UserAlreadyExistsException("Shopper with email already exists - " + email);
        if (shopperRepository.existsByEmail(username)) throw new UserAlreadyExistsException("Shopper with username already exists - " + username);
        Shopper shopper = new Shopper(firstName, lastName, username, email, password);
        return shopperRepository.save(shopper);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return shopperRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User does not exist - " + username));
    }

    @Override
    public Shopper readById(String id){
        UUID _id = UUID.fromString(id);
        return readById(_id);
    }

    @Override
    public Shopper readById(UUID id) throws UserNotFoundException {
        return shopperRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist - " + id));
    }

    @Override
    public Shopper readByEmail(String email) throws UserNotFoundException {
        return shopperRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User does not exist - " + email));
    }

    @Override
    public Shopper readByUsername(String username) throws UserNotFoundException {
        return shopperRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User does not exist - " + username));
    }

    @Override
    public List<Shopper> read() {
        return shopperRepository.findAll();
    }

    @Override
    public boolean exists(UUID id) {
        return shopperRepository.existsById(id);
    }


    @Override
    public boolean existsByEmail(String email) {
        return shopperRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return shopperRepository.existsByUsername(username);
    }
}
