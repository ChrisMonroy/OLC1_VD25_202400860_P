/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Simbolo;

/**
 *
 * @author Christoper
 */
public class Tipo {

    private Datos tipo;
    private int dimensiones;
    private int[] tamanos;
    private boolean esArreglo;
    private Tipo tipogenerico;

    public Tipo(Datos tipo) {
        this.tipo = tipo;
        this.dimensiones = 0;
        this.esArreglo = false;
    }

    public Tipo(Datos tipo, boolean esArreglo) {
        this.tipo = tipo;
        this.esArreglo = esArreglo;
    }

    public Tipo(Datos tipoBase, int dimensiones) {
        this.tipo = tipoBase;
        this.dimensiones = dimensiones;
        this.esArreglo = dimensiones > 0;
        if (dimensiones == 1) {
            this.tamanos = new int[]{0};
        } else if (dimensiones == 2) {
            this.tamanos = new int[]{0, 0};
        }
    }

    public Tipo(Datos tipo, int dimensiones, int[] tamanos) {
        this.tipo = tipo;
        this.dimensiones = dimensiones;
        this.tamanos = tamanos;
        this.esArreglo = dimensiones > 0;
    }

    public Tipo(Datos tipo, int dimensiones, int[] tamanos, Tipo tipoGenerico) {
        this.tipo = tipo;
        this.dimensiones = dimensiones;
        this.tamanos = tamanos;
        this.tipogenerico = tipoGenerico;
        this.esArreglo = false;
    }

    public Datos getTipo() {
        return tipo;
    }

    public void setTipo(Datos tipo) {
        this.tipo = tipo;
    }

    public int getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(int dimensiones) {
        this.dimensiones = dimensiones;
    }

    public int[] getTamanos() {
        return tamanos;
    }

    public void setTamanos(int[] tamanos) {
        this.tamanos = tamanos;
    }

    public Tipo getTipogenerico() {
        return tipogenerico;
    }

    public void setTipogenerico(Tipo tipogenerico) {
        this.tipogenerico = tipogenerico;
    }

    public boolean isArreglo() {
        return esArreglo;
    }

    public boolean esLista() {
        return this.tipo == Datos.LIST && this.tipogenerico != null;
    }

    public Tipo getSubtipo() {
        return this.tipogenerico;
    }

    public boolean esCompatible(Tipo otro) {
        if (otro == null) return false;
        if (this.tipo == otro.tipo) return true;
        if (this.tipo == Datos.DECIMAL && otro.tipo == Datos.ENTERO) return true;
        return false;
    }

    public Object getValorPorDefecto() {
        switch (tipo) {
            case ENTERO:
                return 0;
            case DECIMAL:
                return 0.0;
            case BOOLEANO:
                return false;
            case CARACTER:
                return '\0';
            case CADENA:
                return "";
            default:
                return null;
        }
    }
    
public String toString() {
    if (esLista()) {
        return "List<" + tipogenerico.toString() + ">";
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append(tipo.toString());
    
    for (int i = 0; i < dimensiones; i++) {
        sb.append("[]");
    }
    
    return sb.toString();
}
}
