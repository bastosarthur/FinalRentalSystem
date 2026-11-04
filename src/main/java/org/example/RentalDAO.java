package org.example;

import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                        ORDER BY rentalId
                        """, Rental.class)
                        .setParameter("date", date)
                        .getResultList();
    }

    public List<Rental> rentalByInitialDate(Session session, LocalDate initialDate) {
        return session.createQuery("""
                        FROM Rental
                        WHERE initialDate = :initialDale
                        ORDER BY status DESC, rentalId
                        """, Rental.class)
                        .setParameter("initialDale", initialDate)
                        .getResultList();
    }

    public List<Rental> rentalByReturnDate(Session session, LocalDate returnDate) {
        return session.createQuery("""
                        FROM Rental
                        WHERE returnDate = :returnDate
                        ORDER BY status DESC, rentalId
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
                        .setParameter("status", status.toUpperCase())
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
                        .setParameter("status", status.toUpperCase())
                        .getSingleResult();
    }

    public Double avgRentalPriceByCustomer(Session session, String cpf) {
        return session.createQuery("""
                        SELECT AVG(totalPrice)
                        FROM Rental
                        WHERE LOWER(customer) = :cpf
                        """, Double.class)
                        .setParameter("cpf", cpf.toLowerCase())
                        .getSingleResult();
    }

    public Double avgRentalDaysByCustomer(Session session, String cpf) {
        return session.createQuery("""
                        SELECT AVG(totalDays)
                        FROM Rental
                        WHERE LOWER(customer) = :cpf
                        """, Double.class)
                        .setParameter("cpf", cpf.toLowerCase())
                        .getSingleResult();
    }

    public Double avgRentalDays(Session session) {
        return session.createQuery("""
                        SELECT AVG(totalDays)
                        FROM Rental
                        """, Double.class)
                        .getSingleResult();
    }

    public List<Rental> findRentalByRegistDay(Session session, LocalDateTime startDate, LocalDateTime endDate) {
        return session.createQuery("""
                        FROM Rental
                        WHERE registrationTime >= :startDate
                        AND
                        registrationTime <= :endDate
                        """, Rental.class)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .getResultList();
    }


}
