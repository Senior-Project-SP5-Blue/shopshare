package com.sp5blue.shopshare.controllers.shopper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shoppers")
public class ShopperController {

    @GetMapping
    public String getShoppers() {
//        List<Shopper> shopperList = shopperRepository.findAll();
        return "You got the shoppers";
    }
}
