package org.example;

import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RentalDAO {

    public int persistRental(Session session, Rental rental) {
        session.persist(rental);
        return rental.getRentalId();
    }

    public Rental findRental(Session session, int rentalId) {
        return session.find(Rental.class, rentalId);
    }

    public List<Rental> rentalByDate(Session session, LocalDate date) {
        return session.createQuery("""
                        FROM Rental
                        WHERE :date BETWEEN initialDate AND returnDate
                        ORDER BY initialDate
                        """, Rental.class)
                        .setParameter("date", date)
                        .getResultList();
    }

    public List<Rental> rentalByInitialDate(Session session, LocalDate initialDate) {
        return session.createQuery("""
                        FROM Rental
                        WHERE initialDate = :initialDale
                        ORDER BY status DESC
                        """, Rental.class)
                        .setParameter("initialDale", initialDate)
                        .getResultList();
    }

    public List<Rental> rentalByReturnDate(Session session, LocalDate returnDate) {
        return session.createQuery("""
                        FROM Rental
                        WHERE returnDate = :returnDate
                        ORDER BY status DESC
                        """, Rental.class)
                        .setParameter("returnDate", returnDate)
                        .getResultList();
    }

    public List<Rental> activeRentals(Session session) {
        return session.createQuery("""
                        FROM Rental
                        WHERE status = "active"
                        ORDER BY rentalId
                        """, Rental.class)
                        .getResultList();
    }

    public List<Rental> canceledRentals(Session session) {
        return session.createQuery("""
                        FROM Rental
                        WHERE status = "canceled"
                        ORDER BY rentalId
                        """, Rental.class)
                        .getResultList();
    }

    public List<Rental> rentalByStatus(Session session, String status) {
        return session.createQuery("""
                        FROM Rental
                        WHERE status = :status
                        ORDER BY rentalId
                        """, Rental.class)
                        .setParameter("status", status.toLowerCase())
                        .getResultList();
    }

    public Double avgRentalPrice(Session session) {
        return session.createQuery("""
                        SELECT AVG(totalPrice)
                        FROM Rental
                        """, Double.class)
                        .getSingleResult();
    }

    public Long rentalCount(Session session) {
        return session.createQuery("""
                        SELECT COUNT(*)
                        FROM Rental
                        """, Long.class)
                        .getSingleResult();
    }


}
