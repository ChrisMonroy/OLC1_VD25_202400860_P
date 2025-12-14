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
public class Relacionales extends Instruccion{
    
    private Instruccion cond1;
    private Instruccion cond2;
    private OperadoresRelacionales relacional;
    
    public Relacionales(Instruccion cond1, Instruccion cond2, OperadoresRelacionales relacional, int linea, int col) {
        super(new Tipo(Datos.BOOLEANO), linea, col);
        this.cond1 = cond1;
        this.cond2 = cond2;
        this.relacional = relacional;
    }
    
    
    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
         var condIzq = this.cond1.interpretar(arbol, tabla);
        if (condIzq instanceof Error) {
            return condIzq;
        }

        var condDer = this.cond2.interpretar(arbol, tabla);
        if (condDer instanceof Error) {
            return condDer;
        }

        return switch (relacional) {
            case  IGUAL ->
                this.equals(condIzq, condDer);
            case DIFERENTE ->
                this.diferente(condIzq, condDer);
            case MENOR ->
                this.menor(condIzq, condDer);
            case MENOR_IGUAL ->
                this.menor_igual(condIzq, condDer);
            case MAYOR ->
                this.mayor(condIzq, condDer);
            case MAYOR_IGUAL ->
                this.mayor_igual(condIzq, condDer);
            default ->
                new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
        };
    }

    public Object equals(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 == (int) comp2;
                    case DECIMAL ->
                        (int) comp1 == (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 == (int) comp2;
                    case DECIMAL ->
                        (double) comp1 == (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            case CADENA ->
                switch (comparando2) {
                    case CADENA ->
                        comp1.toString().equalsIgnoreCase(comp2.toString());
                    default ->
                        new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            default ->
                new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
        };
    }

    public Object diferente(Object comp1, Object comp2){
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 != (int) comp2;
                    case DECIMAL ->
                        (int) comp1 != (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 != (int) comp2;
                    case DECIMAL ->
                        (double) comp1 != (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            case CADENA ->
                switch (comparando2) {
                    case CADENA ->
                        !comp1.toString().equalsIgnoreCase(comp2.toString());
                    default ->
                        new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            default ->
                new Error("SEMANTICO", "Relacional Invalido", this.linea, this.col);
        };
    }
    
    public Object menor(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 < (int) comp2;
                    case DECIMAL ->
                        (int) comp1 < (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 < (int) comp2;
                    case DECIMAL ->
                        (double) comp1 < (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            default ->
                new Error("SEMANTICO", "Relacional invaldo",
                this.linea, this.col);
        };
    }
    
    public Object menor_igual(Object comp1, Object comp2){
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 <= (int) comp2; 
                    case DECIMAL ->
                        (int) comp1 <= (double) comp2;  
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 <= (int) comp2;  
                    case DECIMAL ->
                        (double) comp1 <= (double) comp2;  
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            default ->
                new Error("SEMANTICO", "Relacional invaldo",
                this.linea, this.col);
        };
    }
        
    public Object mayor(Object comp1, Object comp2){
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 > (int) comp2;
                    case DECIMAL ->
                        (int) comp1 > (double) comp2;  
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 > (int) comp2;  
                    case DECIMAL ->
                        (double) comp1 > (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            default ->
                new Error("SEMANTICO", "Relacional invaldo",
                this.linea, this.col);
        };
    }
        
    public Object mayor_igual(Object comp1, Object comp2){
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 >= (int) comp2;  
                    case DECIMAL ->
                        (int) comp1 >= (double) comp2;  
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 >= (int) comp2;  
                    case DECIMAL ->
                        (double) comp1 >= (double) comp2;
                    default ->
                        new Error("SEMANTICO", "Relacional invaldo",
                        this.linea, this.col);
                };
            default ->
                new Error("SEMANTICO", "Relacional invaldo",
                this.linea, this.col);
        };
    }
}