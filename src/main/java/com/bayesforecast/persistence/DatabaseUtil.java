package com.bayesforecast.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseUtil {
	 private DatabaseUtil() {
         
     }

     /**
      * M�todo que cierra la conexi�n. En caso de error se redirigir� a stderr.
      * 
      * @param connection
      *            la conexi�n que va a ser cerrada
      */
     public static void close(Connection connection) {
             if (connection != null) {
                     try {
                             connection.close();
                     } catch (SQLException e) {
                             System.err.println("Cierre de conexi�n fallido: "
                                             + e.getMessage());
                             e.printStackTrace();
                     }
             }
     }

     /**
      * M�todo que cierra un Statement. En caso de error se redirigir� a stderr.
      * 
      * @param statement
      *            el statement que va a ser cerrado
      */
     public static void close(PreparedStatement statement) {
             if (statement != null) {
                     try {
                             statement.close();
                     } catch (SQLException e) {
                             System.err.println("Cierre del Statement fallido: "
                                             + e.getMessage());
                             e.printStackTrace();
                     }
             }
     }

     /**
      * M�todo que cierra un ResultSet. En caso de error se redirigir� a stderr.
      * 
      * @param resultSet
      *            ResultSet que va a ser cerrado
      */
     public static void close(ResultSet resultSet) {
             if (resultSet != null) {
                     try {
                             resultSet.close();
                     } catch (SQLException e) {
                             System.err.println("Cierre del ResultSet fallido: "
                                             + e.getMessage());
                             e.printStackTrace();
                     }
             }
     }

     /**
      * M�todo que cierra una conexi�n y un Statement. En caso de error se
      * redirigir� a stderr.
      * 
      * @param connection
      *            la conexi�n que va a ser cerrada
      * @param statement
      *            el statement que va a ser cerrado
      */
     public static void close(Connection connection, PreparedStatement statement) {
             close(statement);
             close(connection);
     }

     /**
      * M�todo que cierra una conexi�n, un Statement y un ResultSet. En caso de
      * error se redirigir� a stderr.
      * 
      * @param connection
      *            la conexi�n que va a ser cerrada
      * @param statement
      *            el statement que va a ser cerrado
      * @param resultSet
      *            ResultSet que va a ser cerrado
      */
     public static void close(Connection connection, PreparedStatement statement,
                     ResultSet resultSet) {
             close(resultSet);
             close(statement);
             close(connection);
     }
}
