package Expresiones;

import Abstracto.Instruccion;
import Simbolo.*;
import Errores.Error;
import static Expresiones.OperadoresAritmeticos.*;

/**
 *
 * @author Christoper
 */
public class Aritmetica extends Instruccion {

    private Instruccion operando1;
    private Instruccion operando2;
    private OperadoresAritmeticos operaciones;
    private Instruccion operandoUnico;

    public Aritmetica(OperadoresAritmeticos operaciones, Instruccion operandoUnico, int linea, int col) {
        super(new Tipo(Datos.ENTERO), linea, col);
        this.operaciones = operaciones;
        this.operandoUnico = operandoUnico;
    }

    public Aritmetica(Instruccion operando1, Instruccion operando2, OperadoresAritmeticos operaciones, int linea, int col) {
        super(new Tipo(Datos.ENTERO), linea, col);
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operaciones = operaciones;
    }

    @Override
    public Object interpretar(Arbol arbol, TablaSimbolos tabla) {
        Object operadorIz = null;
        Object operadorDer = null;
        Object operadorUni = null;

        if (this.operandoUnico != null) {
            operadorUni = this.operandoUnico.interpretar(arbol, tabla);
            if (operadorUni instanceof Error) {
                return operadorUni;
            }
        } else {
            operadorIz = this.operando1.interpretar(arbol, tabla);
            if (operadorIz instanceof Error) {
                return operadorIz;
            }
            operadorDer = this.operando2.interpretar(arbol, tabla);
            if (operadorDer instanceof Error) {
                return operadorDer;
            }
        }

        return switch (operaciones) {
            case SUMA -> this.suma(operadorIz, operadorDer);
            case RESTA -> this.resta(operadorIz, operadorDer);
            case MULTIPLICACION -> this.multi(operadorIz, operadorDer);
            case DIVISION -> this.div(operadorIz, operadorDer);
            case MODULO -> this.mod(operadorIz, operadorDer);
            case POTENCIA -> this.pot(operadorIz, operadorDer);
            case NEGACION -> this.negacion(operadorUni);
            default -> new Error("Error Sintactico", "operador inexistente", this.linea, this.col);
        };
    }

