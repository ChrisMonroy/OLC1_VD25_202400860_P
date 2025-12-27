/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Simbolo;

import java.util.LinkedList;
import Abstracto.Instruccion;
import Errores.Errores;
import Expresiones.AccesoVar;
import Expresiones.Aritmetica;
import Expresiones.Nativo;
import Expresiones.Relacionales;
import Instruccion.Asignacion;
import Instruccion.Declaracion;
import Instruccion.Funcion;
import Instruccion.If;
import Instruccion.Llamada;
import Instruccion.Metodo;
import Instruccion.Print;
import Instruccion.Start;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christoper
 */
public class Arbol {

    private LinkedList<Instruccion> instrucciones;
    private TablaSimbolos tablaGlobal;
    public LinkedList<Errores> errores;
    private String consolas;
    private LinkedList<Instruccion> funciones;
    private LinkedList<Instruccion> vectores;
    private int contadorNodos = 0;
    private List<TablaSimbolos> todasLasTablas = new ArrayList<>();

    // control de recursión
    private int recursionDepth = 0;
    private static final int MAX_RECURSION_DEPTH = 10000;

    public Arbol(LinkedList<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
        this.tablaGlobal = new TablaSimbolos();
        this.errores = new LinkedList<>();
        this.consolas = "";
        this.funciones = new LinkedList<>();
        this.vectores = new LinkedList<>();
    }

    public void enterRecursion() {
        if (++recursionDepth > MAX_RECURSION_DEPTH) {
            throw new RuntimeException("Límite de recursión excedido (máx. " + MAX_RECURSION_DEPTH + ")");
        }
    }

