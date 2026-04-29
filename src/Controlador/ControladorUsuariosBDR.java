/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

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
 * @author chris
 */
public class ControladorUsuariosBDR {

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

    public ControladorUsuariosBDR() {
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

    public ArrayList<String> obtenerPerfiles() {
        ArrayList<String> perfiles = new ArrayList<>();
        if (!online || con == null) { //si la base de datos esta apagada no lo ejecuta
            return perfiles;
        }
        try {
            sentencia = con.createStatement();
            rs = sentencia.executeQuery("SELECT perfil FROM perfiles_profesionales ORDER BY perfil");
            while (rs.next()) {
                perfiles.add(rs.getString("perfil"));
            }
            rs.close();
            sentencia.close();
        } catch (SQLException e) {
            System.err.println("SQL Error al obtener perfiles: " + e.getMessage());
        }
        return perfiles;
    }

    public void añadirDatosEjemplo() {
        try {

            sentencia = con.createStatement();

            // INSERCIÓN INICIAL MASIVA
            sql = "INSERT INTO usuarios (nombre, email, rol, municipio, categoria, descripcion, puntuacion_media) VALUES"
                    + "('Carlos Ruiz', 'carlos@manchupyme.es', 'PROFESIONAL', 'Albacete', 'Electricista', 'Instalaciones electricas residenciales y comerciales', 4.70),\n"
                    + "('Ana Gomez', 'ana@correo.es', 'PARTICULAR', 'Albacete', NULL, NULL, NULL),\n"
                    + "('Pedro Sanchez', 'pedro@manchupyme.es', 'PROFESIONAL', 'Ciudad Real', 'Fontanero', 'Reparacion de tuberias, calefaccion y fontaneria general', 4.20),\n"
                    + "('Marta Lopez', 'marta@correo.es', 'PARTICULAR', 'Ciudad Real', NULL, NULL, NULL),\n"
                    + "('Luis Martinez', 'luis@manchupyme.es', 'PROFESIONAL', 'Guadalajara', 'Carpintero', 'Muebles a medida, puertas y ventanas de madera', 4.90),\n"
                    + "('Sara Diaz', 'sara@correo.es', 'PARTICULAR', 'Guadalajara', NULL, NULL, NULL),\n"
                    + "('Juan Torres', 'juan@manchupyme.es', 'PROFESIONAL', 'Cuenca', 'Cerrajero', 'Apertura de puertas, cambio de cerraduras y copias', 3.80),\n"
                    + "('Elena Moreno', 'elena@manchupyme.es', 'PARTICULAR', 'Albacete', NULL, NULL, NULL),\n"
                    + "('Tomas Jimenez', 'tomas@manchupyme.es', 'PROFESIONAL', 'Ciudad Real', 'Pintor', 'Pintura interior y exterior, tratamientos de fachada', 4.50),\n"
                    + "('Raul Fernandez', 'raul@correo.es', 'PARTICULAR', 'Cuenca', NULL, NULL, NULL);";

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
            listaUsr.clear(); //Borrar lista pa q no se duplique
            sentencia = con.createStatement();

            sql = "SELECT * FROM usuarios";
            rs = sentencia.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[8]; //se crea un objeto para cada fila
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("email");
                fila[3] = rs.getString("rol");
                fila[4] = rs.getString("municipio");
                fila[5] = rs.getString("categoria");
                fila[6] = rs.getString("descripcion");
                fila[7] = rs.getDouble("puntuacion_media");

                listaUsr.add(fila);
            }
            Object[][] matriz = new Object[listaUsr.size()][5];
            for (int i = 0; i < listaUsr.size(); i++) {
                matriz[i] = listaUsr.get(i); //se añade cada cosa de la lista a la matriz
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
            sql = "DELETE FROM usuarios;";//truncate resetea el auto increment

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
            sql = "ALTER TABLE usuarios AUTO_INCREMENT = 1;";//truncate resetea el auto increment

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

    public void añadir(String nombre, String email, String rol, String municipio, String categoria, String descripcion) throws SQLException {

        try {
            sentencia = con.createStatement();

            String munSQL;
            String catSQL;
            String descSQL;

            if (municipio.isEmpty()) {
                munSQL = "NULL";
            } else {
                munSQL = "'" + municipio + "'";
            }

            if (categoria==null||categoria.isEmpty()) {
                catSQL = "NULL";
            } else {
                catSQL = "'" + categoria + "'";
            }

            if (descripcion.isEmpty()) {
                descSQL = "NULL";
            } else {
                descSQL = "'" + descripcion + "'";
            }

            sql = "INSERT INTO usuarios (nombre, email, rol, municipio, categoria, descripcion) VALUES ('" + nombre + "', '" + email + "', '"
                    + rol + "', " + munSQL + ", " + catSQL + ", " + descSQL + ")";

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
            System.out.println("Se ha insertado el nuevo usuario.");

        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void borrarPorEmail(String resp) {
        try {
            sentencia = con.createStatement();
            sql = "DELETE FROM usuarios WHERE email = '" + resp.trim() + "';";
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

    public void borrarSelec(int fila) {
        try {
            int id = (int) listaUsr.get(fila)[0]; // la fila dada, y col 0 en la lista q siempre va a ser id
            sentencia = con.createStatement();
            sql = "DELETE FROM usuarios WHERE id = '" + id + "'";
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

    public void exportar(String nombreFich) {
        String nombre, email, rol, municipio, categoria, descripcion;
        double puntuacion;
        System.out.println("EXPORTANDO A FICHERO: '" + nombreFich + "'");
        try (BufferedWriter fichBuf = new BufferedWriter(new FileWriter(nombreFich, false))) {

            sentencia = con.createStatement();
            sql = "SELECT * FROM usuarios"; //se coge la info d usuarios
            rs = sentencia.executeQuery(sql);

            while (rs.next()) {
                nombre = rs.getString("nombre");
                email = rs.getString("email");
                rol = rs.getString("rol");
                municipio = rs.getString("municipio");
                categoria = rs.getString("categoria");
                descripcion = rs.getString("descripcion");
                puntuacion = rs.getDouble("puntuacion_media");

                // si son null se guardan como espacio para q haya dato
                if (municipio == null) {
                    municipio = "";
                }
                if (categoria == null) {
                    categoria = "";
                }
                if (descripcion == null) {
                    descripcion = "";
                }

                String linea = nombre + ";|" + email + ";|" + rol + ";|" + municipio
                        + ";|" + categoria + ";|" + descripcion + ";|" + puntuacion; //se añaden con separacion

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

    public void importarTXT(String nombreFich) {
        System.out.println("IMPORTANDO DESDE FICHERO: '" + nombreFich + "'");
        listaUsr.clear();

        try (BufferedReader fichBuf = new BufferedReader(new FileReader(nombreFich))) {
            String cadena = fichBuf.readLine();
            while (cadena != null) {//bucle mientras haya linea
                System.out.println("LEIDO: " + cadena); //enseña la lina q esta leyendo

                String[] campos = cadena.split(";\\|"); //divide por los simbolos ; O |

                if (campos.length >= 6) { //deben haber al menos 6 datos

                    listaUsr.add(campos); //se añade al array list

                    try {
                        añadir(campos[0], campos[1], campos[2], campos[3], campos[4], campos[5]); //se añade al sql
                    } catch (SQLException ex) {
                        System.err.println("Error al insertar: " + ex.getMessage());
                    }
                }
                cadena = fichBuf.readLine(); //lee siguiente linea y repite bucle
            }
            fichBuf.close();
            System.out.println("Importacion completada. Total: " + listaUsr.size());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void exportarXML(String nombreFich) {
        FileOutputStream fos;
        XMLEncoder xmle;

        try {
            fos = new FileOutputStream(nombreFich);
            xmle = new XMLEncoder(new BufferedOutputStream(fos));
            xmle.writeObject(listaUsr);
            xmle.close();
        } catch (Exception e) {
            System.err.println("\tERROR en la escritura de datos del archivo: " + nombreFich);
        }
    }

    public void importarXML(String nombreFich) {
        System.out.println("IMPORTANDO DESDE FICHERO XML: '" + nombreFich + "'");
        FileInputStream fis;
        XMLDecoder xmld;
        try {
            fis = new FileInputStream(nombreFich);
            xmld = new XMLDecoder(fis);
            ArrayList<Object[]> listaImportada = (ArrayList<Object[]>) xmld.readObject();
            xmld.close();
            fis.close();

            listaUsr.clear();
            for (Object[] fila : listaImportada) {
                String nombre, email, rol, municipio, categoria, descripcion;

                int offset;
                if (fila.length >= 8) {
                    offset = 1; // saltar el id
                } else if (fila.length >= 6) {
                    offset = 0;
                } else {
                    System.err.println("Fila con formato incorrecto, se omite.");
                    continue;
                }

                if (fila[offset] == null) {
                    nombre = "";
                } else {
                    nombre = fila[offset].toString();
                }

                if (fila[offset + 1] == null) {
                    email = "";
                } else {
                    email = fila[offset + 1].toString();
                }

                if (fila[offset + 2] == null) {
                    rol = "";
                } else {
                    rol = fila[offset + 2].toString();
                }

                // nombre, email y rol son obligatorios: si falta alguno se omite la fila
                if (nombre.isEmpty() || email.isEmpty() || rol.isEmpty()) {
                    System.err.println("Fila omitida: nombre, email y rol no pueden estar vacios.");
                    continue;
                }

                if (fila[offset + 3] == null) {
                    municipio = "";
                } else {
                    municipio = fila[offset + 3].toString();
                }

                if (fila[offset + 4] == null) {
                    categoria = "";
                } else {
                    categoria = fila[offset + 4].toString();
                }

                if (fila[offset + 5] == null) {
                    descripcion = "";
                } else {
                    descripcion = fila[offset + 5].toString();
                }

                listaUsr.add(fila);

                try {
                    añadir(nombre, email, rol, municipio, categoria, descripcion);
                } catch (SQLException ex) {
                    System.err.println("Error al insertar: " + ex.getMessage());
                }
            }
            System.out.println("Importacion XML completada. Total: " + listaImportada.size());
        } catch (Exception e) {
            System.err.println("\tERROR en la lectura de datos del archivo: " + nombreFich);
            e.printStackTrace(System.err);
        }

    }

    public void modificar(String id, String nombre, String email, String rol, String municipio, String categoria, String descripcion) throws SQLException {

        try {
            sentencia = con.createStatement();

            String munSQL;
            String catSQL;
            String descSQL;

            if (municipio.isEmpty()) {
                munSQL = "NULL";
            } else {
                munSQL = "'" + municipio + "'";
            }

            if (categoria == null) {
                catSQL = "NULL";
            } else {
                catSQL = "'" + categoria + "'";
            }

            if (descripcion.isEmpty()) {
                descSQL = "NULL";
            } else {
                descSQL = "'" + descripcion + "'";
            }

            sql = "UPDATE usuarios SET nombre = '" + nombre + "', email = '" + email + "', rol = '" + rol
                    + "', municipio = " + munSQL + ", categoria = " + catSQL + ", descripcion = " + descSQL
                    + " WHERE id = '" + id + "'";

            System.out.println("Sentencia a ejecutar: " + sql);
            int filas = sentencia.executeUpdate(sql);
            System.out.println("Se han modificado " + filas + " filas.");

        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }

}
