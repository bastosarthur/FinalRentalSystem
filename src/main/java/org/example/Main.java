package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;


public class Main {

    Service service = new Service();
    InputValidation validation = new InputValidation();

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

    public void registerCustomer(Scanner input) {
        System.out.print("\n\nCustomer's CPF: ");
        String cpf = validation.getCpf(input);
        System.out.print("First and Last name: ");
        String name = validation.getName(input);
        System.out.print("Email: ");
        String email = validation.getEmail(input);
        System.out.print("Phone: ");
        String phone = validation.getPhone(input);
        Customer customer = new Customer(cpf, name, email, phone);
        try {
            int success = service.registerCustomer(customer);
            if (success == -1) {
                System.out.println("\nError. CPF already registered.");
                return;
            }
            if (success == -2) {
                System.out.println("\nError. Email already registered.");
                return;
            }
            if (success == -3) {
                System.out.println("\nError. Phone already registered.");
                return;
            }
            if (success == 1) {
                System.out.println("\nCustomer registered successfully.");
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

    public Customer searchCustomer(Scanner input) {
        System.out.print("\n\nCustomer's CPF: ");
        String cpf = validation.getCpf(input);
        try {
            Customer customer = service.searchCustomer(cpf);
            if (customer == null) {
                System.out.println("\nCustomer not found.");
                return null;
            }
            System.out.println(customer);
            return customer;
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
            return null;
        }
    }

    public void registerRental(Scanner input) {
        System.out.print("\n\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        System.out.print("Initial date (MM/dd/yyyy): ");
        LocalDate initialDate = validation.getInitialDate(input);
        System.out.print("Return date (MM/dd/yyyy): ");
        LocalDate returnDate = validation.getReturnDate(input);
        List<String> carPlates = new ArrayList<>();
        int contagem = 2;
        while (contagem > 0) {
            System.out.print("Rental car plate: ");
            String plate = validation.getPlate(input);
            carPlates.add(plate);
            contagem--;
        }
        try {
            int rentalId = service.registerRental(cpf, carPlates, initialDate, returnDate);
            switch (rentalId) {
                case -1 -> System.out.print("\nError. Customer not found.");
                case -2 -> System.out.print("\nError. Car(s) not found");
                case -3 -> System.out.print("\nError. Invalid rental date");
                case -4 -> System.out.print("\nError. Invalid Status.");
                default -> System.out.println("\nRental registered successfully.\nRental ID: " + rentalId);
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Main main = new Main();

        //main.registerFeature(input);
        //main.registerCar(input);
        //main.registerCustomer(input);
        //main.searchCustomer(input);
        main.registerRental(input);
    }
}