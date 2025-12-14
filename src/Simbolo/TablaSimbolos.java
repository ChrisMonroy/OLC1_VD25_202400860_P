/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Simbolo;
import java.util.HashMap;

/**
 *
 * @author Christoper
 */
public class TablaSimbolos {
    private TablaSimbolos TablaAnterior;
    private HashMap<String, Simbolo> TablaActual;
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
    
    public Simbolo getVariable(String id){
        Simbolo busqueda = (Simbolo) this.TablaActual.get(id.toLowerCase());
        if (busqueda != null){
           
            return busqueda;
        }
        return null;
    }
}
