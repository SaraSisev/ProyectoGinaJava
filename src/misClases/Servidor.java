/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misClases;
//ultima modificacion, funciona solo que servidor mande preguntas jugador aun no
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import misClases.Conexion;
/**
 *
 * @author Eliba
 */
public class Servidor extends Conexion{//Se hereda de conexion para el uso de sokcets y generar el  ambiente de servidor
    private JFrameTablero frame;
    private JFrameChat frame2;//variable temporal
    //constructor para que se pueda ejecutar un metodo, ya que startserver como tal no acaba
    public Servidor(JFrameTablero frame, JFrameChat frame2) throws IOException{
        super("servidor");
        this.frame = frame;
        this.frame2 = frame2;
    }
    public void startServer(){//Metodo para iniciar el servidor
        try{
            System.out.println("Esperando..");//se espera la conexion
            cs=ss.accept();//comienza el socket y espera una conexion desde el cliente
            frame2.mostrarMensaje("TU INICIAS EL JUEGO. COMIENZA HACIENDO UNA PREGUNTA");
            System.out.println("Cliente en linea");
            if(frame!=null){
                frame.conexionLista();
            }
            salidaCliente = new DataOutputStream(cs.getOutputStream());
            entradaJugador = new DataInputStream(cs.getInputStream());
            
            salidaCliente.writeUTF(frame.getNombre());
            String nombreCliente = entradaJugador.readUTF();
            frame2.setDatosOponente(nombreCliente);
            //enviar flujo de salida al JFrame
            frame2.setSalida(salidaCliente);
            new Thread(() -> {
                try{
                    while(true){
                        String mensaje = entradaJugador.readUTF();
                        frame2.procesarMensajeRecibido(mensaje);
                    }
                }catch(IOException e){
                    frame2.mostrarMensaje("Conexion al iniciar servidor");
                }
            }).start();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
