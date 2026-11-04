package org.example;

import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerDAO {

    public void persistCustomer(Session session, Customer customer) {
        session.persist(customer);
    }

    public Customer findCustomer(Session session, String cpf) {
        return session.find(Customer.class, cpf);
    }

    public List<Customer> findByRegistrationDate(Session session, LocalDateTime startDate, LocalDateTime endDate) {
        return session.createQuery("""
                    FROM Customer
                    WHERE registrationTime >= :startDate
                    AND
                    registrationTime <= :endDate
                    """, Customer.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
    }
}
