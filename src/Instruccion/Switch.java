/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Datos;
import Simbolo.TablaSimbolos;
import Simbolo.Tipo;
import java.util.LinkedList;
import Errores.Errores;

/**
 *
 * @author Christoper
 */
public class Switch extends Instruccion{
    private Instruccion expresion;
    private LinkedList<Instruccion> valoresCasos;
    private LinkedList<LinkedList<Instruccion>> instruccionesCasos;
    private LinkedList<Instruccion> instruccionesDefault;

    public Switch(Instruccion expresion, LinkedList<Instruccion> valoresCasos, LinkedList<LinkedList<Instruccion>> instruccionesCasos, LinkedList<Instruccion> instruccionesDefault, Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.expresion = expresion;
        this.valoresCasos = valoresCasos;
        this.instruccionesCasos = instruccionesCasos;
        this.instruccionesDefault = instruccionesDefault;
    }

    public Switch(Instruccion expresion, LinkedList<Instruccion> valoresCasos, LinkedList<LinkedList<Instruccion>> instruccionesCasos, Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.expresion = expresion;
        this.valoresCasos = valoresCasos;
        this.instruccionesCasos = instruccionesCasos;
    }

    
    
    public Object interpretar(Arbol arbol, TablaSimbolos tabla){
        Object valorSwitch = this.expresion.interpretar(arbol, tabla);
        if (valorSwitch instanceof Errores) {
            return valorSwitch;
        }
        TablaSimbolos tablaLocal = new TablaSimbolos(tabla);
        arbol.agregarTabla(tablaLocal);

        boolean encontrado = false;
        boolean ejecutando = false;
        
        for (int i = 0; i < this.valoresCasos.size(); i++) {
            Instruccion exprCaso = this.valoresCasos.get(i);
            LinkedList<Instruccion> instrsCaso = this.instruccionesCasos.get(i);

            Object valorCaso = exprCaso.interpretar(arbol, tabla);
            if (valorCaso instanceof Errores) {
                return valorCaso;
            }

            if (ejecutando) {
                for (Instruccion inst : instrsCaso) {
                    Object res = inst.interpretar(arbol, tablaLocal);
                    if (res instanceof Errores) return res;
                    if (res instanceof Break) return null;
                }
            }
            else if (compararIgual(valorSwitch, valorCaso)) {
                encontrado = true;
                ejecutando = true;
                for (Instruccion inst : instrsCaso) {
                    Object res = inst.interpretar(arbol, tablaLocal);
                    if (res instanceof Errores) return res;
                    if (res instanceof Break) return null;
                }
            }
        }

        if (!encontrado && this.instruccionesDefault != null) {
            for (Instruccion inst : this.instruccionesDefault) {
                Object res = inst.interpretar(arbol, tablaLocal);
                if (res instanceof Errores) return res;
                if (res instanceof Break) return null;
            }
        }
        return null;
    }
    private boolean compararIgual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        // Soportar comparación de tipos primitivos (Integer, String, Boolean, etc.)
        if (a.getClass() != b.getClass()) return false;
        return a.equals(b);
    }
    
    public static LinkedList<Instruccion> extraerValores(LinkedList<SwitchCaso> casos) {
    LinkedList<Instruccion> valores = new LinkedList<>();
    for (SwitchCaso c : casos) {
        valores.add(c.getExpresion());
    }
    return valores;
}

public static LinkedList<LinkedList<Instruccion>> extraerInstrucciones(LinkedList<SwitchCaso> casos) {
    LinkedList<LinkedList<Instruccion>> instrs = new LinkedList<>();
    for (SwitchCaso c : casos) {
        instrs.add(c.getInstrucciones());
    }
    return instrs;
}

}
