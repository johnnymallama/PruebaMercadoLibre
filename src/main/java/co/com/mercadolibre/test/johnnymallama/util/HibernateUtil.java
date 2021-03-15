package co.com.mercadolibre.test.johnnymallama.util;

import org.hibernate.*;
import org.hibernate.boot.registry.*;
import org.hibernate.cfg.*;
import org.hibernate.service.*;

import co.com.mercadolibre.test.johnnymallama.entity.AdnHist;

public class HibernateUtil {

	private HibernateUtil() {

	}

	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {

		if (null != sessionFactory) {
			return sessionFactory;
		}
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		configuration.addAnnotatedClass(AdnHist.class);
		String jdbcUrl = "jdbc:mysql://" + System.getenv("RDS_HOSTNAME") + "/" + System.getenv("RDS_DB_NAME");
		configuration.setProperty("hibernate.connection.url", jdbcUrl);
		configuration.setProperty("hibernate.connection.username", System.getenv("RDS_USERNAME"));
		configuration.setProperty("hibernate.connection.password", System.getenv("RDS_PASSWORD"));
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		try {
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (HibernateException e) {
			throw new ExceptionInInitializerError(e);
		}
		return sessionFactory;
	}
}
