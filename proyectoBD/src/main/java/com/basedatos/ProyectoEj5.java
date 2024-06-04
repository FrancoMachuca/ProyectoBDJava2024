package com.basedatos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProyectoEj5 {
  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader("proyectoBD/src/main/resources/configPrueba.txt"));

      String driver = reader.readLine();
      String url = reader.readLine();
      String username = reader.readLine();
      String password = reader.readLine();
      reader.close();
      // Load database driver if not already loaded.
      Class.forName(driver);
      // Establish network connection to database.
      Connection connection = DriverManager.getConnection(url, username, password);

      String query = "SELECT * FROM persona ";
      PreparedStatement statement = connection.prepareStatement(query);
      // statement.setString(1, "2");
      ResultSet resultSet = statement.executeQuery();
         while(resultSet.next()) 
      {
       System.out.print("; Nombre: "+resultSet.getString("nombre"));
       System.out.print("; Nacionalidad: " + resultSet.getString("nacionalidad")) ;
       System.out.print("; Cantidad de Participaciones: " + resultSet.getInt("cantParticipaciones")) ;
       System.out.print("\n   ");
       System.out.print("\n   ");
      } 
      
    } catch(ClassNotFoundException cnfe) {
      System.err.println("Error loading driver: " + cnfe);
      cnfe.printStackTrace();
    } catch(SQLException sqle) {
    	sqle.printStackTrace();
      System.err.println("Error connecting: " + sqle);
    } catch(Exception sqle) {
  	sqle.printStackTrace();
    System.err.println("Error connecting: " + sqle);
    }

  }

}
