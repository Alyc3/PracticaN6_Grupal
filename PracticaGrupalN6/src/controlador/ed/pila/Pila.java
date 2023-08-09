/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador.ed.pila;

import controlador.ed.lista.exception.PosicionException;
import controlador.ed.lista.exception.VacioException;
import controlador.ed.lista.exception.exception.TopeException;

/**
 *
 * @author darkangel
 */
public class Pila<E> {

    private PilaI<E> pilai;

    public Pila(Integer cima) {
        pilai = new PilaI<>(cima);
    }

    public void push(E obj) throws TopeException {
        pilai.push(obj);
    }

    public E pop() throws VacioException, PosicionException {
        return pilai.pop();
    }

    public Integer getCima() {
        return pilai.getCima();
    }

    public boolean isEmpty() {
        return pilai.isEmpty(); // 
    }

    public E peek() throws VacioException, PosicionException {
        if (pilai.isEmpty()) {
            throw new VacioException("La pila está vacía");
        }
        return pilai.get(0);
    }

    public void print() throws VacioException {
        pilai.imprimir();
    }

}
