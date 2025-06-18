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
    //variable para pasar la informacion al frame del chat
    private JFrameChat frame;     
//Constructor
    public Jugador() throws IOException{super("cliente");}
    //constructor que recibe la IP y el frame de chat para abrir
    public Jugador(String ipServidor, JFrameChat frame) throws IOException{
        super("cliente",ipServidor);
        this.frame = frame;
    }
   
    
    //Metodo para iniciar al jugador
    public void startJugador() throws IOException{
        this.frame.setSalida(new DataOutputStream(cs.getOutputStream()));
           new Thread(() -> {
            try {
                DataInputStream entrada = new DataInputStream(cs.getInputStream());
                while (true) {
                    String mensaje = entrada.readUTF();
                    frame.mostrarMensaje("Servidor: " + mensaje);
                }
            } catch (IOException e) {
                frame.mostrarMensaje("Error en conexión");
            }
        }).start();
    }
    

}