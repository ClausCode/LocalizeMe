package com.clauscode.localization.store;

import com.clauscode.localization.Cache;
import com.clauscode.localization.Localization;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class LocalizeStoreImpl {
    private final Cache<String, String> cache = new Cache<>(1000);
    private final SessionFactory sessionFactory = new Configuration()
            .addAnnotatedClass(Localization.class)
            .buildSessionFactory();
    private static LocalizeStoreImpl instance;

    public static LocalizeStoreImpl getInstance() {
        if(instance == null) {
            instance = new LocalizeStoreImpl();
        }
        return instance;
    }

    private LocalizeStoreImpl() {
        initLiquibase();
    }

    public String get(String identifier, String language) {
        return getGroup(language, Collections.singletonList(identifier)).get(identifier);
    }

    public Map<String, String> getGroup(String language, List<String> identifiers) {
        Map<String, String> resultMap = new HashMap<>();
        List<String> queryList = new ArrayList<>();

        for(String identifier : identifiers) {
            String key = identifier + "_" + language;
            if (cache.containsKey(key)) {
                resultMap.put(identifier, cache.get(key));
                continue;
            }
            queryList.add(identifier);
        }

        if(queryList.isEmpty()) return resultMap;

        Session session = sessionFactory.getCurrentSession();

        StringBuilder idWhere = new StringBuilder();
        for(int index = 0; index < queryList.size(); index++) {
            String identifier = queryList.get(index);
            if(index < queryList.size() - 1) {
                idWhere.append("l.identifier = '").append(identifier).append("' or ");
            } else idWhere.append("l.identifier = '").append(identifier).append("'");
        }

        try {
            session.beginTransaction();

            session.createQuery("select l from Localization l where " + idWhere + " and l.language = :language", Localization.class)
                    .setParameter("language", language)
                    .getResultList().forEach((obj) -> {
                        String identifier = obj.getIdentifier();
                        String value = obj.getValue();

                        resultMap.put(identifier, value);

                        String key = identifier + "_" + language;
                        cache.put(key, value);
                    });
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            session.getTransaction().commit();
        }

        return resultMap;
    }

    private static void initLiquibase() {
        Liquibase liquibase = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/catalyst_db",
                    "root", "root"
            );

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            liquibase = new Liquibase("liquibase/db.changelog-init.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
