/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;
import Simbolo.Simbolo;
import Simbolo.Arbol;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Errores.Errores;
import Simbolo.Tipo;
import Abstracto.Instruccion;
/**
 *
 * @author Christoper
 */
public class AccesoVar extends Instruccion{
    private String id;

    public AccesoVar(String id, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.id = id;
    }

    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        var valor = tabla.getVariable(this.id);
        if (valor == null){
            return new Errores("semantico", "Variable no existente", this.linea, this.col);
        }
        this.tipo.setTipo(valor.getTipo().getTipo());
        return valor.getValor();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
