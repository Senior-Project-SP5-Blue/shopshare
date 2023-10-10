package com.sp5blue.shopshare.controllers.shopper;

import com.sp5blue.shopshare.security.request.SignInRequest;
import com.sp5blue.shopshare.security.request.SignUpRequest;
import com.sp5blue.shopshare.services.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppers")
public class ShopperController {

    @GetMapping
    public String getShoppers() {
//        List<Shopper> shopperList = shopperRepository.findAll();
        return "You got the shoppers";
    }
}
