package org.example;

import org.hibernate.Session;
import java.util.List;

public class CarDAO {

    public void persistCar(Session session, Car car) {
        session.persist(car);
    }

    public int persistFeature(Session session, Feature feature) {
        session.persist(feature);
        return feature.getFeatureId();
    }

    public Car findCar(Session session, String plate) {
        return session.find(Car.class, plate);
    }
}
