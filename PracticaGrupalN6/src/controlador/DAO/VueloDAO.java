/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.DAO;

import controlador.ed.grafo.GrafoEtiquetadoD;
import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import modelo.Aeropuerto;
import modelo.Vuelo;

/**
 *
 * @author Bravo
 */
public class VueloDAO extends AdaptadorDao<Vuelo> {

    private Vuelo vuelo;
    private GrafoEtiquetadoD<Vuelo> grafo;
    private GrafoEtiquetadoD<Aeropuerto> graf;

    public VueloDAO() {
        super(Vuelo.class);
    }

    public Vuelo getVuelo() {
        if (this.vuelo == null) {
            this.vuelo = new Vuelo();
        }
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public GrafoEtiquetadoD<Aeropuerto> getGraf() {
        return graf;
    }

    public void setGraf(GrafoEtiquetadoD<Aeropuerto> graf) {
        this.graf = graf;
    }

    public GrafoEtiquetadoD<Vuelo> getGrafo() {
        return grafo;
    }

    public void setGrafo(GrafoEtiquetadoD<Vuelo> grafo) {
        this.grafo = grafo;
    }

    public void guardar() throws IOException {
        vuelo.setId(generateID());
        this.guardar(vuelo);
    }

    public void modificar(Integer pos) throws VacioException, PosicionException, IOException {
        this.modificar(vuelo, pos);
    }

    private Integer generateID() {
        return listar().size() + 1;
    }

    public ListaEnlazada<Vuelo> listaPorAeropuerto(String nombreAeropuerto) throws VacioException, PosicionException {
        ListaEnlazada<Vuelo> lista = new ListaEnlazada<>();
        ListaEnlazada<Vuelo> listado = listar();

        for (int i = 0; i < listado.size(); i++) {
            Vuelo aux = listado.get(i);

            if (aux.getOrigen().equals(nombreAeropuerto)) {
                //aux.getOrigen()
                lista.insertar(aux);
            }
        }

        return lista;
    }

    //Si talvez sirve el de mostrar  que relaciones tien cada fila
//    public ListaEnlazada<Vuelo> listaVuelosPorAeropuerto(int idAeropuerto) throws VacioException, PosicionException {
//        ListaEnlazada<Vuelo> vuelosRelacionados = new ListaEnlazada<>();
//
//        // Obten la lista de vuelos
//        ListaEnlazada<Vuelo> listadoVuelos = listar();
//
//        // Obtén el nombre del aeropuerto con el ID proporcionado
//        String nombreAeropuerto = obtenerNombreAeropuertoPorId(idAeropuerto);
//
//        for (int i = 0; i < listadoVuelos.size(); i++) {
//            Vuelo vuelo = listadoVuelos.get(i);
//
//            // Compara el origen y destino del vuelo con el nombre del aeropuerto
//            if (vuelo.getOrigen().equals(nombreAeropuerto) || vuelo.getDestino().equals(nombreAeropuerto)) {
//                vuelosRelacionados.insertar(vuelo);
//            }
//        }
//
//        return vuelosRelacionados;
//    }
//
//    private String obtenerNombreAeropuertoPorId(int idAeropuerto) throws VacioException, PosicionException {
//        ListaEnlazada<Aeropuerto> lista = new ListaEnlazada<>();
//        for (int i = 0; i < lista.size(); i++) {
//            Aeropuerto aeropuerto = lista.get(i);
//            if (aeropuerto.getId() == idAeropuerto) {
//                return aeropuerto.getNombre();
//            }
//        }
//        return "Aeropuerto Desconocido";
//    }

    public static void main(String[] args) throws IOException {

        VueloDAO vd = new VueloDAO();
        AeropuertoDAO ad = new AeropuertoDAO();

//        vd.getVuelo().setDuracion(5);
//        vd.getVuelo().setOrigen("Kobos");
//        vd.getVuelo().setDestino("Alice");
//        vd.guardar();
//        vd.getVuelo().setDuracion(10);
//        vd.getVuelo().setOrigen("Alice");
//        vd.getVuelo().setDestino("Marylin");
//        vd.guardar();
    }
}
