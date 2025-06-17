/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misClases;

import java.io.BufferedReader;
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
    //constructor para que se pueda ejecutar un metodo, ya que startserver como tal no acaba
    public Servidor(JFrameTablero frame) throws IOException{
        super("servidor");
        this.frame = frame;
    }//Se usa el consutructor de la clase padre
    public void startServer(){//Metodo para iniciar el servidor
        try{
            System.out.println("Esperando..");//se espera la conexion
            cs=ss.accept();//comienza el socket y espera una conexion desde el cliente
            System.out.println("Cliente en linea");
            if(frame!=null){
                frame.conexionLista();
            }
            //se obtiene el flujo de salida del cliente
            salidaCliente = new DataOutputStream(cs.getOutputStream());
            //se le envia un mensaje al cliente usando su flujo de salida
            salidaCliente.writeUTF("Peticion recibida y aceptada");
            //se obtiene el  flujo entrante desde el cliente
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            //ciclo que esta mientras haya mensajes desde el cliente
            while((mensajeServidor = entrada.readLine()) != null){
                //se muestra por pantalla el mensaje recibido
                System.out.println(mensajeServidor);
            }
            //System.out.println("Fin de la conexion");
            //ss.close();//finaliza la conexion entre ambas compus
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
