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
        String plate = validation.getPlate(input);
        System.out.print("Brand: ");
        String brand = validation.getBrand(input);
        System.out.print("Model: ");
        String model = validation.getModel(input);
        System.out.print("Daily rate: ");
        BigDecimal dailyRate = validation.getPrice(input);
        List<Integer> carFeaturesId = new ArrayList<>();
        while (true) {
            featuresMenu();
            int feature = input.nextInt();
            input.nextLine();
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
        }
        return null;
    }

    public void registerRental(Scanner input) {
        System.out.print("\n\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        System.out.print("Initial date (MM/dd/yyyy): ");
        LocalDate initialDate = validation.getInitialDate(input);
        System.out.print("Return date (MM/dd/yyyy): ");
        LocalDate returnDate = validation.getReturnDate(input);
        List<String> carPlates = new ArrayList<>();
        System.out.print("How many Cars in this rental: ");
        int contagem = validation.getQtd(input);
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
                case -5 -> System.out.print("\nError. Customer already has an active rental.");
                case -6 -> System.out.print("\nError. Car(s) not available for rental at the moment.");
                default -> System.out.println("\nRental registered successfully.\nRental ID: " + rentalId);
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void cancelRental(Scanner input) {
        System.out.print("\n\nRental ID: ");
        int rentalId = validation.getRentalId(input);
        try {
            int success = service.cancelRental(rentalId);
            if (success == -1) {
                System.out.println("\nError. Rental not found.");
                return;
            }
            if (success == 1) {
                System.out.println("\nRental successfully canceled.");
                return;
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public Car searchCar(Scanner input) {
        System.out.print("\n\nSearching a car.\nCar plate: ");
        String plate = validation.getPlate(input);
        try {
            Car car = service.searchCar(plate);
            if (car == null) {
                System.out.println("\nCar not found.");
                return null;
            }
            System.out.println(car);
            List<Feature> carFeatures = car.getCarFeatures();
            System.out.println("Features: ");
            carFeatures.forEach(System.out::print);
            System.out.println();
            return car;
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
        return null;
    }

    public void customerRentalHistory(Scanner input) {
        System.out.print("\n\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        try {
            Customer customer = service.searchCustomer(cpf);
            if (customer == null) {
                System.out.println("\nCustomer not found.");
                return;
            }
            List<Rental> customerHistory = customer.getRentalHistory();
            System.out.println("\n" + customer);
            System.out.println("Rental History: ");
            if (customerHistory.isEmpty()) {
                System.out.println("\nThis customer has no rental history.");
                return;
            }
            for (Rental rental : customerHistory) {
                System.out.print(rental);
            }

        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void carRentalHistory(Scanner input) {
        System.out.print("\n\nCar Plate: ");
        String plate = validation.getPlate(input);
        try {
            Car car = service.searchCar(plate);
            if (car == null) {
                System.out.println("\nCar not found.");
                return;
            }
            List<Rental> carHistory = car.getRentalHistory();
            for (Rental rental : carHistory) {
                System.out.println("\nRental ID: " + rental.getRentalId() +
                                    "\nCustomer: " + rental.getCustomer().getCpf() +
                                    "\nStatus: " + rental.getStatus().toUpperCase() +
                                    "\nInitial date: " + rental.getInitialDate() +
                                    "\nReturn date: " + rental.getReturnDate() +
                                    "\nTotal days: " + rental.getTotalDays() +
                                    "\nTotal Price: %.2f".formatted(rental.getTotalPrice()));
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void searchRental(Scanner input) {
        System.out.print("\n\nRental ID: ");
        int rentalId = validation.getRentalId(input);
        try {
            Rental rental = service.searchRental(rentalId);
            if (rental == null) {
                System.out.println("\nRental not found.");
                return;
            }
            List<Car> rentalCars = rental.getCarList();
            System.out.println(rental);
            System.out.println("Cars: ");
            rentalCars.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void searchRentalByDate(Scanner input) {
        System.out.print("\n\nSearching all rentals in a certain date.\nDate: ");
        LocalDate date = validation.getInitialDate(input);
        try {
            List<Rental> rentalByDate = service.rentalByDate(date);
            if (rentalByDate.isEmpty()) {
                System.out.println("\nNo rentals on this date.");
                return;
            }
            rentalByDate.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalsByInitialDate(Scanner input) {
        System.out.print("\n\nSearching rental by initial date.\nInitial date: ");
        LocalDate initialDate = validation.getInitialDate(input);
        try {
            List<Rental> rentalsByInitialDate = service.rentalByInitialDate(initialDate);
            if (rentalsByInitialDate.isEmpty()) {
                System.out.println("\nNo rentals on this date.");
                return;
            }
            rentalsByInitialDate.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalsByReturnDate(Scanner input) {
        System.out.print("\n\nSearching rentals by return date.\nReturn date: ");
        LocalDate returnDate = validation.getReturnDate(input);
        try {
            List<Rental> rentalsByReturnDate = service.rentalByReturnDate(returnDate);
            if (rentalsByReturnDate.isEmpty()) {
                System.out.println("\nNo returned rentals on this date.");
                return;
            }
            rentalsByReturnDate.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalsByStatus(Scanner input) {
        System.out.print("\n\nSearching rentals by status.\nStatus (active, reserved, canceled): ");
        String status = validation.getStatus(input);
        try {
            List<Rental> rentalByStatus = service.rentalsByStatus(status);
            if (rentalByStatus.isEmpty()) {
                System.out.print("\nNo " + status + " rentals on our system.");
                return;
            }
            rentalByStatus.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void avgRentalPrice() {
        try {
            Double avgRentalPrice = service.avgRentalPrice();
            System.out.print("\n\nAverage Rental Price in our company: %.2f".formatted(avgRentalPrice));
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalCount() {
        try {
            Long rentalCount = service.rentalCount();
            System.out.println("\n\nTotal number of rentals: " + rentalCount);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalsByCpf(Scanner input) {
        System.out.print("\n\nSearching rentals by CPF.\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        try {
            List<Rental> rentalsByCpf = service.rentalsByCpf(cpf);
            if (rentalsByCpf.isEmpty()) {
                System.out.println("\nNo rentals found for this CPF.");
                return;
            }
            rentalsByCpf.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalsByName(Scanner input) {
        System.out.print("\n\nSearching Rentals by a customer's name.\nFull or partial name: ");
        String tryName = input.nextLine();
        try {
            List<Rental> rentalsByName = service.rentalsByName(tryName);
            if (rentalsByName.isEmpty()) {
                System.out.println("\nNo rentals found for this customer.");
                return;
            }
            rentalsByName.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalsByPrice() {
        try {
            System.out.print("\n\nRentals ordered by price: ");
            List<Rental> rentalsByPrice = service.rentalsByPrice();
            rentalsByPrice.forEach(System.out::print);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void countRentalsByStatus(Scanner input) {
        System.out.print("\n\nNumber of rental by status.\nStatus ('active', 'reserved, 'canceled'): ");
        String status = validation.getStatus(input);
        try {
            long count = service.countRentalsByStatus(status);
            System.out.println("\nThere are " + count + " " + status.toUpperCase() + " rentals in our system.");
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Main main = new Main();

        //main.registerFeature(input);
        //main.registerCustomer(input);
        //main.registerCar(input);
        //main.registerRental(input);

        //main.cancelRental(input);

        //Customer customer = main.searchCustomer(input);
        //main.searchCar(input);
        //main.customerRentalHistory(input);
        //main.carRentalHistory(input);
        //main.searchRental(input);

        //main.searchRentalByDate(input);
        //main.rentalsByInitialDate(input);
        //main.rentalsByReturnDate(input);
        //main.activeRentals(input);
        //main.canceledRentals();
        //main.rentalsByStatus(input);
        //main.avgRentalPrice();
        //main.rentalCount();
        //main.rentalsByCpf(input);
        //main.rentalsByName(input);
        //main.rentalsByPrice();
        main.countRentalsByStatus(input);
    }
}