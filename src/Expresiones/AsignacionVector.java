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
import Simbolo.Datos;
import Errores.Errores;

/**
 *
 * @author Christoper
 */
public class AsignacionVector extends Instruccion {

    private String id;
    private Instruccion index;
    private Instruccion valor;

    public AsignacionVector(String id, Instruccion index, Instruccion valor, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.id = id;
        this.index = index;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        Simbolo simbolo = tabla.getVariable(id);
        if (simbolo == null) {
            return new Errores("SEMANTICO", "Variable '" + id + "' no existe", linea, col);
        }

        Tipo tipoSimbolo = simbolo.getTipo();

        // ✅ Validar dimensión (sin isArreglo)
        if (tipoSimbolo.getDimensiones() != 1) {
            return new Errores("SEMANTICO", "'" + id + "' no es un vector", linea, col);
        }

        // Evaluar índice
        Object idxObj = index.interpretar(arbol, tabla);
        if (idxObj instanceof Errores) return idxObj;
        if (!(idxObj instanceof Integer)) {
            return new Errores("SEMANTICO", "El índice del vector debe ser entero", linea, col);
        }
        int idx = (Integer) idxObj;

        // ✅ Obtener arreglo como Object[]
        Object valorActual = simbolo.getValor();
        if (valorActual == null) {
            return new Errores("SEMANTICO", "Vector '" + id + "' no inicializado", linea, col);
        }

        Object[] arreglo;
        try {
            arreglo = (Object[]) valorActual;
        } catch (ClassCastException e) {
            return new Errores("SEMANTICO", "Error interno: '" + id + "' no es un vector válido", linea, col);
        }

        if (idx < 0 || idx >= arreglo.length) {
            return new Errores("SEMANTICO", "Índice fuera de rango en vector '" + id + "'", linea, col);
        }

        // Evaluar nuevo valor
        Object nuevoValor = valor.interpretar(arbol, tabla);
        if (nuevoValor instanceof Errores) return nuevoValor;

        // ✅ Validar tipo: debe coincidir con el tipo base del vector
        if (valor.tipo == null || valor.tipo.getTipo() != tipoSimbolo.getTipo()) {
            return new Errores("SEMANTICO", "Tipo incompatible en asignación al vector '" + id + "'", linea, col);
        }

        // Asignar
        arreglo[idx] = nuevoValor;
        simbolo.setValor(arreglo); // Opcional, ya que es referencia

        return null;
    }
}