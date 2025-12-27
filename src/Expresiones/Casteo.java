/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;

/**
 *
 * @author Christoper
 */
public class Casteo extends Instruccion {

    private Datos tipoDestino;
    private Instruccion expresion;

    public Casteo(Datos tipoDestino, Instruccion expresion, int linea, int col) {
        // El tipo resultante del casteo es el tipo destino
        super(new Tipo(tipoDestino), linea, col);
        this.tipoDestino = tipoDestino;
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        Object valor = this.expresion.interpretar(arbol, tabla);
        if (valor instanceof Errores) {
            return valor;
        }

        Datos tipoOrigen = this.expresion.tipo.getTipo();

        // Validar combinaciones permitidas según el PDF
        if (tipoDestino == Datos.ENTERO) {
            if (tipoOrigen == Datos.DECIMAL) {
                return ((Double) valor).intValue(); // trunca
            } else if (tipoOrigen == Datos.CARACTER) {
                return (int) ((Character) valor);
            }
        } else if (tipoDestino == Datos.DECIMAL) {
            if (tipoOrigen == Datos.ENTERO) {
                return ((Integer) valor).doubleValue();
            } else if (tipoOrigen == Datos.CARACTER) {
                return (double) ((Character) valor);
            }
        } else if (tipoDestino == Datos.CARACTER) {
            if (tipoOrigen == Datos.ENTERO) {
                return (char) ((Integer) valor).intValue();
            }
        } else if (tipoDestino == Datos.CADENA) {
            if (tipoOrigen == Datos.ENTERO) {
                return valor.toString();
            } else if (tipoOrigen == Datos.DECIMAL) {
                return valor.toString();
            }
        }

        // Si no coincide ninguna regla válida → error semántico
        return new Errores("SEMANTICO", 
            "Casteo no válido: de " + tipoOrigen + " a " + tipoDestino, 
            this.linea, this.col);
    }
}