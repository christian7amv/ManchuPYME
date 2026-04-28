/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import static Controlador.ControladorUsuariosBDR.online;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author EMMA
 */
public class ControladorSuscripcionesBDR {

    public static boolean online = false;
    ArrayList<Object[]> listaSus = new ArrayList<>(); //lista para la matriz y varias operaciones de la lista
    Connection con;
    Statement sentencia;
    ResultSet rs;
    String sql;
    String usuario = "caca";
    String clave = "7IX-MPda_Kz-kWqa";
    String url = "jdbc:mysql://26.132.30.248:3306/machuphymev2"; //online
    //String url = "jdbc:mysql://26.132.30.248:3306/manchupyme"; //local

    public ControladorSuscripcionesBDR() {
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

    public void añadirDatosEjemplo() {
        try {

            sentencia = con.createStatement();

            // INSERCIÓN INICIAL MASIVA con los 3 planes de ejemplo
            sql = "INSERT INTO suscripciones (id, precio, caracteristicas) VALUES"
                    + "('EMPRESARIAL', 49.99, 'Acceso completo y destacado en resultados'),\n"
                    + "('ESTÁNDAR', 9.99, 'Perfil básico con funcionalidad mínima'),\n"
                    + "('PROFESIONAL', 29.99, 'Perfil profesional estándar');";

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public Object[][] obtenerMatriz() {

        try {
            listaSus.clear(); //Borrar lista pa q no se duplique
            sentencia = con.createStatement();

            sql = "SELECT * FROM suscripciones";
            rs = sentencia.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[3]; //se crea un objeto para cada fila (id, precio, caracteristicas)
                fila[0] = rs.getString("id");
                fila[1] = rs.getDouble("precio");
                fila[2] = rs.getString("caracteristicas");

                listaSus.add(fila);
            }
            Object[][] matriz = new Object[listaSus.size()][3];
            for (int i = 0; i < listaSus.size(); i++) {
                matriz[i] = listaSus.get(i); //se añade cada cosa de la lista a la matriz
            }

            return matriz;

        } catch (SQLException e) {
            // Información del Error
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
            return null; //en el caso de que haya error devuelve null
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }

    }

    public void borrarTodo() {
        try {

            sentencia = con.createStatement();
            sql = "DELETE FROM suscripciones;";

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);

        } catch (SQLException e) {
            // Información del Error
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void añadir(String plan, double precio, String caracteristicas) throws SQLException {

        try {
            sentencia = con.createStatement();

            String caracSQL;

            if (caracteristicas == null || caracteristicas.isEmpty()) {
                caracSQL = "NULL";
            } else {
                // se escapan las comillas simples para evitar problemas con SQL
                caracSQL = "'" + caracteristicas.replace("'", "''") + "'";
            }

            String planEsc = plan.replace("'", "''");

            sql = "INSERT INTO suscripciones (id, precio, caracteristicas) VALUES ('"
                    + planEsc + "', " + precio + ", " + caracSQL + ")";

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
            System.out.println("Se ha insertado la nueva suscripción.");

        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void actualizar(String idOriginal, String nuevoPlan, double precio, String caracteristicas) throws SQLException {
        try {
            sentencia = con.createStatement();

            String caracSQL;
            if (caracteristicas == null || caracteristicas.isEmpty()) {
                caracSQL = "NULL";
            } else {
                caracSQL = "'" + caracteristicas.replace("'", "''") + "'";
            }

            String planEsc = nuevoPlan.replace("'", "''");
            String idEsc = idOriginal.replace("'", "''");

            sql = "UPDATE suscripciones SET id = '" + planEsc + "', precio = " + precio
                    + ", caracteristicas = " + caracSQL + " WHERE id = '" + idEsc + "'";

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
            System.out.println("Se ha actualizado la suscripción.");

        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void borrarSelec(int fila) {
        try {
            String id = (String) listaSus.get(fila)[0]; // la fila dada, y col 0 en la lista q siempre va a ser id
            sentencia = con.createStatement();
            sql = "DELETE FROM suscripciones WHERE id = '" + id.replace("'", "''") + "'";
            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
        } catch (SQLException e) {
            // Información del Error
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void borrarPorId(String id) {
        try {
            sentencia = con.createStatement();
            sql = "DELETE FROM suscripciones WHERE id = '" + id.trim().replace("'", "''") + "';";
            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void exportar(String nombreFich) {
        String id, caracteristicas;
        double precio;
        System.out.println("EXPORTANDO A FICHERO: '" + nombreFich + "'");
        try (BufferedWriter fichBuf = new BufferedWriter(new FileWriter(nombreFich, false))) {

            sentencia = con.createStatement();
            sql = "SELECT * FROM suscripciones"; //se coge la info d suscripciones
            rs = sentencia.executeQuery(sql);

            while (rs.next()) {
                id = rs.getString("id");
                precio = rs.getDouble("precio");
                caracteristicas = rs.getString("caracteristicas");

                // si son null se guardan como espacio para q haya dato
                if (caracteristicas == null) {
                    caracteristicas = "";
                }

                String linea = id + ";|" + precio + ";|" + caracteristicas; //se añaden con separacion

                System.out.println("GUARDANDO: " + linea);
                fichBuf.write(linea); //se enseña por terminal
                fichBuf.newLine();
                fichBuf.flush();
            }

            rs.close();
            sentencia.close();
            fichBuf.close();
            System.out.println("Exportacion completada.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
        }
    }

    public void importar(String nombreFich) {
        System.out.println("IMPORTANDO DESDE FICHERO: '" + nombreFich + "'");
        listaSus.clear();

        try (BufferedReader fichBuf = new BufferedReader(new FileReader(nombreFich))) {
            String cadena = fichBuf.readLine();
            while (cadena != null) {//bucle mientras haya linea
                System.out.println("LEIDO: " + cadena); //enseña la lina q esta leyendo

                String[] campos = cadena.split(";\\|"); //divide por los simbolos ; O |

                if (campos.length >= 3) { //deben haber al menos 3 datos (id, precio, caracteristicas)

                    listaSus.add(campos); //se añade al array list

                    try {
                        double precio = Double.parseDouble(campos[1]);
                        añadir(campos[0], precio, campos[2]); //se añade al sql
                    } catch (NumberFormatException ex) {
                        System.err.println("Error al parsear el precio: " + ex.getMessage());
                    } catch (SQLException ex) {
                        System.err.println("Error al insertar: " + ex.getMessage());
                    }
                }
                cadena = fichBuf.readLine(); //lee siguiente linea y repite bucle
            }
            fichBuf.close();
            System.out.println("Importacion completada. Total: " + listaSus.size());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void importarTXT(String nombreFich) {
    System.out.println("IMPORTANDO SUSCRIPCIONES DESDE: '" + nombreFich + "'");
    listaSus.clear();

    try (BufferedReader fichBuf = new BufferedReader(new FileReader(nombreFich))) {
        String linea;

        while ((linea = fichBuf.readLine()) != null) {
            System.out.println("LEIDO: " + linea);

            String[] campos = linea.split(";");

            if (campos.length >= 3) {
                String plan = campos[0];
                double precio = Double.parseDouble(campos[1]);
                String caracteristicas = campos[2];

                listaSus.add(campos);

                try {
                    añadir(plan, precio, caracteristicas);
                } catch (SQLException ex) {
                    System.err.println("Error al insertar: " + ex.getMessage());
                }
            } else {
                System.err.println("Linea incorrecta, se omite.");
            }
        }

        System.out.println("Importación TXT completada. Total: " + listaSus.size());

    } catch (IOException e) {
        System.err.println(e.getMessage());
    }
}
    
    public void exportarXML(String nombreFich) {
    try {
        FileOutputStream fos = new FileOutputStream(nombreFich);
        XMLEncoder xmle = new XMLEncoder(new BufferedOutputStream(fos));

        xmle.writeObject(listaSus);
        xmle.close();

        System.out.println("Exportación XML completada.");
    } catch (Exception e) {
        System.err.println("ERROR al exportar XML: " + e.getMessage());
    }
}
    
    public void importarXML(String nombreFich) {
    System.out.println("IMPORTANDO XML: '" + nombreFich + "'");

    try {
        FileInputStream fis = new FileInputStream(nombreFich);
        XMLDecoder xmld = new XMLDecoder(fis);

        ArrayList<Object[]> listaImportada = (ArrayList<Object[]>) xmld.readObject();

        xmld.close();
        fis.close();

        listaSus.clear();

        for (Object[] fila : listaImportada) {

            if (fila.length < 3) {
                System.err.println("Fila incorrecta, se omite.");
                continue;
            }

            String plan = fila[0] != null ? fila[0].toString() : "";
            double precio = fila[1] != null ? Double.parseDouble(fila[1].toString()) : 0;
            String caracteristicas = fila[2] != null ? fila[2].toString() : "";

            if (plan.isEmpty()) {
                System.err.println("Fila omitida: plan vacío.");
                continue;
            }

            listaSus.add(fila);

            try {
                añadir(plan, precio, caracteristicas);
            } catch (SQLException ex) {
                System.err.println("Error al insertar: " + ex.getMessage());
            }
        }

        System.out.println("Importación XML completada. Total: " + listaImportada.size());

    } catch (Exception e) {
        System.err.println("ERROR al importar XML: " + e.getMessage());
        e.printStackTrace();
    }
}

    public void desconectar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión cerrada.");
            }
            online = false;
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

}
