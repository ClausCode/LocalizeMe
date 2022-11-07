package com.clauscode.localization.store;

import com.clauscode.localization.Cache;
import com.clauscode.localization.Localization;
import com.clauscode.localization.factory.Factory;
import com.clauscode.localization.factory.DefaultLocalizationFactory;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteLocalizationStorage implements Storage {
    private final Cache<String, String> cache = new Cache<>(1000);
    private final SessionFactory sessionFactory;

    private final String url;
    private final String username;
    private final String password;

    public RemoteLocalizationStorage(String driverClass, String dialectClass, String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        initLiquibase();

        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        sessionFactory = new Configuration()
                .addAnnotatedClass(Localization.class)
                .setProperty("hibernate.connection.driver_class", driverClass)
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password)
                .setProperty("hibernate.dialect", dialectClass)
                .setProperty("hibernate.show_sql", "false")
                .setProperty("hibernate.current_session_context_class", "thread")
                .buildSessionFactory();
    }

    @Override
    public Factory buildFactory(String language) {
        return new DefaultLocalizationFactory(this, language);
    }

    public String getSingle(String identifier, String language) {
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

            session.createQuery("select l from Localization l where (" + idWhere + ") and l.language = :language", Localization.class)
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
    private void initLiquibase() {
        Liquibase liquibase;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            liquibase = new Liquibase("liquibase/db.changelog-init.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.setChangeLogParameter("liquibase-logLevel", "OFF");
            liquibase.setChangeLogParameter("liquibase.sql.logLevel", "OFF");
            liquibase.setChangeLogParameter("liquibase.searchPath", "/");
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
