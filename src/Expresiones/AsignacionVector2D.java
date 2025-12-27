/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.TablaSimbolos;
import Simbolo.Tipo;
import Simbolo.Simbolo;
import Errores.Errores;

/**
 *
 * @author Christoper
 */
public class AsignacionVector2D extends Instruccion {
    private String id;
    private Instruccion indice1;
    private Instruccion indice2;
    private Instruccion expresion;

    public AsignacionVector2D(String id, Instruccion indice1, Instruccion indice2, Instruccion expresion, int linea, int col) {
        super(null, linea, col);
        this.id = id;
        this.indice1 = indice1;
        this.indice2 = indice2;
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        Simbolo simbolo = tabla.getVariable(id);
        if (simbolo == null) {
            return new Errores("SEMANTICO", "Matriz '" + id + "' no existe", linea, col);
        }

        Tipo tipoSimbolo = simbolo.getTipo();
        if (tipoSimbolo.getDimensiones() != 2) {
            return new Errores("SEMANTICO", "'" + id + "' no es una matriz 2D", linea, col);
        }

        Object idx1Obj = indice1.interpretar(arbol, tabla);
        Object idx2Obj = indice2.interpretar(arbol, tabla);
        if (idx1Obj instanceof Errores) return idx1Obj;
        if (idx2Obj instanceof Errores) return idx2Obj;
        if (!(idx1Obj instanceof Integer) || !(idx2Obj instanceof Integer)) {
            return new Errores("SEMANTICO", "Índices deben ser enteros", linea, col);
        }

        int i = (Integer) idx1Obj;
        int j = (Integer) idx2Obj;

        Object valorActual = simbolo.getValor();
        if (valorActual == null) {
            return new Errores("SEMANTICO", "Matriz '" + id + "' no inicializada", linea, col);
        }

        Object[][] matriz;
        try {
            matriz = (Object[][]) valorActual;
        } catch (ClassCastException e) {
            return new Errores("SEMANTICO", "Error interno: '" + id + "' no es una matriz válida", linea, col);
        }

        if (i < 0 || i >= matriz.length) {
            return new Errores("SEMANTICO", "Índice de fila fuera de rango en '" + id + "'", linea, col);
        }

        Object[] fila = matriz[i];
        if (fila == null) {
            return new Errores("SEMANTICO", "Fila no inicializada en matriz '" + id + "'", linea, col);
        }

        if (j < 0 || j >= fila.length) {
            return new Errores("SEMANTICO", "Índice de columna fuera de rango en '" + id + "'", linea, col);
        }

        Object nuevoValor = expresion.interpretar(arbol, tabla);
        if (nuevoValor instanceof Errores) return nuevoValor;

        // ✅ Validar tipo
        if (expresion.tipo == null || expresion.tipo.getTipo() != tipoSimbolo.getTipo()) {
            return new Errores("SEMANTICO", "Tipo incompatible en asignación a matriz '" + id + "'", linea, col);
        }

        fila[j] = nuevoValor;
        // simbolo.setValor(matriz); // no necesario (referencia)

        return null;
    }
}