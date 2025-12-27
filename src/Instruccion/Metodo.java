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
public class Metodo extends Instruccion {

    public String id;
    public LinkedList<HashMap> parametros;
    LinkedList<Instruccion> instrucciones;

    public Metodo(String id, LinkedList<HashMap> parametros,
                  LinkedList<Instruccion> instrucciones,
                  Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.id = id;
        this.parametros = parametros;
        this.instrucciones = instrucciones;
    }

    @Override
public Object interpretar(Arbol arbol, TablaSimbolos tabla) {

    arbol.enterRecursion();
    try {
        if (!this.tipo.getTipo().equals(Datos.VOID)) {
            return new Errores(
                "SEMANTICO",
                "Los métodos deben ser de tipo 'void'",
                this.linea,
                this.col
            );
        }

        TablaSimbolos tablaLocal = new TablaSimbolos();
        tablaLocal.setTablaAnterior(tabla);
        arbol.agregarTabla(tablaLocal);

        for (Instruccion instr : this.instrucciones) {
            Object resultado = instr.interpretar(arbol, tablaLocal);

            if (resultado instanceof Errores) {
                return resultado;
            }

            if (resultado instanceof ReturnValor rv) {
                if (rv.getValor() != null) {
                    return new Errores(
                        "SEMANTICO",
                        "Un método void no puede retornar un valor",
                        this.linea,
                        this.col
                    );
                }
                return null;
            }
        }

        return null;

    } finally {
        arbol.exitRecursion();
    }
}

}