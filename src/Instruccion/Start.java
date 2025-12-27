/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Errores;
import java.util.LinkedList;

/**
 *
 * @author Christoper
 */
public class Start extends Instruccion {
    private String id;
    private LinkedList<Instruccion> parametros;

    public Start(String id, LinkedList<Instruccion> parametros, int linea, int col) {
        super(new Tipo(Datos.VOID), linea, col);
        this.id = id;
        this.parametros = parametros;
    }

    public String getId() {
        return id;
    }
    
    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        var busqueda = arbol.getFuncion(id);
        if (busqueda == null) {
            return new Errores("SEMANTICO", "Funcion '" + id + "' no existe", this.linea, this.col);
        }

        if (busqueda instanceof Metodo metodo) {
            var nuevaTabla = new TablaSimbolos(arbol.getTablaGlobal());
            arbol.agregarTabla(nuevaTabla);
            nuevaTabla.setNombre("START");

            if (metodo.parametros.size() != this.parametros.size()) {
                return new Errores("SEMANTICO", "Tamaño de parametros no coincide", this.linea, this.col);
            }

            for (int i = 0; i < this.parametros.size(); i++) {
                var identificador = (String) metodo.parametros.get(i).get("id");
                var valor = this.parametros.get(i).interpretar(arbol, tabla);
                if (valor instanceof Errores) return valor;

                var tipo = (Tipo) metodo.parametros.get(i).get("tipo");
                var simbolo = new Simbolo(identificador, tipo, valor);
                if (!nuevaTabla.agregar(simbolo)) {
                    return new Errores("SEMANTICO", "No se pudo agregar el parametro '" + identificador + "'", this.linea, this.col);
                }
            }

            var resultado = metodo.interpretar(arbol, nuevaTabla);
            return resultado instanceof Errores ? resultado : null;

        } else if (busqueda instanceof Funcion funcion) {
            var nuevaTabla = new TablaSimbolos(arbol.getTablaGlobal());
            arbol.agregarTabla(nuevaTabla);
            nuevaTabla.setNombre("START");

            if (funcion.parametros.size() != this.parametros.size()) {
                return new Errores("SEMANTICO", "Tamaño de parametros no coincide", this.linea, this.col);
            }

            for (int i = 0; i < this.parametros.size(); i++) {
                var identificador = (String) funcion.parametros.get(i).get("id");
                var valor = this.parametros.get(i).interpretar(arbol, tabla);
                if (valor instanceof Errores) return valor;

                var tipo = (Tipo) funcion.parametros.get(i).get("tipo");
                var simbolo = new Simbolo(identificador, tipo, valor);
                if (!nuevaTabla.agregar(simbolo)) {
                    return new Errores("SEMANTICO", "No se pudo agregar el parametro '" + identificador + "'", this.linea, this.col);
                }
            }

            Object resultado = funcion.interpretar(arbol, nuevaTabla);
            if (!(resultado instanceof Errores)) {
                System.out.println("Resultado de start: " + resultado);
            }
            return resultado instanceof Errores ? resultado : null;
        }

        return new Errores("SEMANTICO", "La función '" + id + "' no es válida para 'start'", this.linea, this.col);
    }
    public LinkedList<Instruccion> getParametros() {
    return parametros;
}
}