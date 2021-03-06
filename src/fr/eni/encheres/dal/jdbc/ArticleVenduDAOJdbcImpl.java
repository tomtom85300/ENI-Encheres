package fr.eni.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.CodesResultatDAL;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.UtilisateurDAO;
import fr.eni.encheres.utils.Utils;


/**
 * 
 * Classe en charge d'effectuer les requêtes vers la table ARTICLES_VENDUS
 * @author Camille
 * @version ENI-Encheres - v1.0
 * @date 7 avr. 2020
 */
public class ArticleVenduDAOJdbcImpl implements ArticleVenduDAO {


	private static final String INSERT = "INSERT INTO ARTICLES_VENDUS (nom_article,description, date_debut_encheres,date_fin_encheres,"
			+ "prix_initial,prix_vente,no_utilisateur,no_categorie, picture) " + "values (?,?,?,?,?,?,?,?,?)";
	private static final String DELETE = "DELETE FROM ARTICLES_VENDUS where no_article = ?";
	private static final String DELETE_BY_USER = "DELETE FROM ARTICLES_VENDUS where no_utilisateur = ?";
	private static final String UPDATE = "UPDATE ARTICLES_VENDUS SET nom_article=?,description=?, date_debut_encheres=?,date_fin_encheres=?,"
			+ "prix_initial=?,prix_vente=?,no_utilisateur=?,no_categorie=?, picture=? WHERE no_article=?";
	private static final String SELECT = "SELECT * FROM ARTICLES_VENDUS WHERE no_article = ?";
	private static final String SELECT_ALL = "SELECT * FROM ARTICLES_VENDUS";
	private static final String CHECK_IF_ARTICLE_ALREADY_EXISTS = "SELECT * FROM ARTICLES_VENDUS WHERE nom_article=? AND description=? AND date_debut_encheres=? AND date_fin_encheres = ? AND" 
			 +" prix_initial = ? AND prix_vente = ? AND no_utilisateur = ? AND no_categorie=? ";
	
	@Override
	/**
	 * 
	 * {@inheritDoc}
	 * @see fr.eni.encheres.dal.DAO#insert(java.lang.Object)
	 */
	public ArticleVendu insert(ArticleVendu article) throws BusinessException {

		
		if(article==null)
		{
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_NULL);
			throw businessException;
		}
		
		try (Connection cnx = Utils.getConnection()) {
			PreparedStatement pstmt = cnx.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, article.getNomArticle());
			pstmt.setString(2, article.getDescription());
			pstmt.setDate(3, Utils.dateUtilVersSQL(article.getDateDebutEncheres()));
			pstmt.setDate(4, Utils.dateUtilVersSQL(article.getDateFinEncheres()));
			pstmt.setInt(5, article.getMiseAPrix());
			pstmt.setInt(6, article.getPrixVente());
			pstmt.setInt(7, article.getUtilisateur().getNoUtilisateur());
			pstmt.setInt(8, article.getCategorie().getNoCategorie());
			pstmt.setString(9, article.getPicture());
			pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				article.setNoArticle(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_ARTICLE_ECHEC);

