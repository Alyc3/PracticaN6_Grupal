/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.ed.cola;

import controlador.ed.lista.ListaEnlazada;
import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import controlador.ed.lista.exception.exception.TopeException;

/**
 *
 * @author darkangel
 */
public class ColaI<E> extends ListaEnlazada<E>{
    private Integer tope;

    public ColaI(Integer tope) {
        this.tope = tope;
    }
    public Boolean isFull() {
        return (size() >= tope ); 
    }
    // 7 
    //9
    //7 -- 9
    //4
    //7 -- 9 --4
    public void queue(E dato) throws TopeException{
        if(isFull())
            throw new TopeException("Cola sin espacio");
        else this.insertar(dato);
    }
    //7 -- 9 --4
    //9 --- 4
    //4
    public E dequeue() throws VacioException, PosicionException {
        E dato = null;
        if(isEmpty()) {
            throw new VacioException("Cola vacia");
        } else {
            return this.delete(0);
        }
    }

    public Integer getTope() {
        return tope;
    }
    
    
    
}
