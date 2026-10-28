package org.example;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

public class HibernateUtil {

    private static SessionFactory buildSessionFactory() {

        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");

        return new Configuration()
                .configure("hibernate.cfg.xml")
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password)
                .buildSessionFactory();
    }

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }


}
