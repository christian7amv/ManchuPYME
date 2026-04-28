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
 * @author Jordi Estudios
 */
public class ControladorServiciosBDR {

    public static boolean online = false;
    ArrayList<Object[]> listaSrv = new ArrayList<>(); //lista para la matriz y varias operaciones de la lista
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

    public void asegurarPerfil(String perfil) {
        boolean existe;
        if (perfil == null || perfil.isBlank()) {
            return;
        }
        try {
            // Comprobamos primero si ya existe (evita duplicados aunque no haya UNIQUE)
            Statement st = con.createStatement();
            ResultSet r = st.executeQuery(
                    "SELECT COUNT(*) FROM perfiles_profesionales WHERE perfil = '" + perfil.trim() + "'"); //mira cuantos perfiles hay con el nombre del perfil insertado
            existe = false;
            if (r.next()) {//si devuelve algo
                existe = r.getInt(1) > 0; //las coincidencias son mayor q 0 true
            }
            r.close();

            if (!existe) {
                String sqlIns = "INSERT INTO perfiles_profesionales (perfil) VALUES ('" + perfil.trim() + "')";
                System.out.println("Sentencia a ejecutar: " + sqlIns);
                st.executeUpdate(sqlIns);
                System.out.println("Nuevo perfil añadido a perfiles_profesionales: " + perfil);
            }
            st.close();
        } catch (SQLException e) {
            System.err.println("SQL Error al asegurar perfil '" + perfil + "': " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void añadirDatosEjemplo() {
        try {

            sentencia = con.createStatement();

            // INSERCIÓN INICIAL MASIVA
            sql = "INSERT INTO servicios (perfil, nombre, descripcion) VALUES"
                    + "('Electricista', 'Instalacion electrica basica', 'Instalacion de enchufes, interruptores y puntos de luz en viviendas.'),\n"
                    + "('Electricista', 'Revision cuadro electrico', 'Revision completa del cuadro de distribucion electrica y diferenciales.'),\n"
                    + "('Electricista', 'Cambio de luminarias LED', 'Sustitucion de luminarias antiguas por tecnologia LED de bajo consumo.'),\n"
                    + "('Fontanero', 'Reparacion de tuberias', 'Deteccion y reparacion de fugas en tuberias.'),\n"
                    + "('Fontanero', 'Instalacion de caldera', 'Instalacion y puesta en marcha de caldera de gas o electrica.'),\n"
                    + "('Fontanero', 'Mantenimiento fontaneria', 'Revision anual de toda la instalacion de agua.'),\n"
                    + "('Carpintero', 'Mueble de cocina a medida', 'Diseno y fabricacion de muebles de cocina personalizados.'),\n"
                    + "('Carpintero', 'Reparacion de puertas', 'Ajuste, reparacion o sustitucion de puertas de madera.'),\n"
                    + "('Cerrajero', 'Apertura de puerta sin llave', 'Apertura de urgencia sin danyar la cerradura.'),\n"
                    + "('Cerrajero', 'Cambio de cerradura de seguridad', 'Instalacion de cerradura de alta seguridad con certificado.'),\n"
                    + "('Pintor', 'Pintura interior habitacion', 'Pintura completa de una habitacion incluyendo materiales.'),\n"
                    + "('Pintor', 'Pintura de fachada exterior', 'Tratamiento y pintura de fachadas exteriores.');";

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
            listaSrv.clear(); //Borrar lista pa q no se duplique
            sentencia = con.createStatement();

            sql = "SELECT * FROM servicios";
            rs = sentencia.executeQuery(sql);

            while (rs.next()) {
                Object[] fila = new Object[4]; //se crea un objeto para cada fila
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("perfil");
                fila[2] = rs.getString("nombre");
                fila[3] = rs.getString("descripcion");

                listaSrv.add(fila);
            }
            Object[][] matriz = new Object[listaSrv.size()][4];
            for (int i = 0; i < listaSrv.size(); i++) {
                matriz[i] = listaSrv.get(i); //se añade cada cosa de la lista a la matriz
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
            sql = "DELETE FROM servicios;";

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
            sql = "ALTER TABLE servicios AUTO_INCREMENT = 1;";//resetea el auto increment

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

    public void añadir(String perfil, String nombre, String descripcion) throws SQLException {

         try {
            // Asegura que el perfil exista en perfiles_profesionales antes de insertar el servicio
            asegurarPerfil(perfil);

            sentencia = con.createStatement();

            String descSQL;

            if (descripcion.isEmpty()) {
                descSQL = "NULL";
            } else {
                descSQL = "'" + descripcion + "'";
            }

            sql = "INSERT INTO servicios (perfil, nombre, descripcion) VALUES ('"
                    + perfil + "', '" + nombre + "', " + descSQL + ")";

            System.out.println("Sentencia a ejecutar: " + sql);
            sentencia.executeUpdate(sql);
            System.out.println("Se ha insertado el nuevo servicio.");

        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL código específico: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void borrarPorId(String resp) {
        try {
            sentencia = con.createStatement();
            sql = "DELETE FROM servicios WHERE id = '" + resp.trim() + "';";
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
            int id = (int) listaSrv.get(fila)[0]; // la fila dada, y col 0 en la lista q siempre va a ser id
            sentencia = con.createStatement();
            sql = "DELETE FROM servicios WHERE id = '" + id + "'";
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
        String perfil, nombre, descripcion;
        System.out.println("EXPORTANDO A FICHERO: '" + nombreFich + "'");
        try (BufferedWriter fichBuf = new BufferedWriter(new FileWriter(nombreFich, false))) {

            sentencia = con.createStatement();
            sql = "SELECT * FROM servicios"; //se coge la info d servicios
            rs = sentencia.executeQuery(sql);

            while (rs.next()) {
                perfil = rs.getString("perfil");
                nombre = rs.getString("nombre");
                descripcion = rs.getString("descripcion");

                // si son null se guardan como espacio para q haya dato
                if (descripcion == null) {
                    descripcion = "";
                }

                String linea = perfil + ";|" + nombre + ";|" + descripcion; //se añaden con separacion

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
        listaSrv.clear();

        try (BufferedReader fichBuf = new BufferedReader(new FileReader(nombreFich))) {
            String cadena = fichBuf.readLine();
            while (cadena != null) {//bucle mientras haya linea
                System.out.println("LEIDO: " + cadena); //enseña la lina q esta leyendo

                String[] campos = cadena.split(";\\|"); //divide por los simbolos ; O |

                if (campos.length >= 3) { //deben haber al menos 3 datos (perfil, nombre, descripcion)

                    listaSrv.add(campos); //se añade al array list

                    try {
                        añadir(campos[0], campos[1], campos[2]); //se añade al sql
                    } catch (SQLException ex) {
                        System.err.println("Error al insertar: " + ex.getMessage());
                    }
                }
                cadena = fichBuf.readLine(); //lee siguiente linea y repite bucle
            }
            fichBuf.close();
            System.out.println("Importacion completada. Total: " + listaSrv.size());
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
            xmle.writeObject(listaSrv);
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

            listaSrv.clear();
            for (Object[] fila : listaImportada) {
                String perfil, nombre, descripcion;

                int offset;
                if (fila.length >= 4) {
                    offset = 1; // saltar el id
                } else if (fila.length >= 3) {
                    offset = 0;
                } else {
                    System.err.println("Fila con formato incorrecto, se omite.");
                    continue;
                }

                if (fila[offset] == null) {
                    perfil = "";
                } else {
                    perfil = fila[offset].toString();
                }

                if (fila[offset + 1] == null) {
                    nombre = "";
                } else {
                    nombre = fila[offset + 1].toString();
                }

                // perfil y nombre son obligatorios: si falta alguno se omite la fila
                if (perfil.isEmpty() || nombre.isEmpty()) {
                    System.err.println("Fila omitida: perfil y nombre no pueden estar vacios.");
                    continue;
                }

                if (fila[offset + 2] == null) {
                    descripcion = "";
                } else {
                    descripcion = fila[offset + 2].toString();
                }

                listaSrv.add(fila);

                try {
                    añadir(perfil, nombre, descripcion);
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

    public void modificar(String id, String perfil, String nombre, String descripcion) throws SQLException {

        try {
            sentencia = con.createStatement();

            String descSQL;

            if (descripcion.isEmpty()) {
                descSQL = "NULL";
            } else {
                descSQL = "'" + descripcion + "'";
            }

            sql = "UPDATE servicios SET perfil = '" + perfil + "', nombre = '" + nombre
                    + "', descripcion = " + descSQL
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
