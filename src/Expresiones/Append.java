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
public class Append extends Instruccion {

    private String id;
    private Instruccion valor;

    public Append(String id, Instruccion valor, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.id = id;
        this.valor = valor;
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
        if (!simbolo.getTipo().esLista()) {
            return new Errores(
                "SEMANTICO",
                "'" + id + "' no es una lista",
                linea, col
            );
        }

        Object valorActual = simbolo.getValor();
        if (!(valorActual instanceof ArrayList)) {
            return new Errores(
                "SEMANTICO",
                "Valor interno de '" + id + "' no es una lista",
                linea, col
            );
        }

        ArrayList<Object> lista = (ArrayList<Object>) valorActual;

        // 3. Evaluar el valor a insertar
        Object nuevoValor = valor.interpretar(arbol, tabla);
        if (nuevoValor instanceof Errores) {
            return nuevoValor;
        }

        // 4. Verificar tipo (List<T>)
        Tipo tipoLista = simbolo.getTipo();      // List<T>
        Tipo tipoElemento = tipoLista.getSubtipo(); // T
        Tipo tipoValor = valor.getTipo();

        if (!tipoElemento.esCompatible(tipoValor)) {
            return new Errores(
                "SEMANTICO",
                "No se puede insertar un valor de tipo " + tipoValor +
                " en una lista de tipo " + tipoElemento,
                linea, col
            );
        }

        // 5. Insertar
        lista.add(nuevoValor);
        simbolo.setValor(lista);

        return null; // INSTRUCCIÓN (void)
    }
}
