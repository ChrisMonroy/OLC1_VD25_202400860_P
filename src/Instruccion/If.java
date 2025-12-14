/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import java.util.LinkedList;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Errores.Error;
/**
 *
 * @author Christoper
 */
public class If extends Instruccion{
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
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        var condicion = this.expresion.interpretar(arbol, tabla);
        if (condicion instanceof Error){
            return condicion;
        }
        if (!(condicion instanceof Boolean)){
            return new Error("Error semantico", "La condicion del if tiene que devolver un valor Booleano", this.linea, this.col);
        }
        boolean ejecutarIf = (Boolean) condicion;
        if(ejecutarIf){
            var nuevaTabla = new TablaSimbolos(tabla);
            
            for (var inst: instrucciones){
                var res = inst.interpretar(arbol, nuevaTabla);
                
                if (res instanceof Error){
                    return res;
                }
            }
            return true;
        }
        
        if (instrucciones_elseif != null){
            for(Instruccion elseif: instrucciones_elseif){
                var res = elseif.interpretar(arbol, tabla);
                if (res instanceof Error){
                    return res;
                }
                if (res instanceof Boolean){
                    return true;
                }
            }
        }
        if (instrucciones_else != null){
                var nuevaTabla = new TablaSimbolos(tabla);
                for (var instElse: instrucciones_else){
                    var res = instElse.interpretar(arbol, nuevaTabla);
                    if (res instanceof Error){
                        return res;
                    }
                }
            }
     return null;   
    }
    
}
