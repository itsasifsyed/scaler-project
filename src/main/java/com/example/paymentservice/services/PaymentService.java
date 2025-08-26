package com.example.paymentservice.services;

import com.example.paymentservice.paymentGateways.IPaymentGateway;
import com.example.paymentservice.paymentGateways.PaymentGatewaySelectorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentGatewaySelectorStrategy paymentGatewaySelectorStrategy;

    public String getPaymentLink(Long amount, String orderId, String phoneNumber, String name, String email) {
        IPaymentGateway paymentGateway = paymentGatewaySelectorStrategy.getBestPaymentGateway();
        return paymentGateway.getPaymentLink(amount, orderId, phoneNumber, name, email);
    }
}
