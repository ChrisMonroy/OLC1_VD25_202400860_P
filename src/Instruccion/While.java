/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Abstracto.Instruccion;
import java.util.LinkedList;
import Simbolo.Arbol;
import Simbolo.Datos;
import Simbolo.Tipo;
import Simbolo.TablaSimbolos;
import Errores.Errores;
/**
 *
 * @author Christoper
 */
public class While extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;

    public While(Instruccion expresion, LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }
    @Override 
public Object interpretar(Arbol arbol, TablaSimbolos tabla){
    // Evaluar la condición SIEMPRE en el entorno actual (externo)
    Object condicion = this.expresion.interpretar(arbol, tabla);
    if (condicion instanceof Errores) {
        return condicion;
    }
    
    if (!(condicion instanceof Boolean)) {
        return new Errores("Semantico", "La condicion del while tiene que devolver un valor booleano", this.linea, this.col);
    }

    while ((boolean) condicion) {
        // Crear entorno nuevo SOLO para el cuerpo
        TablaSimbolos tablaWhile = new TablaSimbolos(tabla);

        for (var ins : instrucciones) {
            Object resultado = ins.interpretar(arbol, tablaWhile);
            if (resultado instanceof Errores) {
                return resultado;
            }
        }

        // Re-evaluar la condición en el ENTORNO EXTERNO (tabla), no en tablaWhile
        condicion = this.expresion.interpretar(arbol, tabla);
        if (condicion instanceof Errores) {
            return condicion;
        }

        if (!(condicion instanceof Boolean)) {
            return new Errores("Semantico", "La condicion del while tiene que devolver un valor booleano", this.linea, this.col);
        }
    }
    return null;
}
}
