// 代码生成时间: 2025-10-09 23:31:51
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.Scanner;

// 定义图表数据实体类
class ChartData {
    private Long id;
    private String label;
    private double value;

    // 省略构造函数、getter和setter方法
}

// 定义图表生成器类
public class InteractiveChartGenerator {
    private SessionFactory sessionFactory;

    public InteractiveChartGenerator() {
        // 配置Hibernate并创建SessionFactory
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public void generateChart() {
        Scanner scanner = new Scanner(System.in);
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // 创建图表数据
            ChartData chartData = new ChartData();
            System.out.println(