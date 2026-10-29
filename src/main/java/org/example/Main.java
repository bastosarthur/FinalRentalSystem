package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    Service service = new Service();

    public void featuresMenu() {
        System.out.println("\nAvailable Features:");

        System.out.println("1. GPS");
        System.out.println("2. GPS");
        System.out.println("6. Air Conditioner");
        System.out.println("7. Airbags (6)");
        System.out.println("8. Airbags (8)");
        System.out.println("9. Auto Cruise");
        System.out.println("10. Automatic Gear");
        System.out.println("11. Manual Gear");
        System.out.println("12. Turbo");
        System.out.println("13. Electric");
        System.out.println("14. Fuel");
        System.out.println("15. Hybrid");
        System.out.println("16. 2x4");
        System.out.println("17. 4x4");
        System.out.println("0. EXIT");
        System.out.println("\nFeature ID: ");
    }

    public void registerCar(Scanner input) {
        System.out.print("\n\nCar Plate: ");
        String plate = input.nextLine();
        System.out.print("Brand: ");
        String brand = input.nextLine();
        System.out.print("Model: ");
        String model = input.nextLine();
        System.out.print("Daily rate: ");
        BigDecimal dailyRate = input.nextBigDecimal();
        List<Integer> carFeaturesId = new ArrayList<>();
        while (true) {
            featuresMenu();
            int feature = input.nextInt();
            if (feature == 0) {
                break;
            }
            carFeaturesId.add(feature);
        }
        try {
            Car car = new Car(plate.toUpperCase(), brand, model, dailyRate);
            int success = service.registerCar(car, carFeaturesId);
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
        Scanner input = new Scanner(System.in);
        Main main = new Main();

        //main.registerFeature(input);
        main.registerCar(input);
    }
}