package com.javalive.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import com.javalive.entity.Account;
import com.javalive.entity.CreditAccount;
import com.javalive.entity.DebitAccount;

/**
 * MappedSuperclass - Inheritance is implemented in the domain model only
 * without reflecting it in the database schema. In this strategy, the parent
 * classes can’t be entities.
 * 
 * Only two tables get created viz. DebitAccount and CreditAccount. No table for Account class.
 * 
 * This approach is rarely used as it not support polymorphic queries. 
 */
public class HibernateUtil {

	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

				Map<String, Object> settings = new HashMap<>();
				settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
				settings.put(Environment.URL, "jdbc:mysql://localhost:3306/test?useSSL=false");
				settings.put(Environment.USER, "root");
				settings.put(Environment.PASS, "root");
				settings.put(Environment.HBM2DDL_AUTO, "create");
				settings.put(Environment.SHOW_SQL, "true");

				registryBuilder.applySettings(settings);
				registry = registryBuilder.build();

				MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Account.class)
						.addAnnotatedClass(CreditAccount.class).addAnnotatedClass(DebitAccount.class);

				Metadata metadata = sources.getMetadataBuilder().build();

				sessionFactory = metadata.getSessionFactoryBuilder().build();
			} catch (Exception e) {
				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
				}
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}
