package pt.ulisboa.tecnico.cmov.projcmu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.projcmu.Shared.Location;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.Message;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.Restriction;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.User;
import pt.ulisboa.tecnico.cmov.projcmu.request.AddLocationRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.AddMessageRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.GetInfoFromServerRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.LogInRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.RemoveLocationRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.RemoveMessageRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.Request;
import pt.ulisboa.tecnico.cmov.projcmu.request.SignInRequest;
import pt.ulisboa.tecnico.cmov.projcmu.response.Response;

public class TestClient {

	public static void main(String[] args) {
//		try {
//			Socket client = new Socket("localhost", CMUServer.portNumber);
//			
//			ObjectOutputStream outToServer = new ObjectOutputStream(client.getOutputStream());
//	        ObjectInputStream inFromServer = new ObjectInputStream(client.getInputStream());
//
//	        outToServer.writeObject(new LogInRequest("User","Pass"));
//	        Response a = (Response) inFromServer.readObject();
//			System.out.println("Result: " +a.Run());
//			System.out.println("Done");
//			client.close();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		User user = new User("User","Pass");
		Location loc = new Location(38,-9,"Local 1",user);
		Restriction restriction = new Restriction(true);
		restriction.AddRestrictions("clube", "num dá");
		Message message = new Message(loc,"alguma cena",restriction,user);
		user.setLocation(loc);
		Client cli = new Client();
		List<String> BeaonIds = new ArrayList<String>();
		
//		SendRequest(new SignInRequest("User","Pass"));
		cli.SignUp(user);
//		SendRequest(new LogInRequest("User","Pass"));
		cli.LogIn(user);
//		SendRequest(new SignInRequest("User","Pass"));
		cli.SignUp(user);
//		
//		SendRequest(new AddLocationRequest(loc));
		cli.createLocations(loc);
//		SendRequest(new AddMessageRequest(message));
		cli.postMessage(message);
//		SendRequest(new GetInfoFromServerRequest(user,loc));
		for(Location location : cli.getLocations(loc, new ArrayList<String>())){
			location.DumpInfo();
		}
		
		//user.addKeyPair("clube", "num dá");
		cli.addKeyPair("clube", "num dá");
//		SendRequest(new GetInfoFromServerRequest(user,loc));
		for(Location location : cli.getLocations(loc, BeaonIds)){
			location.DumpInfo();
		}
		System.out.println("||start");
		cli.LogOut();
		cli.LogIn(user);
		for(Location location : cli.getLocations(loc, BeaonIds)){
			location.DumpInfo();
		}
		System.out.println("||end");
////		SendRequest(new RemoveMessageRequest(message,user));
//		cli.unpostMessage(message);
////		SendRequest(new GetInfoFromServerRequest(user,loc));
//		for(Location location : cli.getLocations(loc, null)){
//			location.DumpInfo();
//		}
////		
////		SendRequest(new RemoveLocationRequest(loc,user));
//		cli.removeLocations(loc);
////		SendRequest(new GetInfoFromServerRequest(user,loc));
//		for(Location location : cli.getLocations(loc, null)){
//			location.DumpInfo();
//		}		
	}

}
