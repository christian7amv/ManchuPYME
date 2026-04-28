package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ControladorValoracionesBDR {

    Connection con;
    Statement sentencia;
    String sql;

    String usuario = "caca";
    String clave = "7IX-MPda_Kz-kWqa";
    String url = "jdbc:mysql://26.132.30.248:3306/machuphymev2"; //online
    // -------------------------------------------------

    public ControladorValoracionesBDR() {
        conectar();
    }

    private void conectar() {
        try {
            con = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conexion establecida con " + url);
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL codigo: " + e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public Object[][] obtenerMatriz() {
        ArrayList<Object[]> lista = new ArrayList<>();
        sql = "SELECT v.id, u1.nombre AS particular, u2.nombre AS profesional, "
                + "v.puntuacion, v.comentario, DATE_FORMAT(v.fecha, '%d-%m-%Y') AS fecha "
                + "FROM valoraciones v "
                + "JOIN usuarios u1 ON u1.id = v.particular_id "
                + "JOIN usuarios u2 ON u2.id = v.profesional_id "
                + "ORDER BY v.id";
        try {
            sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery(sql);
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id"),
                    rs.getString("particular"),
                    rs.getString("profesional"),
                    rs.getInt("puntuacion"),
                    rs.getString("comentario"),
                    rs.getString("fecha")
                };
                lista.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
        }
        return lista.toArray(new Object[0][]);
    }

    public String[] obtenerParticulares() {
        ArrayList<String> lista = new ArrayList<>();
        sql = "SELECT id, nombre FROM usuarios WHERE rol = 'PARTICULAR' ORDER BY nombre";
        try {
            sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery(sql);
            while (rs.next()) {
                lista.add(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
        return lista.toArray(new String[0]);
    }

    public String[] obtenerProfesionales() {
        ArrayList<String> lista = new ArrayList<>();
        sql = "SELECT id, nombre, categoria FROM usuarios WHERE rol = 'PROFESIONAL' ORDER BY nombre";
        try {
            sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery(sql);
            while (rs.next()) {
                lista.add(rs.getInt("id") + " - " + rs.getString("nombre")
                        + " (" + rs.getString("categoria") + ")");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
        return lista.toArray(new String[0]);
    }

    // ------------------------------------------------------------------ //
    //  INSERTAR                                                          //
    // ------------------------------------------------------------------ //
    public void insertar(int particularId, int profesionalId,
            int puntuacion, String comentario, String fechaDDMMYYYY) {
        // Convertir dd-MM-yyyy -> yyyy-MM-dd para MariaDB
        String fechaBD = convertirFecha(fechaDDMMYYYY);
        sql = "INSERT INTO valoraciones (particular_id, profesional_id, puntuacion, comentario, fecha) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, particularId);
            ps.setInt(2, profesionalId);
            ps.setInt(3, puntuacion);
            ps.setString(4, comentario);
            ps.setString(5, fechaBD);
            int filas = ps.executeUpdate();
            System.out.println("Valoracion insertada. Filas afectadas: " + filas);
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL codigo: " + e.getErrorCode());
        }
    }

    // ------------------------------------------------------------------ 
    //  MODIFICAR                                                         
    // ------------------------------------------------------------------ 
    public void modificar(int id, int particularId, int profesionalId,
            int puntuacion, String comentario, String fechaDDMMYYYY) {
        String fechaBD = convertirFecha(fechaDDMMYYYY);
        sql = "UPDATE valoraciones SET particular_id=?, profesional_id=?, "
                + "puntuacion=?, comentario=?, fecha=? WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, particularId);
            ps.setInt(2, profesionalId);
            ps.setInt(3, puntuacion);
            ps.setString(4, comentario);
            ps.setString(5, fechaBD);
            ps.setInt(6, id);
            int filas = ps.executeUpdate();
            System.out.println("Valoracion modificada. Filas afectadas: " + filas);
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL codigo: " + e.getErrorCode());
        }
    }

    // ------------------------------------------------------------------
    //  ELIMINAR por ID                                                  
    // ------------------------------------------------------------------ 
    public void eliminar(int id) {
        sql = "DELETE FROM valoraciones WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println("Valoracion eliminada. Filas afectadas: " + filas);
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: " + e.getSQLState());
            System.err.println("SQL codigo: " + e.getErrorCode());
        }
    }

    public void generarDatosDeEjemplo() {
        insertar(2, 5, 5, "Instalación de puertas perfecta y a tiempo.", "27-04-2026");
        insertar(4, 9, 3, "Buen trabajo de pintura, pero manchó un poco el suelo.", "28-04-2026");
        insertar(6, 3, 4, "Reparación rápida de la tubería, muy atento.", "29-04-2026");
        insertar(10, 1, 5, "Revisión eléctrica impecable, gran profesional.", "30-04-2026");
    }

    private String convertirFecha(String fechaDDMMYYYY) {
        // Acepta tanto dd-MM-yyyy como dd/MM/yyyy
        String[] partes = fechaDDMMYYYY.split("[-/]");
        if (partes.length == 3) {
            return partes[2] + "-" + partes[1] + "-" + partes[0];
        }
        return fechaDDMMYYYY;
    }

// EXPORTAR A TXT
    public void exportarTxt(java.io.File archivo, Object[][] datos) {
        try {
            java.io.PrintWriter pw = new java.io.PrintWriter(
                    new java.io.FileWriter(archivo));

            pw.println("ID | Particular | Profesional | Puntuacion | Comentario | Fecha");
            pw.println("------------------------------------------------------------------");

            for (int i = 0; i < datos.length; i++) {
                pw.println(
                        datos[i][0] + " | "
                        + datos[i][1] + " | "
                        + datos[i][2] + " | "
                        + datos[i][3] + " | "
                        + datos[i][4] + " | "
                        + datos[i][5]
                );
            }

            pw.close();
            System.out.println("Exportacion TXT completada: " + archivo.getAbsolutePath());

        } catch (java.io.IOException e) {
            System.err.println("Error al exportar TXT: " + e.getMessage());
        }
    }

// EXPORTAR A XML
    public void exportarXml(java.io.File archivo, Object[][] datos) {
        try {
            java.io.PrintWriter pw = new java.io.PrintWriter(
                    new java.io.FileWriter(archivo));

            pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.println("<valoraciones>");

            for (int i = 0; i < datos.length; i++) {
                pw.println("    <valoracion>");
                pw.println("        <id>" + datos[i][0] + "</id>");
                pw.println("        <particular>" + datos[i][1] + "</particular>");
                pw.println("        <profesional>" + datos[i][2] + "</profesional>");
                pw.println("        <puntuacion>" + datos[i][3] + "</puntuacion>");
                pw.println("        <comentario>" + datos[i][4] + "</comentario>");
                pw.println("        <fecha>" + datos[i][5] + "</fecha>");
                pw.println("    </valoracion>");
            }

            pw.println("</valoraciones>");
            pw.close();
            System.out.println("Exportacion XML completada: " + archivo.getAbsolutePath());

        } catch (java.io.IOException e) {
            System.err.println("Error al exportar XML: " + e.getMessage());
        }
    }

// IMPORTAR DESDE TXT
    public int[] importarTxt(java.io.File archivo) {
        int insertados = 0;
        int errores = 0;
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader(archivo));

            br.readLine(); // saltar cabecera
            br.readLine(); // saltar guiones

            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isBlank()) {
                    String[] partes = linea.split("\\|");
                    if (partes.length == 6) {
                        try {
                            int particularId = obtenerIdPorNombre(partes[1].trim(), "PARTICULAR");
                            int profesionalId = obtenerIdPorNombre(partes[2].trim(), "PROFESIONAL");
                            int puntuacion = Integer.parseInt(partes[3].trim());
                            String comentario = partes[4].trim();
                            String fecha = partes[5].trim();

                            if (particularId != -1 && profesionalId != -1) {
                                insertar(particularId, profesionalId, puntuacion, comentario, fecha);
                                insertados++;
                            } else {
                                errores++;
                            }
                        } catch (NumberFormatException e) {
                            errores++;
                        }
                    }
                }
            }
            br.close();

        } catch (java.io.IOException e) {
            System.err.println("Error al importar TXT: " + e.getMessage());
        }
        return new int[]{insertados, errores}; // devuelve [0]=insertados [1]=errores
    }

