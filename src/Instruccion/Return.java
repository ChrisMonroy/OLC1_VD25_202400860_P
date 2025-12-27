/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import Errores.Errores;
import Simbolo.Arbol;
import Simbolo.TablaSimbolos;

/**
 *
 * @author Christoper
 */
public class Return extends Instruccion {

    private Instruccion expresion;

    public Return(Instruccion expresion, int linea, int col) {
        super(null, linea, col);
        this.expresion = expresion;
    }

    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {

        if (this.expresion == null) {
            return new ReturnValor(null);
        }

        Object valor = this.expresion.interpretar(arbol, tabla);

        if (valor instanceof Errores) {
            return valor;
        }
        return new ReturnValor(valor);
    }
}
