/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misClases;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import misClases.Conexion;
/**
 *
 * @author Eliba
 */
//Clase que representa nuestro cliente al servidor
public class Jugador extends Conexion{
    boolean jugando = true;//Bandera que representa que el jugador sigue jugando y asi siga mandando mensajes al servidor
    Scanner sc = new Scanner(System.in);//Variable para capturar las respuestas
    //Constructor
    public Jugador() throws IOException{super("cliente");}
    
    //Metodo para iniciar al jugador
    public void startJugador(){
        try{
           //flujo de datos hacia el servidor
           salidaServidor = new DataOutputStream(cs.getOutputStream());
           entradaServidor = new DataInputStream(cs.getInputStream());
           //ciclo que se repite hasta que no haya flujo de datos(hasta que acabe el juego)
           while(jugando){
              System.out.print("Tu pregunta o adivinanza: ");
              String mensaje = sc.nextLine();

              // enviar mensaje al servidor
              salidaServidor.writeUTF(mensaje);

              // recibir respuesta del servidor
              String respuesta = entradaServidor.readUTF();
              System.out.println("Servidor dice: " + respuesta);

              // lógica de fin de juego
              if (respuesta.equalsIgnoreCase("¡Correcto!") || respuesta.equalsIgnoreCase("Fin del juego")) {
                    jugando = false; 
              }
           }
           //cs.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }  
    }

}