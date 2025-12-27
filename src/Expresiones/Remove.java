/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import java.util.ArrayList;

/**
 *
 * @author Christoper
 */
public class Remove extends Instruccion {

    private String id;
    private Instruccion indice;

    public Remove(String id, Instruccion indice, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col); // se ajusta al interpretar
        this.id = id;
        this.indice = indice;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {

        // 1. Buscar la lista
        Simbolo simbolo = tabla.getVariable(id);
        if (simbolo == null) {
            return new Errores(
                "SEMANTICO",
                "La lista '" + id + "' no existe",
                linea, col
            );
        }

        // 2. Verificar que sea lista
        Tipo tipoLista = simbolo.getTipo();
        if (!tipoLista.esLista()) {
            return new Errores(
                "SEMANTICO",
                "'" + id + "' no es una lista",
                linea, col
            );
        }

        // 🔥 El tipo del remove ES el subtipo de la lista
        this.tipo = tipoLista.getSubtipo();

        Object valorActual = simbolo.getValor();
        if (!(valorActual instanceof ArrayList)) {
            return new Errores(
                "SEMANTICO",
                "Valor interno de '" + id + "' no es una lista",
                linea, col
            );
        }

        ArrayList<Object> lista = (ArrayList<Object>) valorActual;

        // 3. Evaluar índice
        Object idxObj = indice.interpretar(arbol, tabla);
        if (idxObj instanceof Errores) {
            return idxObj;
        }

        if (!(idxObj instanceof Integer)) {
            return new Errores(
                "SEMANTICO",
                "El índice de remove() debe ser entero",
                linea, col
            );
        }

        int idx = (Integer) idxObj;

        // 4. Validar rango
        if (idx < 0 || idx >= lista.size()) {
            return new Errores(
                "SEMANTICO",
                "Índice fuera de rango en remove()",
                linea, col
            );
        }

        // 5. Remover y devolver
        Object eliminado = lista.remove(idx);
        simbolo.setValor(lista);

        return eliminado; // EXPRESIÓN
    }
}
