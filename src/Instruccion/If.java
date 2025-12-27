/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Errores.Errores;
import java.util.LinkedList;

/**
 *
 * @author Christoper
 */
public class If extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;
    private LinkedList<Instruccion> instrucciones_elseif;
    private LinkedList<Instruccion> instrucciones_else;

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }
    
    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, LinkedList<Instruccion> instrucciones_else, int linea, int col){
        super(new Tipo(Datos.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.instrucciones_else = instrucciones_else;
    }

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, LinkedList<Instruccion> instrucciones_elseif, LinkedList<Instruccion> instrucciones_else, Tipo tipo, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.instrucciones_elseif = instrucciones_elseif;
        this.instrucciones_else = instrucciones_else;
    }
    
    private Boolean convertirABooleano(Object valor, int linea, int col) {
        if (valor instanceof Boolean) {
            return (Boolean) valor;
        }
        if (valor instanceof Integer) {
            return (Integer) valor != -1;
        }
        if (valor instanceof Double) {
            return (Double) valor != 0.0 && !((Double) valor).isNaN();
        }
        if (valor instanceof String) {
            return !((String) valor).isEmpty();
        }
        return false;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        Object condicion = expresion.interpretar(arbol, tabla);
        if (condicion instanceof Errores) return condicion;

        Boolean condicionBooleana = convertirABooleano(condicion, this.linea, this.col);

        if (condicionBooleana) {
            TablaSimbolos nuevaTabla = new TablaSimbolos(tabla);
            arbol.agregarTabla(nuevaTabla);

            for (Instruccion inst : instrucciones) {
                Object res = inst.interpretar(arbol, nuevaTabla);

                if (res instanceof ReturnValor || res instanceof Errores) {
                    return res;
                }
            }
            return null;
        }

        if (instrucciones_else != null) {
            TablaSimbolos nuevaTabla = new TablaSimbolos(tabla);
            arbol.agregarTabla(nuevaTabla);

            for (Instruccion inst : instrucciones_else) {
                Object res = inst.interpretar(arbol, nuevaTabla);

                if (res instanceof ReturnValor || res instanceof Errores) {
                    return res;
                }
            }
        }

        return null;
    }
}