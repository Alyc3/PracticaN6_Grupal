/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.ed.grafo;

import controlador.ed.cola.Cola;
import controlador.ed.grafo.exception.GrafoSizeExeption;
import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import controlador.ed.lista.exception.exception.TopeException;
import controlador.ed.pila.Pila;

/**
 *
 * @author darkangel
 */
public class GrafoD extends Grafo {

    protected Integer numV;
    protected Integer numA;
    protected ListaEnlazada<Adycencia> listaAdycencia[];
    private Double distancias[][];
    private double[] distanciaAux;
    private Integer[] camino;

    public GrafoD(Integer nroVertices) {
        numV = nroVertices;
        numA = 0;
        listaAdycencia = new ListaEnlazada[nroVertices + 1];
        for (int i = 1; i <= nroVertices; i++) {
            listaAdycencia[i] = new ListaEnlazada<>();
        }
    }

    @Override
    public Integer numVertices() {
        return numV;
    }

    @Override
    public Integer numAristas() {
        return numA;
    }

    @Override
    public Boolean existeArista(Integer i, Integer j) throws GrafoSizeExeption {
        Boolean esta = false;
        if (i.intValue() <= numV && j.intValue() <= numV) {
            ListaEnlazada<Adycencia> lista = listaAdycencia[i];
            for (int k = 0; k < lista.size(); k++) {
                try {
                    Adycencia aux = lista.get(k);
                    if (aux.getDestino().intValue() == j.intValue()) {
                        esta = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }
        } else {
            throw new GrafoSizeExeption();
        }
        return esta;
    }

    @Override
    public Double pesoArista(Integer i, Integer j) throws GrafoSizeExeption {
        Double esta = Double.NaN;
        if (i.intValue() <= numV && j.intValue() <= numV) {
            ListaEnlazada<Adycencia> lista = listaAdycencia[i];
            for (int k = 0; k < lista.size(); k++) {
                try {
                    Adycencia aux = lista.get(k);
                    if (aux.getDestino().intValue() == j.intValue()) {
                        esta = aux.getPeso();
                        break;
                    }
                } catch (Exception e) {
                }
            }
        } else {
            throw new GrafoSizeExeption();
        }
        return esta;
    }

    @Override
    public void insertar(Integer i, Integer j) throws GrafoSizeExeption {
        insertar(i, j, Double.NaN);
    }

    @Override
    public void insertar(Integer i, Integer j, Double peso) throws GrafoSizeExeption {
        if (i.intValue() <= numV
                && j.intValue() <= numV) {
            if (!existeArista(i, j)) {
                listaAdycencia[i].insertar(new Adycencia(j, peso));
                numA++;
            }
        } else {
            throw new GrafoSizeExeption();
        }
    }

    @Override
    public ListaEnlazada<Adycencia> adycentes(Integer i) {
        return listaAdycencia[i];
    }

    public void floyd() throws Exception {
        distancias = new Double[numV + 1][numV + 1];
        
        for (int i = 1; i <= numV; i++) {
            for (int j = 1; j <= numV; j++) {
                if (i == j) {
                    distancias[i][j] = 0.0;
                } else if (existeArista(i, j)) {
                    distancias[i][j] = pesoArista(i, j);
                } else {
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        for (int k = 1; k <= numV; k++) {
            for (int i = 1; i <= numV; i++) {
                for (int j = 1; j <= numV; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }
        for (int k = 1; k <= numV; k++) {
            for (int i = 1; i <= numV; i++) {
                for (int j = 1; j <= numV; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }
    }

    public ListaEnlazada<Integer> obtenerCaminoFloyd(int origen, int destino) throws Exception {
        floyd();
        ListaEnlazada<Integer> camino = new ListaEnlazada<>();
        if (distancias[origen][destino] == Double.POSITIVE_INFINITY) {
            return camino;
        }
        int verticeActual = destino;
        camino.insertarInicio(verticeActual);
        while (verticeActual != origen) {
            for (int i = 1; i <= numV; i++) {
                if (distancias[origen][i] + pesoArista(i, verticeActual) == distancias[origen][verticeActual]) {
                    verticeActual = i;
                    camino.insertarInicio(verticeActual);
                    break;
                }
            }
        }
        return camino;
    }

    public ListaEnlazada<Double> distanciasHaciaTodosLosVerticesFloyd(Integer inicio) throws Exception {
        floyd();
        ListaEnlazada<Double> listaDistancias = new ListaEnlazada<>();
        listaDistancias.insertar(0.0);
        for (int i = 1; i <= numV; i++) {
            if (i != inicio) {
                listaDistancias.insertar(distancias[inicio][i]);
            }
        }
        return listaDistancias;
    }

    public Double obtenerDistanciaFloyd(Integer o, Integer d) throws Exception {
        floyd();
        return distancias[o][d];
    }

    public ListaEnlazada recorridoEnAnchura(Integer origen) throws TopeException, VacioException, PosicionException {
        ListaEnlazada<Integer> recorrido = new ListaEnlazada<>();

        Boolean[] visitados = new Boolean[numVertices() + 1];
        for (int i = 1; i <= numVertices(); i++) {
            visitados[i] = false;
        }
        Cola<Integer> cola = new Cola<>(numVertices());
        visitados[origen] = true;
        cola.queue(origen);
        while (!cola.isEmpty()) {//le agregue el codigo a Cola

            origen = cola.dequeue();
            recorrido.insertar(origen);
            ListaEnlazada<Adycencia> adyacentes = adycentes(origen);
            for (int i = 0; i < adyacentes.size(); i++) {
                Adycencia a = adyacentes.get(i);
                if (!visitados[a.getDestino()]) {
                    visitados[a.getDestino()] = true;
                    cola.queue(a.getDestino());
                }
            }
        }
        return recorrido;
    }

    public ListaEnlazada recorridoEnProfundidad(Integer origen) throws TopeException, VacioException, PosicionException {
        ListaEnlazada<Integer> recorrido = new ListaEnlazada<>();
        Boolean[] visitados = new Boolean[numVertices() + 1];
        for (int i = 1; i <= numVertices(); i++) {
            visitados[i] = false;
        }

        Pila<Integer> pila = new Pila<>(numVertices());
        pila.push(origen);
        visitados[origen] = true;

        while (!pila.isEmpty()) { //le agregue el codigo a Pila
            Integer vertice = pila.pop();
            recorrido.insertar(vertice);
            ListaEnlazada<Adycencia> adyacentes = adycentes(vertice);
            for (int i = 0; i < adyacentes.size(); i++) {
                Adycencia a = adyacentes.get(i);
                Integer destino = a.getDestino();
                if (!visitados[destino]) {
                    pila.push(destino);

                    visitados[destino] = true;
                }
            }
        }
        return recorrido;
    }

    public ListaEnlazada<Integer> bellmanFord(Integer origen, Integer destino) throws Exception {
        distancias = new Double[numV + 1][numV + 1];
        distanciaAux = new double[numV + 1];
        camino = new Integer[numV + 1];
        ListaEnlazada<Integer> caminoIndices = new ListaEnlazada<>();

        for (int i = 1; i <= numV; i++) {
            distanciaAux[i] = Double.POSITIVE_INFINITY;
            camino[i] = null;
            for (int j = 1; j <= numV; j++) {
                distancias[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        distanciaAux[origen] = 0.0;

        for (int i = 1; i <= numV - 1; i++) {
            for (int j = 1; j <= numV; j++) {
                ListaEnlazada<Adycencia> adyacentes = adycentes(j);
                for (int k = 0; k < adyacentes.size(); k++) {
                    Adycencia a = adyacentes.get(k);
                    Integer destinoAdy = a.getDestino();
                    Double peso = a.getPeso();
                    if (distanciaAux[j] + peso < distanciaAux[destinoAdy]) {
                        distanciaAux[destinoAdy] = distanciaAux[j] + peso;
                        camino[destinoAdy] = j;
                    }
                }
            }
        }
        Integer verticeActual = destino; // Comenzar desde el destino
        while (verticeActual != null) {
            caminoIndices.insertarInicio(verticeActual);
            verticeActual = camino[verticeActual];
        }
        return caminoIndices;
    }

    public ListaEnlazada<Integer> camino(int origen, int destino) throws TopeException, VacioException, PosicionException {
        ListaEnlazada<Integer> camino = new ListaEnlazada<>();
        Boolean[] visitados = new Boolean[numV + 1];
        for (int i = 1; i <= numV; i++) {
            visitados[i] = false;
        }

        Pila<Integer> pila = new Pila<>(numV);
        pila.push(origen);
        visitados[origen] = true;

        while (!pila.isEmpty()) {
            Integer vertice = pila.peek();
            if (vertice == destino) {
                break;
            }

            ListaEnlazada<Adycencia> adyacentes = adycentes(vertice);
            boolean encontrado = false;
            for (int i = 0; i < adyacentes.size(); i++) {
                Adycencia a = adyacentes.get(i);
                Integer vecino = a.getDestino();
                if (!visitados[vecino]) {
                    pila.push(vecino);
                    visitados[vecino] = true;
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                pila.pop();
            }
        }

        while (!pila.isEmpty()) {
            camino.insertarInicio(pila.pop());
        }

        if (!visitados[destino]) {
            camino.isEmpty();
        }

        return camino;
    }

}
