package com.sp5blue.shopshare.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/next")
public class Test {

    @GetMapping("/page")
    public String nextPage() {
        return "Successfully logged in";
    }

    @GetMapping("/page1")
    public String nextPage1() {
        return "Successfully logged in 2";
    }
}
