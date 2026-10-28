package org.example;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    Service service = new Service();
    /*
    public void registerCar(Scanner input) {
        System.out.print("\n\nCar Plate: ");
        String plate = input.nextLine();
        System.out.print("Brand: ");
        String brand = input.nextLine();
        System.out.print("Model: ");
        String model = input.nextLine();
        System.out.print("Daily rate: ");
        BigDecimal dailyRate = input.nextBigDecimal();
        try {
            Car car = new Car(plate, brand, model, dailyRate);
            int success = service.registerCar(car, carFeatures);
            if (success == -1) {
                System.out.println("\nCar already registered.");
                return;
            }
            if (success == 1) {
                System.out.println("\nCar registered.");
                return;
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }
    */

    public void registerFeature(Scanner input) {
        System.out.print("\nFeature: ");
        String feature = input.nextLine();
        try {
            Feature newFeature = new Feature(feature);
            int featureId = service.persistFeature(newFeature);
            if (featureId == -1) {
                System.out.println("\nError. Feature already registered.");
                return;
            }
            System.out.print("Feature registered.\nFeature ID " + featureId + "\n");
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner (System.in);
        Main main = new Main();

        main.registerFeature(input);
    }
}