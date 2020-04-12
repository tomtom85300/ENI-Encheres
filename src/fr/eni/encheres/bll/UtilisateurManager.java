/**
 * 
 */
package fr.eni.encheres.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dal.ArticleVenduDAO;
import fr.eni.encheres.dal.CategorieDAO;
import fr.eni.encheres.dal.CodesResultatDAL;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.EnchereDAO;
import fr.eni.encheres.dal.RetraitDAO;
import fr.eni.encheres.dal.UtilisateurDAO;

/**
 * BLL UtilisateurManager
 * @author Oguzhan
 * @version ENI-Encheres - v1.0
 * @date 9 avr. 2020
 */
public class UtilisateurManager 
{
	private static UtilisateurManager instance;
	private UtilisateurDAO utilisateurDAO;
	private RetraitDAO retraitDAO;
	private EnchereDAO enchereDAO;
	private CategorieDAO categorieDAO;
	private ArticleVenduDAO articleDAO;
	
	/**
	 * Constructeur
	 */
	private UtilisateurManager()
	{
		utilisateurDAO = DAOFactory.getUtilisateurDAO();
		enchereDAO = DAOFactory.getEnchereDAO();
		categorieDAO = DAOFactory.getCategorieDAO();
		retraitDAO = DAOFactory.getRetraitDAO();
		articleDAO = DAOFactory.getArticleVenduDAO();
		
	}
	
	/**
	 * Méthode en charge de récuperer l'instance
	 * @return UtilisateurManager
	 */
	public static UtilisateurManager getInstance()
	{
		if(instance == null)
		{
			return new UtilisateurManager();
		}
		return instance;
	}
	
	/**
	 * 
	 * Méthode en charge de récupérer tous les utilisateurs
	 * @return List<Utilisateur>
	 * @throws BusinessException
	 */
	public List<Utilisateur> getAllUtilisateurs() throws BusinessException
	{
		List<Utilisateur> lesUtilisateurs = new ArrayList<Utilisateur>();
		try
		{
			lesUtilisateurs = utilisateurDAO.getAllUtilisateur();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.ERREUR_RECUPERATION_UTILISATEURS);

			throw businessException;	
		}
		
