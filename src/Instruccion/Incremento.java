/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Simbolo.Tipo;
import Errores.Errores;
/**
 *
 * @author Christoper
 */
public class Incremento extends Instruccion{
    private String id;
    private boolean esIncremento;

    public Incremento(String id, boolean esIncremento, int linea, int columna) {
        super(new Tipo(Datos.VOID), linea, columna);
        this.id = id;
        this.esIncremento = esIncremento;
    }
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        
        var simbolo = tabla.getVariable(id);
        if (simbolo == null) {
            return new Errores("SEMANTICO", "La variable '" + id + "' no está declarada.", this.linea, this.col);
        }

        Object valor = simbolo.getValor();
        if (valor == null) {
            return new Errores("SEMANTICO", "La variable '" + id + "' no ha sido inicializada.", this.linea, this.col);
        }
        
        if (!(valor instanceof Integer || valor instanceof Double || valor instanceof Character)) {
            return new Errores("SEMANTICO", 
                "No se puede aplicar incremento/decremento al tipo '" + simbolo.getTipo().getTipo() + "'.", 
                this.linea, this.col);
        }
        
        Object nuevoValor;
        if (valor instanceof Integer) {
            int v = (Integer) valor;
            nuevoValor = esIncremento ? v + 1 : v - 1;
        } else if (valor instanceof Double) {
            double v = (Double) valor;
            nuevoValor = esIncremento ? v + 1.0 : v - 1.0;
        } else { // Character
            char v = (Character) valor;
            nuevoValor = (char) (esIncremento ? v + 1 : v - 1);
        }
        
        simbolo.setValor(nuevoValor);

        return null;
    }
        
}
