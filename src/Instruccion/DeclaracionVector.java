/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import Expresiones.InicializadorVector;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Christoper
 */
public class DeclaracionVector extends Instruccion {
    private String id;
    private LinkedList<Instruccion> valores;
    private int dimension; // 1 para vector[], 2 para vector[][]

    public DeclaracionVector(String id, Tipo tipoBase, int dimension, LinkedList<Instruccion> valores, int linea, int col) {
        // El tipo del vector se construye con dimensiones
        super(new Tipo(tipoBase.getTipo(), dimension), linea, col);
        this.id = id;
        this.dimension = dimension;
        this.valores = valores;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        Object estructuraDatos = null;

        if (valores != null && !valores.isEmpty()) {
            // Interpretar los valores iniciales
            InicializadorVector ini = new InicializadorVector(valores, this.linea, this.col);
            Object resultado = ini.interpretar(arbol, tabla);
            if (resultado instanceof Errores) {
                return resultado;
            }
            estructuraDatos = resultado;
        } else {
            // Vector vacío
            if (dimension == 1) {
                estructuraDatos = new ArrayList<Object>();
            } else {
                estructuraDatos = new ArrayList<ArrayList<Object>>();
            }
        }

        // Validación adicional para 2D (opcional pero recomendada)
        if (dimension == 2) {
            if (!(estructuraDatos instanceof ArrayList)) {
                return new Errores("SEMANTICO", "Vector 2D mal formado", this.linea, this.col);
            }
            for (Object fila : (ArrayList<?>) estructuraDatos) {
                if (!(fila instanceof ArrayList)) {
                    return new Errores("SEMANTICO", "Una fila no es un vector", this.linea, this.col);
                }
            }
        }
        Simbolo simbolo;
        if (dimension == 1 || dimension == 2) {
            // Usa el constructor: Simbolo(Tipo, String, Object, int, int)
            simbolo = new Simbolo(this.tipo, this.id, estructuraDatos, this.linea, this.col);
        } else {
            return new Errores("SEMANTICO", "Dimensión de vector no soportada", this.linea, this.col);
        }

        boolean creado = tabla.setVariables(simbolo);
        if (!creado) {
            return new Errores("SEMANTICO", "La variable '" + id + "' ya existe", this.linea, this.col);
        }

        return null;
    }
}