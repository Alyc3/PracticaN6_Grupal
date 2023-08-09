/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Bravo
 */
public class Vuelo {

    private Integer id;
    private String origen;
    private String destino;
    private int duracion;

    public Vuelo(int id, String origen, String destino, int duracion) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.duracion = duracion;
    }

    public Vuelo() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Integer getId() {
        return id;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public int getDuracion() {
        return duracion;
    }

    @Override
    public String toString() {
        return origen + " -> " + destino + " (Duración: " + duracion + " horas)";
    }
}
