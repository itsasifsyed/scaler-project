package com.example.paymentservice.paymentGateways;

public interface IPaymentGateway {
    String getPaymentLink(Long amount, String orderId, String phoneNumber, String name, String email);
}
