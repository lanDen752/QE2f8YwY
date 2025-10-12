// 代码生成时间: 2025-10-12 20:17:08
package com.tokengovernance;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.UUID;

public class TokenGovernanceSystem {
    private SessionFactory sessionFactory;

    // Constructor to create a SessionFactory
    public TokenGovernanceSystem() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    // Method to create a new token
    public Token createToken(String tokenName, int tokenValue) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Token token = new Token(UUID.randomUUID().toString(), tokenName, tokenValue);
            session.save(token);
            tx.commit();
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to transfer tokens from one account to another
    public boolean transferTokens(String fromAccountId, String toAccountId, int amount) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Account fromAccount = session.get(Account.class, fromAccountId);
            Account toAccount = session.get(Account.class, toAccountId);
            if (fromAccount == null || toAccount == null) {
                return false;
            }
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            session.update(fromAccount);
            session.update(toAccount);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to query tokens by account ID
    public List<Token> getTokensByAccountId(String accountId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Token where account_id = :accountId", Token.class)
                    .setParameter("accountId", accountId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utility method to close the SessionFactory
    public void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}

/*
 * Token.java
 * 
 * This class represents a token entity with name, value, and account ID.
 */
class Token {
    private String id;
    private String name;
    private int value;
    private String accountId;

    public Token(String id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.accountId = "";
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
}

/*
 * Account.java
 * 
 * This class represents an account entity with account ID and balance.
 */
class Account {
    private String id;
    private int balance;

    public Account(String id) {
        this.id = id;
        this.balance = 0;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }
}
