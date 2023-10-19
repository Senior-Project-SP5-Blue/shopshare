package com.sp5blue.shopshare.controllers.shopper;

import com.sp5blue.shopshare.models.shopper.Shopper;
import com.sp5blue.shopshare.services.shopper.IShopperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shoppers")
public class ShopperController {

    private final IShopperService shopperService;

    @Autowired
    public ShopperController(IShopperService shopperService) {
        this.shopperService = shopperService;
    }

    @GetMapping
    public List<Shopper> getShoppers() {
        return shopperService.getShoppers();
    }
}
