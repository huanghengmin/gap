package com.inetec.ichange.service.monitor.utils;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.avdy.p4j.jdbc.PersistenceConfig;
import com.avdy.p4j.jdbc.dbms.DataProviderFactoryImpl;
import com.avdy.p4j.jdbc.service.DataProvider;
import com.avdy.p4j.jdbc.service.DataProviderFactory;

public class DaoService {
    private static Logger logger = Logger.getLogger(DaoService.class);
    private static Properties config_p = new Properties();
    private static DataProvider dataProvider;
    private static DaoService service = new DaoService();

    private DaoService() {
        init();
    }

    public static DaoService getDaoService() {
        return service;
    }

    public void init() {
        try {
            config_p.load(DaoService.class.getResourceAsStream("/persistence.properties"));
        } catch (Exception e) {
            logger.error(e);
        }
        PersistenceConfig config = new PersistenceConfig(config_p);
        DataProviderFactory dataProviderFactory = new DataProviderFactoryImpl(config);
        String databaseName = config_p.getProperty("use.jdbc.database.name");
        String dbmsName = config_p.getProperty("use.jdbc.dbms");
        boolean isTransactional = false;
        try {
            dataProvider = dataProviderFactory.getDataProvider(databaseName,
                    dbmsName, isTransactional);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            logger.error(e);
        }
    }

    public static DataProvider getDataProvider() {
        return dataProvider;
    }

}
