/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Simbolo.Arbol;
import Simbolo.TablaSimbolos;
import Simbolo.Datos;
import Simbolo.Tipo;
import Abstracto.Instruccion;
import Errores.Error;
import java.util.LinkedList;
/**
 *
 * @author Christoper
 */
public class For extends Instruccion{
    
    private Instruccion asignacion;
    private Instruccion condicion;
    private Instruccion actualizacion;
    private LinkedList<Instruccion> instrucciones;

    public For(Instruccion asignacion, Instruccion condicion, Instruccion actualizacion, LinkedList<Instruccion> instrucciones, Tipo tipo, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.asignacion = asignacion;
        this.condicion = condicion;
        this.actualizacion = actualizacion;
        this.instrucciones = instrucciones;
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        TablaSimbolos ambitoFor = new TablaSimbolos(tabla);
        
        Object resultadoAsignacion = this.asignacion.interpretar(arbol, tabla);
        if (resultadoAsignacion instanceof Error){
            return resultadoAsignacion;
        }
        
        Object cond = this.condicion.interpretar(arbol, ambitoFor);
        if (cond instanceof Error) {
            return cond;
        }

        // Validar que la condición sea booleana
        if (!(cond instanceof Boolean)) {
            return new Error("Semantico", "La condición del for debe ser de tipo booleano.", this.linea, this.col);
        }
        
        while ((Boolean) cond){
            TablaSimbolos ambitoCuerpo = new TablaSimbolos(ambitoFor);
            
            for (Instruccion ins: instrucciones){
                Object resultado = ins.interpretar(arbol, ambitoCuerpo);
                
                if (resultado instanceof Error){
                    return resultado;
                }
                if (resultado instanceof Break){
                    return null;
                }
                if (resultado instanceof Continue){
                    break;
                }
            }
             Object resActualizacion = this.actualizacion.interpretar(arbol, ambitoFor);
            if (resActualizacion instanceof Error) {
                return resActualizacion;
                
        }
            cond = this.condicion.interpretar(arbol, ambitoFor);
            if (cond instanceof Error) {
                return cond;
            }

            if (!(cond instanceof Boolean)) {
                return new Error("Semantico", "La condición del for debe ser de tipo booleano.", this.linea, this.col);
            }
        }
        
       return null; 
    }
}
