/**
 * [ServerConnector.java]
 * this class manages the interactions between server
 * and user that will result in them connecting 
 * until they are ready to run the game
 * it later runs main game so the game can begin
 * @author Ali Meshkat
 */


import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JOptionPane;

public class ServerConnector {
 private ServerSocket socket;//server main socket 
 private Socket player1, player2; //user sockets
 private int port;
 
 ServerConnector(){
  boolean running = false;
  boolean bothRegistered = false; //whether two connections are made or not
  this.player1 = null;
  this.player2 = null;
  
  
  boolean correctFormat = false;
  while (!correctFormat) {
   try {
    this.port = Integer.parseInt(JOptionPane.showInputDialog("Please enter port number to set server on: "));
    correctFormat = true;
   } catch (NumberFormatException e) {//checks  for numberFormatException
    //tellls usser about error
    JOptionPane.showMessageDialog(null, "Please ensure the port entered is a number");

    
    System.out.println("wrong format entered for port");
    e.printStackTrace();
   }
  }
  
  try{
   this.socket = new ServerSocket(this.port);
   this.socket.setSoTimeout(100000); //closes after 100secs
   System.out.println("waiting for clients...");
   running = true;
  }catch(SocketException E){
   System.out.println("Error initializing socket in ServerConnector constructor");
   E.printStackTrace(); 
  }catch(Exception E){
   System.out.println("non-socket error in ServerConnector constructor");
   E.printStackTrace();
  }
  
  
  
  //waits for two clients to connect 
  while (running){
   try{
    //accepts players if not already
    if(this.player1 == null){
     this.player1 = this.socket.accept();
     System.out.println("player1 joined");
    }else if (this.player2 == null){
     this.player2 = this.socket.accept();
     System.out.println("player2 joined");
     System.out.println("starting game");
    }else{// if both accepted
     running = false;
     bothRegistered = true;
    
    }
 
   }catch(SocketException E){
    System.out.println("Error initializing socket at ServerConnector");
    E.printStackTrace(); 
   }catch(Exception E){
    System.out.println("non-socket error in ServerConnector constructor(while looop)");
    E.printStackTrace();
   }
  }
  //ends loop
  if(bothRegistered){
   sendConfirmation();
  }
 }
 
 /**
  * sendConfirmation is called after both clients are connected 
  * it sends them info on what player number they are assigned and 
  * tells them when to  begin game
  * @return void
  * @param none
  */
 void sendConfirmation(){
  try{
   System.out.println("sending confirmation");
   PrintWriter output = new PrintWriter(this.player1.getOutputStream());
   output.println("connected:1");//tells the player their player number
   output.println("startGame");
   output.flush();
   output = new PrintWriter(this.player2.getOutputStream());
   output.println("connected:2");//tells the player their player number
   output.println("startGame");
   output.flush();
   new TestMainGame( this.player1, this.player2);//starts game 
   System.out.println("main game ran");
  }catch(SocketException E){
   System.out.println("Error initializing socket in ServerConnector sendConfirmation");
   E.printStackTrace(); 
  }catch(Exception E){
   System.out.println("non-socket error in ServerConnector sendConfirmation");
   E.printStackTrace();
  }
 }
}
