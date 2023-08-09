/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.ed.grafo;

/**
 *
 * @author darkangel
 */
public class Main {
    public static void main(String[] args) {
        GrafoND g = new GrafoND(6);
        try {
            g.insertar(1, 3, 6.7);
          //  g.insertar(6, 4, 5.0);
           // g.insertar(2, 3, 9.0);
            //g.insertar(3, 4, 2.0);
            //g.insertar(4, 5, 19.0);
            
            System.out.println(g.toString());
            //System.out.println(g.pesoArista(4, 10));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        try {
            
            //System.out.println(ged.getEtiqueta(1));
        } catch (Exception e) {
            System.out.println("error en GE "+e);
        }
        
        //clases
        //Persona
        //id
        //apellidos
        //nombres
        //Docente extends Persona
        //titulo
        //BD
        //persona
        //id int pk auto_increment
        //nombres varchar(50)
        //apellidos varchar(50) not null
        //docente
        //id int pk fk
        //titulo varchar(50)
    }
}