    public Object suma(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        // Si alguno es CADENA, el resultado es CADENA
        if (tipo1 == Datos.CADENA || tipo2 == Datos.CADENA) {
            this.tipo.setTipo(Datos.CADENA);
            return op1.toString() + op2.toString();
        }

        // Si alguno es CARÁCTER y el otro es CADENA, ya fue cubierto arriba
        // Si ambos son CARÁCTER, la tabla dice: CADENA
        if (tipo1 == Datos.CARACTER && tipo2 == Datos.CARACTER) {
            this.tipo.setTipo(Datos.CADENA);
            return op1.toString() + op2.toString();
        }

        // Si uno es CARÁCTER y el otro es ENTERO/DECIMAL, convertir CARÁCTER a ENTERO
        if (tipo1 == Datos.CARACTER) {
            op1 = (int) ((Character) op1);
            tipo1 = Datos.ENTERO;
        }
        if (tipo2 == Datos.CARACTER) {
            op2 = (int) ((Character) op2);
            tipo2 = Datos.ENTERO;
        }

        // Ahora operar como números
        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.ENTERO);
                        return (int) op1 + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (int) op1 + (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "suma erronea", this.linea, this.col);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 + (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "suma erronea", this.linea, this.col);
                    }
                }
            }
            default -> {
                return new Error("ERROR semantico", "suma erronea", this.linea, this.col);
            }
        }
    }

    public Object negacion(Object op1) {
        var opU = this.operandoUnico.tipo.getTipo();
        switch (opU) {
            case ENTERO -> {
                this.tipo.setTipo(Datos.ENTERO);
                return (int) op1 * -1;
            }
            case DECIMAL -> {
                this.tipo.setTipo(Datos.DECIMAL);
                return (double) op1 * -1;
            }
            default -> {
                return new Error("ERROR semantico", "negacion erronea", this.linea, this.col);
            }
        }
    }

    public Object resta(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        // No se permite operar con cadenas
        if (tipo1 == Datos.CADENA || tipo2 == Datos.CADENA) {
            return new Error("ERROR semantico", "resta con cadena no permitida", this.linea, this.col);
        }

        // Única combinación inválida con carácter: Carácter - Carácter
        if (tipo1 == Datos.CARACTER && tipo2 == Datos.CARACTER) {
            return new Error("ERROR semantico", "resta entre caracteres no permitida", this.linea, this.col);
        }

        // Convertir CARÁCTER a ENTERO si es necesario
        if (tipo1 == Datos.CARACTER) {
            op1 = (int) ((Character) op1);
            tipo1 = Datos.ENTERO;
        }
        if (tipo2 == Datos.CARACTER) {
            op2 = (int) ((Character) op2);
            tipo2 = Datos.ENTERO;
        }

        // Asegurar tipos numéricos
        if ((tipo1 != Datos.ENTERO && tipo1 != Datos.DECIMAL) ||
            (tipo2 != Datos.ENTERO && tipo2 != Datos.DECIMAL)) {
            return new Error("ERROR semantico", "tipo no válido para resta", this.linea, this.col);
        }

        // Determinar tipo de retorno y operar
        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.ENTERO);
                        return (int) op1 - (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (int) op1 - (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para resta", this.linea, this.col);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 - (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 - (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para resta", this.linea, this.col);
                    }
                }
            }
            default -> {
                return new Error("ERROR semantico", "tipo no válido para resta", this.linea, this.col);
            }
        }
    }

    public Object multi(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        // No se permite operar con cadenas
        if (tipo1 == Datos.CADENA || tipo2 == Datos.CADENA) {
            return new Error("ERROR semantico", "multiplicación con cadena no permitida", this.linea, this.col);
        }

        // Única combinación inválida con carácter: Carácter * Carácter
        if (tipo1 == Datos.CARACTER && tipo2 == Datos.CARACTER) {
            return new Error("ERROR semantico", "multiplicación entre caracteres no permitida", this.linea, this.col);
        }

        // Convertir CARÁCTER a ENTERO si es necesario
        if (tipo1 == Datos.CARACTER) {
            op1 = (int) ((Character) op1);
            tipo1 = Datos.ENTERO;
        }
        if (tipo2 == Datos.CARACTER) {
            op2 = (int) ((Character) op2);
            tipo2 = Datos.ENTERO;
        }

        // Asegurar tipos numéricos
        if ((tipo1 != Datos.ENTERO && tipo1 != Datos.DECIMAL) ||
            (tipo2 != Datos.ENTERO && tipo2 != Datos.DECIMAL)) {
            return new Error("ERROR semantico", "tipo no válido para multiplicación", this.linea, this.col);
        }

        // Determinar tipo de retorno y operar
        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.ENTERO);
                        return (int) op1 * (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (int) op1 * (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para multiplicación", this.linea, this.col);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 * (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 * (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para multiplicación", this.linea, this.col);
                    }
                }
            }
            default -> {
                return new Error("ERROR semantico", "tipo no válido para multiplicación", this.linea, this.col);
            }
        }
    }

    public Object div(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        // No se permite operar con cadenas
        if (tipo1 == Datos.CADENA || tipo2 == Datos.CADENA) {
            return new Error("ERROR semantico", "división con cadena no permitida", this.linea, this.col);
        }

        // Única combinación inválida con carácter: Carácter / Carácter
        if (tipo1 == Datos.CARACTER && tipo2 == Datos.CARACTER) {
            return new Error("ERROR semantico", "división entre caracteres no permitida", this.linea, this.col);
        }

        // Verificar división por cero
        if ((tipo2 == Datos.ENTERO && (int) op2 == 0) ||
            (tipo2 == Datos.DECIMAL && (double) op2 == 0.0)) {
            return new Error("ERROR semantico", "división por cero", this.linea, this.col);
        }

        // Convertir CARÁCTER a ENTERO si es necesario
        if (tipo1 == Datos.CARACTER) {
            op1 = (int) ((Character) op1);
            tipo1 = Datos.ENTERO;
        }
        if (tipo2 == Datos.CARACTER) {
            op2 = (int) ((Character) op2);
            tipo2 = Datos.ENTERO;
        }

        // Asegurar tipos numéricos
        if ((tipo1 != Datos.ENTERO && tipo1 != Datos.DECIMAL) ||
            (tipo2 != Datos.ENTERO && tipo2 != Datos.DECIMAL)) {
            return new Error("ERROR semantico", "tipo no válido para división", this.linea, this.col);
        }

        // Determinar tipo de retorno y operar
        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) (int) op1 / (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (int) op1 / (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para división", this.linea, this.col);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 / (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 / (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para división", this.linea, this.col);
                    }
                }
            }
            default -> {
                return new Error("ERROR semantico", "tipo no válido para división", this.linea, this.col);
            }
        }
    }

    public Object mod(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        // No se permite operar con cadenas
        if (tipo1 == Datos.CADENA || tipo2 == Datos.CADENA) {
            return new Error("ERROR semantico", "módulo con cadena no permitido", this.linea, this.col);
        }

        // Única combinación inválida con carácter: Carácter % Carácter
        if (tipo1 == Datos.CARACTER && tipo2 == Datos.CARACTER) {
            return new Error("ERROR semantico", "módulo entre caracteres no permitido", this.linea, this.col);
        }

        // Verificar módulo por cero
        if ((tipo2 == Datos.ENTERO && (int) op2 == 0) ||
            (tipo2 == Datos.DECIMAL && (double) op2 == 0.0)) {
            return new Error("ERROR semantico", "módulo por cero", this.linea, this.col);
        }

        // Convertir CARÁCTER a ENTERO si es necesario
        if (tipo1 == Datos.CARACTER) {
            op1 = (int) ((Character) op1);
            tipo1 = Datos.ENTERO;
        }
        if (tipo2 == Datos.CARACTER) {
            op2 = (int) ((Character) op2);
            tipo2 = Datos.ENTERO;
        }

        // Asegurar tipos numéricos
        if ((tipo1 != Datos.ENTERO && tipo1 != Datos.DECIMAL) ||
            (tipo2 != Datos.ENTERO && tipo2 != Datos.DECIMAL)) {
            return new Error("ERROR semantico", "tipo no válido para módulo", this.linea, this.col);
        }

        // Determinar tipo de retorno y operar
        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.ENTERO);
                        return (int) op1 % (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (int) op1 % (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para módulo", this.linea, this.col);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 % (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(Datos.DECIMAL);
                        return (double) op1 % (double) op2;
                    }
                    default -> {
                        return new Error("ERROR semantico", "tipo no válido para módulo", this.linea, this.col);
                    }
                }
            }
            default -> {
                return new Error("ERROR semantico", "tipo no válido para módulo", this.linea, this.col);
            }
        }
    }

    public Object pot(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        // El PDF no menciona CARÁCTER en potencia, así que lo prohibimos
        if (tipo1 == Datos.CARACTER || tipo2 == Datos.CARACTER) {
            return new Error("ERROR semantico", "potencia con caracter no permitida", this.linea, this.col);
        }

        // No se permite operar con cadenas
        if (tipo1 == Datos.CADENA || tipo2 == Datos.CADENA) {
            return new Error("ERROR semantico", "potencia con cadena no permitida", this.linea, this.col);
        }

        // Asegurar tipos numéricos
        if ((tipo1 != Datos.ENTERO && tipo1 != Datos.DECIMAL) ||
            (tipo2 != Datos.ENTERO && tipo2 != Datos.DECIMAL)) {
            return new Error("ERROR semantico", "tipo no válido para potencia", this.linea, this.col);
        }

        // Convertir a double para usar Math.pow
        double val1 = (tipo1 == Datos.ENTERO) ? (int) op1 : (double) op1;
        double val2 = (tipo2 == Datos.ENTERO) ? (int) op2 : (double) op2;

        // Calcular la potencia
        double resultado = Math.pow(val1, val2);

        // Determinar tipo de retorno
        if (tipo1 == Datos.ENTERO && tipo2 == Datos.ENTERO && val2 >= 0 && val2 == (int) val2) {
            // Solo si ambos son enteros y el exponente es entero no negativo → resultado entero
            this.tipo.setTipo(Datos.ENTERO);
            return (int) resultado;
        } else {
            this.tipo.setTipo(Datos.DECIMAL);
            return resultado;
        }
    }
}