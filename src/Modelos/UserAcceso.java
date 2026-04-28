/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 *
 * @author honey
 */
@Entity
@NamedQuery(name = "queryTodosUsuariosAcceso",
        query = "SELECT u FROM User u ORDER BY u.username")
public class UserAcceso implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private int id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    
    private String password;
    private String rol;
    private String estado;

    public UserAcceso() {
    }

    public UserAcceso( String username, String email, String password, String rol, String estado) {
        
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", rol=" + rol + ", estado=" + estado + '}';
    }

    
    
}
