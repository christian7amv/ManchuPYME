/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Libreria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author chris
 */
public class Leer {

    /**
     * Lee una cadena de texto
     *
     * @param mensaje
     * @return
     * @throws java.io.IOException
     */
    public static String LeerCadena(String mensaje) throws IOException {
        InputStreamReader flujo = new InputStreamReader(System.in, "ISO-8859-1");
        BufferedReader teclado = new BufferedReader(flujo);
        System.out.print(mensaje);
        return teclado.readLine().trim();
    }

    /**
     * Lee un int mayor a cero
     *
     * @param mensaje
     * @return
     * @throws IOException
     */
    public static int enteroMayorACero(String mensaje) throws IOException {
        InputStreamReader flujo = new InputStreamReader(System.in, "UTF-8");
        BufferedReader teclado = new BufferedReader(flujo);

        int numero = 0;
        boolean error;
        do {
            error = false;
            System.out.print(mensaje);
            try {
                numero = Integer.parseInt(teclado.readLine());
                if (numero < 0) {
                    System.err.println("Error: el numero introducido debe ser mayor de cero.");
                    error = true;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: formato de n?mero entero no v?lido.");
                error = true;
            }
        } while (error);
        return numero;
    }

    /**
     * Lee la altura en decimal entre x nums
     *
     * @param mensaje
     * @return
     * @throws IOException
     */
    public static double LeerDecimalAltura(String mensaje) throws IOException {
        InputStreamReader flujo = new InputStreamReader(System.in, "ISO-8859-1");
        BufferedReader teclado = new BufferedReader(flujo);
        double numMAX = 2;
        double numMIN = 0.5;
        //Cambia valores max y min
        double numero = 0;
        boolean error;

        do {
            error = false;
            System.out.print(mensaje);
            try {
                numero = Double.parseDouble(teclado.readLine().replace(',', '.').trim());
                if (numero < numMIN || numero > numMAX) {
                    System.err.println("Error: el n�mero debe estar entre " +numMIN +" y " + numMAX+".");
                    error = true;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: introduce un n�mero decimal v�lido.");
                error = true;
            }
        } while (error);

        return numero;

    }
    /**
     * Solo lee una letra
     * @param mensaje
     * @return
     * @throws IOException 
     */
    public static char LeerLetra(String mensaje) throws IOException {
        String cadena;
        boolean error;
        char letra = '0';

        do {
            try { //captura errores
                error = false;
                cadena = LeerCadena(mensaje);
                if (!(cadena.length() == 1)) { //comprueba que cadena es solo un car�cter
                    System.err.println("Error, solo un car�cter.");
                    error = true; //si es m�s o menos de un car�cter lo devuelve en blanco
                }

                letra = cadena.charAt(0); //convierte el string a char
                letra = Character.toUpperCase(letra); //mayusculas

                if (!((letra >= 'A' && letra <= 'Z') || letra == '�')) { //Si la palara es minuscula o no tiene una de estas letras error (ignora si tiene letras o no)
                    System.err.println("Error no uses n�meros, tildes ni caracteres especiales");
                    error = true;
                }
            } catch (Exception e) { //este try catch esta porque si no pones nada (enter) da una excepcion
                System.err.println("Error en la lectura, vuelva a introducir un car�cter.");
                error = true;
            }
        } while (error);

        return letra;
    }
    public static String LeerTelefono(String mensaje) throws IOException {
        InputStreamReader flujo = new InputStreamReader(System.in, "UTF-8");
        BufferedReader teclado = new BufferedReader(flujo);

        String telefono = "";
        boolean lecturaCorrecta;

        do {
            lecturaCorrecta = true;
            System.out.print(mensaje);
            try {
                telefono = teclado.readLine();
            } catch (IOException ex) {
                System.out.println("\tError!!!: fallo en la lectura");
            }

            // Expresi�n regular para validar tel�fono (9 o 14 d�gitos, opcional '+')
            String regex = "^[\\+]?\\d{9,14}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(telefono);

            if (!matcher.matches()) {
                System.err.println("\tError: formato de tel�fono incorrecto. Ejemplo: +34600123456 o 600123456");
                lecturaCorrecta = false;
            }
        } while (!lecturaCorrecta);

        return telefono;
    }
/**
     * M�todo para leer un correo electr�nico v�lido. Acepta formatos tipo
     * Ejemplo: usuario@mail.com
     *
     * @param mensaje
     * @return
     * @throws java.io.IOException
     */
    public static String LeerEmail(String mensaje) throws IOException {
        InputStreamReader flujo = new InputStreamReader(System.in, "UTF-8");
        BufferedReader teclado = new BufferedReader(flujo);

        String email = "";
        boolean lecturaCorrecta;

        do {
            lecturaCorrecta = true;
            System.out.print(mensaje);
            try {
                email = teclado.readLine();
            } catch (IOException ex) {
                System.out.println("\tError: fallo en la lectura");
            }

            // Expresi�n regular para validar email
            String regex = "^\\w+([\\.-]?\\w+)@\\w+([\\.-]?\\w+)(\\.\\w{2,3})+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            //intenta hacer coincidir toda la secuencia de entrada
            if (!matcher.matches()) {
                System.err.println("\tError: formato de email incorrecto.");
                lecturaCorrecta = false;
            }
        } while (!lecturaCorrecta);

        return email;
    }
    /**
     * Lee un n�mero decimal con validaci�n
     *
     * @param mensaje
     * @return
     * @throws java.io.IOException
     */
    public static double LeerDecimal(String mensaje) throws IOException {
        InputStreamReader flujo = new InputStreamReader(System.in, "ISO-8859-1");
        BufferedReader teclado = new BufferedReader(flujo);
        String texto;
        double numero = 0;
        boolean error;
        do {
            error = false;
            System.out.print(mensaje);
            texto = teclado.readLine();
            try {
                numero = Double.parseDouble(texto.replace(',', '.').trim());
            } catch (NumberFormatException e) {
                System.err.println("Error: formato de n�mero decimal no v�lido.");
                error = true;
            }
        } while (error);
        return numero;
    }
    /**
     * Limppia la pantalla con 20 intros
     * 
     */
    public static void LimpiezaPantalla() {

        final int numLineas = 20;

        for (int i = 0; i < numLineas; i++) {
            System.out.println("");

        }
    }
     public static boolean ComprobarEmail(String email) {
       
            // Expresi�n regular para validar email
            String regex = "^\\w+([\\.-]?\\w+)@\\w+([\\.-]?\\w+)(\\.\\w{2,3})+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
        //intenta hacer coincidir toda la secuencia de entrada
        return matcher.matches();
    }
}
