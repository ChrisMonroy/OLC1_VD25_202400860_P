/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Christoper
 */
public class Find extends Instruccion {

    private String id;
    private Instruccion valorBuscado;

    public Find(String id, Instruccion valorBuscado, int linea, int col) {
        super(new Tipo(Datos.ENTERO), linea, col);
        this.id = id;
        this.valorBuscado = valorBuscado;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {

        Object buscado = valorBuscado.interpretar(arbol, tabla);
        if (buscado instanceof Errores) return buscado;

        Simbolo sim = tabla.getVariable(id);
        if (sim == null) {
            return new Errores(
                "SEMANTICO",
                "Variable no declarada: '" + id + "'",
                linea,
                col
            );
        }

        Object valor = sim.getValor();
        Tipo tipo = sim.getTipo();

        // ================= LISTA DINÁMICA =================
        if (tipo.esLista()) {

            if (!(valor instanceof List)) {
                return new Errores("SEMANTICO", "Valor interno no es una lista", linea, col);
            }

            List<?> lista = (List<?>) valor;
            for (int i = 0; i < lista.size(); i++) {
                if (Objects.equals(lista.get(i), buscado)) {
                    return i;
                }
            }
            return -1;
        }

        // ================= ARREGLO 1D =================
        if (tipo.getDimensiones() == 1) {

            // 👉 CASO 1: almacenado como List
            if (valor instanceof List) {
                List<?> arreglo = (List<?>) valor;
                for (int i = 0; i < arreglo.size(); i++) {
                    if (Objects.equals(arreglo.get(i), buscado)) {
                        return i;
                    }
                }
                return -1;
            }

            // 👉 CASO 2: almacenado como Object[]
            if (valor instanceof Object[]) {
                Object[] arreglo = (Object[]) valor;
                for (int i = 0; i < arreglo.length; i++) {
                    if (Objects.equals(arreglo[i], buscado)) {
                        return i;
                    }
                }
                return -1;
            }

            return new Errores(
                "SEMANTICO",
                "Valor interno no es un arreglo",
                linea,
                col
            );
        }

        return new Errores(
            "SEMANTICO",
            "'" + id + "' no es una lista ni un arreglo",
            linea,
            col
        );
    }
}
