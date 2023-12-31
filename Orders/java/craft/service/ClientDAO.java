package craft.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import craft.config.DatabaseConnnect;
import craft.model.Client;
import craft.model.I_Client;
import craft.model.Commande;

public class ClientDAO implements I_Client{
	
    Connection con = DatabaseConnnect.getInstance().getConnection();
    PreparedStatement statement = null;
    ResultSet st=null;
	Client cl=null;
	SqlOperations sqloperation=new SqlOperations();
    static Logger log = Logger.getLogger(CommandeDAO.class.getName());  

	@Override
	public Client ajouterClient(Client c) {
		 // Ajouter Client
		String insertQuery = "INSERT INTO client(nom, prenom, tel, adresse) VALUES ('"+c.getNom()+"', '"+c.getPrenom()+"', '"+c.getTel()+"', '"+c.getAdresse()+"')";
		int idClient =  sqloperation.ajouterSql(insertQuery,"ADD");
		log.debug("Client ajouter");
		return this.afficherClientsAvecId(idClient);
	}
	
	@Override
	public Client modifierClient(Client c) {
		 // Modifier Client
		   String insertQuery = "UPDATE client set nom='"+c.getNom()+"', prenom='"+c.getPrenom()+"', tel='"+c.getTel()+"', adresse='"+c.getAdresse()+"' WHERE id_client="+c.getId_client()+"";
		   int check= sqloperation.ajouterSql(insertQuery,null);
		   if(check>0) {
				log.debug("Client Modifier");

			   return this.afficherClientsAvecId(c.getId_client());
		   }
		return null;
	}
	
	@Override
	public Client afficherClientsAvecId(int id){
		//Afficher Les information de client par id client

		 try {
			String qry="select * from client where id_client="+id;
			st=sqloperation.getSql(qry);
			
			while (st.next()) {
				cl=new Client.ClientBuilder().setId_client(st.getInt(1)).setNom(st.getString(2)).setPrenom(st.getString(3)).setTel(st.getString(4)).setAdresse(st.getString(5)).build();
			}
			log.debug("Client afficher avec id"+id);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cl; 
	}
	
	@Override
	public ArrayList<Client> afficherClients(){
		//Afficher tous les clients
		ArrayList<Client> clientsList=new ArrayList<>();
		 try {
			String qry="select * from client";
			st=sqloperation.getSql(qry);
			while (st.next()) {
				cl=new Client.ClientBuilder().setId_client(st.getInt(1)).setNom(st.getString(2)).setPrenom(st.getString(3)).setTel(st.getString(4)).setAdresse(st.getString(5)).build();
				clientsList.add(cl);
			}
			log.debug("afficher list Clients");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clientsList; 
	}
	
	@Override
	public boolean supprimeClient(int id) {
		//Supprimer le client et ses commandes
		Commande cmd=null;
		CommandeDAO cmddao=new CommandeDAO();
		String qry="select id_commande from commande where id_client="+id;
		st=sqloperation.getSql(qry);
		try {
			while (st.next()) {
				cmd=new Commande.CommandeBuilder().setId_commande(st.getInt(1)).build();
				cmddao.supprimeCommandes(cmd.getId_commande());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String query = "DELETE FROM client WHERE id_client = "+id;
		int check= sqloperation.ajouterSql(query,null);
	   if(check>0) {
			log.debug("Client supprimer");
		   return true;
	   }
		return false;
	}
}
