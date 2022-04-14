/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Flujo;


import Servidor.Servidor;
import java.net.*;
import java.io.*;
import java.util.*;



public class Flujo extends Thread
{
       Socket cs; // Declaramos el Socket del Cliente
       BufferedInputStream bufferIn; // Declaramos el buffer de los datos de entrada
       BufferedOutputStream bufferOut; // Declaramos el buffer de los datos de salida
       DataInputStream in; // Declaramos los datos procesados de entrada desde el servidor
       DataOutputStream out; // Declaramoslos datos preocesados de salida hacia el servidor

       
       
       // El constructor Flujo acepta como parametro un socket de conexion
       public Flujo (Socket cs){

         try
         {
             // Almacenamos en buffers la entrada/salida de info del servidor
             bufferIn = new BufferedInputStream(cs.getInputStream());
             bufferOut = new BufferedOutputStream(cs.getOutputStream());
         
             // Proceso la entrada/salida de info del servidor
              in = new DataInputStream(bufferIn);
              out = new DataOutputStream(bufferOut);
         }
         catch(IOException e)
         {
             System.out.println("IOException(Flujo): "+ e);
         }
     }
     
     // Metodo para la ejecucion por hilo
     public void run()
     {
       
       // Adherimos el cliente al vector de usuarios
       String mensaje;
       Servidor.usuarios.add (this);
       Servidor.LogUsuarios.add(this);
       
       
       // Notificamos en el chat del nuevo usuario
       mensaje = ">Usuario "+ Servidor.LogUsuarios.indexOf(this) +" se ha conectado";
       broadcast(mensaje);

       
       // Mientras se pueda ejecutar el hilo sin errores
           while(true)
           {
                 try
                 {
                     // Guarda en una cadena el resultado codificado en UTF del mensaje procesado entrante.
                     String linea = in.readUTF();
                     
                     // Si hay informacion recibida
                     if (!linea.equals(""))
                     {
                        
                           // Concatena la Dir IP del cliente a la cadena.
                           linea =">Usuario "+ Servidor.LogUsuarios.indexOf(this)+" "+ linea;
                           
                           // Envia el mensaje al chat
                           broadcast(linea);
                     }
                 }
                 catch(IOException ioe)
                 {
                     // Si el usuario desconecta elimina el objeto del vector usuarios
                     Servidor.usuarios.removeElement(this);
                     
                     // Notifica el mensaje por el metodo broadcast
                     mensaje =">Usuario "+ Servidor.LogUsuarios.indexOf(this) +" se ha desconectado";
                     broadcast(mensaje);
                     
                     break;
                     
                 }
                     
           }
           
    }
     
      // Metodo que acepta cadenas como parametro
      public void broadcast(String mensaje)
      {
         // El hilo recupera el vector de usuarios de manera coordianada con el resto de hilos
         synchronized (Servidor.usuarios)
         {
           
             // Creamos un objeto tipo enumeration con los elementos del vector
             Enumeration e = Servidor.usuarios.elements();
             
             
             // Iteramos hasta que existan elementos 
             while (e.hasMoreElements())
             {
               
                 // Casteamos como objeto tipo Flujo para conseguir el socket de conexion del cliente
                 Flujo f = (Flujo) e.nextElement();
                 
                     try
                     {
                         // Hacemos la distribucion al cliente de manera sincronizada
                         synchronized(f.out)
                         {
                             
                             // Codificamos el mensaje al usuario por UTF
                             f.out.writeUTF(mensaje);
                             
                             // Enviamos el mensaje al usuario 
                             f.out.flush();
                         }
                     }
                     catch(IOException ioe)
                     {
                         System.out.println("Error: "+ioe);
                     }
             }
         }
     }
} 