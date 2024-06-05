package com.basedatos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import javax.swing.InputMap;

public class ProyectoEj5 {
  public static void main(String[] args) throws SQLException, IOException {

    String msj = "La conexión a la base de datos usada por este programa se debería encontrar en un archivo llamado 'configPrueba.txt' ubicado en la carpeta resources de este proyecto que contiene, en este orden, los siguientes datos: \n"
        + "driver \n" + "url a la base de datos \n" + "username \n" + "password \n";
    System.out.println(msj);
    String respuesta = "m";
    Scanner s = new Scanner(System.in);
    while (!respuesta.toLowerCase().equals("y") && !respuesta.toLowerCase().equals("n")) {
      System.out.println("¿Creaste el archivo y agregaste la información necesaria? (Y/N) \n");
      respuesta = s.nextLine();
    }
    if (respuesta.toLowerCase().equals("y")) {
      Connection connection;
      BufferedReader reader = new BufferedReader(new FileReader("proyectoBD/src/main/resources/configPrueba.txt"));

      String driver = reader.readLine();
      String url = reader.readLine();
      String username = reader.readLine();
      String password = reader.readLine();
      reader.close();
      try {
        // Load database driver if not already loaded.
        Class.forName(driver);
        // Establish network connection to database.
        connection = DriverManager.getConnection(url, username, password);
        if (connection.isValid(0)) {
          System.out.println("Conexión exitosa");
        }
        connection.close();
      } catch (ClassNotFoundException cnfe) {
        System.err.println("Error loading driver: " + cnfe);
        cnfe.printStackTrace();
      } catch (SQLException sqle) {
        sqle.printStackTrace();
        System.err.println("Error connecting: " + sqle);
      } catch (Exception sqle) {
        sqle.printStackTrace();
        System.err.println("Error connecting: " + sqle);
      }

      connection = DriverManager.getConnection(url, username, password);

      char opcion = '0';
      List<Character> posiblesOpciones = new ArrayList<>();
      posiblesOpciones.add('q');
      posiblesOpciones.add('1');
      posiblesOpciones.add('2');
      posiblesOpciones.add('3');
      msj = "\n Elige una opción: \n" + posiblesOpciones.get(1) + "- Insertar un cine \n" + posiblesOpciones.get(2)
          + "- Insertar una sala en un cine \n" + posiblesOpciones.get(3)
          + "- Listar todos los cines y las salas de cada uno \n"
          + posiblesOpciones.get(0) + "- Salir";

      do {
        System.out.println(msj);
        String opcionS = s.next();
        opcion = opcionS.charAt(0);
        switch (opcion) {
          case 'q':
            System.out.println("Saliendo...");
            s.close();
            connection.close();
            break;

          case '1':
            System.out.println("Ingrese el nombre del cine a crear: ");
            String nombre1 = s.nextLine();
            System.out.println();
            System.out.println("Ingrese la dirección del cine: ");
            String direccion = s.nextLine();
            System.out.println();
            System.out.println("Ingrese el teléfono del cine: ");
            String telefono = s.nextLine();
            System.out.println();
            String query = "INSERT INTO Cine (nombre, direccion, telefono) VALUES ('" + nombre1 + "', '" + direccion
                + "', '" + telefono + "');";
            PreparedStatement statement = connection.prepareStatement(query);
            if (statement.executeUpdate() > 0) {
              System.out.println("Cine creado exitosamente. \n");
            } else {
              System.out.println("No se pudo crear el cine especificado. \n");
            }
            opcion = '0';
            break;
          case '2':
            System.out.println("Ingrese el nombre del cine donde pertenece la sala: ");
            System.out.println();
            String nombre2 = s.nextLine();
            String checkQuery = "SELECT * FROM Cine WHERE nombre = " + "'" + nombre2 + "'" + ";";
            PreparedStatement statement2 = connection.prepareStatement(checkQuery);
            ResultSet rSet = statement2.executeQuery();
            if (!rSet.next()) {
              System.out.println("No se encontró un cine con ese nombre.");
            } else {
              System.out.println();
              System.out.println("Ingrese el número de butacas que posee la sala: ");
              int nroButacas = s.nextInt();
              String query2 = "INSERT INTO Sala(cantButacas, nombre) VALUES (" + nroButacas + ",'" + nombre2 + "');";
              statement2 = connection.prepareStatement(query2);
              statement2.executeUpdate();
            }
            opcion = '0';
            break;

          case '3':
            String query3 = "SELECT cine.nombre FROM Cine;";
            PreparedStatement statement3 = connection.prepareStatement(query3);
            ResultSet cineSet = statement3.executeQuery();
            if (!cineSet.next()) {
              System.out.println("No se encontró ningun cine.");
            } else {
              do {
                String nombreCine = cineSet.getString("cine.nombre");
                query3 = "SELECT cine.nombre, sala.id_sala, sala.cantButacas FROM (Cine LEFT OUTER JOIN Sala ON cine.nombre = sala.nombre) WHERE cine.nombre = "
                    + "'" + nombreCine + "'" + ";";
                statement3 = connection.prepareStatement(query3);
                ResultSet cineSalaSet = statement3.executeQuery();
                System.out.println("Mostrando salas del cine: " + nombreCine + "\n");
                while (cineSalaSet.next()) {
                  int nroSala = cineSalaSet.getInt("id_sala");
                  if (nroSala == 0) {
                    System.out.println("Este cine no posee salas. \n");
                  } else {
                    System.out.println("Nro. de sala: " + nroSala);
                    System.out.println("Cantidad de butacas: " + cineSalaSet.getInt("cantButacas") + "\n");
                  }
                }
              } while (cineSet.next());
            }
            opcion = '0';
            break;
          default:
            System.out.println("La opción introducida no es válida! \n" + "Ingresa de nuevo la opción: ");
            break;
        }
      } while ((!posiblesOpciones.contains(opcion)));
    }
  }

}
