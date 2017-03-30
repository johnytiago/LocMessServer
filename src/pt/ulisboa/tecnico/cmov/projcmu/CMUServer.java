package pt.ulisboa.tecnico.cmov.projcmu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.projcmu.core.Context;
import pt.ulisboa.tecnico.cmov.projcmu.request.Request;

public class CMUServer {
	
	public static final int portNumber = 8086;
	private static ServerSocket server = null;
	
	public static void main(String[] args) {
		try{
			Context app = new Context();
			server = new ServerSocket(portNumber);
			System.out.println("accept");
			while(true){
				Socket client = server.accept();
				
				ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
		        ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
				
		        Request req = (Request) inFromClient.readObject();
		        
		        outToClient.writeObject(req.Run(app));
		        outToClient.flush();
			}
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}	
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
