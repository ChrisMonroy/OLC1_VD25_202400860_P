/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.Arbol;
import Simbolo.TablaSimbolos;
import Errores.Errores;
import java.util.List;

/**
 *
 * @author Christoper
 */
public class AccesoVector extends Instruccion {

    private String identificador;
    private Instruccion indice;

    public AccesoVector(String identificador, Instruccion indice, int linea, int col) {
        super(null, linea, col); // tipo se determina en interpretar
        this.identificador = identificador;
        this.indice = indice;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        // Evaluar el índice
        Object indiceValor = this.indice.interpretar(arbol, tabla);
        if (indiceValor instanceof Errores) {
            return indiceValor;
        }

        // Validar que el índice sea entero
        if (!(indiceValor instanceof Integer)) {
            Errores err = new Errores("SEMANTICO", "El índice debe ser entero", this.linea, this.col);
            arbol.getErrores().add(err);
            return err;
        }

        int idx = (Integer) indiceValor;
        if (idx < 0) {
            Errores err = new Errores("SEMANTICO", "Índice negativo no permitido", this.linea, this.col);
            arbol.getErrores().add(err);
            return err;
        }

        // Buscar la variable
        Simbolo sim = tabla.getVariable(this.identificador);
        if (sim == null) {
            Errores err = new Errores("SEMANTICO", "Variable no declarada: '" + this.identificador + "'", this.linea, this.col);
            arbol.getErrores().add(err);
            return err;
        }

        Tipo tipoVar = sim.getTipo();

        // ✅ Soportar tanto arreglos como listas
        if (!tipoVar.isArreglo() && !tipoVar.esLista()) {
            Errores err = new Errores("SEMANTICO", "'" + this.identificador + "' no es un vector ni una lista", this.linea, this.col);
            arbol.getErrores().add(err);
            return err;
        }

        Object valorAlmacenado = sim.getValor();
        Tipo tipoElemento = null;
        Object resultado = null;

        // 🔹 Caso 1: es una lista dinámica (List<int>, etc.)
        if (tipoVar.esLista()) {
            if (!(valorAlmacenado instanceof List)) {
                Errores err = new Errores("SEMANTICO", "Valor interno de '" + this.identificador + "' no es una lista", this.linea, this.col);
                arbol.getErrores().add(err);
                return err;
            }

            List<?> lista = (List<?>) valorAlmacenado;
            if (idx >= lista.size()) {
                Errores err = new Errores("SEMANTICO", "Índice fuera de rango en lista '" + this.identificador + "'", this.linea, this.col);
                arbol.getErrores().add(err);
                return err;
            }

            resultado = lista.get(idx);
            tipoElemento = tipoVar.getSubtipo(); // tipo del elemento en la lista
        }
        // 🔹 Caso 2: es un arreglo estático (int[], etc.)
        else if (tipoVar.isArreglo()) {
            if (!(valorAlmacenado instanceof Object[])) {
                Errores err = new Errores("SEMANTICO", "Valor interno de '" + this.identificador + "' no es un arreglo", this.linea, this.col);
                arbol.getErrores().add(err);
                return err;
            }

            Object[] arreglo = (Object[]) valorAlmacenado;
            if (idx >= arreglo.length) {
                Errores err = new Errores("SEMANTICO", "Índice fuera de rango en arreglo '" + this.identificador + "'", this.linea, this.col);
                arbol.getErrores().add(err);
                return err;
            }

            resultado = arreglo[idx];
            tipoElemento = new Tipo(tipoVar.getTipo()); // mismo tipo base
        }

        // Actualizar el tipo de esta expresión
        this.tipo = tipoElemento;
        return resultado;
    }
}