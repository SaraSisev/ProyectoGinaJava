/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misClases;

import java.io.IOException;
import misClases.Jugador;
/**
 *
 * @author Eliba
 */
//Clase que ejcutura el Jugador
public class MainJugador {
    public static void main(String []arggs) throws IOException{
        Jugador jugador = new Jugador();//se crea la variable del jugador
        System.out.println("Iniciando cliente\n");
        jugador.startJugador();
    }
}
