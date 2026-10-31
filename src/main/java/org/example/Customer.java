package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers", schema = "final_rental")
public class Customer {

    @Id
    @Column(name = "cpf", length = 11, updatable = false)
    private String cpf;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, updatable = true, length = 50)
    private String email;

    @Column(name = "phone", nullable = false, updatable = true, length = 11)
    private String phone;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "active_rental")
    Rental activeRental;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    List<Rental> rentalHistory;

    @Override
        public String toString() {
            return "\nCPF: " + cpf +
                    "\nName: " + name +
                    "\nEmail: " + email +
                    "\nPhone: " + phone +
                    "\nActive rental: " + (activeRental != null ? activeRental.getRentalId() : "N/A\n");
    }

    protected Customer() {}

    public Customer(String cpf, String name, String email, String phone) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Rental getActiveRental() {
        return activeRental;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setPhone(String newPhone) {
        this.phone = newPhone;
    }

    public void setActiveRental(Rental activeRental) {
        this.activeRental = activeRental;
    }


}
