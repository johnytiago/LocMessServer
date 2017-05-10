package pt.ulisboa.tecnico.cmov.projcmu.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.projcmu.Shared.Location;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.Message;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.Restriction;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.ServerContext;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.User;

public class Context implements ServerContext{
	Map<Location,Map<Restriction,List<Message>>> AllMessages = new HashMap<Location,Map<Restriction,List<Message>>>();
	List<User> users = new ArrayList<User>();
	
	public boolean register(User user){
		if(!users.contains(user)){
			users.add(user);
			return true;
		}
		return false;
	}
	
	public User userRegistered(User user){
		if(users.contains(user)){
			User user1 = users.get(users.indexOf(user));
			if(user1.isValid(user)){
				return user1;
			}
			return null;//user1.isValid(user);
		}
		return null;
		//return false;
	}
	
	public boolean AddLocation(Location loc){
		//test if location already exists
		if(loc.getCreationUser()==null){
			//TODO: return no user created
			return false;
		}
		if(AllMessages.containsKey(loc)){
			//TODO: location already exists
			return false;
		}
		HashMap<Restriction,List<Message>> Restriction_messages = new HashMap<Restriction,List<Message>>();
		AllMessages.put(loc, Restriction_messages);
		//System.out.println(" Has null?? ");
		//System.out.println(AllMessages.get(loc)!=null);
		return true;
	}
	
	public List<Location> getNearLocationsWithMessages(User user){
		List<Location> Locations = new ArrayList<Location>();
		
		//Retrieve locations
		//for(Location loc : AllMessages.keySet()){
		for(Location loc : getNearLocations(user)){
			Map<Restriction,List<Message>> locationMessages = (Map<Restriction,List<Message>>) AllMessages.get(loc);
			Locations.add(loc);
			//Retrieve Relevant Messages From Locations
			System.out.flush();
			for(Restriction restriction: locationMessages.keySet()){
				//tests if restriction matches
				if(restriction.match(user.getProfile().getKeyPairs())){
					//if matches add messages to be sent
					loc.getMessages().addAll(locationMessages.get(restriction));
				}
			}
		}
		return Locations; 
	}
	
	public List<Location> getNearLocations(User user){
		List<Location> locations = new ArrayList<Location>();
		System.out.println("Start GetLocation");
		System.out.println("User: "+user.getUsername());
		if(user.getLocation()!=null){
			System.out.println("lat: "+user.getLocation().getLat());
			System.out.println("lng: "+user.getLocation().getLng());
			//Retrieve locations
			for(Location loc : AllMessages.keySet()){
				if(loc.in(user.getLocation())){
					locations.add(loc.clone());
				}
			}
		}
		System.out.println("End GetLocation");
		return locations; 
	}
	
	public boolean newMessage(Message message){
		Location messageLocation = message.getLocation();
		Map<Restriction,List<Message>> messages = AllMessages.get(messageLocation);
		if(messages==null){
			//TODO: no location
			return false;
		}
		if(messages.keySet().contains(message.getRestriction())){
			//has restriction
			messages.get(message.getRestriction()).add(message);
		}else{
			//no restriction
			List<Message> AddMessages = new ArrayList<Message>();
			AddMessages.add(message);
			messages.put(message.getRestriction(), AddMessages);
			messageLocation.getMessages().add(message);
		}
		return true;
	}
	
	public boolean RemoveMessage(Message message,User creationUser){
		Location messageLocation = message.getLocation();
		Map<Restriction,List<Message>> messages = AllMessages.get(messageLocation);
		if(messages==null){
			//TODO: no location
			return false;
		}
		for(List<Message> insideMessages : messages.values()){
			// Se tem a mensagem e o user da mensagem é o mesmo do pedido remove e sai
			System.out.println("entra");
			System.out.println(insideMessages.contains(message));
			System.out.println(message.getUser().equals(creationUser));
			if(insideMessages.contains(message) && message.getUser().equals(creationUser)){
				insideMessages.remove(message);
				return true;
			}
		}
		return false;
	}

	public boolean RemoveLocationRequest(Location location, User creationUser) {
		//List<Location> location = AllMessages.keySet();
		for(Location loc : AllMessages.keySet()){
			if(loc.equals(location) && loc.getCreationUser().equals(creationUser)){
				AllMessages.remove(loc);
				return true;
			}
		}
		return false;
	}

	public boolean SaveProfile(User user) {
		if(!users.contains(user)){
			return false;
		}
		
		User user1 = users.get(users.indexOf(user));
		if(user1.isValid(user)){
			user1.setProfile(user.getProfile());
			return true;
		}
		
		return false;
	}
	
}
