package pt.ulisboa.tecnico.cmov.projcmu;

import java.util.List;

import pt.ulisboa.tecnico.cmov.projcmu.Shared.Location;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.Message;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.Profile;
import pt.ulisboa.tecnico.cmov.projcmu.Shared.User;

public interface ClientInterface {
	
	/*
	 * Fun��o usaada para registar um novo utilizador na aplica��o
	 * recebe username e password em plaintext
	 * devolve se a opera��o de registo correu com sucesso 
	 * ou houve algum utilizador j� tem o mesmo username
	 * */	
	public boolean SignUp(User user); //Done
	
	/*
	 * Fun��o usada para iniciar sess�o deste utilizador
	 * retorna se a sess�o foi iniciada com sucesso
	 * 
	 * Nota: a interface com o cliente deve manter estado da sess�o iniciada
	 * */
	public boolean LogIn(User user); //Done
	
	/*
	 * Fun��o usada para iniciar sess�o deste utilizador
	 * retorna se a sess�o foi fechada com sucesso
	 * */
	public boolean LogOut(); //Done
	
	/*
	 * Func�o usada para obter a lista de localiza��es perto do utilizador
	 * Recebe utilizador com localiza��o gps do mesmo e ids dos beacons bluetooth encontrados
	 * devolve lista de localiza��es mais pr�ximas. estas localiza��es v�m j� com mensagens dispon�veis
	 * */
	public List<Location> getLocations(Location loc, List<Integer> BeaconIds);
	
	/*
	 * Criar uma nova localiza��o
	 * */
	public boolean createLocations(Location loc); //Done
	
	/*
	 * Remover uma localiza��o criada
	 * */
	public boolean removeLocations(Location loc); //Done

	/*
	 * associar uma mensagem a uma localiza��o
	 * */
	public boolean postMessage(Message m); //Done
	
	/*
	 * Remover a mensagem da localiza��o
	 * */
	public boolean unpostMessage(Message m); //Done
	
	/*
	 * obtem todas as mensagens dispon�veis para qualquer localiza��o perto do mesmo
	 * */
	public List<Message> getAvailableMessages(); //Done
	
	/*
	 * Guardar altera��es ao profile do servidor
	 * */
	public boolean addKeyPair(String key, String value); //Done
	
}
