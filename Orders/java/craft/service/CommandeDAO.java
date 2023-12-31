package craft.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import craft.config.DatabaseConnnect;
import craft.model.Article;
import craft.model.Commande;
import craft.model.I_Commande;
import craft.model.Etat;

public class CommandeDAO implements I_Commande{

    PreparedStatement statement = null;
    ResultSet st=null;
	Commande cmd=null;
	SqlOperations sqloperation=new SqlOperations();
    Connection con = DatabaseConnnect.getInstance().getConnection();
    LocalDate date = LocalDate.now();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static Logger log = Logger.getLogger(CommandeDAO.class.getName());  

    @Override
	public Commande ajouterCommande(Commande c,String listart) {
    	//Ajouter Une Commande
		 try {
	            date.format(formatter);
                // Ajouter Commande
                String insertQuery = "INSERT INTO commande(id_client, etat,date_creation,date_modification) VALUES ('"+c.getId_client()+"', '"+c.getEtat()+"', '"+date+"', '"+date+"')";
                int idCommande = sqloperation.ajouterSql(insertQuery,"ADD");
                cmd=new Commande.CommandeBuilder().setId_commande(idCommande).build();
                        
    			JSONArray array = new JSONArray(listart);  
    			for(int i=0; i < array.length(); i++)   
    			{  
    			JSONObject object = array.getJSONObject(i); 
    			String Query = "INSERT INTO commande_article(id_commande, id_article,qty) VALUES ('"+cmd.getId_commande()+"', '"+object.getInt("id_article")+"', '"+object.getInt("qty")+"')";
                statement = con.prepareCall(Query);
                statement.execute(); 
    			}
    		 log.debug("Commande Ajouter");
    		 
                return cmd;
	                
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	}
	@Override
	public Commande afficherCommandeAvecId(int id){
		//Afficher une commande avec id commande
		 try {
				String qry="select * from commande where id_commande="+id;
				st=sqloperation.getSql(qry);
				while (st.next()) {
					cmd=new Commande.CommandeBuilder().setId_commande(st.getInt(1)).setId_client(st.getInt(2)).setEtat(st.getString(3)).setcreated_at(st.getDate(4).toLocalDate()).setupdated_at(st.getDate(5).toLocalDate()).build();
				}
				log.debug("Afficher Commande  id"+id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cmd; 
	}
	@Override
	public ArrayList<Commande> afficherCommandes(){
		//afficher tous les commandes
		ArrayList<Commande> commandesList=new ArrayList<>();
		 try {
			 	String qry="select * from commande";
				st=sqloperation.getSql(qry);
			
			while (st.next()) {
				cmd=new Commande.CommandeBuilder().setId_commande(st.getInt(1)).setId_client(st.getInt(2)).setEtat(st.getString(3)).setcreated_at(st.getDate(4).toLocalDate()).setupdated_at(st.getDate(5).toLocalDate()).build();
				commandesList.add(cmd);
			}
			log.debug("afficher list Commandes");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commandesList; 
	}
	
	@Override
	public ArrayList<String> afficherInfosCommande(int id){
		//afficher les informations des commandes
		ArrayList<String> commandesList=new ArrayList<>();
		 try {
			 String qry="SELECT commande.id_commande,commande.id_client,commande_article.id_article,commande_article.qty FROM `commande`,commande_article WHERE commande.id_commande=commande_article.id_commande and commande.id_commande="+id;
			st=sqloperation.getSql(qry);
			String ob=null;
			while (st.next()) {
			ob=st.getInt(1)+","+st.getInt(2)+","+ st.getInt(3)+","+ st.getInt(4);
			commandesList.add(ob);
			}
			log.debug("Afficher les infos commande");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commandesList; 
	}
		
	@Override
	public Commande modifieretat(int id,String etat) {
		//modifier l'etat de la commande
		 // Modifier Etat
		Commande cmd=this.afficherCommandeAvecId(id);
		ArticleDAO artdao =new ArticleDAO();
		ArrayList<String> listart=this.afficherInfosCommande(id);
		//System.out.print(cmd.getEtat());
		Etat enumEtat = null;
		if(cmd.getEtat().equals(enumEtat.EnCours.name().toString())|| cmd.getEtat().equals(enumEtat.EnAttente.name().toString())) {
			if(etat.equals(enumEtat.Complete.name().toString())) {
				for(String std:listart) {
					String[] s=std.split(",");
					Article art=artdao.afficherArticleAvecId(Integer.parseInt(s[2]));
					int qty=art.getStock()-Integer.parseInt(s[3]);
					art=new Article.ArticleBuilder().setId_article(art.getId_article()).setLibelle(art.getLibelle()).setCategorie(art.getCategorie()).setPrix(art.getPrix()).setStock(qty).build();
					//System.out.print(art);
					artdao.modifierArticle(art);
				}
			}
		}
		if(cmd.getEtat().equals(enumEtat.Complete.name().toString())) {
			if(etat.equals(enumEtat.Annuler.name().toString())) {
				for(String std:listart) {
					String[] s=std.split(",");
					Article art=artdao.afficherArticleAvecId(Integer.parseInt(s[2]));
					int qty=art.getStock()+Integer.parseInt(s[3]);
					art=new Article.ArticleBuilder().setId_article(art.getId_article()).setLibelle(art.getLibelle()).setCategorie(art.getCategorie()).setPrix(art.getPrix()).setStock(qty).build();
					//System.out.print(art);
					artdao.modifierArticle(art);
				}
			}
		}
        date.format(formatter);
		 String insertQuery = "UPDATE commande set etat='"+etat+"',date_modification='"+date+"' WHERE id_commande="+id;
		 int check= sqloperation.ajouterSql(insertQuery,null);
		 if(check>0) {
			 log.debug("Etat commande modifier");
			   return this.afficherCommandeAvecId(id);
		   }
		  return null;
	}
	
	@Override
	public boolean supprimeCommandes(int id) {
		//Delete from Table Commande Article
        String query = "DELETE FROM commande_article WHERE id_commande = "+id;
		int check= sqloperation.ajouterSql(query,null);
   	   if(check>0) {
   		   //Delete from Table Commande
		   String query1 = "DELETE FROM commande WHERE id_commande = "+id;
		   int check1= sqloperation.ajouterSql(query1,null);
		   if(check1>0) {
			   log.debug("Commande Supprimer");
			   return true;
		   }
   	   }
		return false;
	}
	
}
