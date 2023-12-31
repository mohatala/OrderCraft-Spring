package craft.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import craft.config.DatabaseConnnect;
import org.apache.log4j.Logger;

import craft.model.Article;
import craft.model.I_Article;
import craft.model.Client;
public class ArticleDAO implements I_Article{
    Connection con = DatabaseConnnect.getInstance().getConnection();
    PreparedStatement statement = null;
    ResultSet st=null;
    Article artDao=null;
	SqlOperations sqloperation=new SqlOperations();
    Logger log = Logger.getLogger(CommandeDAO.class.getName());
	@Override
	public Article ajouterArticle(Article c) {
		 // Ajouter Article
		String insertQuery = "INSERT INTO article(libelle, categorie, prix, stock) VALUES ('"+c.getLibelle()+"', '"+c.getCategorie()+"', '"+c.getPrix()+"', '"+c.getStock()+"')";     
		int idArticle =  sqloperation.ajouterSql(insertQuery,"ADD");
		log.debug("Article Ajouter");
		return this.afficherArticleAvecId(idArticle);
	}
	
	@Override
	public Article modifierArticle(Article c) {
		 // Modifier Article
		   String insertQuery = "UPDATE article set libelle='"+c.getLibelle()+"', categorie='"+c.getCategorie()+"', prix='"+c.getPrix()+"', stock='"+c.getStock()+"' WHERE id_article="+c.getId_article();
		   int idArticle =  sqloperation.ajouterSql(insertQuery,null);
			log.debug("Article Modifier");
		   return this.afficherArticleAvecId(c.getId_article());
	}
	
	@Override
	public Article afficherArticleAvecId(int id){
		//Afficher Les Article Par id article
		 try {
			String qry="select * from article where id_article="+id;
			st=sqloperation.getSql(qry);
			
			while (st.next()) {
				artDao=new Article.ArticleBuilder().setId_article(st.getInt(1)).setLibelle(st.getString(2)).setCategorie(st.getString(3)).setPrix(st.getDouble(4)).setStock(st.getInt(5)).build();
			}
			log.debug("Article afficher avec id="+id);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return artDao; 
	}
	
	@Override
	public ArrayList<Article> afficherArticles(){
		//Afficher tous Les Articles

		ArrayList<Article> articlesList=new ArrayList<>();
		 try {
			 String qry="select * from article";
			 st=sqloperation.getSql(qry);
			
			while (st.next()) {
				artDao=new Article.ArticleBuilder().setId_article(st.getInt(1)).setLibelle(st.getString(2)).setCategorie(st.getString(3)).setPrix(st.getDouble(4)).setStock(st.getInt(5)).build();
				articlesList.add(artDao);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			log.debug("afficher list des Articles");

		return articlesList; 
	}
	
	@Override
	public boolean supprimeArticle(int id) {
			//Supprimer Article
        	String query = "DELETE FROM article WHERE id_article = "+id;
    		int check= sqloperation.ajouterSql(query,null);
        	if(check>0) {
        		log.debug("Article Supprimer");
     		   return true;
     	   }
     		return false;
	}
}
