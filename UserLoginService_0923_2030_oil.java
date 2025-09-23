// 代码生成时间: 2025-09-23 20:30:31
// UserLoginService.java
// A simple user login verification system using Java and Hibernate framework.
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.Properties;

// User class represents a user entity
class User {
    private int id;
    private String username;
    private String password;
    // Constructors, getters and setters
    public User() {}
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

// UserRepository interface for database operations
interface UserRepository {
    User findUserByUsernameAndPassword(String username, String password);
}

// UserRepositoryImpl class implements UserRepository interface
class UserRepositoryImpl implements UserRepository {
    private SessionFactory sessionFactory;
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            String hql = "from User where username = :username and password = :password";
            List<User> users = session.createQuery(hql)
                .setParameter("username", username)
                .setParameter("password", password)
                .list();
            if (users.size() > 0)
                return users.get(0);
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (transaction != null) transaction.rollback();
            session.close();
        }
    }
}

// UserLoginService class handles the login logic
public class UserLoginService {
    private UserRepository userRepository;
    public UserLoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean login(String username, String password) {
        User user = userRepository.findUserByUsernameAndPassword(username, password);
        if (user != null) {
            return true;
        } else {
            System.out.println("Login failed: Invalid username or password");
            return false;
        }
    }
}

// Configuration and usage example
public class Main {
    public static void main(String[] args) {
        // Hibernate configuration
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        UserRepository userRepository = new UserRepositoryImpl(sessionFactory);
        UserLoginService userLoginService = new UserLoginService(userRepository);
        
        // User login attempt
        boolean loginSuccess = userLoginService.login("testUser", "testPassword");
        if (loginSuccess) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }
    }
}
