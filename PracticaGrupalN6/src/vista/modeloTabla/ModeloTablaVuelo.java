/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.modeloTabla;

import controlador.ed.lista.ListaEnlazada;
import javax.swing.table.AbstractTableModel;
import modelo.Vuelo;

/**
 *
 * @author Bravo
 */
public class ModeloTablaVuelo extends AbstractTableModel {

    private ListaEnlazada<Vuelo> lista = new ListaEnlazada<>();

    public ListaEnlazada<Vuelo> getLista() {
        return lista;
    }

    public void setLista(ListaEnlazada<Vuelo> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return 4; // Cambiar al número correcto de columnas
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";
            case 1:
                return "Origen";
            case 2:
                return "Destino";
            case 3:
                return "Duración";

            default:
                return null;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Vuelo vuelo = null;

        try {
            vuelo = lista.get(row);

        } catch (Exception e) {
        }
        switch (column) {
            case 0:
                return vuelo.getId();
            case 1:
                return vuelo.getOrigen();
            case 2:
                return vuelo.getDestino();
            case 3:
                return vuelo.getDuracion();

            default:
                return null;
        }
    }
}
