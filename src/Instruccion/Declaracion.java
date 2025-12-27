/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import Errores.Errores;
import Simbolo.Arbol;
import Simbolo.Simbolo;
import Simbolo.TablaSimbolos;
import Simbolo.Tipo;
import Simbolo.Datos;

/**
 *
 * @author Christoper
 */
public class Declaracion extends Instruccion {

    public String identificador;
    public Instruccion valor;

    public Declaracion(String identificador, Instruccion valor, Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.identificador = identificador;
        this.valor = valor;
    }

    public Declaracion(String identificador, Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.identificador = identificador;
        this.valor = null;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        Object valorInterpretado = null;

        if (this.valor != null) {
            valorInterpretado = this.valor.interpretar(arbol, tabla);
            if (valorInterpretado instanceof Errores) {
                return valorInterpretado;
            }

            if (this.tipo.getDimensiones() == 0) {
                if (this.valor.tipo == null ||
                    this.valor.tipo.getTipo() != this.tipo.getTipo()) {

                    return new Errores(
                        "semantico",
                        "Tipos erroneos en la declaración de '" + this.identificador + "'",
                        this.linea,
                        this.col
                    );
                }
            }

        } else {

            if (this.tipo.getDimensiones() == 0) {
                valorInterpretado = getValorPorDefecto(this.tipo.getTipo());

            } else if (this.tipo.getDimensiones() == 1) {
                int[] tamanos = this.tipo.getTamanos();
                if (tamanos == null || tamanos.length < 1) {
                    return new Errores(
                        "semantico",
                        "Tamaño no definido para vector '" + this.identificador + "'",
                        this.linea,
                        this.col
                    );
                }

                int size = tamanos[0];
                Object[] arreglo = new Object[size];
                for (int i = 0; i < size; i++) {
                    arreglo[i] = getValorPorDefecto(this.tipo.getTipo());
                }
                valorInterpretado = arreglo;

            } else if (this.tipo.getDimensiones() == 2) {
                int[] tamanos = this.tipo.getTamanos();
                if (tamanos == null || tamanos.length < 2) {
                    return new Errores(
                        "semantico",
                        "Tamaños no definidos para matriz '" + this.identificador + "'",
                        this.linea,
                        this.col
                    );
                }

                int filas = tamanos[0];
                int columnas = tamanos[1];
                Object[][] matriz = new Object[filas][columnas];

                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        matriz[i][j] = getValorPorDefecto(this.tipo.getTipo());
                    }
                }
                valorInterpretado = matriz;
            }
        }

        Tipo tipoFinal = this.tipo;
        if (this.tipo.getDimensiones() > 0) {
            tipoFinal = new Tipo(
                this.tipo.getTipo(),
                this.tipo.getDimensiones(),
                this.tipo.getTamanos()
            );
        }

        Simbolo s = new Simbolo(tipoFinal, this.identificador, valorInterpretado, this.linea, this.col);
        boolean creacion = tabla.setVariables(s);
        if (!creacion) {
            return new Errores(
                "semantico",
                "Variable ya existente",
                this.linea,
                this.col
            );
        }

        return null;
    }

    private Object getValorPorDefecto(Datos tipo) {
        switch (tipo) {
            case ENTERO: return 0;
            case DECIMAL: return 0.0;
            case BOOLEANO: return false;
            case CARACTER: return '\0';
            case CADENA: return "";
            default: return null;
        }
    }

    public String getId() {
        return identificador;
    }

    public void setId(String identificador) {
        this.identificador = identificador;
    }

    public Instruccion getValor() {
        return valor;
    }

    public void setValor(Instruccion valor) {
        this.valor = valor;
    }
    
    
}
