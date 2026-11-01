package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class Service {

    CarDAO carDAO = new CarDAO();
    CustomerDAO customerDAO = new CustomerDAO();
    RentalDAO rentalDAO = new RentalDAO();

    public int registerCar(Car car, List<Integer> carFeaturesId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Car tryCar = carDAO.findCar(session, car.getPlate());
            if (tryCar != null) {
                return -1; // CAR ALREADY REGISTERED.
            }
            List<Feature> carFeatures = new ArrayList<>();
            for (Integer featureId : carFeaturesId) {
                Feature tryFeature = carDAO.findFeature(session, featureId);
                if (tryFeature != null) {
                    carFeatures.add(tryFeature);
                }
            }
            Transaction transaction = session.beginTransaction();
            try {
                car.setFeatureList(carFeatures);
                carDAO.persistCar(session, car);
                transaction.commit();
                return 1; // SUCCESS
            } catch (org.hibernate.exception.ConstraintViolationException exception) {
                if ("valid_brands".equalsIgnoreCase(exception.getConstraintName())) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                    throw exception;
                }
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public int registerCustomer(Customer customer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                customerDAO.persistCustomer(session, customer);
                transaction.commit();
                return 1;
            } catch (org.hibernate.exception.ConstraintViolationException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                if ("customers_pkey".equalsIgnoreCase(exception.getConstraintName())) {
                    return -1; // CPF ALREADY REGISTERED
                }
                if ("unique_email".equalsIgnoreCase(exception.getConstraintName())) {
                    return -2; // EMAIL ALREADY REGISTERED
                }
                if ("unique_phone".equalsIgnoreCase(exception.getConstraintName())) {
                    return -3; // PHONE ALREADY REGISTERED
                }

                throw exception;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public Customer searchCustomer(String cpf) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return customerDAO.findCustomer(session, cpf);
        }
    }

    public int persistFeature(Feature feature) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Feature tryFeature = carDAO.findFeatureByName(session, feature.getFeature());
            if (tryFeature != null) {
                return -1; // FEATURE ALREADY REGISTERED.
            }
            Transaction transaction = session.beginTransaction();
            try {
                int featureId = carDAO.persistFeature(session, feature);
                transaction.commit();
                return featureId;
            } catch (org.hibernate.exception.ConstraintViolationException exception) {
                if ("unique_feature".equalsIgnoreCase(exception.getConstraintName())) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                        return -1; // FEATURE ALREADY REGISTERED.
                    }
                }
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public int registerRental(String cpf, List<String> carPlates, LocalDate initialDate, LocalDate returnDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Customer customer = customerDAO.findCustomer(session, cpf);
            if (customer == null) {
                return -1; // CUSTOMER NOT FOUND
            }
            if (customer.getActiveRental() != null) {
                return -5; // CUSTOMER ALREADY HAS AN ACTIVE RENTAL
            }
            List<Car> carList = new ArrayList<>();
            BigDecimal sumPrice = BigDecimal.ZERO;
            for (String carPlate : carPlates) {
                Car car = carDAO.findCar(session, carPlate);
                if (car == null) {
                    return -2; // CAR(s) NOT FOUND;
                }
                if (car.getActiveRental() != null) {
                    return -6; // CAR ALREADY HAS AN ACTIVE RENTAL.
                }
                carList.add(car);
                sumPrice = sumPrice.add(car.getDailyRate());
            }
            long billableDays = ChronoUnit.DAYS.between(initialDate, returnDate);
            billableDays++;
            BigDecimal totalPrice = sumPrice.multiply(BigDecimal.valueOf(billableDays));
            LocalDate currentDate = LocalDate.now();
            if (initialDate.isBefore(currentDate)) {
                return -3; // INVALID RENTAL DATE;
            }
            String status;
            if (initialDate.isEqual(LocalDate.now())) {
                status = "ACTIVE";
            } else {
                status = "RESERVED";
            }
            Rental rental = new Rental(customer, carList, initialDate, returnDate, totalPrice, status);
            Transaction transaction = session.beginTransaction();
            try {
                int rentalId = rentalDAO.persistRental(session, rental);
                session.flush();
                customer.setActiveRental(rental);
                for (Car car : carList) {
                    car.setActiveRental(rental);
                }
                session.flush();
                transaction.commit();
                return rentalId;
            } catch (org.hibernate.exception.ConstraintViolationException exception) {
                if ("valid_rental_period".equalsIgnoreCase(exception.getConstraintName())) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                    return -3; // INVALID RENTAL DATE;
                }
                if ("valid_status".equalsIgnoreCase(exception.getConstraintName())) {
                    if (transaction.isActive()) {
                        transaction.rollback();
                    }
                    return -4; // INVALID STATUS
                }
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public int cancelRental(int rentalId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Rental rental = rentalDAO.findRental(session, rentalId);
            if (rental == null) {
                return -1; // RENTAL NOT FOUND
            }
            if (rental.getStatus().equalsIgnoreCase("CANCELED")) {
                return -2; // RENTAL ALREADY CANCELED.
            }
            if (rental.getStatus().equalsIgnoreCase("COMPLETED")) {
                return -3; // RENTAL ALREADY COMPLETED, CANT BE CANCELED.
            }
            Transaction transaction = session.beginTransaction();
            try {
                Customer customer = rental.getCustomer();
                customer.setActiveRental(null);
                for (Car car : rental.getCarList()) {
                    car.setActiveRental(null);
                }
                rental.changeStatus("CANCELED");
                transaction.commit();
                return 1; // SUCCESS
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public int completeRental(int rentalId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Rental rental = rentalDAO.findRental(session, rentalId);
            if (rental == null) {
                return -1; // RENTAL NOT FOUND;
            }
            if (!rental.getStatus().equalsIgnoreCase("ACTIVE")) {
                return -2; // RENTAL NOT ACTIVE, MUST BE ACTIVE RENTAL TO BE COMPLETED.
            }
            if (!rental.getReturnDate().equals(LocalDate.now())) {
                return -3; // NOT THE RENTAL RETURN DATE, PLEASE CHANGE THE RETURN DATE AND ADJUST PRICE BEFORE COMPLETING
            }
            Transaction transaction = session.beginTransaction();
            try {
                Customer customer = rental.getCustomer();
                List<Car> carList = rental.getCarList();
                customer.setActiveRental(null);
                for (Car car : carList) {
                    car.setActiveRental(null);
                }
                rental.changeStatus("COMPLETED");
                session.flush();
                transaction.commit();
                return 1;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public Car searchCar(String plate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return carDAO.findCar(session, plate);
        }
    }

    public Rental searchRental(int rentalId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.findRental(session, rentalId);
        }
    }

    public List<Rental> rentalByDate(LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.rentalByDate(session, date);
        }
    }

    public List<Rental> rentalByInitialDate(LocalDate initialDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.rentalByInitialDate(session, initialDate);
        }
    }

    public List<Rental> rentalByReturnDate(LocalDate returnDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.rentalByReturnDate(session, returnDate);
        }
    }


    public List<Rental> rentalsByStatus(String status) {
        if (!status.equalsIgnoreCase("active") && !status.equalsIgnoreCase("canceled")
        && !status.equalsIgnoreCase("reserved") && !status.equalsIgnoreCase("completed")) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.rentalByStatus(session, status);
        }
    }

    public Double avgRentalPrice() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.avgRentalPrice(session);
        }
    }

    public Long rentalCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.rentalCount(session);
        }
    }

    public List<Rental> rentalsByCpf(String cpf) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.findRentalsByCpf(session, cpf);
        }
    }

    public List<Rental> rentalsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.findRentalsByName(session, name);
        }
    }

    public List<Rental> rentalsByPrice() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.rentalsByPrice(session);
        }
    }

    public Long countRentalsByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return rentalDAO.countRentalByStatus(session, status);
        }
    }

    public int changeReturnDate(int rentalId, LocalDate newDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Rental rental = rentalDAO.findRental(session, rentalId);
            if (rental == null) {
                return -1; // RENTAL NOT FOUND
            }
            if (rental.getStatus().equalsIgnoreCase("CANCELED") ||
                    rental.getStatus().equalsIgnoreCase("COMPLETED")) {
                return -2; // CANT CHANGE RETURN DATE OF CANCELED OF COMPLETED RENTALS;
            }
            if (rental.getReturnDate().isEqual(newDate)) {
                return -3; // SAME RETURN DATES;
            }
            Transaction transaction = session.beginTransaction();
            try {
                rental.changeReturnDate(newDate);
                session.flush();
                List<Car> carList = rental.getCarList();
                BigDecimal newPrice = BigDecimal.ZERO;
                for (Car car : carList) {
                    newPrice = newPrice.add(car.getDailyRate());
                }
                long newBillableDays = rental.getTotalDays();
                newBillableDays++;
                BigDecimal newTotalPrice = newPrice.multiply(BigDecimal.valueOf(newBillableDays));
                rental.changeTotalPrice(newTotalPrice);
                session.flush();
                transaction.commit();
                return rental.getRentalId();
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }














}
