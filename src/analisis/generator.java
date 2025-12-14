/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analisis;

/**
 *
 * @author Christoper
 */
public class generator {
    public static void main(String[] args){
        generarCompi();
    }
    public static void generarCompi(){
        try{
        String ruta = "src/analisis/";
        String Flex[] = {ruta + "lexico.jflex", "-d", ruta};
        jflex.Main.generate(Flex);
        
        String Cup[] = {"-destdir", ruta, "-parser", "parser", ruta + "sintactico.cup"};
        java_cup.Main.main(Cup);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
