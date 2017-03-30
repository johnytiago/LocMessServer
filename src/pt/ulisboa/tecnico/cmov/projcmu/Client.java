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
import pt.ulisboa.tecnico.cmov.projcmu.Shared.Profile;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.User;
import pt.ulisboa.tecnico.cmov.projcmu.request.AddLocationRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.AddMessageRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.GetInfoFromServerRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.LogInRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.RemoveLocationRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.RemoveMessageRequest;
import pt.ulisboa.tecnico.cmov.projcmu.request.Request;
import pt.ulisboa.tecnico.cmov.projcmu.request.SignInRequest;
import pt.ulisboa.tecnico.cmov.projcmu.response.AddLocationResponse;
import pt.ulisboa.tecnico.cmov.projcmu.response.AddMessageResponse;
import pt.ulisboa.tecnico.cmov.projcmu.response.GetInfoFromServerResponse;
import pt.ulisboa.tecnico.cmov.projcmu.response.LogInResponse;
import pt.ulisboa.tecnico.cmov.projcmu.response.RemoveLocationResponse;
import pt.ulisboa.tecnico.cmov.projcmu.response.RemoveMessageResponse;
import pt.ulisboa.tecnico.cmov.projcmu.response.Response;
import pt.ulisboa.tecnico.cmov.projcmu.response.SignInResponse;

public class Client implements ClientInterface{
	public static final int portNumber = 8086;
	
	public static Response SendRequest(Request hr) {
		try {
			Socket client = new Socket("localhost", portNumber);
			
			ObjectOutputStream outToServer = new ObjectOutputStream(client.getOutputStream());
	        ObjectInputStream inFromServer = new ObjectInputStream(client.getInputStream());

	        outToServer.writeObject(hr);
	        Response a = (Response) inFromServer.readObject();
	        
			System.out.println("Result: " +a.Run());
			//System.out.println("Done");
			client.close();
			return a;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private User user = null;
	private List<Location> locations = null;

	@Override
	public boolean SignUp(User user) {
		if(this.user!=null){
			//Session already initiated
			return false;
		}
		SignInResponse resp = (SignInResponse) SendRequest(new SignInRequest(user.getUsername(),user.getPassword()));
		if(resp.getSuccessfull()){
			this.user = user;
			return true;
		}
		return false;
	}

	@Override
	public boolean LogIn(User user) {
		if(this.user!=null){
			//Session already initiated
			return false;
		}
		LogInResponse resp = (LogInResponse) SendRequest(new LogInRequest(user.getUsername(),user.getPassword()));
		if(resp.getSuccessfull()){
			//TODO: alterar para buscar defini��es do utilizador
			this.user = user;
			return true;
		}
		return false;
	}

	@Override
	public boolean LogOut() {
		// TODO clean variables
		user = null;
		locations = null;
		return true;
	}

	@Override
	public List<Location> getLocations(Location loc, List<Integer> BeaconIds) {
		if(user==null){
			System.err.println("session not initiated");
			return null;
		}
		GetInfoFromServerResponse resp = (GetInfoFromServerResponse) SendRequest(new GetInfoFromServerRequest(this.user,loc));
		locations = resp.getLocations();
		return resp.getLocations();
	}

	@Override
	public boolean createLocations(Location loc) {
		if(user==null){
			System.err.println("session not initiated");
			return false;
		}
		AddLocationResponse resp = (AddLocationResponse) SendRequest(new AddLocationRequest(loc));
		return resp.isSuccess();
	}

	@Override
	public boolean removeLocations(Location loc) {
		if(user==null){
			System.err.println("session not initiated");
			return false;
		}
		RemoveLocationResponse resp = (RemoveLocationResponse) SendRequest(new RemoveLocationRequest(loc,this.user));
		return resp.isSuccess();
	}

	@Override
	public boolean postMessage(Message m) {
		if(user==null){
			System.err.println("session not initiated");
			return false;
		}
		AddMessageResponse resp = (AddMessageResponse) SendRequest(new AddMessageRequest(m));
		return resp.isSuccess();
	}

	@Override
	public boolean unpostMessage(Message m) {
		if(user==null){
			System.err.println("session not initiated");
			return false;
		}
		RemoveMessageResponse resp = (RemoveMessageResponse) SendRequest(new RemoveMessageRequest(m,this.user));
		return resp.isSuccess();
	}

	@Override
	public List<Message> getAvailableMessages() {
		List<Message> messages = new ArrayList<Message>();
		if(user==null){
			System.err.println("session not initiated");
			return messages;
		}
		for(Location loc : locations){
			messages.addAll(loc.getMessages());
		}
		return messages;
	}

	@Override
	public boolean saveProfile(Profile profile) {
		if(user==null){
			System.err.println("session not initiated");
			return false;
		}
		this.user.setProfile(profile);
		return false;
	}
	
	public boolean addKeyPair(String key, String value){
		if(user==null){
			System.err.println("session not initiated");
			return false;
		}
		this.user.addKeyPair(key, value);
		return true;
	}
	
}
