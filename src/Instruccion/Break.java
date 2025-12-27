/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Abstracto.Instruccion;
import Simbolo.*;
/**
 *
 * @author Christoper
 */
public class Break extends Instruccion{
    
    public Break (int linea, int col){
        super(new Tipo(Datos.VOID), linea, col);
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        return null;
    }
}
