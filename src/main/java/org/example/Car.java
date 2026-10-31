package org.example;

import jakarta.persistence.*;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "cars", schema = "final_rental")
public class Car {

    @Id
    @Column(name = "plate", length = 6, updatable = false)
    private String plate;

    @Column(name = "brand", nullable = false, updatable = false, length = 20)
    private String brand;

    @Column(name = "model", nullable = false, updatable = false, length = 20)
    private String model;

    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;

    @ManyToOne
    @JoinColumn(name = "active_rental")
    private Rental activeRental;

    @ManyToMany
    @JoinTable(
            name = "car_features",
            joinColumns = @JoinColumn(name = "car_plate"),
            inverseJoinColumns = @JoinColumn(name = "feature_id"))
    private List<Feature> carFeatures;

    @ManyToMany(mappedBy = "carList")
    private List<Rental> rentalHistory;

    protected Car() {}

    public Car(String plate, String brand, String model, BigDecimal dailyRate) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.dailyRate = dailyRate;
    }

    @Override
        public String toString() {
            return "\nCar Plate: " + plate +
                    "\nBrand: " + brand +
                    "\nModel: " + model +
                    "\nDaily Price: %.2f".formatted(dailyRate) +
                    "\nActive Rental: " + (activeRental != null ? activeRental.getRentalId() : "N/A") +
                    "\nFeatures: " + carFeatures +
                    "\nRental History: " + (rentalHistory.isEmpty() ? "N/A" : rentalHistory) + "\n";
    }

    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public Rental getActiveRental() {
        return activeRental;
    }

    public void setActiveRental(Rental activeRental) {
        this.activeRental = activeRental;
    }

    public void setNewDailyRate(BigDecimal newDailyRate) {
        this.dailyRate = newDailyRate;
    }

    public List<Feature> getCarFeatures() {
        return carFeatures;
    }

    public void setNewFeature(Feature newFeature) {
        carFeatures.add(newFeature);
    }

    public void setFeatureList(List<Feature> carFeatures) {
        this.carFeatures = carFeatures;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }
}
