package com.projects.ecommerce.multivendor.repo;

import com.projects.ecommerce.multivendor.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepo extends JpaRepository<PaymentOrder, Long> {
    PaymentOrder findByPaymentLinkId(String paymentLinkId);
}
