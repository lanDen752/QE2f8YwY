// 代码生成时间: 2025-10-07 03:32:24
package com.example.promotion;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import java.util.List;
import java.util.UUID;

// 营销活动实体
class PromotionActivity {
    private String id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;

    // 省略getter和setter方法
}

// 营销活动管理类
public class PromotionActivityManager {

    private SessionFactory sessionFactory;

    public PromotionActivityManager() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    // 添加营销活动
    public String addPromotionActivity(PromotionActivity activity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(activity);
                transaction.commit();
                return activity.getId();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new RuntimeException("Error adding promotion activity", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error opening session", e);
        }
    }

    // 更新营销活动
    public void updatePromotionActivity(PromotionActivity activity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(activity);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new RuntimeException("Error updating promotion activity", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error opening session", e);
        }
    }

    // 删除营销活动
    public void deletePromotionActivity(String id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                PromotionActivity activity = session.get(PromotionActivity.class, id);
                if (activity != null) {
                    session.delete(activity);
                    transaction.commit();
                }
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                throw new RuntimeException("Error deleting promotion activity", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error opening session", e);
        }
    }

    // 获取营销活动列表
    public List<PromotionActivity> getPromotionActivities() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM PromotionActivity", PromotionActivity.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving promotion activities", e);
        }
    }

    // 关闭SessionFactory
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
