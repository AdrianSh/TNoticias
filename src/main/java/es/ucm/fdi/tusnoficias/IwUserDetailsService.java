package es.ucm.fdi.tusnoficias;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import es.ucm.fdi.tusnoficias.model.User;

public class IwUserDetailsService implements UserDetailsService {

	private static Logger log = Logger.getLogger(IwUserDetailsService.class);
    private EntityManager entityManager;
    private static IwUserDetailsService instance;
    
    @Autowired
	public IwUserDetailsService() {
    	IwUserDetailsService.instance = this;
	}
    
    public static IwUserDetailsService getInstance() {
    	return IwUserDetailsService.instance;
    }
    
    @PersistenceContext
    public void setEntityManager(EntityManager em){
        entityManager = em;
    }

    public UserDetails loadUserByUsername(String username){
    	try {
	        User u = entityManager.createQuery("from User where login = :login", User.class)
	                            .setParameter("login", username)
	                            .getSingleResult();
	        log.warn("loadUserByUsername('" + username + "'): " + (u!= null ? u.getLogin() : ""));
	        return new es.ucm.fdi.tusnoficias.UserDetails(u);
	    } catch (Exception e) {
    		log.info("No such user: " + username);
    		throw new UsernameNotFoundException(username, e);
    	}
    }
    
    public User attachUser(User u) {
    	return this.entityManager.find(User.class, u.getId());
    }
}