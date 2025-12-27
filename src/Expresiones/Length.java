/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import java.lang.reflect.Array;
import java.util.List;

/**
 *
 * @author Christoper
 */
public class Length extends Instruccion {

    private Instruccion expresion;

    public Length(Instruccion expresion, int linea, int col) {
        super(new Tipo(Datos.ENTERO), linea, col);
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {

        Object valor = expresion.interpretar(arbol, tabla);

        if (valor instanceof Errores) {
            return valor;
        }

        if (valor == null) {
            return new Errores(
                "SEMANTICO",
                "No se puede aplicar length a un valor null",
                this.linea,
                this.col
            );
        }

        // Arreglos (1D o 2D)
        if (valor.getClass().isArray()) {
            return Array.getLength(valor);
        }

        // Listas dinámicas
        if (valor instanceof List) {
            return ((List<?>) valor).size();
        }

        // Cadenas
        if (valor instanceof String) {
            return ((String) valor).length();
        }

        return new Errores(
            "SEMANTICO",
            "La función length no es aplicable a este tipo",
            this.linea,
            this.col
        );
    }
}
