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
public class Print extends Instruccion{
    private Instruccion expresion;
    
    public Print(Instruccion expresion, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.expresion = expresion;
    }
    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        var resultado = this.expresion.interpretar(arbol, tabla);
        arbol.Print(resultado.toString());
        return null;
    }

    public Instruccion getExpresion() {
        return expresion;
    }

    public void setExpresion(Instruccion expresion) {
        this.expresion = expresion;
    }
    
}
