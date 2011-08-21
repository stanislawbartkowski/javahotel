/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.db2.trusted;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.ibm.db2.jcc.DB2ConnectionPoolDataSource;
import com.ibm.db2.jcc.DB2PooledConnection;

public class TrustedConnection {

    private final int portNumber;
    private final String databaseName;
    private final String userName;
    private final String password;
    private final String hostName;
    private final String defaultSchema;
    private Object[] objects;
    private final DB2ConnectionPoolDataSource dataSource;

    /**
     * Constructor, provide all details necessary to estalish trusted connecton
     * @param portNumber  port number (most common is 50000)
     * @param databaseName e.g SAMPLE
     * @param userName e.g trust
     * @param password e.g trustme
     * @param hostName IP address or hostname
     * @param defaultSchema e.g DB2INST1 (important: case sensitive !)
     */
            
    public TrustedConnection(int portNumber, String databaseName,
            String userName, String password, String hostName,
            String defaultSchema) {
        this.portNumber = portNumber;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;
        this.hostName = hostName;
        this.defaultSchema = defaultSchema;
        dataSource = new DB2ConnectionPoolDataSource();
    }

    /**
     * Establishes trusted connection, should be called only once
     * @throws SQLException
     */
    public void trustConnect() throws SQLException {
        dataSource.setDatabaseName(databaseName);
        dataSource.setServerName(hostName);
        dataSource.setDriverType(4);
        dataSource.setPortNumber(portNumber);
        dataSource.setCurrentSchema(defaultSchema);
        // Call getDB2TrustedPooledConnection to get the trusted connection
        // instance and the cookie for the connection
        objects = dataSource.getDB2TrustedPooledConnection(userName, password,
                new java.util.Properties());
    }

    /**
     * Switch and reuse trusted connection as different user (without authentication)
     * @param newUser id of the new user
     * @return Connection (java.sqlx)
     * @throws SQLException
     */
    public Connection useConnection(String newUser) throws SQLException {
        DB2PooledConnection pooledCon = (DB2PooledConnection) objects[0];
        Properties properties = new Properties();
        byte[] cookie = (byte[]) (objects[1]);
        String userRegistry = null;
        byte[] userSecTkn = null;
        String originalUser = null;
        Connection con = pooledCon.getDB2Connection(cookie, newUser, null,
                userRegistry, userSecTkn, originalUser, properties);
        return con;
    }
    
    /**
     * For the purpose of JdbcTemplate. 
     * DataSource with getConnection method override
     * New user id as constructor parameter
     * @author sbartkowski
     *
     */
    private class TrustedDataSource implements DataSource {

        private final String newUser;

        public TrustedDataSource(String newUser) {
            this.newUser = newUser;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return dataSource.getLogWriter();
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {
            dataSource.setLogWriter(out);
        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {
            dataSource.setLoginTimeout(seconds);
        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return dataSource.getLoginTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public Connection getConnection() throws SQLException {
            return useConnection(newUser);
        }

        @Override
        public Connection getConnection(String username, String password)
                throws SQLException {
            return getConnection();
        }

    }

    public DataSource constructReuseDataSource(String newUser) {
        return new TrustedDataSource(newUser);
    }

}