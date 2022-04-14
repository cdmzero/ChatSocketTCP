/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Flujo.Flujo;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Servidor extends Thread
{
    

  
    // Creamos un vector (array unidimensional) para almcenar usuarios
     public static Vector usuarios = new Vector();
     public static Vector LogUsuarios = new Vector();
     public static int contador;
     
  
    
     public static void main (String args[])
     {
         
       
     
       
       
     // Declaramos el socket del servidor
     ServerSocket ss = null;
       try
       {
           // Inicializamos el socket del servidor exponiendo el puerto 50002
           ss = new ServerSocket(50004);
           
       }
       catch (IOException e)
       {
           // Notificamos error 
           System.out.println("Comunicación rechazada."+ e);
           System.exit(1);
       }
       
         System.out.println("Esperando...");
       // Si el socket del servidor inicializa sin errores
       while (true)
       {
           try
           {
               // Aceptamos conexiones de clientes
               Socket cs = ss.accept();
             
               // Pasamos un contador al cliente para generar un chat numerado
               contador++;
               
               // Definimos una salida de flujo
               DataOutputStream out;
               
               // Costruimos la conexion saliente con el socket y el flujo
               out = new DataOutputStream(cs.getOutputStream());
               
               // Le mandamos un tipo entero al cliente
               out.writeInt(contador);
               
               
               System.out.println("Conexion aceptada del Cliente: "+cs.getPort());
             
               // Declaramos el flujo de informacion a traves del socket del cliente. 
               Flujo flujo = new Flujo(cs);
             
              
               // Declaramos el hilo de ejecucion con el flujo de informacion del cliente
               Thread t = new Thread(flujo);
        
               // Procesamos el hilo
               t.start();
           }
           catch(IOException ioe)
           {
           System.out.println("Error: "+ioe);
           }
       }
     }
}