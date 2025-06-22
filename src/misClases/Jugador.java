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
    private String nombreJugador;
//variable para pasar la informacion al frame del chat
    private JFrameChat frame;     
//Constructor
    public Jugador() throws IOException{super("cliente");}
    //constructor que recibe la IP y el frame de chat para abrir
    public Jugador(String ipServidor, JFrameChat frame,String nombre) throws IOException{
        super("cliente",ipServidor);
        this.frame = frame;
        this.nombreJugador = nombre;
    }
   
    
    //Metodo para iniciar al jugador
    public void startJugador() throws IOException{
            DataOutputStream salida = new DataOutputStream(cs.getOutputStream());
            DataInputStream entrada = new DataInputStream(cs.getInputStream());
            this.frame.setSalida(salida);

            // Recibir nombre del servidor
            String nombreServidor = entrada.readUTF();
            // Enviar nombre del jugador al servidor 
            salida.writeUTF(nombreJugador);/* Aquí necesitas obtener el nombre del jugador local */
            // Pasar nombre del servidor al chat
            frame.setDatosOponente(nombreServidor);
            // -----------------------------
        
           new Thread(() -> {
            try {
                //DataInputStream entrada = new DataInputStream(cs.getInputStream());
                while (true) {
                    String mensaje = entrada.readUTF();
                    frame.procesarMensajeRecibido(mensaje);

                }
            } catch (IOException e) {
                frame.mostrarMensaje("Error en conexión");
            }
        }).start();
    }
    
    

}