/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Simbolo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Christoper
 */
public class TablaSimbolos {
    private TablaSimbolos TablaAnterior;
    public HashMap<String, Simbolo> TablaActual;
    private String nombre;
    
    public TablaSimbolos(){
        this.TablaActual = new HashMap<>();
        this.nombre = "";
    }
    
    public TablaSimbolos getTablaAnterior(){
        return TablaAnterior;
    }
    
     public void setTablaAnterior(TablaSimbolos tablaAnterior) {
        this.TablaAnterior = tablaAnterior;
    }
     
     public HashMap<String, Simbolo> getTablaActual(){
         return TablaActual;
     }
     
     public void setTablaActual(HashMap<String, Simbolo> TablaActual){
         this.TablaActual = TablaActual;
     }
     
     public String getNombre(){
         return nombre;
     }
     
     public void setNombre(String nombre){
         this.nombre = nombre;
     }
     
     public TablaSimbolos(TablaSimbolos TablaAnterior){
         this.TablaAnterior = TablaAnterior;
         this.TablaActual = new HashMap<>();
         this.nombre = "";
     }
     
        public boolean setVariables(Simbolo simbolo){
        Simbolo busqueda = (Simbolo) this.TablaActual.get(simbolo.getId().toLowerCase());
        if (busqueda == null){
            this.TablaActual.put(simbolo.getId().toLowerCase(), simbolo);
            return true;
        }
        return false;
    }
    
    public Simbolo getVariable(String id) {
    String key = id.toLowerCase();
    Simbolo simbolo = this.TablaActual.get(key);
    if (simbolo != null) {
        return simbolo;
    }
    // Si no está aquí, buscar en el entorno padre
    if (this.TablaAnterior != null) {
        return this.TablaAnterior.getVariable(id);
    }
    return null;
    
}
    
    public List<Simbolo> obtenerTodosLosSimbolos() {
    List<Simbolo> simbolos = new ArrayList<>(this.TablaActual.values());
    
    // Si hay tabla anterior (ámbitos anidados), incluye sus símbolos
    if (this.TablaAnterior != null) {
        simbolos.addAll(this.TablaAnterior.obtenerTodosLosSimbolos());
    }
    
    return simbolos;
}

public boolean agregar(Simbolo simbolo) {
    if (simbolo == null || simbolo.getId() == null) {
        return false;
    }
    String id = simbolo.getId().toLowerCase();
    if (this.TablaActual.containsKey(id)) {
        return false;
    }
    this.TablaActual.put(id, simbolo);
    return true;
}

public Simbolo get(String id) {
    // Buscar en la tabla actual
    Simbolo simbolo = this.TablaActual.get(id.toLowerCase());
    if (simbolo != null) {
        return simbolo;
    }
    // Buscar en tablas anteriores (ámbitos superiores)
    if (this.TablaAnterior != null) {
        return this.TablaAnterior.get(id);
    }
    return null;
}
}
