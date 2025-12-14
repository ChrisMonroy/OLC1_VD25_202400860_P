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
import Errores.Error;
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
        // se crear una tabla para el while
               // se ejecuta condicion inicial del while
        Object condicion = this.expresion.interpretar(arbol, tabla);
        // validamos que la condicion no tenga errores
        if (condicion instanceof Error){
            return condicion;
        } 
        // se ejecutan instrucciones mientras condicion sea verdadero
        while ((boolean)condicion){
            TablaSimbolos tablaWhile = new TablaSimbolos(tabla);

            for (var ins: instrucciones){
                Object resultado = ins.interpretar(arbol, tablaWhile);
                // validamos que la instruccion no traiga un errore
                if (resultado instanceof Error){
                    return resultado;
                } 
            }
            // se ejecuta condicion nuevamente despues del bloque de instrucciones
            condicion = this.expresion.interpretar(arbol, tablaWhile);
            // validamos que la condicion no tenga errores
            if (condicion instanceof Error){
                return condicion;
            } 
            // verificar que condicion sea un booleano despues del bloque de instrucciones
            if(!(condicion instanceof Boolean)){
                return new Error("Semantico", "La condicion del while tiene que devolver un valor booleano", this.linea, this.col);
            }
        }
        return null;
    }
}
