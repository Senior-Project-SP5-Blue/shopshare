package com.sp5blue.shopshare.controllers.shopper;

import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.services.shopper.ShopperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shoppers")
public class ShopperController {

    private final ShopperService shopperService;

    @Autowired
    public ShopperController(ShopperService shopperService) {
        this.shopperService = shopperService;
    }

    @GetMapping
    public List<Shopper> getShoppers() {
        return shopperService.readShoppers();
    }
}
