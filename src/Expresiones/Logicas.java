/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Errores.Errores;
import Simbolo.*;

/**
 *
 * @author Christoper
 */
public class Logicas extends Instruccion {

    private Instruccion opIzq;
    private Instruccion opDer;
    private OperadoresLogicos operador;
    private Instruccion opUnario; // solo para NOT

    // Constructor unario (NOT)
    public Logicas(OperadoresLogicos operador, Instruccion opUnario, int linea, int col) {
        super(new Tipo(Datos.BOOLEANO), linea, col);
        this.operador = operador;
        this.opUnario = opUnario;
    }

    // Constructor binario (AND, OR, XOR)
    public Logicas(Instruccion opIzq, Instruccion opDer, OperadoresLogicos operador, int linea, int col) {
        super(new Tipo(Datos.BOOLEANO), linea, col);
        // El parser puede construir el nodo pasando null en el primer argumento
        // para representar el operador unario (NOT). Manejar ambos casos aquí.
        if (opIzq == null) {
            this.opUnario = opDer;
        } else {
            this.opIzq = opIzq;
            this.opDer = opDer;
        }
        this.operador = operador;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        if (this.operador == OperadoresLogicos.NOT) {
            Object valor = this.opUnario.interpretar(arbol, tabla);
            if (valor instanceof Errores) return valor;
            return this.not(valor);
        } else {
            Object izq = this.opIzq.interpretar(arbol, tabla);
            if (izq instanceof Errores) return izq;
            Object der = this.opDer.interpretar(arbol, tabla);
            if (der instanceof Errores) return der;
            return switch (this.operador) {
                case AND -> this.and(izq, der);
                case OR -> this.or(izq, der);
                case XOR -> this.xor(izq, der);
                default -> new Errores("SEMANTICO", "Operador lógico inválido", this.linea, this.col);
            };
        }
    }

    private Object not(Object valor) {
        var tipo = this.opUnario.tipo.getTipo();
        if (tipo != Datos.BOOLEANO) {
            return new Errores("SEMANTICO", "El operador NOT solo se puede aplicar a booleanos", this.linea, this.col);
        }
        return !(Boolean) valor;
    }

    private Object validarBooleano(Object val, Datos tipo) {
        if (tipo != Datos.BOOLEANO) {
            return new Errores("SEMANTICO", "Operador lógico requiere operandos booleanos", this.linea, this.col);
        }
        return null;
    }

    private Object and(Object izq, Object der) {
        var tipoIzq = this.opIzq.tipo.getTipo();
        var tipoDer = this.opDer.tipo.getTipo();

        var errorIzq = validarBooleano(izq, tipoIzq);
        if (errorIzq != null) return errorIzq;
        var errorDer = validarBooleano(der, tipoDer);
        if (errorDer != null) return errorDer;

        return (Boolean) izq && (Boolean) der;
    }

    private Object or(Object izq, Object der) {
        var tipoIzq = this.opIzq.tipo.getTipo();
        var tipoDer = this.opDer.tipo.getTipo();

        var errorIzq = validarBooleano(izq, tipoIzq);
        if (errorIzq != null) return errorIzq;
        var errorDer = validarBooleano(der, tipoDer);
        if (errorDer != null) return errorDer;

        return (Boolean) izq || (Boolean) der;
    }

    private Object xor(Object izq, Object der) {
        var tipoIzq = this.opIzq.tipo.getTipo();
        var tipoDer = this.opDer.tipo.getTipo();

        var errorIzq = validarBooleano(izq, tipoIzq);
        if (errorIzq != null) return errorIzq;
        var errorDer = validarBooleano(der, tipoDer);
        if (errorDer != null) return errorDer;

        return (Boolean) izq ^ (Boolean) der;
    }
}