			throw businessException;

		}
		return article;
	}

	@Override
	/**
	 * 
	 * {@inheritDoc}
	 * @see fr.eni.encheres.dal.DAO#delete(int)
	 */
	public boolean delete(int id) throws BusinessException {

		int nbLignesModifiees = 0;

		try (Connection cnx = Utils.getConnection()) {

			PreparedStatement pstmt = cnx.prepareStatement(DELETE);
			pstmt.setInt(1, id);
			nbLignesModifiees = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SUPPRESSION_ARTICLE_ERREUR);
			throw businessException;
		}
		return nbLignesModifiees > 0;

	}
	@Override
	/**
	 * 
	 * {@inheritDoc}
	 * @see fr.eni.encheres.dal.DAO#delete(int)
	 */
	public boolean deleteByUser(int userId) throws BusinessException {

		int nbLignesModifiees = 0;

		try (Connection cnx = Utils.getConnection()) {

			PreparedStatement pstmt = cnx.prepareStatement(DELETE_BY_USER);
			pstmt.setInt(1, userId);
			nbLignesModifiees = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SUPPRESSION_ARTICLE_ERREUR);
			throw businessException;
		}
		return nbLignesModifiees > 0;

	}

	@Override
	/**
	 * 
	 * {@inheritDoc}
	 * @see fr.eni.encheres.dal.DAO#update(java.lang.Object)
	 */
	public boolean update(ArticleVendu article) throws BusinessException {
		int nbLignesModifiees = 0;

		try (Connection cnx = Utils.getConnection()) {

			PreparedStatement pstmt = cnx.prepareStatement(UPDATE);
			pstmt.setString(1, article.getNomArticle());
			pstmt.setString(2, article.getDescription());
			pstmt.setDate(3, Utils.dateUtilVersSQL(article.getDateDebutEncheres()));
			pstmt.setDate(4, Utils.dateUtilVersSQL(article.getDateFinEncheres()));
			pstmt.setInt(5, article.getMiseAPrix());
			pstmt.setInt(6, article.getPrixVente());
			pstmt.setInt(7, article.getUtilisateur().getNoUtilisateur());
			pstmt.setInt(8, article.getCategorie().getNoCategorie());
			pstmt.setString(9, article.getPicture());
			pstmt.setInt(10, article.getNoArticle());
			pstmt.executeUpdate(); 
			nbLignesModifiees = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.MODIFICATION_ARTICLE_ERREUR);
			throw businessException;
		}
		return nbLignesModifiees > 0;

	}

	@Override
	/**
	 * 
	 * {@inheritDoc}
	 * @see fr.eni.encheres.dal.DAO#selectAll()
	 */
	public List<ArticleVendu> selectAll() throws BusinessException {
		 List<ArticleVendu> listeArticles = new ArrayList<>();

		 try (Connection cnx = Utils.getConnection()) {
				PreparedStatement pstmt = cnx.prepareStatement(SELECT_ALL);
				ResultSet rs = pstmt.executeQuery();
				ArticleVendu article = new ArticleVendu();
				while (rs.next()) {
					article = articleBuilder(rs);
					listeArticles.add(article);

				}
			} catch (Exception e) {
				e.printStackTrace();
				BusinessException businessException = new BusinessException();
				businessException.ajouterErreur(CodesResultatDAL.ERREUR_RECUPERATION_ARTICLES_VENDUS);
				throw businessException;

			}
			return listeArticles;
	}

	@Override
	/**
	 * 
	 * {@inheritDoc}
	 * @see fr.eni.encheres.dal.DAO#selectById(int)
	 */
	public ArticleVendu selectById(int noArticle) throws BusinessException {

		ArticleVendu article = null;

		try (Connection cnx = Utils.getConnection()) {
            PreparedStatement requete = cnx.prepareStatement(SELECT);
            requete.setInt(1, noArticle);
            ResultSet rs = requete.executeQuery();

            if (rs.next()) {
            	article = articleBuilder(rs);
            }
        } catch (Exception e) {
        	e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECTION_ARTICLE_ERREUR);
			throw businessException;
        }
        return article;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 * @see fr.eni.encheres.dal.ArticleVenduDAO#checkIfArticleAlreadyExists(fr.eni.encheres.bo.ArticleVendu)
	 */
	@Override
	public boolean checkIfArticleAlreadyExists(ArticleVendu articleToAdd) throws BusinessException 
	{

		ArticleVendu article = null;
		
		try (Connection cnx = Utils.getConnection()) 
		{
			PreparedStatement pstmt = cnx.prepareStatement(CHECK_IF_ARTICLE_ALREADY_EXISTS);
			pstmt.setString(1, articleToAdd.getNomArticle());
			pstmt.setString(2, articleToAdd.getDescription());
			pstmt.setDate(3,Utils.dateUtilVersSQL(articleToAdd.getDateDebutEncheres()));
			pstmt.setDate(4,Utils.dateUtilVersSQL(articleToAdd.getDateFinEncheres()));
			pstmt.setInt(5, articleToAdd.getMiseAPrix());
			pstmt.setInt(6, articleToAdd.getPrixVente());
			pstmt.setInt(7, articleToAdd.getUtilisateur().getNoUtilisateur());
			pstmt.setInt(8, articleToAdd.getCategorie().getNoCategorie());

            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next())
            {
            	article = articleBuilder(rs);
            }
            
            if(article != null)
    		{
    			return true;
    		}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_OBJET_ECHEC);

			throw businessException;						
		}	
		return false;
	}

	/**
	 * 
	 * Méthode en charge de construire un objet ArticleVendu
	 * @param rs
	 * @return ArticleVendu
	 * @throws SQLException
	 * @throws BusinessException 
	 */
	public ArticleVendu articleBuilder(ResultSet rs) throws SQLException, BusinessException {

		ArticleVendu articleVendu = new ArticleVendu();
		articleVendu.setNoArticle(rs.getInt("no_article"));
		articleVendu.setNomArticle(rs.getString("nom_article"));
		articleVendu.setDescription(rs.getString("description"));
		articleVendu.setDateDebutEncheres(rs.getDate("date_debut_encheres"));
		articleVendu.setDateFinEncheres(rs.getDate("date_fin_encheres"));
		articleVendu.setPrixVente(rs.getInt("prix_vente"));
		articleVendu.setMiseAPrix(rs.getInt("prix_initial"));
		articleVendu.setUtilisateur(this.getUserArticle(rs.getInt("no_utilisateur")));
		articleVendu.setCategorie(this.getCategoryArticle(rs.getInt("no_categorie")));
		articleVendu.setPicture(rs.getString("picture"));

		return articleVendu;

	}
	
	/**
	 * 
	 * Méthode en charge de récupérer l'objet Utilisateur associé à l'article en question
	 * @param userId
	 * @return Utilisateur
	 * @throws BusinessException 
	 */
	private Utilisateur getUserArticle(int userId) throws BusinessException {

		Utilisateur utilisateurArticle = null;
		UtilisateurDAO utilisateurDao = DAOFactory.getUtilisateurDAO();
		utilisateurArticle = utilisateurDao.getUtilisateurById(userId); //On récupère l'utilisateur à partir de son id

		return utilisateurArticle;
	}

	/**
	 * 
	 * Méthode en charge de récupérer l'ojet Catégorie associé à l'article en question
	 * @param categoryId
	 * @return
	 * @throws BusinessException 
	 */
	private Categorie getCategoryArticle(int categoryId) throws BusinessException {

		Categorie categoryArticle = null;
		CategorieDAO categorieDAO = DAOFactory.getCategorieDAO();
		categoryArticle = categorieDAO.selectById(categoryId); 		//On récupère la catégorie à partir de son id

		return categoryArticle;
	}

}
