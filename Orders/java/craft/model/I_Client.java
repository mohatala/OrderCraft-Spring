package craft.model;

import java.util.ArrayList;

public interface I_Client {
	
	public Client ajouterClient(Client c);
	public Client modifierClient(Client c);
	public Client afficherClientsAvecId(int id);
	public ArrayList<Client> afficherClients();
	public boolean supprimeClient(int id);

}
