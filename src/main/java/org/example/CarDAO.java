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

    public Feature findFeature(Session session, int featureId) {
        return session.find(Feature.class, featureId);
    }

    public Feature findFeatureByName(Session session, String featureName) {
        return session.createQuery("""
                    FROM Feature
                    WHERE LOWER(feature) = :featureName
                    """, Feature.class)
                    .setParameter("featureName", featureName.toLowerCase())
                    .getSingleResultOrNull();
    }

}
