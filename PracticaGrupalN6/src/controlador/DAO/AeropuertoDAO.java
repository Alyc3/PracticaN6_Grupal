/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.DAO;

import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import java.io.IOException;
import modelo.Aeropuerto;
import modelo.Aeropuerto;
import modelo.Vuelo;

/**
 *
 * @author Bravo
 */
public class AeropuertoDAO extends AdaptadorDao<Aeropuerto> {

    private Aeropuerto ae;

    public AeropuertoDAO() {
        super(Aeropuerto.class);
    }

    public Aeropuerto getAeropuerto() {
        if (this.ae == null) {
            this.ae = new Aeropuerto();
        }
        return ae;
    }

    public void setAeropuerto(Aeropuerto ae) {
        this.ae = ae;
    }

    public void guardar() throws IOException {
        ae.setId(generateID());
        this.guardar(ae);
    }

    public void modificar(Integer pos) throws VacioException, PosicionException, IOException {
        this.modificar(ae, pos);
    }

    private Integer generateID() {
        return listar().size() + 1;
    }
    
    

 
    public static void main(String[] args) throws IOException {
        AeropuertoDAO ad = new AeropuertoDAO();
        
//        ad.getAeropuerto().setNombre("Kobos");
//        ad.guardar();
//        ad.setAeropuerto(null);
//        ad.getAeropuerto().setNombre("Alice");
//        ad.guardar();
//        ad.setAeropuerto(null);
        ad.getAeropuerto().setNombre("Marylin");
        ad.guardar();
        ad.setAeropuerto(null);
        
        
    }

}
