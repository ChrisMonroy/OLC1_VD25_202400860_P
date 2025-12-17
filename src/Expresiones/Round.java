/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;
import Abstracto.Instruccion;
import Simbolo.Tipo;
import Simbolo.Arbol;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Errores.Errores;
/**
 *
 * @author Christoper
 */
public class Round extends Instruccion{
    private Instruccion instruccion;

    public Round(Instruccion instruccion, Tipo tipo, int linea, int col) {
        super(new Tipo(Datos.ENTERO), linea, col);
        this.instruccion = instruccion;
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        Object valor = instruccion.interpretar(arbol, tabla);
        
        if (valor instanceof Errores){
            return valor;
        }
        if (valor instanceof Double){
            return (int) Math.round((Double) valor);
        } else if(valor instanceof Integer){
            return valor; // Ya es entero
        } else {
            new Errores("Semantico", "round() solo acepta entero y double"+valor.getClass(), this.linea, this.col);
        }
        return 0;
    }
    }
