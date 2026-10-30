package org.example;

import org.hibernate.Session;
import java.util.List;

public class CustomerDAO {

    public void persistCustomer(Session session, Customer customer) {
        session.persist(customer);
    }

    public Customer findCustomer(Session session, String cpf) {
        return session.find(Customer.class, cpf);
    }
}
