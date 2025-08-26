package com.example.paymentservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    @PostMapping("/stripeWebhook")
    public void respondToEvents(@RequestBody String event) {
        //Add logic to send email or do CRUD based on event
        System.out.println(event);
    }
}
