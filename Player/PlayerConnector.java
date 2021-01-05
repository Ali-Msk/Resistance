/**
 * [PlayerConnector.java]
 * this class gets has GUI for the user to 
 * input server's IP and a port number 
 * so they can connect to the server
 * @author Ali Meshkat
 */
 

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;


public class PlayerConnector {
 private Socket socket;
 private int playerNum;
 private String serverIP;
 private int serverPort;


 PlayerConnector() {
  System.out.println("player connector running");
  
  
  //use dialog boxes to get server ip and port from user
  boolean serverNotConnected = true;
  while (serverNotConnected) {//if not connected yet
   
   this.serverIP = JOptionPane.showInputDialog("Please enter server's IP address:");// asks for IP
   
   boolean correctFormat = false;
   while (!correctFormat) {
    try {
     this.serverPort = Integer.parseInt(JOptionPane.showInputDialog("Please enter port to connect via: "));
     correctFormat = true;
    } catch (NumberFormatException e) {//checks  for numberFormatException
     //tellls usser about error
     JOptionPane.showMessageDialog(null, "Please ensure the port entered is a number");

     
     System.out.println("wrong format entered for port");
     e.printStackTrace();
    }
   }
   
   try {
    this.socket = new Socket(this.serverIP, this.serverPort);
    this.socket.setSoTimeout(10000); // closes after
    serverNotConnected = false;
    getMessage();
   } catch (Exception E) {
    //tells user about failed connection and asks whether they want to exit or try again
    int option = JOptionPane.showConfirmDialog(null, "Could not find server. Server may be down or IP and/or port were entered incorrectly. Try again?");
    if (option == 1 || option ==2) {//gets response and exits if user says so
     System.exit(0);
    }
    
    System.out.println("Error initializing socket in player connector constructor");
    E.printStackTrace();
   }
  }
 }

 /**
  * getMessage this method runs a new thread that reads from the server
  * 
  * @param a
  *            socket used to connect to server
  * @return a string representing the message
  * @author: Ali Meshkat
  */
 public void getMessage() {
  System.out.println("running readFromServer");
  Thread t = new Thread(new ReadFromServer()); // create a thread for

  t.start(); // start the new thread

 }

 /**
  * [ReadFromServer] this class is used as a thread to input  from
  * the server and send it to the getMessageln method
  * 
  * @author Ali Meshkat
  *
  */
 class ReadFromServer implements Runnable {
  private BufferedReader input; // Stream for network input
  private boolean running;

  ReadFromServer() {
   try {
    InputStreamReader stream = new InputStreamReader(socket.getInputStream()); // input steam to be used in
                       // buffered reader
    this.input = new BufferedReader(stream); // buffered reader for input
   } catch (Exception E) {
    E.printStackTrace();
   }
   running = true; // sets running to true (used to determine for how long the it will try to read)

  }

  public void run() {
   String msg = "";
   while (running) { // loop unit a message is received
    try {
     if (input.ready()) { // check for an incoming messge
      msg = input.readLine(); // get a message from the client
      if (msg.startsWith(("connected:"))) {
       playerNum = Integer.parseInt(msg.substring(msg.length() - 1, msg.length()));
      } else if (msg.startsWith("startGame")) {//when recieved starts game right away
       new PlayerDisplayer(socket, playerNum);
       running = false;
      }
     }
    } catch (IOException e) {
     System.out.println("Failed to receive msg from the client");
     e.printStackTrace();
    }
   }
  }
 }

}
