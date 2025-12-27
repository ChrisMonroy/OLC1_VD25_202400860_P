/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import java.util.LinkedList;
import java.util.HashMap;

/**
 *
 * @author Christoper
 */
public class Funcion extends Instruccion {

    public String id;
    public LinkedList<HashMap> parametros;
    LinkedList<Instruccion> instrucciones;

    public Funcion(String id,
                   LinkedList<HashMap> parametros,
                   LinkedList<Instruccion> instrucciones,
                   Tipo tipo,
                   int linea,
                   int col) {

        super(tipo, linea, col);
        this.id = id;
        this.parametros = parametros;
        this.instrucciones = instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        // Solo registrar
        arbol.addFunciones(this);
        return null;
    }

    public Object ejecutar(Arbol arbol, TablaSimbolos tablaLocal) {
    arbol.agregarTabla(tablaLocal);
    arbol.enterRecursion();
    try {
        for (Instruccion instr : instrucciones) {
            Object res = instr.interpretar(arbol, tablaLocal);
            if (res instanceof ReturnValor) {
                // Si la función es VOID, el return debe ser null o ignorado
                if (this.tipo.getTipo() == Datos.VOID) {
                    return null; // void: return sin valor
                }
                return ((ReturnValor) res).getValor();
            }
            if (res instanceof Errores) {
                return res;
            }
        }

        // ✅ Solo exigir retorno si NO es VOID
        if (this.tipo.getTipo() != Datos.VOID) {
            return new Errores(
                "SEMANTICO",
                "La función '" + id + "' debe retornar un valor",
                linea, col
            );
        }

        return null; // void: ok sin return explícito
    } finally {
        arbol.exitRecursion();
    }
}

}
