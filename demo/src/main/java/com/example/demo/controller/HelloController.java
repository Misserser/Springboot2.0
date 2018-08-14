package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController
{
    interface Greeting
    {
        public String greet(String name);
    }

    @GetMapping(value = "/hello")
    public String say(@RequestParam(name = "name", required = false, defaultValue = "") String name)
    {
        //return  (name == "")?"Hello":"Hello " + name;
        Greeting greetor = n -> { return (n == "")?"Good evening!":"Good evening " + n + "!";};

        return greetor.greet(name);
    }
}
