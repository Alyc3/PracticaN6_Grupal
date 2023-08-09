/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.grafos;

import controlador.DAO.grafo.VueloGrafo;
import controlador.ed.grafo.GrafoD;
import controlador.ed.grafo.GrafoEtiquetadoD;
import controlador.ed.grafo.GrafoEtiquetadoND;
import controlador.ed.grafo.GrafoND;

/**
 *
 * @author darkangel
 */
public class Main {
    public static void main(String[] args) {
        GrafoND g = new GrafoND(6);
        GrafoD gd = new GrafoD(6);
        VueloGrafo vg = new VueloGrafo();
        try {
            new FrmGrafo(null, true, vg.getGrafo()).setVisible(true);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        

    }
}
