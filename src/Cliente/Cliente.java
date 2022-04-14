/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class Cliente extends Frame implements ActionListener
{
    static Socket ss = null; // Declaramos el Socket del servidor
    static BufferedInputStream bufferIn; // Declaramos el buffer de los datos de entrada
    static BufferedOutputStream bufferOut; // Declaramos el buffer de los datos de salida
    static DataInputStream in; // Declaramos los datos procesados de entrada desde el servidor
    static DataOutputStream out; // Declaramos los datos procesados de salida hacia el servidor
    static TextField salida;
    static TextArea entrada;
    static String texto;
    static Button btnenviar;
    static int contador;
   
  
        public Cliente(int contador)
        {
            setTitle("Chat "+contador);
            setSize(350,200);
            salida = new TextField(30);
            salida.addActionListener(this);

            entrada = new TextArea();
            entrada.setEditable(false);

            btnenviar = new Button("Enviar");
            btnenviar.addActionListener(enviar);
            add("South", salida);
            add("Center", entrada);
            add("North", btnenviar);
            setVisible(true);
         }
         
         public static void main(String[] args) throws IOException{
         
         
            // Creamos un menu de cliente

            
            
            
               
                    // Seteamos el socket del servidor
                    ss = new Socket("127.0.0.1",50004);
                try
                {
                    // Almacenamos en buffers la entrada/salida de info del/para servidor
                    bufferIn  = new BufferedInputStream(ss.getInputStream());
                    bufferOut = new BufferedOutputStream(ss.getOutputStream());
         
                    // Procesamos la entrada/salida de info del/para servidor
                    in = new DataInputStream(bufferIn);
                    
                    //Recuperamos la posicion desde el servidor;
                    contador = in.readInt();
                    
                    
                    Cliente cliente = new Cliente(contador);
                
            
                    out = new DataOutputStream(bufferOut);
                }
                catch (UnknownHostException e)
               {   
                System.out.println("No se puede acceder al servidor.");
                System.exit(1);
               }
                catch (IOException ioe)
               {
               System.out.println("Comunicación rechazada.");
               System.exit(1);
               }
               
                
          
               //Mientras la conexion contra el servidor esté libre de fallos
               while (true)
               {
                    try
                     {
                         // Procemos en UTF la entrada de informacion y la volcamos en una cadena.
                         String linea = in.readUTF();
                         
                         // Agrega una nueva linea al texto 
                         entrada.append(linea+"\n");
                     }
                     catch(IOException e)
                     {
                         System.exit(1);
                     }
               }
         
        }
             
       
        
ActionListener enviar = new ActionListener(){         
       @Override
       public void actionPerformed (ActionEvent e)
       {
          //Declaramos la variables 
          texto = salida.getText();
           System.out.println(texto);
           salida.setText("");
          
          
         
              try
              {
                    out.writeUTF(texto);
                    out.flush();
              }
              catch (IOException es)
              {
              System.out.println("Error: "+ es);
              }
       }
};
    
      public boolean handleEvent(Event e)
      {
          if ((e.target == this) && (e.id == Event.WINDOW_DESTROY))
          {
              if (ss != null)
              {
                  try
                  {
                      ss.close();
                  }
                  catch (IOException ioe) 
                  {
                     System.out.println("Error: "+ioe);
                  }
                
                 this.dispose();
             }
         }
         return true;
     }

    
    public void actionPerformed(ActionEvent e) {
             //Declaramos la variables 
          texto = salida.getText();
           System.out.println(texto);
           salida.setText("");
          
          
         
              try
              {
                    out.writeUTF(texto);
                    out.flush();
              }
              catch (IOException es)
              {
              System.out.println("Error: "+ es);
              }
    }

}