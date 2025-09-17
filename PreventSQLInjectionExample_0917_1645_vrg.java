// 代码生成时间: 2025-09-17 16:45:39
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PreventSQLInjectionExample.java
 * Provides an example of how to prevent SQL injection using Hibernate.
 */
public class PreventSQLInjectionExample {

    private static final Logger LOGGER = Logger.getLogger(PreventSQLInjectionExample.class.getName());
    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            LOGGER.log(Level.SEVERE, "Hibernate initialization error", ex);
        }
    }

    public static void main(String[] args) {
        try {
            // Start a new transaction
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            // Example of using HQL (Hibernate Query Language) to prevent SQL injection
            String name = "John Doe";
            String hql = "FROM User WHERE name = :name";
            List<User> users = session.createQuery(hql, User.class).setParameter("name", name).getResultList();

            // Iterate over the results to show that SQL injection is prevented
            for (User user : users) {
                System.out.println("User found: " + user.getName());
            }

            // Commit the transaction
            transaction.commit();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in preventing SQL injection", e);
        } finally {
            sessionFactory.close();
        }
    }
}

/**
 * User.java
 * Represents a user entity with a name attribute.
 */
class User {
    private Long id;
    private String name;

    // Getters and setters for id and name
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
