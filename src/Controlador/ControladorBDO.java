/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelos.UserAcceso;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author honey
 */
public class ControladorBDO {

    final String NOMBRE_BDO = "ManchuAccesos.odb";
    String RUTA_BDO = "db/" + NOMBRE_BDO;
    String jpql;
    EntityManagerFactory emf;
    EntityManager em;

    private ArrayList<UserAcceso> listaAccesos = new ArrayList<>();

    public ControladorBDO() {
        conectar();
    }

    public void conectar() {
        emf = Persistence.createEntityManagerFactory(RUTA_BDO);
        em = emf.createEntityManager();
    }

    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        System.out.println("Conexión con BDO cerrada.");
    }

    /**
     * Método para simular auto-incremental
     *
     * @return
     */
    private int siguienteId() {
        try {
            Query q = em.createQuery("SELECT MAX(u.id) FROM UserAcceso u");
            Object res = q.getSingleResult();
            if (res == null) {
                return 1;
            }
            return ((Number) res).intValue() + 1;//pide un num de res, lo convierte a int y le añade uno
        } catch (Exception e) {
            return 1;
        }
    }

    public void añadir(String username, String email, String password,
            String rol, String estado) {
        try {
            UserAcceso nuevo = new UserAcceso(username, email, password, rol, estado);
            nuevo.setId(siguienteId());
            em.getTransaction().begin();
            em.persist(nuevo);
            em.getTransaction().commit();
            System.out.println("Usuario añadido: " + nuevo);
        } catch (Exception e) {
            System.err.println("Error al añadir usuario: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public Object[][] obtenerMatriz() {
        try {
            listaAccesos.clear();
            TypedQuery<UserAcceso> q = em.createQuery(
                    "SELECT u FROM UserAcceso u ORDER BY u.id", UserAcceso.class);
            List<UserAcceso> resultado = q.getResultList();//añade los resultados de q a la lista
            Object[][] matriz = new Object[resultado.size()][6];
            for (int i = 0; i < resultado.size(); i++) {
                UserAcceso u = resultado.get(i);
                listaAccesos.add(u);
                matriz[i][0] = u.getId();
                matriz[i][1] = u.getUsername();
                matriz[i][2] = u.getEmail();
                matriz[i][3] = u.getPassword();
                matriz[i][4] = u.getRol();
                matriz[i][5] = u.getEstado();
            }
            return matriz;
        } catch (Exception e) {
            System.err.println("Error al obtener matriz: " + e.getMessage());
            return new Object[0][6];
        }
    }

    public void modificar(String idStr, String username, String email,
            String password, String rol, String estado) {
        int id;
        try {
            id = Integer.parseInt(idStr);
            UserAcceso u = em.find(UserAcceso.class, id); //el resultado se guarda en el objeto u
            if (u == null) {
                System.err.println("No existe usuario con id=" + id);
                return;
            }
            em.getTransaction().begin();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(password);
            u.setRol(rol);
            u.setEstado(estado);
            em.getTransaction().commit();
            System.out.println("Usuario modificado: " + u);
        } catch (NumberFormatException e) {
            System.err.println("Id no valido: " + idStr);
        } catch (Exception e) {
            System.err.println("Error al modificar usuario: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void borrarTodo() {
        try {
            em.getTransaction().begin();
            int filas = em.createQuery("DELETE FROM UserAcceso u").executeUpdate();
            em.getTransaction().commit();
            System.out.println("Borrados " + filas + " usuarios.");
        } catch (Exception e) {
            System.err.println("Error al borrar todo: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void borrarSelec(int fila) {
        if (fila < 0 || fila >= listaAccesos.size()) {
            System.err.println("Fila fuera de rango: " + fila);
            return;
        }
        try {
            UserAcceso u = listaAccesos.get(fila);
            UserAcceso adj = em.find(UserAcceso.class, u.getId()); //en caso d q no se controle por no entrar en la persistencia, se vuelve a coger
            if (adj == null) {
                return;
            }
            em.getTransaction().begin();
            em.remove(adj);
            em.getTransaction().commit();
            System.out.println("Usuario borrado: id=" + u.getId());
        } catch (Exception e) {
            System.err.println("Error al borrar seleccion: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void borrarPorUsername(String username) {
        try {
            em.getTransaction().begin();
            int filas = em.createQuery("DELETE FROM UserAcceso u WHERE u.username = :us")//em es un parametro provisional
                    .setParameter("us", username.trim()) //convierte el em en el username q pasamos
                    .executeUpdate();
            em.getTransaction().commit();
            System.out.println("Borrados " + filas + " usuarios con nombre=" + username);
        } catch (Exception e) {
            System.err.println("Error al borrar por usuario: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
    public void añadirDatosEjemplo() {
        // datos de prueba: (username, email, password, rol, estado)
        String[][] ejemplos = {
            {"root",    "root@root.es",   "root",  "admin",    "activo"},
            {"pedro",    "sanchez@manchupyme.es",   "espanya", "admin",    "activo"},
            {"carlos",   "gilabert@correo.es",      "aaa",   "invitado",  "activo"},
            {"ana",      "bohuelo@correo.es",         "ana1",   "invitado",  "activo"},
            {"pedro",    "pedro@correo.es",       "pedrito",   "invitado",  "inactivo"},
            {"invitado", "invitado@correo.es",    "guest",     "invitado", "activo"},
            {"mario",    "mario@correo.es",       "luigi",  "invitado",  "inactivo"},
        };
        for (String[] e : ejemplos) {
            añadir(e[0], e[1], e[2], e[3], e[4]);
        }
    }

        public UserAcceso validarLogin(String userOEmail, String password) {
        if (userOEmail == null || password == null) {
            return null;
        }
        try {
            TypedQuery<UserAcceso> q = em.createQuery(
                    "SELECT u FROM UserAcceso u "
                  + "WHERE (u.username = :ue OR u.email = :ue) "
                  + "AND u.password = :pw",
                    UserAcceso.class);
            q.setParameter("ue", userOEmail.trim());
            q.setParameter("pw", password);
            List<UserAcceso> resultado = q.getResultList(); //añadira el resultado de las coincidencias
            if (resultado.isEmpty()) { //no hay resultado
                return null;
            }
            UserAcceso u = resultado.get(0);
            if (!"activo".equalsIgnoreCase(u.getEstado())) {
                return null; // existe pero está inactivo: no se le deja entrar
            }
            return u;
        } catch (Exception e) {
            System.err.println("Error al validar login: " + e.getMessage());
            return null;
        }
    }

    
}
