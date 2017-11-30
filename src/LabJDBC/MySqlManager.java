package LabJDBC;

import java.sql.*;
import java.util.Properties;

class MySqlManager {

    private Connection connection;
    private Statement statement;

    public void connectDatabase() {

        Properties props = PropertyManager.getProperties();
        String url = props.getProperty("jdbc.url");

        try {
            connection = DriverManager.getConnection(url, props);

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public PreparedStatement createPreparedStatement(String query){
        try {
            return connection.prepareStatement(query);
        } catch (SQLException e){
            System.out.println(e);
            return null;
        }
    }

    public int executeUpdate(String query) {

        int res = 0;
        try {
            statement = connection.createStatement();
            res = statement.executeUpdate(query);
        } catch (SQLException e) {
            //System.out.println(e);
        } finally {
            return res;
        }
    }

    public ResultSet executeQuery(String query) {

        ResultSet res = null;
        try {
            statement = connection.createStatement();
            res = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return res;
        }
    }

    public void disconnectDatabase() {
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        if (statement != null)
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }
}
