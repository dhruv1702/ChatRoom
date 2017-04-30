/* CHAT ROOM <MyClass.java>  
 * EE422C Project 7 submission by  
 * Dhruv Verma  
 * dv7229  
 * 16230
 * Sagar Krishnaraj	
 * sk37433
 * Slip days used: 1 
 * Spring 2017 
 */

package assignment7;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.scene.image.Image;

public class Client implements Runnable {
	String host_name;
	int port_number;
	String username;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	public Socket socket;
	public Client_GUI gui;

	public Client(String host, String user) {
		this.host_name = host;
		this.port_number = 4000;
		this.username = user;
	}

	public void run(){
		try {
			socket = new Socket(host_name, port_number);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}catch(IOException e){
			cannotConnect();
			System.out.println("Count Not Connect to IO");
		}


		try{
			connect();
			while(socket.isConnected()){
				ChatMessage message = null;
				message = (ChatMessage) in.readObject();

				if(message != null){
					gui.displayMessage(message);
				}
			}
		}  catch (IOException | ClassNotFoundException e) {
			cannotConnect();
			e.printStackTrace();
		}  
	}

	public void connect() throws IOException{
		ChatMessage createMessage = new ChatMessage();
		createMessage.setSender(username);
		createMessage.setMessage("Connected");
		out.writeObject(createMessage);
		out.flush();
	}

	public void sendMessage(String msg) throws IOException{
		ChatMessage newMessage = new ChatMessage();
		newMessage.setMessage(msg);
		newMessage.setSender(username);
		out.writeObject(newMessage);
		out.flush();
	}
	
	public void sendPhoto(Image image) throws IOException{
		ChatMessage newMessage = new ChatMessage();
		newMessage.setImage(image);
		newMessage.isImage = true;
		newMessage.setSender(username);
		out.writeObject(newMessage);
		out.flush();
	}
	
	public void setGUI(Client_GUI gui){
		this.gui = gui;
	}

	public void cannotConnect(){
		ChatMessage CannotConnect = new ChatMessage();
		CannotConnect.setSender("ERROR");
		CannotConnect.setMessage("Cannot establish connection to server, please exit and try again.");
		gui.displayMessage(CannotConnect);
	}
}