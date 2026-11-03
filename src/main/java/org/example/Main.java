package org.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;


public class Main {

    final static Service service = new Service();
    final static InputValidation validation = new InputValidation();

    public void featuresMenu() {
        System.out.println("\nAvailable Features:");
        System.out.println("1. GPS");
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
        System.out.println("18. Auto-drive");
        System.out.println("0. EXIT");
        System.out.println("\nFeature ID: ");
    }

    public void registerCar(Scanner input) {
        System.out.print("\nCar Plate: ");
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
        System.out.print("\nCustomer's CPF: ");
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

    public void searchCustomer(Scanner input) {
        System.out.print("\nCustomer's CPF: ");
        String cpf = validation.getCpf(input);
        try {
            Customer customer = service.searchCustomer(cpf);
            if (customer == null) {
                System.out.println("\nCustomer not found.");
            }
            System.out.println(customer);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void updateCustomerEmail(Scanner input) {
        System.out.print("\nUpdating a customer's email.\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        try {
            Customer customer = service.searchCustomer(cpf);
            if (customer == null) {
                System.out.println("\nCustomer not found.");
                return;
            }
            System.out.println(customer);
            System.out.print("Customer new email: ");
            String newEmail = validation.getEmail(input);
            int success = service.updateCustomerEmail(customer.getCpf(), newEmail);
            switch (success) {
                case -1 -> System.out.println("\nCustomer not found.");
                case -2 -> System.out.println("\nError. New email needs to be different from current email.");
                case 1 -> System.out.println("\nCustomer email updated successfully.");
                default -> System.out.print("\nError. Please try again.");
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void updateCustomerPhone(Scanner input) {
        System.out.print("\nUpdating a customer's phone number.\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        try {
            Customer customer = service.searchCustomer(cpf);
            if (customer == null) {
                System.out.println("\nCustomer not found.");
                return;
            }
            System.out.println(customer);
            System.out.print("Customer new phone number: ");
            String newPhone = validation.getPhone(input);
            int success = service.updateCustomerPhone(customer.getCpf(), newPhone);
            switch (success) {
                case -1 -> System.out.println("\nCustomer not found.");
                case -2 -> System.out.println("\nError. Same phone number.");
                case 1 -> System.out.println("\nCustomer phone number updated successfully.");
                default -> System.out.print("\nError. Please try again.");
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void registerRental(Scanner input) {
        System.out.print("\nCustomer CPF: ");
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
                case -1 -> System.out.println("\nError. Customer not found.");
                case -2 -> System.out.println("\nError. Car(s) not found");
                case -3 -> System.out.println("\nError. Invalid rental date");
                case -4 -> System.out.println("\nError. Invalid Status.");
                case -5 -> System.out.println("\nError. Customer already has an active rental.");
                case -6 -> System.out.println("\nError. Car(s) not available for rental at the moment.");
                default -> System.out.println("\nRental registered successfully.\nRental ID: " + rentalId);
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void cancelRental(Scanner input) {
        System.out.print("\nRental ID: ");
        int rentalId = validation.getRentalId(input);
        try {
            int success = service.cancelRental(rentalId);
            if (success == -1) {
                System.out.println("\nError. Rental not found.");
                return;
            }
            if (success == -2) {
                System.out.println("\nError. Rental had already been canceled.");
                return;
            }
            if (success == -3) {
                System.out.println("\nError. Rental already completed.");
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

    public void searchCar(Scanner input) {
        System.out.print("\n\nSearching a car.\nCar plate: ");
        String plate = validation.getPlate(input);
        try {
            Car car = service.searchCar(plate);
            if (car == null) {
                System.out.println("\nCar not found.");
            }
            System.out.println(car);
            List<Feature> carFeatures = car.getCarFeatures();
            System.out.println("Features: ");
            carFeatures.forEach(System.out::print);
            System.out.println();
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void updateDailyRate(Scanner input) {
        System.out.print("\nUpdating a Car's Daily Rate.\nCar plate: ");
        String plate = validation.getPlate(input);
        try {
            Car car = service.searchCar(plate);
            if (car == null) {
                System.out.println("\nCar not found.");
                return;
            }
            System.out.println(car);
            System.out.print("\nNew Daily Rate: ");
            BigDecimal newDailyRate = validation.getPrice(input);
            int success = service.updateDailyRate(car.getPlate(), newDailyRate);
            switch (success) {
                case -1 -> System.out.println("\nCar not found.");
                case -2 -> System.out.println("\nError. Same Daily Rate.");
                case 1 -> System.out.println("\nCar's Daily Rate updated successfully.");
                default -> System.out.println("\nError. Please try again.");
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void customerRentalHistory(Scanner input) {
        System.out.print("\nCustomer CPF: ");
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
        System.out.print("\nCar Plate: ");
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
        System.out.print("\nRental ID: ");
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
        System.out.print("\nSearching rental by initial date.\nInitial date: ");
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
        System.out.print("\nSearching rentals by return date.\nReturn date: ");
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
        System.out.print("\nSearching rentals by status.\nStatus ('active', 'completed', 'reserved', 'canceled'): ");
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
            System.out.println("\nAverage Rental Price in our company: %.2f".formatted(avgRentalPrice));
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalCount() {
        try {
            Long rentalCount = service.rentalCount();
            System.out.println("\nTotal number of rentals: " + rentalCount);
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void rentalsByCpf(Scanner input) {
        System.out.print("\nSearching rentals by CPF.\nCustomer CPF: ");
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
        System.out.print("\nSearching Rentals by a customer's name.\nFull or partial name: ");
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
            System.out.print("\nRentals ordered by price: ");
            List<Rental> rentalsByPrice = service.rentalsByPrice();
            rentalsByPrice.forEach(System.out::print);
            System.out.println();
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void countRentalsByStatus(Scanner input) {
        System.out.print("\nNumber of rental by status.\nStatus ('active', 'completed, 'reserved, 'canceled'): ");
        String status = validation.getStatus(input);
        try {
            long count = service.countRentalsByStatus(status);
            System.out.println("\nThere are " + count + " " + status.toUpperCase() + " rentals in our system.");
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void completeRental(Scanner input) {
        System.out.print("\nCompleting a rental and returning the cars.\nRental ID: ");
        int rentalId = validation.getRentalId(input);
        try {
            int success = service.completeRental(rentalId);
            switch (success) {
                case -1 -> System.out.println("\nRental not found.");
                case -2 -> System.out.println("\nRental not ACTIVE. Can`t be canceled.");
                case -3 -> System.out.println("""
                        Today is not the Rental's return date.
                        Please change the Rental's return date and adjust the total price before confirming its completion.""");
                case 1 -> System.out.println("\nRental completed and all cars returned.");
                default -> System.out.println("\nError. Please try again.");
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void changeReturnDate(Scanner input) {
        System.out.print("\nChanging a Rental's return date.\nRental ID: ");
        int rentalId = validation.getRentalId(input);
        System.out.print("New return date: ");
        LocalDate newReturnDate = validation.getReturnDate(input);
        try {
            int success = service.changeReturnDate(rentalId, newReturnDate);
            switch (success) {
                case -1 -> System.out.println("\nError. Rental not found.");
                case -2 -> System.out.println("\nError. Can't change return dates of COMPLETED or CANCELED Rentals.");
                case -3 -> System.out.println("\nError. Same return date as the Rental currently has.");
                default -> System.out.println("\nNew return date successfully changed.\n" +
                        "Check adjusted price on the rental ID: " + success);
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void avgRentalPriceByCustomer(Scanner input) {
        System.out.print("\nAverage rental price by customer.\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        try {
            BigDecimal avg = service.avgRentalPriceByCustomer(cpf);
            if (avg == null) {
                System.out.println("\nCustomer not found.");
                return;
            }
            System.out.println("\nAverage rental price by this customer: %.2f".formatted(avg));
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void avgRentalDays() {
        try {
            Double avgRentalDays = service.avgRentalDays();
            System.out.println("\nAverage rental days in our company: %.1f".formatted(avgRentalDays));
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void avgRentalDaysByCustomer(Scanner input) {
        System.out.print("\nAverage rental days by customer.\nCustomer CPF: ");
        String cpf = validation.getCpf(input);
        try {
            Double avgRentalDays = service.avgRentalDaysByCustomer(cpf);
            if (avgRentalDays == -1.0) {
                System.out.println("\nCustomer not found.");
                return;
            }
            else if (avgRentalDays == 0.0) {
                System.out.println("\nCustomer has never rented with us.");
                return;
            } else {
                System.out.println("\nAverage rental days by this customer: %.1f".formatted(avgRentalDays));
                return;
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError: " + exception.getMessage());
            System.out.println("Please try again.");
        }
    }

    public void initialMenu() {
        System.out.println("""
                ====================================
                       CAR RENTAL MANAGEMENT
                ====================================
                
                1. Car Management
                2. Customer Management
                3. Rental Management
                4. Rental Reports & Statistics
                0. Exit""");
        System.out.print("\nChoose an option: ");
    }

    public void carMenu() {
        System.out.println("""
                ====================================
                           CAR MANAGEMENT
                ====================================
    
                1. Register Car
                2. Search Car
                3. Car Rental History
                4. Register New Feature
                5. Update a Car's Daily Rate
                0. Back""");
        System.out.print("\nChoose an option: ");
    }

    public void customerMenu() {
        System.out.println("""
                ====================================
                        CUSTOMER MANAGEMENT
                ====================================
                
                1. Register Customer
                2. Search Customer
                3. Customer Rental History
                4. Update Customer's Email
                5. Update Customer's Phone Number
                0. Back""");
        System.out.print("\nChoose an option: ");
    }

    public void rentalMenu() {
        System.out.println("""
                ====================================
                          RENTAL MANAGEMENT
                ====================================
                
                1. Register Rental
                2. Search Rental
                3. Cancel Rental
                4. Complete Rental
                5. Change Return Date
                0. Back""");
        System.out.print("\nChoose an option: ");
    }

    public void rentalReportsMenu() {
        System.out.println("""
                ====================================
                       RENTAL REPORTS & STATS
                ====================================
                
                1. Search Rentals
                2. Rental Statistics
                3. Customer Statistics
                0. Back""");
        System.out.print("\nChoose an option: ");
    }

    public void searchRentalsMenu() {
        System.out.println("""
                ====================================
                           SEARCH RENTALS
                ====================================
                
                1. Search by Rental ID
                2. Search by Date
                3. Search by Initial Date
                4. Search by Return Date
                5. Search by Status
                6. Search by Customer CPF
                7. Search by Customer Name
                8. List Rentals by Price
                0. Back
                """);
        System.out.print("\nChoose an option: ");
    }

    public void rentalsStatisticsMenu() {
        System.out.println("""
                ====================================
                         RENTAL STATISTICS
                ====================================
                
                1. Total Number of Rentals
                2. Average Rental Price
                3. Average Rental Duration
                4. Count Rentals by Status
                0. Back
                """);
        System.out.print("\nChoose an option: ");
    }

    public void customerStatisticsMenu() {
        System.out.println("""
                ====================================
                        CUSTOMER STATISTICS
                ====================================
                
                1. Average Rental Price by Customer
                2. Average Rental Duration by Customer
                0. Back""");
        System.out.print("\nChoose an option: ");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Main main = new Main();

        boolean exit = false;
        while (!exit) {
            main.initialMenu();
            int firstMenu = validation.getMenuOptions(input);
            switch (firstMenu) {
                case 1 -> {
                    int carMenu = -1;
                    while (carMenu != 0) {
                        main.carMenu();
                        carMenu = validation.getMenuOptions(input);

                        switch (carMenu) {
                            case 1 -> main.registerCar(input);
                            case 2 -> main.searchCar(input);
                            case 3 -> main.carRentalHistory(input);
                            case 4 -> main.registerFeature(input);
                            case 5 -> main.updateDailyRate(input);
                            case 0 -> System.out.println("\nReturning to the previous menu.");
                            default -> System.out.print("\nInvalid option.");
                        }
                    }
                }
                case 2 -> {
                    int customerMenu = -1;
                    while (customerMenu != 0) {
                        main.customerMenu();
                        customerMenu = validation.getMenuOptions(input);
                        switch (customerMenu) {
                            case 1 -> main.registerCustomer(input);
                            case 2 -> main.searchCustomer(input);
                            case 3 -> main.customerRentalHistory(input);
                            case 4 -> main.updateCustomerEmail(input);
                            case 5 -> main.updateCustomerPhone(input);
                            case 0 -> System.out.println("\nReturning to the previous menu.");
                            default -> System.out.print("\nInvalid option.");
                        }
                    }
                }
                case 3 -> {
                    int rentalMenu = -1;
                    while (rentalMenu != 0) {
                        main.rentalMenu();
                        rentalMenu = validation.getMenuOptions(input);
                        switch (rentalMenu) {
                            case 1 -> main.registerRental(input);
                            case 2 -> main.searchRental(input);
                            case 3 -> main.cancelRental(input);
                            case 4 -> main.completeRental(input);
                            case 5 -> main.changeReturnDate(input);
                            case 0 -> System.out.println("\nReturning to the previous menu.");
                            default -> System.out.print("\nInvalid option.");
                        }
                    }
                }
                case 4 -> {
                    int reportsMenu = -1;
                    while (reportsMenu != 0) {
                        main.rentalReportsMenu();
                        reportsMenu = validation.getMenuOptions(input);
                        switch (reportsMenu) {
                            case 1 -> {
                                int searchRentalsMenu = -1;
                                while (searchRentalsMenu != 0) {
                                    main.searchRentalsMenu();
                                    searchRentalsMenu = validation.getMenuOptions(input);
                                    switch (searchRentalsMenu) {
                                        case 1 -> main.searchRental(input);
                                        case 2 -> main.searchRentalByDate(input);
                                        case 3 -> main.rentalsByInitialDate(input);
                                        case 4 -> main.rentalsByReturnDate(input);
                                        case 5 -> main.rentalsByStatus(input);
                                        case 6 -> main.rentalsByCpf(input);
                                        case 7 -> main.rentalsByName(input);
                                        case 8 -> main.rentalsByPrice();
                                        case 0 -> System.out.println("\nReturning to the previous menu.");
                                        default -> System.out.print("\nInvalid option.");
                                    }
                                }
                            }
                            case 2 -> {
                                int rentalStatsMenu = -1;
                                while (rentalStatsMenu != 0) {
                                    main.rentalsStatisticsMenu();
                                    rentalStatsMenu = validation.getMenuOptions(input);
                                    switch (rentalStatsMenu) {
                                        case 1 -> main.rentalCount();
                                        case 2 -> main.avgRentalPrice();
                                        case 3 -> main.avgRentalDays();
                                        case 4 -> main.countRentalsByStatus(input);
                                        case 0 -> System.out.println("\nReturning to the previous menu.");
                                        default -> System.out.print("\nInvalid option.");
                                    }
                                }
                            }
                            case 3 -> {
                                int customerStatsMenu = -1;
                                while (customerStatsMenu != 0) {
                                    main.customerStatisticsMenu();
                                    customerStatsMenu = validation.getMenuOptions(input);
                                    switch (customerStatsMenu) {
                                        case 1 -> main.avgRentalPriceByCustomer(input);
                                        case 2 -> main.avgRentalDaysByCustomer(input);
                                        case 0 -> System.out.println("\nReturning to the previous menu.");
                                        default -> System.out.print("\nInvalid option.");
                                    }
                                }
                            }
                        }
                    }
                }
                case 0 -> exit = true;
            }
        }
        input.close();
        HibernateUtil.getSessionFactory().close();
    }
}