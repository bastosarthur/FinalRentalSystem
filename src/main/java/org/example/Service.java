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
            List<Car> carList = new ArrayList<>();
            BigDecimal sumPrice = BigDecimal.ZERO;
            for (String carPlate : carPlates) {
                Car car = carDAO.findCar(session, carPlate);
                if (car == null) {
                    return -2; // CAR(s) NOT FOUND;
                }
                carList.add(car);
                sumPrice = sumPrice.add(car.getDailyRate());
            }
            long totalDays = ChronoUnit.DAYS.between(initialDate, returnDate);
            BigDecimal totalPrice = sumPrice.multiply(BigDecimal.valueOf(totalDays));
            LocalDate currentDate = LocalDate.now();
            if (initialDate.isBefore(currentDate)) {
                return -3; // INVALID RENTAL DATE;
            }
            String status;
            if (initialDate.isEqual(LocalDate.now())) {
                status = "active";
            } else {
                status = "reserved";
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
}
