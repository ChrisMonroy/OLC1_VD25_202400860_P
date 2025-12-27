/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Christoper
 */
public class Llamada extends Instruccion {

    private String id;
    private LinkedList<Instruccion> parametros;

    public Llamada(String id, LinkedList<Instruccion> parametros, int linea, int col) {
        super(null, linea, col);
        this.id = id;
        this.parametros = parametros;
    }

@Override
public Object interpretar(Arbol arbol, TablaSimbolos tabla) {

    Instruccion busqueda = arbol.getFuncion(id);
    if (busqueda == null) {
        return new Errores(
            "SEMANTICO",
            "Función/Método '" + id + "' no existe",
            this.linea,
            this.col
        );
    }

    LinkedList<HashMap> paramsDecl;
    String ambito;

    if (busqueda instanceof Metodo m) {
        paramsDecl = m.parametros;
        ambito = "Metodo_";
    } else if (busqueda instanceof Funcion f) {
        paramsDecl = f.parametros;
        ambito = "Funcion_";
        this.tipo = f.tipo;
    } else {
        return new Errores("SEMANTICO", "Llamada inválida", linea, col);
    }

    TablaSimbolos tablaParametros = new TablaSimbolos(arbol.getTablaGlobal());
    arbol.agregarTabla(tablaParametros);
    tablaParametros.setNombre(ambito + id);

    if (paramsDecl.size() != parametros.size()) {
        return new Errores(
            "SEMANTICO",
            "Cantidad de parámetros no coincide en '" + id + "'",
            linea, col
        );
    }

    for (int i = 0; i < parametros.size(); i++) {

        String nombre = (String) paramsDecl.get(i).get("id");
        Tipo tipo = (Tipo) paramsDecl.get(i).get("tipo");

        Object valor = parametros.get(i).interpretar(arbol, tabla);
        if (valor instanceof Errores) return valor;

        Simbolo sim = new Simbolo(nombre, tipo, valor);
        tablaParametros.agregar(sim);
    }

    if (busqueda instanceof Metodo metodo) {
        return metodo.interpretar(arbol, tablaParametros);
    }

    if (busqueda instanceof Funcion funcion) {
        return funcion.ejecutar(arbol, tablaParametros);
    }

    return null;
}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
