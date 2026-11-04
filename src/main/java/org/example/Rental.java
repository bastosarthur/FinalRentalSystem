package org.example;

import jakarta.persistence.*;
import org.hibernate.generator.EventType;
import org.hibernate.annotations.Generated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;


@Entity
@Table(name = "rentals", schema = "final_rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private int rentalId;

    @ManyToOne
    @JoinColumn(name = "customer_cpf", nullable = false, updatable = false)
    private Customer customer;

    @Column(name = "initial_date", nullable = false)
    private LocalDate initialDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "total_days", nullable = false, insertable = true, updatable = true)
    private int totalDays;

    @Column(name = "total_price", nullable = false, updatable = true)
    private BigDecimal totalPrice;

    @Column(name = "status", nullable = false, updatable = true)
    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rental_cars",
            joinColumns = @JoinColumn(name = "rental_id"),
            inverseJoinColumns = @JoinColumn(name = "car_plate"))
    List<Car> carList = new ArrayList<>();

    @Column(name = "registered_at", nullable = false, updatable = false, insertable = false)
    LocalDateTime registrationTime;

    protected Rental() {}

    public Rental(Customer customer, List<Car> carList, LocalDate initialDate, LocalDate returnDate,
                  BigDecimal totalPrice, String status) {
        this.customer = customer;
        this.carList = carList;
        this.initialDate = initialDate;
        this.returnDate = returnDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    @Override
        public String toString() {
            return "\nRental ID: " + rentalId +
                    "\nCustomer: " + customer.getCpf() +
                    "\nStatus: " + status.toUpperCase() +
                    "\nInitial date: " + initialDate +
                    "\nReturn date: " + returnDate +
                    "\nTotal days: " + totalDays +
                    "\nTotal Price: %.2f".formatted(totalPrice) + "\n";
                    //"\nCars: \n" + carList + "\n";
    }

    public int getRentalId() {
        return rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void changeInitialDate(LocalDate newDate) {
        this.initialDate = newDate;
    }

    public void changeReturnDate(LocalDate newDate) {
        this.returnDate = newDate;
    }

    public void changeTotalPrice(BigDecimal newPrice) {
        this.totalPrice = newPrice;
    }

    public void changeStatus(String newStatus) {
        this.status = newStatus;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public void setReturnDate(LocalDate newReturnDate) {
        this.returnDate = newReturnDate;
    }
}
