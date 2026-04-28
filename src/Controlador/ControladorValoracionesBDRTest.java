package Controlador;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControladorValoracionesBDRTest {

    @org.junit.jupiter.api.Test
    void obtenerParticulares() {
// 1. Instanciamos el controlador
        ControladorValoracionesBDR gestor = new ControladorValoracionesBDR();

        System.out.println("==================================================");
        System.out.println("INICIANDO PRUEBA VISUAL: LEYENDO BASE DE DATOS...");
        System.out.println("==================================================");

        // --- PASO NUEVO: Insertamos un dato "a la fuerza" para asegurar que haya algo ---
        // Usamos el ID 2 (Ana) y el ID 1 (Carlos)
        gestor.insertar(2, 1, 5, "PRUEBA DESDE JUNIT", "28-04-2026");

        // 2. Ejecutamos el método que trae todos los datos
        Object[][] matriz = gestor.obtenerMatriz();

        // 3. Imprimimos los datos en la consola para VERLOS
        for (int i = 0; i < matriz.length; i++) {
            System.out.println("Fila " + i + " -> " +
                    "Particular: " + matriz[i][1] +
                    " | Profesional: " + matriz[i][2] +
                    " | Nota: " + matriz[i][3] + " estrellas" +
                    " | Fecha: " + matriz[i][5]);
        }

        System.out.println("==================================================");
        System.out.println("TOTAL DE FILAS ENCONTRADAS: " + matriz.length);
        System.out.println("==================================================");

        // 4. Aserciones
        assertNotNull(matriz, "La matriz no puede ser nula");
        assertTrue(matriz.length > 0, "Debe haber valoraciones en la tabla");
    }
}