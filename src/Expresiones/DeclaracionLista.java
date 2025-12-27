/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Simbolo.*;
import Abstracto.Instruccion;
import Errores.Errores;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christoper
 */
public class DeclaracionLista extends Instruccion {

    private String identificador;
    private List<Instruccion> valoresIniciales;

    public DeclaracionLista(String identificador, Tipo tipo, int linea, int col) {
        this(identificador, tipo, null, linea, col);
    }

    public DeclaracionLista(String identificador, Tipo tipo, List<Instruccion> valoresIniciales, int linea, int col) {
        super(tipo, linea, col);
        this.identificador = identificador;
        this.valoresIniciales = valoresIniciales;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {

        if (!this.tipo.esLista()) {
            Errores err = new Errores(
                "SEMANTICO",
                "El tipo declarado para '" + identificador + "' no es una lista",
                linea, col
            );
            arbol.getErrores().add(err);
            return err;
        }

        Tipo tipoElemento = this.tipo.getSubtipo();
        ArrayList<Object> valores = new ArrayList<>();

        if (valoresIniciales != null) {
            for (Instruccion expr : valoresIniciales) {

                Object valor = expr.interpretar(arbol, tabla);
                if (valor instanceof Errores) {
                    arbol.getErrores().add((Errores) valor);
                    continue;
                }

                Tipo tipoValor = expr.getTipo();
                if (!tipoElemento.esCompatible(tipoValor)) {
                    Errores err = new Errores(
                        "SEMANTICO",
                        "No se puede insertar un valor de tipo " + tipoValor +
                        " en una lista de tipo " + tipoElemento,
                        linea, col
                    );
                    arbol.getErrores().add(err);
                    continue;
                }

                valores.add(valor);
            }
        }

        // ✅ Crea el símbolo pasando la lista como 'valor' (Object)
        Simbolo simbolo = new Simbolo(this.tipo, this.identificador, valores, this.linea, this.col);
        boolean creado = tabla.setVariables(simbolo);

        if (!creado) {
            Errores err = new Errores(
                "SEMANTICO",
                "La variable ya existe: '" + identificador + "'",
                linea, col
            );
            arbol.getErrores().add(err);
            return err;
        }

        return null;
    }
}