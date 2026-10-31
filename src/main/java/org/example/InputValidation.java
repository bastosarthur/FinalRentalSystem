package org.example;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InputValidation {

    public String getCpf(Scanner input) {
        while (true) {
            String cpf = input.nextLine();
            if (cpf.matches("^[0-9]{11}$")) {
                return cpf;
            }
            System.out.print("Please type a 11-digit CPF number: ");
        }
    }

    public String getName(Scanner input) {
        while (true) {
            String name = input.nextLine();
            if (name.matches("^[A-Za-z]{3,15} [A-Za-z]{3,20}$")) {
                return name;
            }
            System.out.print("Please type your First and Last name: ");
        }
    }

    public String getEmail(Scanner input) {
        while (true) {
            String email = input.nextLine();
            if (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return email;
            }
            System.out.print("Please type a valid email address: ");
        }
    }

    public String getPhone(Scanner input) {
        while (true) {
            String phone = input.nextLine();
            if (phone.matches("^[0-9]{11}$")) {
                return phone;
            }
            System.out.print("Please type a 11-digit phone number (including area code): ");
        }
    }

    public LocalDate getInitialDate(Scanner input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        while (true) {
            try {
                String tryInitialDate = input.nextLine();
                LocalDate initialDate = LocalDate.parse(tryInitialDate, formatter);
                return initialDate;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please try again (MM/dd/yyyy):");
            }
        }
    }

    public LocalDate getReturnDate(Scanner input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        while (true) {
            try {
                String tryReturnDate = input.nextLine();
                LocalDate returnDate = LocalDate.parse(tryReturnDate, formatter);
                return returnDate;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please try again (MM/dd/yyyy):");
            }
        }
    }

    public String getPlate(Scanner input) {
        while (true) {
            String plate = input.nextLine();
            if (plate.matches("^[A-Z-a-z]{2}[0-9]{4}$")) {
                return plate.toUpperCase();
            }
            System.out.println("Invalid plate, format = (XX1234): ");
        }
    }

    public String getBrand(Scanner input) {
        while (true) {
            String brand = input.nextLine();
            if (brand.equalsIgnoreCase("mercedes") || brand.equalsIgnoreCase("bmw")
                    || brand.equalsIgnoreCase("volvo")) {
                return brand.toLowerCase();
            }
            System.out.print("\nWe only work with 'mercedes', 'bmw' and 'volvo': ");
        }

    }

    public String getModel(Scanner input) {
        while (true) {
            String model = input.nextLine();
            if (model.matches("^[A-Za-z, 0-9-]{2,20}$")) {
                return model;
            }
            System.out.print("Type a valid model from 2 to 20 digits: ");
        }
    }

    public BigDecimal getPrice(Scanner input) {
        while (true) {
            try {
                BigDecimal price = input.nextBigDecimal();
                input.nextLine();
                if (price.compareTo(BigDecimal.ZERO) > 0) {
                    return price;
                }
                System.out.print("Type a positive price: ");
            } catch (InputMismatchException e) {
                System.out.print("Type a positive price: ");
                input.nextLine();
            }
        }
    }

    public int getRentalId(Scanner input) {
        while (true) {
            try {
                int rentalId = input.nextInt();
                input.nextLine();
                if (rentalId >= 100) {
                    return rentalId;
                }
                System.out.print("Please type a valid rental 3-digit rental ID: ");
            } catch (InputMismatchException e) {
                System.out.print("Please type a valid rental 3-digit rental ID: ");
                input.nextLine();
            }
        }
    }

    public int getQtd(Scanner input) {
        while (true) {
            try {
                int qtd = input.nextInt();
                input.nextLine();
                if (qtd >= 0) {
                    return qtd;
                }
                System.out.print("Please type the numbers of Cars in this rental: ");
            } catch (InputMismatchException e) {
                System.out.print("Please type the numbers of Cars in this rental: ");
                input.nextLine();
            }
        }
    }

    public String getStatus(Scanner input) {
        while (true) {
            String status = input.nextLine();
            if (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("reserved")
                || status.equalsIgnoreCase("canceled")) {
                return status;
            }
            System.out.print("Please type a valid status (active, reserved or canceled): ");
        }
    }












}
