package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Service {

    CarDAO carDAO = new CarDAO();

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
}
