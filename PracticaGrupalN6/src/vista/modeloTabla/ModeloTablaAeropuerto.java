/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.modeloTabla;

import controlador.ed.lista.ListaEnlazada;
import javax.swing.table.AbstractTableModel;
import modelo.Aeropuerto;

/**
 *
 * @author Bravo
 */
public class ModeloTablaAeropuerto extends AbstractTableModel {
    
    private ListaEnlazada<Aeropuerto> lista = new ListaEnlazada<>();

    public ListaEnlazada<Aeropuerto> getLista() {
        return lista;
    }

    public void setLista(ListaEnlazada<Aeropuerto> lista) {
        this.lista = lista;
    }
    
    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return  3; 
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "Id";
            case 1: return "Nombre Aeropuerto";
            case 2: return "Ciudad";

            
            default: return null;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Aeropuerto aeropuerto = null;
      
        try {
            aeropuerto = lista.get(row);
           
        } catch (Exception e) {
        }
        switch (column) {
            case 0: return aeropuerto.getId();
            case 1: return aeropuerto.getNombre();
            case 2: return aeropuerto.getCiudad();    
            default: return null;
        }
    }
}

