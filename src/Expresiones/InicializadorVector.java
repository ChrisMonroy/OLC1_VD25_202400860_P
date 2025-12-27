/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import java.util.LinkedList;

/**
 *
 * @author Christoper
 */
public class InicializadorVector extends Instruccion {
    private LinkedList<Instruccion> expresiones;

    public InicializadorVector(LinkedList<Instruccion> expresiones, int linea, int col) {
        super(null, linea, col);
        this.expresiones = expresiones;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        if (expresiones == null || expresiones.isEmpty()) {
            // Si no sabemos la dimensión, devolvemos Object[0]
            this.tipo = new Tipo(Datos.ENTERO); // placeholder
            return new Object[0];
        }

        // Interpretar el primer elemento para ver si es un subvector
        Instruccion primerExpr = expresiones.get(0);
        Object primerValor = primerExpr.interpretar(arbol, tabla);
        if (primerValor instanceof Errores) return primerValor;

        boolean esMatriz2D = (primerValor instanceof Object[]);

        if (esMatriz2D) {
            // Es un vector 2D → crear Object[][]
            Object[][] matriz = new Object[expresiones.size()][];
            Datos tipoBase = null;

            for (int i = 0; i < expresiones.size(); i++) {
                Instruccion expr = expresiones.get(i);
                Object valor = expr.interpretar(arbol, tabla);
                if (valor instanceof Errores) return valor;

                if (!(valor instanceof Object[])) {
                    return new Errores("SEMANTICO", "Todos los elementos deben ser vectores en una matriz 2D", linea, col);
                }

                Object[] fila = (Object[]) valor;
                matriz[i] = fila;

                // Opcional: validar que todos los elementos de la fila sean del mismo tipo
                if (i == 0 && fila.length > 0) {
                    tipoBase = obtenerTipo(fila[0]);
                }
                // Validación de tipos en filas se puede hacer en Declaracion.java
            }

            this.tipo = new Tipo(Datos.ENTERO, 2); // placeholder
            return matriz;

        } else {
            // Es un vector 1D → crear Object[]
            Object[] vector = new Object[expresiones.size()];
            Datos tipoBase = null;

            for (int i = 0; i < expresiones.size(); i++) {
                Instruccion expr = expresiones.get(i);
                Object valor = expr.interpretar(arbol, tabla);
                if (valor instanceof Errores) return valor;

                if (valor instanceof Object[]) {
                    return new Errores("SEMANTICO", "No se permiten vectores anidados en un vector 1D", linea, col);
                }

                Datos tipoActual = obtenerTipo(valor);
                if (tipoActual == null) {
                    return new Errores("SEMANTICO", "Tipo no soportado en vector", linea, col);
                }

                if (i == 0) {
                    tipoBase = tipoActual;
                } else if (tipoActual != tipoBase) {
                    return new Errores("SEMANTICO", "Elementos del vector deben ser del mismo tipo", linea, col);
                }

                vector[i] = valor;
            }

            this.tipo = new Tipo(tipoBase);
            return vector;
        }
    }

    private Datos obtenerTipo(Object valor) {
        if (valor instanceof Integer) return Datos.ENTERO;
        if (valor instanceof Double) return Datos.DECIMAL;
        if (valor instanceof Boolean) return Datos.BOOLEANO;
        if (valor instanceof Character) return Datos.CARACTER;
        if (valor instanceof String) return Datos.CADENA;
        return null;
    }
}