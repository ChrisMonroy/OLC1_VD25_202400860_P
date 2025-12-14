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
public class Do_While extends Instruccion{
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;

    public Do_While(Instruccion expresion, LinkedList<Instruccion> instrucciones, Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        Object condicion = this.expresion.interpretar(arbol, tabla);
        
        if (condicion instanceof Error){
            return condicion;
        }
        
        while ((boolean) condicion){
            TablaSimbolos tablaDoWhile = new TablaSimbolos(tabla);
            
            for (var ins : instrucciones) {
                if (ins instanceof Break) {
                    return ins;
                }
                Object resultado = ins.interpretar(arbol, tablaDoWhile);
                if (resultado instanceof Error) {
                    return resultado;
                }
            }
            if (!(condicion instanceof Boolean)) {
                return new Error("Semantico", "La condición del do-while debe ser de tipo booleano", this.linea, this.col);
            }
        }
        return null;
    }
}
