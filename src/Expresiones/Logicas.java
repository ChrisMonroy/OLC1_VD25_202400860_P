/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Error;

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
        this.opIzq = opIzq;
        this.opDer = opDer;
        this.operador = operador;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        if (this.operador == OperadoresLogicos.NOT) {
            Object valor = this.opUnario.interpretar(arbol, tabla);
            if (valor instanceof Error) return valor;
            return this.not(valor);
        } else {
            Object izq = this.opIzq.interpretar(arbol, tabla);
            if (izq instanceof Error) return izq;
            Object der = this.opDer.interpretar(arbol, tabla);
            if (der instanceof Error) return der;
            return switch (this.operador) {
                case AND -> this.and(izq, der);
                case OR -> this.or(izq, der);
                case XOR -> this.xor(izq, der);
                default -> new Error("SEMANTICO", "Operador lógico inválido", this.linea, this.col);
            };
        }
    }

    private Object not(Object valor) {
        var tipo = this.opUnario.tipo.getTipo();
        if (tipo != Datos.BOOLEANO) {
            return new Error("SEMANTICO", "El operador NOT solo se puede aplicar a booleanos", this.linea, this.col);
        }
        return !(Boolean) valor;
    }

    private Object validarBooleano(Object val, Datos tipo) {
        if (tipo != Datos.BOOLEANO) {
            return new Error("SEMANTICO", "Operador lógico requiere operandos booleanos", this.linea, this.col);
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