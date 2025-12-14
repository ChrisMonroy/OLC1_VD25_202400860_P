/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;
import Simbolo.Tipo;
import Simbolo.Arbol;
import Abstracto.Instruccion;
import Simbolo.TablaSimbolos;
/**
 *
 * @author Christoper
 */
public class Nativo extends Instruccion{
    
    public Object valor;
    
    public Nativo(Object valor, Tipo tipo, int linea, int colu){
        super(tipo, linea, colu);
        this.valor = valor;
    }
    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tablasimbolos){
        return this.valor;
    }
}
