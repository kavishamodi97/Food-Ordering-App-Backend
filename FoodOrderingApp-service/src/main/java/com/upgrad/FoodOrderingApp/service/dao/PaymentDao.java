package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class PaymentDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method gets all the payment methods
     *
     * @return
     */
    public List<PaymentEntity> getPaymentMethods() {
        List<PaymentEntity> paymentMethods = entityManager.createNamedQuery("getAllPaymentMethods", PaymentEntity.class).getResultList();
        if (paymentMethods != null) {
            return paymentMethods;
        }
        return Collections.emptyList();
    }
}
