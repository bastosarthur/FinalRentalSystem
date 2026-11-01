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

    public List<Rental> findRentalsByCpf(Session session, String cpf) {
        return session.createQuery("""
                        FROM Rental
                        WHERE customer.cpf = :cpf
                        ORDER BY rentalId
                        """, Rental.class)
                .setParameter("cpf", cpf)
                .getResultList();
    }

    public List<Rental> findRentalsByName(Session session, String name) {
        return session.createQuery("""
                        FROM Rental
                        WHERE LOWER(customer.name) LIKE :name
                        ORDER BY rentalId
                        """, Rental.class)
                        .setParameter("name", "%" + name.toLowerCase() + "%")
                        .getResultList();
    }

    public List<Rental> rentalsByPrice(Session session) {
        return session.createQuery("""
                        FROM Rental
                        ORDER BY totalPrice
                        """, Rental.class)
                        .getResultList();
    }

    public Long countRentalByStatus(Session session, String status) {
        return session.createQuery("""
                        SELECT COUNT(*)
                        FROM Rental
                        WHERE status = :status
                        """, Long.class)
                        .setParameter("status", status.toLowerCase())
                        .getSingleResult();
    }


}
