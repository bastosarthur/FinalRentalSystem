package org.example;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "features", schema = "final_rental")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feature_id")
    private int featureId;

    @Column(name = "feature", unique = true, updatable = true)
    private String feature;

    @ManyToMany(mappedBy = "carFeatures")
    List<Car> carsWithFeatureList;

    protected Feature() {}

    public Feature(String feature) {
        this.feature = feature;
    }

    @Override
        public String toString() {
            return "\nFeature ID: " + featureId +
                    " / " + feature.toUpperCase();
    }

    public int getFeatureId() {
        return featureId;
    }

    public String getFeature() {
        return feature;
    }
}
