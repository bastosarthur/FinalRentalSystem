package org.example;

import org.hibernate.Session;

public class RentalDAO {

    public int persistRental(Session session, Rental rental) {
        session.persist(rental);
        return rental.getRentalId();
    }

    public Rental findRental(Session session, int rentalId) {
        return session.find(Rental.class, rentalId);
    }
}
