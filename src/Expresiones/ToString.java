/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;
import Simbolo.Arbol;
import Simbolo.Datos;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Abstracto.Instruccion;
import Errores.Errores;
import Simbolo.Tipo;
/**
 *
 * @author Christoper
 */
public class ToString extends Instruccion{
    private Instruccion instruccion;

    public ToString(Instruccion instruccion, Tipo tipo, int linea, int col) {
        super(new Tipo(Datos.CADENA), linea, col);
        this.instruccion = instruccion;
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        Object valor = instruccion.interpretar(arbol, tabla);
        if (valor instanceof Errores){
            return valor;
        }
        
        if (valor == null){
            new Errores("Semantico", "No puede haber un toString nulo", this.linea, this.col);
        }
        if (valor instanceof Integer || valor instanceof Double || valor instanceof Boolean ||
                valor instanceof Character){
            return valor.toString();
        } else if(valor instanceof String){
            return valor;
        } else {
            new Errores("Semantico", "No se acepto la clase del valor", this.linea, this.col);
        }
        return null;
    }
}
