/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itext.pdf.dao;

import com.itext.pdf.properties.JdbcProperties;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author emmanuel.idoko
 */
public class DaoConnection {

    private static Logger logger = LoggerFactory.getLogger(DaoConnection.class);
    private java.sql.Connection connection;
    private final String REPORT_HOST = "report.host";
    private final String REPORT_PORT = "report.port";
    private final String REPORT_DB = "report.db";
    private final String REPORT_USERNAME = "report.username";
    private final String REPORT_PASSWORD = "report.password";
    private final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    //private final Properties prop;

    public DaoConnection() throws Exception {
        // this.prop = JdbcProperties.getInstance();
    }

    /**
     * Opens a connection to the database. Configuration parameters are loaded
     * from the jdbc configuration properties file.
     *
     * @return
     * @throws ClassNotFoundException if driver class not found (in this case,
     * microsoft)
     * @throws SQLException
     */
//    public java.sql.Connection prepareSqlServerConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        String connectionURL = "jdbc:sqlserver://" + prop.getProperty(REPORT_HOST) + ":" + prop.getProperty(REPORT_PORT)
//                + ";databaseName=" + prop.getProperty(REPORT_DB);
//        logger.info("Attempting to establish connection to: [{}]", connectionURL);
//        connection = DriverManager.getConnection(connectionURL, prop.getProperty(REPORT_USERNAME), prop.getProperty(REPORT_PASSWORD));
//        logger.info("Connected to: [{}]", connectionURL);
//        System.out.println("Connected to: " + connectionURL);
//        return connection;
//    }
    /**
     * Opens a connection to the database. Configuration parameters are loaded
     * from the jdbc configuration properties file.
     *
     * @return
     * @throws ClassNotFoundException if driver class not found (in this case,
     * microsoft)
     * @throws SQLException
     */
    public java.sql.Connection SqlServerConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionURL = "jdbc:sqlserver://13.90.255.165:1433;databaseName=radical";
        logger.info("Attempting to establish connection to: [{}]", connectionURL);
        connection = DriverManager.getConnection(connectionURL, "sa", "Apl@$app");
        logger.info("Connected to: [{}]", connectionURL);
        System.out.println("Connected to: " + connectionURL);
        return connection;
    }

    /**
     * Runs the provided sql script.
     *
     * @param sqlStatement the sql statement script
     * @param parameters the parameters to feed into the scripts
     * @return the result list, containing two items: (1) the headers, (2) the
     * data
     * @throws SQLException
     * @throws Exception
     */
    public List runStatement(String sqlStatement, Object... parameters) throws SQLException, Exception {
        if (parameters.length != countParameterBlocksInStatement(sqlStatement)) {
            logger.info("The parameter blocks in the statement does not match the number of parameters passed");
            throw new Exception("The parameter blocks in the statement does not match the number of parameters passed");
        }

        PreparedStatement statement = connection.prepareStatement(sqlStatement);
        statement = insertParameters(statement, parameters);
        ResultSet rs = statement.executeQuery();

        List resultList = extractDataFromResultset(rs);
        statement.close();

        return resultList;
    }

    /**
     * A standard sql escape syntax to runs stored procedure without parameter
     * [this is peculiar for script that includes DML, DDL or/and DCL]
     *
     * @param sqlStatement the sql statement script
     * @param parameters the parameters to feed into the scripts
     * @return the result list, containing two items: (1) the headers, (2) the
     * data
     * @throws SQLException
     * @throws Exception
     */
    public List runNonParameterizedProcedureStatement(String sqlStatement, Object... parameters) throws SQLException, Exception {
        if (parameters.length != countParameterBlocksInStatement(sqlStatement)) {
            logger.info("The parameter blocks in the statement does not match the number of parameters passed");
            throw new Exception("The parameter blocks in the statement does not match the number of parameters passed");
        }

        logger.info("the sql escape syntax eventually sent to server is", sqlStatement);
        CallableStatement statement = connection.prepareCall(sqlStatement);
        statement = insertProcedureParameters(statement, parameters);
        ResultSet rs = null;
        if (statement.execute()) {
            rs = statement.getResultSet();
        } else {
            logger.info("No record is found is the resultset object...");
        }

        List resultList = extractDataFromResultset(rs);
        return resultList;
    }

    /**
     * A standard sql escape syntax to runs stored procedure without parameter
     * [this is peculiar for script that includes DML, DDL or/and DCL]
     *
     * @param sqlStatement the sql statement script
     * @param parameters the parameters to feed into the scripts
     * @return the result list, containing two items: (1) the headers, (2) the
     * data
     * @throws SQLException
     * @throws Exception
     */
    public ResultSet runNonParameterizedProcedureStatement_ResultSet(String sqlStatement, Object... parameters) throws SQLException, Exception {
        if (parameters.length != countParameterBlocksInStatement(sqlStatement)) {
            logger.info("The parameter blocks in the statement does not match the number of parameters passed");
            throw new Exception("The parameter blocks in the statement does not match the number of parameters passed");
        }

        logger.info("the sql escape syntax eventually sent to server is", sqlStatement);
        CallableStatement statement = connection.prepareCall(sqlStatement);
        statement = insertProcedureParameters(statement, parameters);
        ResultSet rs = null;
        if (statement.execute()) {
            rs = statement.getResultSet();
        } else {
            logger.info("No record is found is the resultset object...");
        }
        return rs;
    }

    /**
     * Dynamically inserts query parameter into prepared statement script.
     *
     * @param statement the prepared statement
     * @param parameters the parameters to input into the prepared statement
     * @return the prepared statement with parameters in them
     * @throws SQLException
     */
    private PreparedStatement insertParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);//at index 0 for paramters, the first parameter is set to 1, hence i+1
        }
        return statement;
    }

    /**
     * Dynamically inserts query parameter into prepared statement script.
     *
     * @param statement the prepared statement
     * @param parameters the parameters to input into the prepared statement
     * @return the prepared statement with parameters in them
     * @throws SQLException
     */
    private CallableStatement insertProcedureParameters(CallableStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);//at index 0 for paramters, the first parameter is set to 1, hence i+1
        }
        return statement;
    }

    /**
     * Counts the number of parameter entries required by the sql script.
     * Parameter blocks are denoted with a question mark (?)
     *
     * @param sqlStatement the sql script
     * @return the number of parameter entries
     */
    private int countParameterBlocksInStatement(String sqlStatement) {
        char where = '?';
        int count = 0;
        for (int i = 0; i < sqlStatement.length(); i++) {
            if (sqlStatement.charAt(i) == where) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Extracts the headers and data content from the prepared statement's
     * result set.
     *
     * @param resultset the result set
     * @return the list containing the headers and data content
     * @throws Exception
     */
    public List extractDataFromResultset(ResultSet resultset) throws Exception {
        List<String> columnheaders = new ArrayList<>();
        List<Object[]> datacontent = new ArrayList<>();
        List records = new ArrayList();

        if (resultset != null) {
            int noOfColumns = resultset.getMetaData().getColumnCount();

            //get columns
            columnheaders.add("S/N");
            for (int i = 0; i < noOfColumns; i++) {
                columnheaders.add(resultset.getMetaData().getColumnLabel(i + 1));//value in result set starts from 1
            }

            //get data contents
            while (resultset.next()) {
                if (noOfColumns <= 0) {
                    logger.info("Result set is empty");
                }

                Object[] resultsetArray = new Object[noOfColumns];
                for (int i = 0; i < noOfColumns; i++) {
                    resultsetArray[i] = resultset.getObject(i + 1);
                }
                //insert row into data content list
                datacontent.add(resultsetArray);
            }
            records.add(columnheaders);
            records.add(datacontent);

            resultset.close();
        }

        return records;
    }

    /**
     * Closes the connection to the database. Must be called after
     * {@link #commitTransaction()}
     *
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * generate random alpha numeric string
     *
     * @param count the size of the string to generate
     * @return generated string
     */
    public String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}
