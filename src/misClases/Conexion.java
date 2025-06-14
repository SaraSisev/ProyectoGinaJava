/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misClases;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Eliba
 */
public class Conexion {
    //atributos final para definir que no sean modificados, esos son de ley
    private final int puerto = 54321;//Puerto que utilizaremos para realizar la conexion
    private final String host = "172.16.127.208";//Host para nuestra conexion
    protected String mensajeServidor;//Mensajes de entrada (recibidios) en el servidor
    //Sera el puerto de escucha de nuestro servidor
    protected ServerSocket ss;//Socket del servidor 
    //Es el canal de comunicacion activo entre el Jugador y el servidor
    protected Socket cs;//Socket del cliente
    // Cliente->Servidor     // Servidor->Cliente
    protected DataOutputStream salidaServidor, salidaCliente;//Flujo de datos de salida
    // Servidor<-Cliente     Cliente<-Servidor
    protected DataInputStream entradaServidor, entradaJugador;//Flujo de datos de entrada   
    
    //constructor
    public Conexion(String tipo) throws IOException{
        if(tipo.equalsIgnoreCase("servidor")){
            ss=new ServerSocket(puerto);//se instancia el socket para el servidor con el valor del puerto
            cs=new Socket();//instancia del socket para el cliente
        }else{
            cs = new Socket(host, puerto);//instancia de nuestro socket para el cliente en caso de localhost 
        }
    }
}
