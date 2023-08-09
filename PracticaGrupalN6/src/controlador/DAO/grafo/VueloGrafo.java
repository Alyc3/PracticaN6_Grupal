/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.DAO.grafo;

import controlador.DAO.AeropuertoDAO;
import controlador.DAO.VueloDAO;
import controlador.ed.grafo.GrafoEtiquetadoD;
import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import java.util.HashMap;
import java.util.Map;
import modelo.Aeropuerto;
import modelo.Vuelo;

/**
 *
 * @author Bravo
 */
import java.util.*;

public class VueloGrafo {

    private GrafoEtiquetadoD<Aeropuerto> grafo;
    private ListaEnlazada<Aeropuerto> lista = new ListaEnlazada<>();

    public VueloGrafo() {
        AeropuertoDAO ad = new AeropuertoDAO();
        lista = ad.listar();

        grafo = new GrafoEtiquetadoD<>(lista.size());

        try {
            for (int i = 0; i < lista.size(); i++) {
                grafo.etiquetarVertice(i + 1, lista.get(i));
                System.out.println(lista.get(i));
            }
            llenarGrafo();
        } catch (Exception e) {
        }
    }

    public ListaEnlazada<Aeropuerto> getLista() {
        return lista;
    }

    public void setLista(ListaEnlazada<Aeropuerto> lista) {
        this.lista = lista;
    }
    
    public ListaEnlazada<Aeropuerto> getListaAeropuertos() {
        return lista;
    }

    public void setListaAeropuertos(ListaEnlazada<Aeropuerto> lista) {
        this.lista = lista;
    }

    public GrafoEtiquetadoD<Aeropuerto> getGrafo() {
        return grafo;
    }

    public void setGrafo(GrafoEtiquetadoD<Aeropuerto> grafo) {
        this.grafo = grafo;
    }

    private void llenarGrafo() {
        try {
            for (int i = 0; i < lista.size(); i++) {
                String nombreOrigen = lista.get(i).getNombre();
                HashMap<String, Double> mapa = new HashMap<>();
                System.out.println("Aeropuerto Origen: " + nombreOrigen);

                ListaEnlazada<Vuelo> listaVuelos = new VueloDAO().listaPorAeropuerto(nombreOrigen);
                for (int j = 0; j < listaVuelos.size(); j++) {
                    Vuelo vuelo = listaVuelos.get(j);
                    String nombreDestino = vuelo.getDestino();
                    double duracion = vuelo.getDuracion();

                    if (mapa.containsKey(nombreDestino)) {
                        double suma = mapa.get(nombreDestino) + duracion;
                        mapa.put(nombreDestino, suma);
                    } else {
                        mapa.put(nombreDestino, duracion);
                    }
                }

                for (Map.Entry<String, Double> entry : mapa.entrySet()) {
                    String nombreDestino = entry.getKey();
                    double duracion = entry.getValue();

                    // Obtener instancias de Aeropuerto basado en los nombres
                    Aeropuerto aeropuertoOrigen = getAeropuerto(nombreOrigen);
                    Aeropuerto aeropuertoDestino = getAeropuerto(nombreDestino);

                    grafo.insertarAristaE(aeropuertoOrigen, aeropuertoDestino, duracion);
                }
            }
        } catch (Exception e) {
            // Manejar la excepción apropiadamente
        }
    }

    private Aeropuerto getAeropuerto(String nombre) {
        Aeropuerto aux = null;
        try {
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getNombre().equals(nombre)) {
                    aux = lista.get(i);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return aux;
    }

    public ListaEnlazada<Aeropuerto> camino(Aeropuerto origen, Aeropuerto destino) throws VacioException, PosicionException {
        int indiceOrigen = grafo.getVerticeNum(origen);
        int indiceDestino = grafo.getVerticeNum(destino);

        ListaEnlazada<Integer> indicesDeCamino = grafo.camin0(indiceOrigen, indiceDestino);

        ListaEnlazada<Aeropuerto> camino = new ListaEnlazada<>();
        for (int i = 0; i < indicesDeCamino.size(); i++) {
            int indice = indicesDeCamino.get(i);
            camino.insertar(grafo.getEtiqueta(indice));
        }

        return camino;
    }
}
