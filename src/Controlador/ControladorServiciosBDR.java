/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Jordi Estudios
 */
public class ControladorServiciosBDR {

    public static boolean online = false;
    ArrayList<Object[]> listaUsr = new ArrayList<>(); //lista para la matriz y varias operaciones de la lista
    Connection con;
    Statement sentencia;
    ResultSet rs;
    String sql;
    String usuario = "caca";
    String clave = "7IX-MPda_Kz-kWqa";
    String url = "jdbc:mysql://26.132.30.248:3306/machuphymev2"; //online
    //String url = "jdbc:mysql://26.132.30.248:3306/manchupyme"; //local

    public ControladorServiciosBDR() {
        conectar();
    }

    public void conectar() {
        try {
            // conexión con la BD
            con = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conexión establecida con " + url);
            online = true;
            // cierre de la conexión
        } catch (SQLException e) {
            // Información del Error
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}
