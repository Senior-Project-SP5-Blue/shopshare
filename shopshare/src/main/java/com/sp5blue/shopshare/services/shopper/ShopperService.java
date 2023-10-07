package com.sp5blue.shopshare.services.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.Shopper;
import com.sp5blue.shopshare.repositories.ShopperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
public class ShopperService implements UserDetailsService, IShopperService {

    private final ShopperRepository shopperRepository;

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
        Optional<Shopper> shopper = shopperRepository.findByEmail(username);
        if (shopper.isEmpty()) throw new UserNotFoundException("User does not exist - " + username);
        return shopper.get();
    }

    @Override
    public Shopper readById(String id){
        UUID _id = UUID.fromString(id);
        return readById(_id);
    }

    @Override
    public Shopper readById(UUID id) throws UserNotFoundException {
        Optional<Shopper> shopper = shopperRepository.findById(id);
        if (shopper.isEmpty()) throw new UserNotFoundException("User does not exist - " + id);
        return shopper.get();
    }

    @Override
    public Shopper readByEmail(String email) throws UserNotFoundException {
        Optional<Shopper> shopper = shopperRepository.findByEmail(email);
        if (shopper.isEmpty()) throw new UserNotFoundException("User does not exist - " + email);
        return shopper.get();
    }

    @Override
    public List<Shopper> read() {
        return shopperRepository.findAll();
    }

    @Override
    public boolean shopperExists(UUID id) {
        return shopperRepository.existsById(id);
    }
}
