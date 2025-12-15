/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Instruccion;
import Abstracto.Instruccion;
import java.util.LinkedList;
/**
 *
 * @author Christoper
 */
public class SwitchCaso {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;

    public SwitchCaso(Instruccion expresion, LinkedList<Instruccion> instrucciones) {
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }

    public Instruccion getExpresion() {
        return expresion;
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }
    
    
}
