// 代码生成时间: 2025-10-04 20:59:17
package com.example.deviceupdate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.Properties;
import java.io.Serializable;

public class FirmwareUpdateService {

    private static SessionFactory sessionFactory;

    // Initialize the session factory
    static {
        try {
            // Create the session factory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + "Exception: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Get the session factory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Close the session factory
    public static void shutdown() {
        getSessionFactory().close();
    }

    // Update firmware for a specific device
    public void updateFirmware(String deviceId, String firmwareVersion) {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open a session
            session = getSessionFactory().openSession();
            
            // Begin a transaction
            transaction = session.beginTransaction();
            
            // Fetch the device entity
            Device device = session.get(Device.class, deviceId);
            
            if (device == null) {
                throw new IllegalArgumentException("Device with ID: " + deviceId + " not found.");
            }

            // Update firmware version
            device.setFirmwareVersion(firmwareVersion);
            
            // Save changes
            session.saveOrUpdate(device);
            
            // Commit the transaction
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

/**
 * Device.java
 *
 * Entity class representing a device.
 */
package com.example.deviceupdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "devices")
public class Device implements Serializable {

    @Id
    private String id;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    // Standard getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }
}
