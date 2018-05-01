package es.ucm.fdi.iw.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.tusnoficias.LocalData;

@Controller	
@RequestMapping("admin")
public class AdminController {
	
	private static Logger log = Logger.getLogger(AdminController.class);
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EntityManager entityManager;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("s", "../static");
    }

	@GetMapping({"", "/"})
	public String root(Model m) {
		m.addAttribute("users", entityManager
				.createQuery("select u from User u").getResultList());
		
		return "admin";	
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@Transactional
	public String addUser(
			@RequestParam String login, 
			@RequestParam String password, 
			@RequestParam(required=false) String isAdmin, Model m) {
		User u = new User();
		u.setLogin(login);
		u.setPassword(passwordEncoder.encode(password));
		u.setRoles("on".equals(isAdmin) ? "ADMIN,USER" : "USER");
		entityManager.persist(u);
		
		entityManager.flush();
		m.addAttribute("users", entityManager
				.createQuery("select u from User u").getResultList());
		
		return "admin";
	}
	
	/**
	 * Returns a users' photo
	 * @param id of user to get photo from
	 * @return the image, or error
	 */
	@RequestMapping(value="/photo/{id}", 
			method = RequestMethod.GET, 
			produces = MediaType.IMAGE_JPEG_VALUE)
	public void userPhoto(@PathVariable("id") String id, 
			HttpServletResponse response) {
	    File f = localData.getFile("user", id);
	    try (InputStream in = f.exists() ? 
		    	new BufferedInputStream(new FileInputStream(f)) :
		    	new BufferedInputStream(this.getClass().getClassLoader()
		    			.getResourceAsStream("unknown-user.jpg"))) {
	    	FileCopyUtils.copy(in, response.getOutputStream());
	    } catch (IOException ioe) {
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
	    	log.info("Error retrieving file: " + f + " -- " + ioe.getMessage());
	    }
	}
	
	/**
	 * Uploads a photo for a user. Intended to be used via Ajax
	 * @param id of user 
	 * @param photo to upload
	 * @return a textual response indicating success, status code
	 */
	@RequestMapping(value="/photo/{id}", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(
    		HttpServletResponse response,
    		@RequestParam("photo") MultipartFile photo,
    		@PathVariable("id") String id){

		String error = "";
        if (photo.isEmpty()) {
        	error = "You failed to upload a photo for " 
                + id + " because the file was empty.";        
        } else {
	        File f = localData.getFile("user", id);
	        try (BufferedOutputStream stream =
	                new BufferedOutputStream(
	                    new FileOutputStream(f))) {
	            stream.write(photo.getBytes());
	            return "Uploaded " + id 
	            		+ " into " + f.getAbsolutePath() + "!";
	        } catch (Exception e) {
		    	error = "Upload failed " 
		    			+ id + " => " + e.getMessage();
	        }
        }
        // exit with error, blame user
    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return error;
	}
}
