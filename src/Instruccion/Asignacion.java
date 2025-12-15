/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.TablaSimbolos;
import Simbolo.Datos;
import Abstracto.Instruccion;
import Errores.Errores;
/**
 *
 * @author Christoper
 */
public class Asignacion extends Instruccion{
    private String id;
    public Instruccion valor;
    
    public Asignacion(String id, Instruccion valor, int linea, int col){
        super(new Tipo(Datos.VOID), linea, col);
        this.id = id;
        this.valor = valor;
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        var variable = tabla.getVariable(id);
        if (variable == null){
            return new Errores("Semantico", "Variable no existente", this.linea, this.col);
        }
        var newValor = this.valor.interpretar(arbol, tabla);
        if (newValor instanceof Errores){
            return newValor;
        }
        if(variable.getTipo().getTipo() != this.valor.tipo.getTipo()){
            return new Errores("Semantico", "Tipos Erroneos", this.linea, this.col);
        }
        variable.setValor(newValor);
        return null;
    }
}
