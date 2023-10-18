package com.sp5blue.shopshare.services.shopper;

import com.sp5blue.shopshare.exceptions.authentication.UserAlreadyExistsException;
import com.sp5blue.shopshare.exceptions.authentication.UserNotFoundException;
import com.sp5blue.shopshare.models.shopper.Shopper;
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
public class ShopperService1 implements UserDetailsService, IShopperService1 {

    private final ShopperRepository shopperRepository;

    private final Logger logger = LoggerFactory.getLogger(ShopperService1.class);

    @Autowired
    public ShopperService1(ShopperRepository shopperRepository) {
        this.shopperRepository = shopperRepository;
    }

    @Override
    @Transactional
    public Shopper createShopper(Shopper shopper) throws UserAlreadyExistsException {
        if (shopperRepository.existsByEmail(shopper.getEmail())) throw new UserAlreadyExistsException("Shopper with email already exists - " + shopper.getEmail());
        if (shopperRepository.existsByUsername(shopper.getUsername())) throw new UserAlreadyExistsException("Shopper with username already exists - " + shopper.getUsername());
        return shopperRepository.save(shopper);
    }

    @Override
    @Transactional
    public Shopper createShopper(String firstName, String lastName, String username, String email, String password) throws UserAlreadyExistsException {
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
    public Shopper readShopperById(String id){
        UUID _id = UUID.fromString(id);
        return readShopperById(_id);
    }

    @Override
    public Shopper readShopperById(UUID id) throws UserNotFoundException {
        return shopperRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist - " + id));
    }

    @Override
    public Shopper readShopperByEmail(String email) throws UserNotFoundException {
        return shopperRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User does not exist - " + email));
    }

    @Override
    public Shopper readShopperByUsername(String username) throws UserNotFoundException {
        return shopperRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User does not exist - " + username));
    }

    @Override
    public List<Shopper> readShoppersByShopperGroup(UUID groupId) {
        return shopperRepository.findByShopperGroup(groupId);
    }

    @Override
    public List<Shopper> readShoppers() {
        return shopperRepository.findAll();
    }

    @Override
    public boolean shopperExists(UUID id) {
        return shopperRepository.existsById(id);
    }

    @Override
    public boolean shopperExistsByGroup(UUID shopperId, UUID groupId) {
        return shopperRepository.existsByGroup(shopperId, groupId);
    }


    @Override
    public boolean shopperExistsByEmail(String email) {
        return shopperRepository.existsByEmail(email);
    }

    @Override
    public boolean shopperExistsByUsername(String username) {
        return shopperRepository.existsByUsername(username);
    }

    @Override
    public boolean shopperExistsAsAdminByGroup(UUID shopperId, UUID groupId) {
        return shopperRepository.existsAsAdminByGroup(shopperId, groupId);
    }
}
