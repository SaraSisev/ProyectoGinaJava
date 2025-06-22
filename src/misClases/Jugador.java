
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
    private JFrameTablero tablero;
    
    //Constructor
    public Jugador() throws IOException{super("cliente");}
    //constructor que recibe la IP y el frame de chat para abrir
    public Jugador(String ipServidor, JFrameChat frame,String nombre,JFrameTablero tablero) throws IOException{
        super("cliente",ipServidor);
        this.frame = frame;
        this.tablero = tablero;
        this.nombreJugador = nombre;
    }
   
    
    //Metodo para iniciar al jugador
    public void startJugador() throws IOException{
            DataOutputStream salida = new DataOutputStream(cs.getOutputStream());
            DataInputStream entrada = new DataInputStream(cs.getInputStream());
            this.frame.setSalida(salida);

            // Recibir nombre y personaje del servidor
            String nombreServidor = entrada.readUTF();       
            String personajeServidor = entrada.readUTF();
            
            // Enviar nombre del jugador al servidor 
            salida.writeUTF(nombreJugador);/* Aquí necesitas obtener el nombre del jugador local */
            salida.writeUTF(tablero.getPersonaje());
            // Pasar nombre del servidor al chat
            frame.setDatosOponente(nombreServidor, personajeServidor);
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