// IMPORTAR DESDE XML
    public int[] importarXml(java.io.File archivo) {
        int insertados = 0;
        int errores = 0;
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader(archivo));

            String linea;
            String particular = "", profesional = "", puntuacion = "",
                    comentario = "", fecha = "";

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                if (linea.startsWith("<particular>")) {
                    particular = linea.replace("<particular>", "").replace("</particular>", "").trim();
                } else if (linea.startsWith("<profesional>")) {
                    profesional = linea.replace("<profesional>", "").replace("</profesional>", "").trim();
                } else if (linea.startsWith("<puntuacion>")) {
                    puntuacion = linea.replace("<puntuacion>", "").replace("</puntuacion>", "").trim();
                } else if (linea.startsWith("<comentario>")) {
                    comentario = linea.replace("<comentario>", "").replace("</comentario>", "").trim();
                } else if (linea.startsWith("<fecha>")) {
                    fecha = linea.replace("<fecha>", "").replace("</fecha>", "").trim();
                } else if (linea.startsWith("</valoracion>")) {
                    try {
                        int particularId = obtenerIdPorNombre(particular, "PARTICULAR");
                        int profesionalId = obtenerIdPorNombre(profesional, "PROFESIONAL");
                        int punt = Integer.parseInt(puntuacion);

                        if (particularId != -1 && profesionalId != -1) {
                            insertar(particularId, profesionalId, punt, comentario, fecha);
                            insertados++;
                        } else {
                            errores++;
                        }
                    } catch (NumberFormatException e) {
                        errores++;
                    }
                    particular = "";
                    profesional = "";
                    puntuacion = "";
                    comentario = "";
                    fecha = "";
                }
            }
            br.close();

        } catch (java.io.IOException e) {
            System.err.println("Error al importar XML: " + e.getMessage());
        }
        return new int[]{insertados, errores};
    }

// Busca ID de usuario por nombre y rol. Devuelve -1 si no existe
    public int obtenerIdPorNombre(String nombre, String rol) {
        sql = "SELECT id FROM usuarios WHERE nombre = ? AND rol = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, rol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
        return -1;
    }

}
