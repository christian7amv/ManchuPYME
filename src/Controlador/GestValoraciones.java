package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class GestValoraciones {

    Connection con;
    Statement sentencia;
    String sql;

    String usuario = "root";
    String clave   = "1ZzcNRpxGCBd";
    String url     = "jdbc:mariadb://localhost:3306/machuphymev2";
    // -------------------------------------------------

    public GestValoraciones() {
        conectar();
    }

    private void conectar() {
        try {
            con = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conexion establecida con " + url);
        } catch (SQLException e) {
            System.err.println("SQL Error mensaje: " + e.getMessage());
            System.err.println("SQL Estado: "        + e.getSQLState());
            System.err.println("SQL codigo: "        + e.getErrorCode());
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
            System.err.println("SQL Estado: "        + e.getSQLState());
            System.err.println("SQL codigo: "        + e.getErrorCode());
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
            System.err.println("SQL Estado: "        + e.getSQLState());
            System.err.println("SQL codigo: "        + e.getErrorCode());
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
            System.err.println("SQL Estado: "        + e.getSQLState());
            System.err.println("SQL codigo: "        + e.getErrorCode());
        }
    }

    // ------------------------------------------------------------------
    //  BUSCAR             
    // ------------------------------------------------------------------
    public Object[][] buscarPorNombre(String nombre) {
        ArrayList<Object[]> lista = new ArrayList<>();
        sql = "SELECT v.id, u1.nombre AS particular, u2.nombre AS profesional, "
            + "v.puntuacion, v.comentario, DATE_FORMAT(v.fecha, '%d-%m-%Y') AS fecha "
            + "FROM valoraciones v "
            + "JOIN usuarios u1 ON u1.id = v.particular_id "
            + "JOIN usuarios u2 ON u2.id = v.profesional_id "
            + "WHERE u1.nombre LIKE ? OR u2.nombre LIKE ? "
            + "ORDER BY v.id";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
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

    private String convertirFecha(String fechaDDMMYYYY) {
        // Acepta tanto dd-MM-yyyy como dd/MM/yyyy
        String[] partes = fechaDDMMYYYY.split("[-/]");
        if (partes.length == 3) {
            return partes[2] + "-" + partes[1] + "-" + partes[0];
        }
        return fechaDDMMYYYY; 
    }
}