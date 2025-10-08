// 代码生成时间: 2025-10-09 02:52:26
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

// SensorData entity class representing the sensor data
class SensorData {
    private long id;
    private String sensorId;
    private double temperature;
    private double humidity;
    private long timestamp;
    // Getters and setters for the fields
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}

public class SensorDataCollection {
    // Factory for creating Hibernate session
    private static final SessionFactory sessionFactory = buildSessionFactory();

    // Build a SessionFactory from hibernate.cfg.xml
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + "Exception: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Method to add sensor data to the database
    public SensorData addSensorData(String sensorId, double temperature, double humidity) {
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(sensorId);
        sensorData.setTemperature(temperature);
        sensorData.setHumidity(humidity);
        sensorData.setTimestamp(System.currentTimeMillis());
        return saveSensorData(sensorData);
    }

    // Save or update sensor data
    private SensorData saveSensorData(SensorData sensorData) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(sensorData);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
        return sensorData;
    }

    // Method to retrieve all sensor data
    public List<SensorData> getAllSensorData() {
        List<SensorData> sensorDataList = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query<SensorData> query = session.createQuery("FROM SensorData", SensorData.class);
            sensorDataList = query.getResultList();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
        return sensorDataList;
    }

    // Main method for demonstration purposes
    public static void main(String[] args) {
        SensorDataCollection collection = new SensorDataCollection();
        SensorData newSensorData = collection.addSensorData("sensor123", 25.5, 60.2);
        System.out.println("New Sensor Data Added: " + newSensorData);

        List<SensorData> allSensorData = collection.getAllSensorData();
        System.out.println("All Sensor Data: ");
        for (SensorData data : allSensorData) {
            System.out.println("Sensor ID: " + data.getSensorId() + ", Temperature: " + data.getTemperature() + ", Humidity: " + data.getHumidity());
        }
    }
}