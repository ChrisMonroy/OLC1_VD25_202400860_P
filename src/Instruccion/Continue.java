/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Simbolo.Datos;
import Simbolo.Tipo;
import Abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.TablaSimbolos;
/**
 *
 * @author Christoper
 */
public class Continue extends Instruccion{

    public Continue(int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        return this;
    }
}
