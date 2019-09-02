package com.novatronic.valorados.services.db;

import java.sql.Connection;
import java.sql.SQLException;
import novatronic.com.encripter.Encripter;
import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.apache.log4j.Logger;
import com.novatronic.valorados.services.util.LoggerHelper;

public class NovaOracleConnectionPool {

    private final static Logger logger = LoggerHelper.getLogger();
    private static PoolDataSource pds;
    private static UniversalConnectionPoolManager mgr;
    private static String connectionPoolName = "NovaImporterConnectionPool";
    String frase = "dummy";

    private NovaOracleConnectionPool() throws SQLException {
        Encripter encr = new Encripter(frase);
        pds = PoolDataSourceFactory.getPoolDataSource();
        pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        pds.setURL(NovaOracleConnectionInfo.URL);
        pds.setUser(NovaOracleConnectionInfo.USER);
        String psw = NovaOracleConnectionInfo.PASSWORD;
        System.out.println("password:" + psw);
        psw = encr.decrypt(psw);

        pds.setPassword(psw);
        pds.setConnectionPoolName(connectionPoolName);
        pds.setInitialPoolSize(0);
        pds.setMinPoolSize(0);
        pds.setMaxPoolSize(20);
        pds.setMaxConnectionReuseCount(300);
        pds.setMaxConnectionReuseCount(100);
        pds.setConnectionWaitTimeout(10);
        pds.setTimeToLiveConnectionTimeout(20);
        pds.setValidateConnectionOnBorrow(true);
        if (mgr == null) {
            try {
                mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
            } catch (UniversalConnectionPoolException e) {
                e.printStackTrace();
                throw new SQLException("No se pudo crear el ConnectionPoolManager", e);
            }
        }
        try {
            if (!yaExistePool()) {
                mgr.createConnectionPool((UniversalConnectionPoolAdapter) pds);
            }
        } catch (UniversalConnectionPoolException e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }

    private static boolean yaExistePool() {
        try {
            String[] names = mgr.getConnectionPoolNames();

            for (String name : names) {
                if (connectionPoolName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Connection getConnection() throws SQLException {
        if (pds == null) {
            new NovaOracleConnectionPool();
        }
        return pds.getConnection();
    }

    public static void reset() {
        try {
            if (mgr != null) {
                if (yaExistePool()) {
                    mgr.destroyConnectionPool(connectionPoolName);
                }
            }
        } catch (UniversalConnectionPoolException e) {
            e.printStackTrace();
        }
        pds = null;
    }
}
