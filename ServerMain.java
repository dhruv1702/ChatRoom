package assignment7;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ServerMain {
	private static int port = 4000;
	private static ArrayList<ObjectOutputStream> writers = new ArrayList<ObjectOutputStream>();
	private static ArrayList<String> users = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(port);
		try{
			while(true){
				ServerHandler newConnection = new ServerHandler(server.accept());
				newConnection.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			server.close();
		}
	}

	public static class ServerHandler extends Thread{
		String username;
		public Socket socket;
		boolean kicked = false;

		public ServerHandler(Socket socket) throws IOException {
			this.socket = socket;
		}

		public void run(){
			ObjectInputStream in = null;
			ObjectOutputStream out = null;


			try {
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());

				ChatMessage message = (ChatMessage) in.readObject();
				username = message.getSender();

				if(username.equals("SERVER") || users.contains(username)){
					ChatMessage IllegalUsername = new ChatMessage();
					IllegalUsername.setSender("SERVER");
					IllegalUsername.setMessage("Username is already active or reserved on this server, please exit and try again.");
					IllegalUsername.user_list.addAll(users);
					out.writeObject(IllegalUsername);
					socket.close();
					kicked = true;

				}else{
					writers.add(out);
					users.add(username);
					message.setSender("SERVER");
					message.setMessage(username + " has connected");
					broadcast(message);
				}


				while(socket.isConnected()){
					ChatMessage input_message = (ChatMessage) in.readObject();
					if(input_message != null){
						broadcast(input_message);	
					}
				}

			} catch (IOException | ClassNotFoundException e) {
				if(!kicked){
					writers.remove(out);
					users.remove(username);
					ChatMessage message = new ChatMessage();
					message.setSender("SERVER");
					message.setMessage(username + " has disconnected");
					try {
						broadcast(message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}	
		}

		public void broadcast(ChatMessage message) throws IOException{
			message.user_list.addAll(users);
			for(ObjectOutputStream writer: writers){
				writer.writeObject(message);
			}
		}


	}
}