		return lesUtilisateurs;		
	}
	
	/**
	 * 
	 * Méthode en charge de récupérer un utilisateur via son id
	 * @param id
	 * @return Utilisateur
	 * @throws BusinessException
	 */
	public Utilisateur getUtilisateurById(int id) throws BusinessException
	{
		Utilisateur unUtilisateur =  utilisateurDAO.getUtilisateurById(id);
		
		/*Utilisateur unUtilisateur = new Utilisateur();
		try
		{
			unUtilisateur = utilisateurDAO.getUtilisateurById(id);
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UTILISATEUR_INEXISTANT);

			throw businessException;	
		}*/	
		return unUtilisateur;	
	}
	
	/**
	 * 
	 * Méthode en charge de récupérer un utilisateur via un login (pseudo/email) et mdp
	 * @param login
	 * @param MotDePasse
	 * @return Utilisateur
	 * @throws BusinessException
	 */
	public Utilisateur getUtilisateurByPseudoPassword(String login, String MotDePasse) throws BusinessException
	{
		return utilisateurDAO.getUtilisateurByLoginPassword(login, MotDePasse);
	}
	
	/**
	 * 
	 * Méthode en charge d'ajouter un utilisateur
	 * @param unUtilisateur
	 * @return Utilisateur
	 * @throws BusinessException
	 */
	/*public Utilisateur addUtilisateur(Utilisateur unUtilisateur) throws BusinessException
	{		
		if(unUtilisateur.getPseudo().trim().equals("") 
				|| unUtilisateur.getPrenom().trim().equals("") 
				|| unUtilisateur.getNom().trim().equals("") 
				|| unUtilisateur.getEmail().trim().equals("") 
				|| unUtilisateur.getRue().trim().equals("") 
				|| unUtilisateur.getCodePostal().trim().equals("") 
				|| unUtilisateur.getVille().trim().equals("") 
				|| unUtilisateur.getMotDePasse().trim().equals(""))
		{
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.UN_CHAMP_NON_SAISI);

			throw businessException;
		}
		
		Pattern patternPseudo = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher matcherPseudo = patternPseudo.matcher(unUtilisateur.getPseudo());
		boolean resultatPseudo = matcherPseudo.matches();
		if(!resultatPseudo)
		{
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.PSEUDO_NON_ALPHANUMERIQUE);

			throw businessException;	
		}
		
		Pattern patternEmail = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
		Matcher matcherEmail = patternEmail.matcher(unUtilisateur.getPseudo());
		boolean resultatEmail = matcherEmail.matches();
		if(!resultatEmail)
		{
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.EMAIL_FORMAT_INCORRECT);

			throw businessException;	
		}
		
		utilisateurDAO.insert(unUtilisateur);		
		
		return unUtilisateur;
	}*/
	
	/**
	 * 
	 * Méthode en charge d'ajouter un utilisateur
	 * @param unUtilisateur
	 * @return Utilisateur
	 * @throws BusinessException
	 */
	public Utilisateur addUtilisateur(Utilisateur unUtilisateur) throws BusinessException
	{		
		//On contrôle les données du formulaire puis on tente la modification
		this.checkUserFormInfos(unUtilisateur, new BusinessException());
		return utilisateurDAO.insert(unUtilisateur);	

	}
	
	/**
	 * 
	 * Méthode en charge de supprimer un utilisateur
	 * @param unUtilisateur
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean deleteUtilisateur(Utilisateur unUtilisateur) throws BusinessException
	{
		
		//TODO***  Besoin de supprimer toutes les tables liées avant + mettre alerte de confirmation pour prévenir l'utilisateur
		/*
		 * DELETE ENCHERES;
			DELETE RETRAITS;
			DELETE ARTICLES_VENDUS;
			DELETE CATEGORIES;
			DELETE UTILISATEURS;
		 */

		//Suppression des objets liés à l'utilisateur
		 enchereDAO.deleteByUser(unUtilisateur.getNoUtilisateur());
		 articleDAO.deleteByUser(unUtilisateur.getNoUtilisateur());
		 
		 //TODO*** Suppression articles faire foonction suppresion tous articles par user
		 //TODO*** Suppression retrait faire foonction suppresion tous retraits par user
		 
		
		
		//Suppression de l'utilisateur
		return utilisateurDAO.delete(unUtilisateur);
		/*try
		{
			utilisateurDAO.delete(unUtilisateur);
			return true;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.ERREUR_RECUPERATION_UTILISATEURS);

			throw businessException;			
		}*/
	}
	
	/**
	 * 
	 * Méthode en charge de modifier un utilisateur
	 * @param unUtilisateur
	 * @return boolean
	 * @throws BusinessException
	 */
	/*public boolean updateUtilisateur(Utilisateur unUtilisateur) throws BusinessException
	{
		if(unUtilisateur.getPseudo().trim().equals("") 
				|| unUtilisateur.getPrenom().trim().equals("") 
				|| unUtilisateur.getNom().trim().equals("") 
				|| unUtilisateur.getEmail().trim().equals("") 
				|| unUtilisateur.getRue().trim().equals("") 
				|| unUtilisateur.getCodePostal().trim().equals("") 
				|| unUtilisateur.getVille().trim().equals("") 
				|| unUtilisateur.getMotDePasse().trim().equals(""))
		{
			//todo
		}
		
		try
		{
			utilisateurDAO.update(unUtilisateur);
			return true;
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.ERREUR_RECUPERATION_UTILISATEURS);

			throw businessException;			
		}
	}*/
	
	/**
	 * 
	 * Méthode en charge de modifier un utilisateur
	 * @param unUtilisateur
	 * @return boolean
	 * @throws BusinessException
	 */
	public boolean updateUtilisateur(Utilisateur unUtilisateur) throws BusinessException
	{

		//On contrôle les données du formulaire puis on tente la modification
		this.checkUserFormInfos(unUtilisateur, new BusinessException());
		return utilisateurDAO.update(unUtilisateur);	
	}
	
	/**
	 * 
	 * Méthode en charge de contrôler toutes les données du formulaire utilisateur (inscription et modification)
	 * @param unUtilisateur
	 * @param businessException
	 * @throws BusinessException
	 */
	private void checkUserFormInfos(Utilisateur unUtilisateur, BusinessException businessException) throws BusinessException {
		
		//On vérifie que tous les champs sont remplis
		if(unUtilisateur.getPseudo().trim().equals("") 
				|| unUtilisateur.getPrenom().trim().equals("") 
				|| unUtilisateur.getNom().trim().equals("") 
				|| unUtilisateur.getEmail().trim().equals("") 
				|| unUtilisateur.getRue().trim().equals("") 
				|| unUtilisateur.getCodePostal().trim().equals("") 
				|| unUtilisateur.getVille().trim().equals("") 
				|| unUtilisateur.getMotDePasse().trim().equals(""))
		{
			businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.UN_CHAMP_NON_SAISI);

			throw businessException;
		}
		
		//On contrôle les formats des champs
		Pattern patternPseudo = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher matcherPseudo = patternPseudo.matcher(unUtilisateur.getPseudo());
		boolean resultatPseudo = matcherPseudo.matches();
		if(!resultatPseudo)
		{
			businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.PSEUDO_NON_ALPHANUMERIQUE);

			throw businessException;	
		}
		
		Pattern patternEmail = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
		Matcher matcherEmail = patternEmail.matcher(unUtilisateur.getEmail());
		boolean resultatEmail = matcherEmail.matches();
		if(!resultatEmail)
		{
			businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.EMAIL_FORMAT_INCORRECT);

			throw businessException;	
		}
		
		//On vérifie que le pseudo et l'email ne sont pas déjà pris
		if(utilisateurDAO.emailExistant(unUtilisateur.getEmail(), unUtilisateur.getNoUtilisateur())) {
			businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.EMAIL_EXISTANT);
			throw businessException;
		}
		if(utilisateurDAO.pseudoExistant(unUtilisateur.getPseudo(),unUtilisateur.getNoUtilisateur())) {
			businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatBLL.PSEUDO_EXISTANT);
			throw businessException;
		}
		
		
	}
}