    public void exitRecursion() {
        recursionDepth--;
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(LinkedList<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public TablaSimbolos getTablaGlobal() {
        return tablaGlobal;
    }

    public void setTablaGlobal(TablaSimbolos tablaGlobal) {
        this.tablaGlobal = tablaGlobal;
    }

    public LinkedList<Errores> getErrores() {
        return errores;
    }

    public void setErrores(LinkedList<Errores> errores) {
        this.errores = errores;
    }

    public String getConsolas() {
        return consolas;
    }

    public void setConsolas(String consolas) {
        this.consolas = consolas;
    }
    
    public void agregarTabla(TablaSimbolos tabla) {
        todasLasTablas.add(tabla);
    }

    public List<TablaSimbolos> getTodasLasTablas() {
        return todasLasTablas;
    }

    public void Print(String valor) {
        this.consolas += valor + "\n";
    }

    public LinkedList<Instruccion> getFunciones() {
        return funciones;
    }

    public void setFunciones(LinkedList<Instruccion> funciones) {
        this.funciones = funciones;
    }

    public void addFunciones(Instruccion funcion) {
        this.funciones.add(funcion);
    }

    public Instruccion getFuncion(String id) {
        for (Instruccion instr : this.funciones) {
            if (instr instanceof Metodo metodo) {
                if (metodo.id.equalsIgnoreCase(id)) {
                    return instr;
                }
            } else if (instr instanceof Funcion funcion) {
                if (funcion.id.equalsIgnoreCase(id)) {
                    return instr;
                }
            }
        }
        return null;
    }

    public boolean esTipoCompatible(Object valor, Tipo tipoEsperado) {
        if (valor == null || tipoEsperado == null) return false;
        Datos tipoValor = obtenerTipoDeValor(valor);
        if (tipoValor == tipoEsperado.getTipo()) {
            return true;
        }

        if (tipoEsperado.getTipo() == Datos.ENTERO) {
            return tipoValor == Datos.ENTERO ||
                   tipoValor == Datos.CARACTER ||
                   tipoValor == Datos.BOOLEANO;
        }
        if (tipoEsperado.getTipo() == Datos.DECIMAL) {
            return tipoValor == Datos.ENTERO ||
                   tipoValor == Datos.DECIMAL ||
                   tipoValor == Datos.CARACTER ||
                   tipoValor == Datos.BOOLEANO;
        }
        if (tipoEsperado.getTipo() == Datos.CARACTER) {
            return tipoValor == Datos.ENTERO;
        }
        if (tipoEsperado.getTipo() == Datos.BOOLEANO) {
            return tipoValor == Datos.BOOLEANO;
        }
        if (tipoEsperado.getTipo() == Datos.CADENA) {
            return true; // todos a string
        }
        return false;
    }

    private Datos obtenerTipoDeValor(Object valor) {
        if (valor instanceof Integer) return Datos.ENTERO;
        if (valor instanceof Double) return Datos.DECIMAL;
        if (valor instanceof String) return Datos.CADENA;
        if (valor instanceof Character) return Datos.CARACTER;
        if (valor instanceof Boolean) return Datos.BOOLEANO;
        return null;
    }
    
    public String generarDOT() {
    StringBuilder dot = new StringBuilder();
    dot.append("digraph AST {\n");
    dot.append("node [shape=ellipse, style=filled, fillcolor=\"#E0E0E0\", fontname=\"Arial\", fontsize=12];\n");
    dot.append("edge [fontsize=10];\n");
    dot.append("rankdir=TB;\n");
    contadorNodos = 0;

    String raiz = "n" + contadorNodos++;
    dot.append("  ").append(raiz).append(" [label=\"exec\"];\n");

    String instruccionesNodo = "n" + contadorNodos++;
    dot.append("  ").append(instruccionesNodo).append(" [label=\"Instrucciones\"];\n");
    dot.append("  ").append(raiz).append(" -> ").append(instruccionesNodo).append(";\n");

    for (Instruccion inst : instrucciones) {
        if (inst != null) {
            String hijo = generarNodoSemantico(inst, dot);
            dot.append("  ").append(instruccionesNodo).append(" -> ").append(hijo).append(";\n");
        }
    }

    dot.append("}\n");
    return dot.toString();
}

private String generarNodoSemantico(Instruccion inst, StringBuilder dot) {
    String nodoInst = "n" + contadorNodos++;
    dot.append("  ").append(nodoInst).append(" [label=\"Instrucción\"];\n");

    if (inst instanceof Print p) {
        String llamadaNodo = "n" + contadorNodos++;
        dot.append("  ").append(llamadaNodo).append(" [label=\"Llamada_función\"];\n");
        dot.append("  ").append(nodoInst).append(" -> ").append(llamadaNodo).append(";\n");

        String printReservada = "n" + contadorNodos++;
        dot.append("  ").append(printReservada).append(" [label=\"print (Palabra Reservada)\"];\n");
        dot.append("  ").append(llamadaNodo).append(" -> ").append(printReservada).append(";\n");

        String paramsNodo = "n" + contadorNodos++;
        dot.append("  ").append(paramsNodo).append(" [label=\"Parametros\"];\n");
        dot.append("  ").append(llamadaNodo).append(" -> ").append(paramsNodo).append(";\n");

        if (p.getExpresion() != null) {
            String paramNodo = "n" + contadorNodos++;
            dot.append("  ").append(paramNodo).append(" [label=\"Parametro\"];\n");
            dot.append("  ").append(paramsNodo).append(" -> ").append(paramNodo).append(";\n");

            procesarExpresionSemantica(p.getExpresion(), paramNodo, dot);
        }
    }

    else if (inst instanceof Declaracion d) {
        String declNodo = "n" + contadorNodos++;
        dot.append("  ").append(declNodo).append(" [label=\"Declaración\"];\n");
        dot.append("  ").append(nodoInst).append(" -> ").append(declNodo).append(";\n");

        String varNodo = "n" + contadorNodos++;
        dot.append("  ").append(varNodo).append(" [label=\"var (Palabra Reservada)\"];\n");
        dot.append("  ").append(declNodo).append(" -> ").append(varNodo).append(";\n");

        String idNodo = "n" + contadorNodos++;
        dot.append("  ").append(idNodo).append(" [label=\"").append(escape(d.getId())).append(" (Identificador)\"];\n");
        dot.append("  ").append(declNodo).append(" -> ").append(idNodo).append(";\n");

        if (d.getValor() != null) {
            String asignNodo = "n" + contadorNodos++;
            dot.append("  ").append(asignNodo).append(" [label=\"= (Operador)\"];\n");
            dot.append("  ").append(declNodo).append(" -> ").append(asignNodo).append(";\n");

            procesarExpresionSemantica(d.getValor(), declNodo, dot);
        }
    }

    else if (inst instanceof Asignacion a) {
        String asignNodo = "n" + contadorNodos++;
        dot.append("  ").append(asignNodo).append(" [label=\"Asignación\"];\n");
        dot.append("  ").append(nodoInst).append(" -> ").append(asignNodo).append(";\n");

        String idNodo = "n" + contadorNodos++;
        dot.append("  ").append(idNodo).append(" [label=\"").append(escape(a.getId())).append(" (Identificador)\"];\n");
        dot.append("  ").append(asignNodo).append(" -> ").append(idNodo).append(";\n");

        String opNodo = "n" + contadorNodos++;
        dot.append("  ").append(opNodo).append(" [label=\"= (Operador)\"];\n");
        dot.append("  ").append(asignNodo).append(" -> ").append(opNodo).append(";\n");

        if (a.getValor() != null) {
            procesarExpresionSemantica(a.getValor(), asignNodo, dot);
        }
    }

    else if (inst instanceof If) {
        String ifNodo = "n" + contadorNodos++;
        dot.append("  ").append(ifNodo).append(" [label=\"if (Palabra Reservada)\"];\n");
        dot.append("  ").append(nodoInst).append(" -> ").append(ifNodo).append(";\n");

        // Aquí podrías expandir condición y cuerpo, pero para entrega básica, con "if" basta
    }

    else if (inst instanceof Funcion f) {
        String funcNodo = "n" + contadorNodos++;
        dot.append("  ").append(funcNodo).append(" [label=\"Función\"];\n");
        dot.append("  ").append(nodoInst).append(" -> ").append(funcNodo).append(";\n");

        String tipoNodo = "n" + contadorNodos++;
        dot.append("  ").append(tipoNodo).append(" [label=\"").append(escape(f.tipo.getTipo().name())).append(" (Tipo)\"];\n");
        dot.append("  ").append(funcNodo).append(" -> ").append(tipoNodo).append(";\n");

        String idNodo = "n" + contadorNodos++;
        dot.append("  ").append(idNodo).append(" [label=\"").append(escape(f.id)).append(" (Identificador)\"];\n");
        dot.append("  ").append(funcNodo).append(" -> ").append(idNodo).append(";\n");
    }

    else if (inst instanceof Start s) {
        String startNodo = "n" + contadorNodos++;
        dot.append("  ").append(startNodo).append(" [label=\"START\"];\n");
        dot.append("  ").append(nodoInst).append(" -> ").append(startNodo).append(";\n");

        String mainNodo = "n" + contadorNodos++;
        dot.append("  ").append(mainNodo).append(" [label=\"main (Identificador)\"];\n");
        dot.append("  ").append(startNodo).append(" -> ").append(mainNodo).append(";\n");
    }

    else if (inst instanceof Llamada l) {
        String llamadaNodo = "n" + contadorNodos++;
        dot.append("  ").append(llamadaNodo).append(" [label=\"Llamada_función\"];\n");
        dot.append("  ").append(nodoInst).append(" -> ").append(llamadaNodo).append(";\n");

        String idNodo = "n" + contadorNodos++;
        dot.append("  ").append(idNodo).append(" [label=\"").append(escape(l.getId())).append(" (Identificador)\"];\n");
        dot.append("  ").append(llamadaNodo).append(" -> ").append(idNodo).append(";\n");

        String paramsNodo = "n" + contadorNodos++;
        dot.append("  ").append(paramsNodo).append(" [label=\"Parametros\"];\n");
        dot.append("  ").append(llamadaNodo).append(" -> ").append(paramsNodo).append(";\n");

        // Parámetros se omiten para simplicidad en entrega
    }

    return nodoInst;
}

private void procesarExpresionSemantica(Instruccion expr, String padre, StringBuilder dot) {
    if (expr == null) return;

    // Nativo: valor literal
    if (expr instanceof Nativo n) {
        String valor = n.getValor().toString();
        String tipo = " (Desconocido)";
        if (n.getValor() instanceof String) tipo = " (Cadena)";
        else if (n.getValor() instanceof Integer) tipo = " (Entero)";
        else if (n.getValor() instanceof Double) tipo = " (Decimal)";
        else if (n.getValor() instanceof Boolean) tipo = " (Booleano)";
        else if (n.getValor() instanceof Character) tipo = " (Carácter)";

        String valorNodo = "n" + contadorNodos++;
        dot.append("  ").append(valorNodo).append(" [label=\"").append(escape(valor)).append(tipo).append("\"];\n");
        dot.append("  ").append(padre).append(" -> ").append(valorNodo).append(";\n");
    }

    // Acceso a variable
    else if (expr instanceof AccesoVar a) {
        String varNodo = "n" + contadorNodos++;
        dot.append("  ").append(varNodo).append(" [label=\"").append(escape(a.getId())).append(" (Identificador)\"];\n");
        dot.append("  ").append(padre).append(" -> ").append(varNodo).append(";\n");
    }

    // Expresiones binarias
    else if (expr instanceof Aritmetica ar) {
        String op = ar.getOperaciones().toString();
        String opNodo = "n" + contadorNodos++;
        dot.append("  ").append(opNodo).append(" [label=\"").append(escape(op)).append(" (Operador)\"];\n");
        dot.append("  ").append(padre).append(" -> ").append(opNodo).append(";\n");

        if (ar.getOperando1() != null) {
            procesarExpresionSemantica(ar.getOperando1(), opNodo, dot);
        }
        if (ar.getOperando2() != null) {
            procesarExpresionSemantica(ar.getOperando2(), opNodo, dot);
        }
    }

    else if (expr instanceof Relacionales rel) {
        String op = rel.getRelacional().toString();
        String opNodo = "n" + contadorNodos++;
        dot.append("  ").append(opNodo).append(" [label=\"").append(escape(op)).append(" (Operador)\"];\n");
        dot.append("  ").append(padre).append(" -> ").append(opNodo).append(";\n");

        if (rel.getCond1() != null) {
            procesarExpresionSemantica(rel.getCond1(), opNodo, dot);
        }
        if (rel.getCond2() != null) {
            procesarExpresionSemantica(rel.getCond2(), opNodo, dot);
        }
    }

    else {
        String genNodo = "n" + contadorNodos++;
        dot.append("  ").append(genNodo).append(" [label=\"").append(escape(expr.getClass().getSimpleName())).append("\"];\n");
        dot.append("  ").append(padre).append(" -> ").append(genNodo).append(";\n");
    }
}

private String escape(String s) {
    if (s == null) return "null";
    return s.replace("\"", "\\\"").replace("\n", "\\n");
}
}