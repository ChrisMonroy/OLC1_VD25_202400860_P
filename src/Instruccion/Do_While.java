/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import java.util.LinkedList;
import Simbolo.Arbol;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Simbolo.Tipo;
import Errores.Errores;

/**
 *
 * @author Christoper
 */
public class Do_While extends Instruccion {
    private Instruccion condicion;
    private LinkedList<Instruccion> instrucciones;

    public Do_While(Instruccion condicion, LinkedList<Instruccion> instrucciones, Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.condicion = condicion;
        this.instrucciones = instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        // El cuerpo del do-while debe ejecutarse AL MENOS UNA VEZ
        do {
            // Crear tabla de símbolos local para el ámbito del do-while
            TablaSimbolos tablaLocal = new TablaSimbolos(tabla);

            // Ejecutar las instrucciones del cuerpo
            for (Instruccion ins : instrucciones) {
                Object resultado = ins.interpretar(arbol, tablaLocal);

                // Manejo de errores
                if (resultado instanceof Errores) {
                    return resultado;
                }

                // Manejo de 'break': sale del do-while
                if (resultado instanceof Break) {
                    return null;
                }

                // Manejo de 'continue': salta al final del ciclo (reevaluar condición)
                if (resultado instanceof Continue) {
                    break; // Sale del for, pero continúa con la evaluación de la condición
                }
            }

            // Reevaluar la condición al FINAL de cada iteración
            Object valorCondicion = condicion.interpretar(arbol, tabla);
            if (valorCondicion instanceof Errores) {
                return valorCondicion;
            }

            // Validar que la condición sea booleana
            if (!(valorCondicion instanceof Boolean)) {
                return new Errores("SEMANTICO",
                    "La condición del do-while debe ser de tipo booleano.",
                    this.linea, this.col);
            }

            // Si la condición es false, termina el ciclo
            if (!((Boolean) valorCondicion)) {
                break;
            }

        } while (true); // El control lo hace internamente con break

        return null;
    }
}