/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;
import Simbolo.Tipo;
import Simbolo.Arbol;
import Abstracto.Instruccion;
import Simbolo.TablaSimbolos;
import Simbolo.Datos;
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
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        if(this.tipo.getTipo() == Datos.CADENA){
            String  texto =  valor.toString();
            texto = texto.replace("\\n", "\n");
            return texto;
        }
        return this.valor;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
    
}
