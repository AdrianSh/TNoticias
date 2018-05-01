package es.ucm.fdi.tusnoficias.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.tusnoficias.LocalData;
import es.ucm.fdi.tusnoficias.model.Actividad;
import es.ucm.fdi.tusnoficias.model.Amigos;
import es.ucm.fdi.tusnoficias.model.ComentarioPerfil;
import es.ucm.fdi.tusnoficias.model.User;


@Controller
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private LocalData localData;
	
	@Autowired
	private Environment env;
	
	@ModelAttribute
    public void addAttributes(Model model, Locale locale) {
        model.addAttribute("s", "/static");
		model.addAttribute("prefix", env.getProperty("es.ucm.fdi.tusnoticias.site-url"));
		model.addAttribute("siteName", env.getProperty("es.ucm.fdi.tusnoticias.site-name"));
		model.addAttribute("shortSiteName", env.getProperty("es.ucm.fdi.tusnoticias.short-site-name"));
		
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(new Date());
		
		model.addAttribute("serverTime", formattedDate);
    }
	
	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public String registro(Locale locale, Model model, HttpSession session) {
		String returnn = "registro";
		if (ping(session))
			returnn = "redirect:home";
		model.addAttribute("pageTitle", "Registro");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		return returnn;
	}

	@RequestMapping(value = "/usuario/crear", method = RequestMethod.GET)
	public String registro2(Locale locale, Model model) {
		model.addAttribute("pageTitle", "Registro");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		model.addAttribute("prefix", "./../");
		return "registro";
	}

	

	@RequestMapping(value = "/ajustes", method = RequestMethod.POST)
	@Transactional
	public String handleFileAjustes(@RequestParam("avatar") MultipartFile avatar, @RequestParam("email") String email,
			@RequestParam("pass") String pass, HttpSession session, Model model) {
		User u = (User) session.getAttribute("user");
		String returnn = "redirect:/perfil";
		long id = u.getId();
		String st = Long.toString(id);
		BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
		if (!avatar.isEmpty()) {
			try {

				byte[] bytes = avatar.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(localData.getFile("user", st)));
				stream.write(bytes);
				stream.close();
				u.setAvatar("user/" + u.getId() + "/photo");
				model.addAttribute("avatar",
						Encode.forUriComponent(localData.getFile("user", st).getAbsolutePath()));
				// ContextInitializer.getFile("user", id).getAbsolutePath();

			} catch (Exception e) {
				return "You failed to upload " + id + " => " + e.getMessage();
			}
		}

		if (!email.isEmpty()) {
			u.setEmail(email);
			model.addAttribute("email", Encode.forHtmlContent(email));
		}

		if (!pass.isEmpty()) {
			// entityManager.getTransaction().begin();
			// u.setHashedAndSalted(bcryptEncoder.encode(pass));
			// entityManager.getTransaction().commit();
			u.cambiarUserPass(pass);
			model.addAttribute("HashedAndSalted", bcryptEncoder.encode(pass));
		}

		session.setAttribute("user", u);
		model.addAttribute("user", u);
		entityManager.merge(u);
		// entityManager.persist(u);

		return returnn;
	}

	/**
	 * Crear un usuario
	 */
	@RequestMapping(value = "/usuario/crear", params = { "login", "pass", "nombre", "apellido", "email", "passConf",
			"pregunta", "respuesta" }, method = RequestMethod.POST)
	@Transactional
	public String crearUsuario(@RequestParam("login") String login, @RequestParam("passConf") String passConf,
			@RequestParam("pass") String pass, @RequestParam("nombre") String nombre,
			@RequestParam("apellido") String apellido, @RequestParam("email") String email,
			@RequestParam("pregunta") String pregunta, Model model, @RequestParam("respuesta") String respuesta,
			HttpServletRequest request, HttpServletResponse response, HttpSession session, Locale locale) {
		String returnn = "redirect:/";

		model.addAttribute("pageTitle", "Registro");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		try {
			User u1 = (User) entityManager.createNamedQuery("userByLogin")
					.setParameter("loginParam", Encode.forHtmlContent(login)).getSingleResult();
			model.addAttribute("error", "Ese nombre de usuario ya existe");
			returnn = "registro";
		} catch (NoResultException e) {
			if (!pass.equals(passConf)) {
				logger.info("Contraseñas fallidas: {}, {}", pass, passConf);
				model.addAttribute("error", "Las contraseñas no coinciden, verifique todos los datos.");
				returnn = "registro";
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			if (login == null || login.length() < 4 || pass == null || pass.length() < 4 || nombre == null
					|| apellido == null || email == null) {
				model.addAttribute("error",
						"Verifique todos los campos y recuerde que el usuario y la contraseña deben tener al menos 4 caracteres.");
				returnn = "registro";
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				User user = User.createUser(login, pass, "user", nombre, apellido, email, pregunta, respuesta);
				entityManager.persist(user);

				logger.info("User logged {}", user.getLogin());
				session.setAttribute("user", user);
				// sets the anti-csrf token
				getTokenForSession(session);

			}
		}
		return returnn;
	}

	/**
	 * Intercepts login requests generated by the header; then continues to load
	 * normal page
	 */

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@Transactional
	public String login(@RequestParam("login") String formLogin, @RequestParam("pass") String formPass,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes model, HttpSession session,
			Locale locale) {

		if (formLogin == null || formLogin.length() < 4 || formPass == null || formPass.length() < 4) {
			model.addAttribute("loginError", "usuarios y contraseñas: 4 caracteres mínimo");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			User u = null;
			try {
				u = (User) entityManager.createNamedQuery("userByLogin")
						.setParameter("loginParam", Encode.forHtmlContent(formLogin)).getSingleResult();
				if (u.isPassValid(formPass)) {
					logger.info("pass was valid");
					Actividad atv = Actividad.createActividad("Se ha conectado!", u, new Date());

					u.getActividad().add(atv);
					session.setAttribute("user", u);
					// sets the anti-csrf token
					getTokenForSession(session);
				} else {
					logger.info("pass was NOT valid");
					session.setAttribute("loginError", "error en usuario o contraseña");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			} catch (NoResultException nre) {
				logger.info("no such login: {}", formLogin);
				session.setAttribute("loginError", "error en usuario o contraseña");
			}
		}
		return "redirect:home";
	}

	@RequestMapping(value = "/adminlogin", method = RequestMethod.POST)
	@Transactional
	public String adminlogin(@RequestParam("login") String formLogin, @RequestParam("pass") String formPass,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes model, HttpSession session,
			Locale locale) {
		login(formLogin, formPass, request, response, model, session, locale);
		return "redirect:admin";
	}

	@RequestMapping(value = "/perfil", method = RequestMethod.GET)
	@Transactional
	public String perfil(Locale locale, Model model, HttpSession session) {
		model.addAttribute("pageTitle", "Perfil");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		String returnn = "perfil";
		if (!ping(session)) {
			returnn = "redirect:home";
		} else {
			User u = (User) session.getAttribute("user");
			model.addAttribute("amigos",
					entityManager.createNamedQuery("allAmigos").setParameter("userParam", u).getResultList());
			model.addAttribute("comentariosPerfil", entityManager.createNamedQuery("allComentarioPerfilByUser")
					.setParameter("userParam", u).getResultList());

			model.addAttribute("actividad", u.getActividad());
		}

		return returnn;
	}

	@ResponseBody
	@RequestMapping(value = "/user/{id}/photo", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] userPhoto(@PathVariable("id") long id) throws IOException {
		try {
			String st = Long.toString(id);
			File f = localData.getFile("user", st);
			InputStream in = null;
			if (f.exists()) {
				in = new BufferedInputStream(new FileInputStream(f));
			} else {
				in = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream("unknown-user.jpg"));
			}

			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			logger.warn("Error cargando " + id, e);
			throw e;
		}
	}

	@RequestMapping(value = "/ajustes", method = RequestMethod.GET)
	@Transactional
	public String ajustes(Locale locale, Model model, HttpSession session) {
		model.addAttribute("pageTitle", "Ajustes");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		String returnn = "ajustes";
		if (!ping(session)) {
			returnn = "redirect:home";
		} else {
			User u = (User) session.getAttribute("user");
			model.addAttribute("email", Encode.forHtmlContent(u.getEmail()));
		}

		return returnn;
	}

	@RequestMapping(value = { "/user/{id}/add", "/perfil/{id}/add" }, method = RequestMethod.GET)
	@Transactional
	public String adduserasfriend(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale,
			HttpSession session) {
		User us = entityManager.find(User.class, id);
		if (!ping(session)) {
		} else {
			User u = (User) session.getAttribute("user");
			u = (User) entityManager.find(User.class, u.getId());

			if (u != null) {
				Actividad atv = Actividad.createActividad(
						"Ha agregado como amigo a " + us.getName() + " " + us.getLname(), u, new Date());
				u.getActividad().add(atv);

				if (u.getAmigos().contains(us)) {
				} else {
					Amigos ami = Amigos.createAmistad(u, us);
					u.getAmigos().add(ami);
				}

				session.setAttribute("user", u);
			}
		}
		return "redirect:/user/" + us.getId();
	}

	@RequestMapping(value = { "/user/{id}/addComment", "/perfil/{id}/addComment" }, method = RequestMethod.POST)

	@Transactional

	public String userPerfilAddComment(@RequestParam("comment") String comentario, @PathVariable("id") long id,
			HttpServletResponse response, Model model, Locale locale,

			HttpSession session) {

		User us = entityManager.find(User.class, id);

		if (!ping(session)) {

		} else {

			User u = (User) session.getAttribute("user");

			u = (User) entityManager.find(User.class, u.getId());

			if (u != null) {

				Actividad atv = Actividad.createActividad(

						"Ha comentado el perfil de " + Encode.forHtmlContent(us.getName()) + " "
								+ Encode.forHtmlContent(us.getLname()),
						u, new Date());

				u.getActividad().add(atv);

				ComentarioPerfil comp = ComentarioPerfil.createComment(comentario, u, us, new Date());

				u.getComentariosPerfil().add(comp);

				session.setAttribute("user", u);

			}

		}

		return "redirect:/user/" + us.getId();

	}

	@RequestMapping(value = { "/user/{id}", "/perfil/{id}" }, method = RequestMethod.GET)
	@Transactional
	public String userPerfil(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale,
			HttpSession session) {
		String returnn = "userperfil";
		model.addAttribute("pageTitle", "Perfil");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
		model.addAttribute("prefix", "./../");
		User us = entityManager.find(User.class, id);
		if (us == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such user: {}", id);
			returnn = "redirect:/";
		} else {
			model.addAttribute("userp", us);
			model.addAttribute("amigos", us.getAmigos());

			if (!ping(session)) {
				returnn = "redirect:/";
			} else {
				User u = (User) session.getAttribute("user");
				u = (User) entityManager.find(User.class, u.getId());

				if (u != null) {
					Actividad atv = Actividad.createActividad("Ha visitado el perfil de "
							+ Encode.forHtmlContent(us.getName()) + " " + Encode.forHtmlContent(us.getLname()), u,
							new Date());
					u.getActividad().add(atv);
					session.setAttribute("user", u);

					if (Amigos.comprobarAmigo(u.getAmigos(), us)) {
						// Son amigos
						// logger.info("Son amigos:" + u.getEmail() + " de " +
						// us.getEmail());
						model.addAttribute("amistad", false);
					} else {
						// logger.info("No son amigos:" + u.getEmail() + " de "
						// + us.getEmail());
						// logger.info(u.getAmigos().toString());
						model.addAttribute("amistad", true);
					}
				} else {
					logger.error("Usuario no encontrado, verifique si esta loggeado.");
				}
				model.addAttribute("user", u);
			}

		}
		return returnn;
	}

	/**
	 * Logout (also returns to home view).
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@Transactional
	public String logout(HttpSession session) {
		User u = (User) session.getAttribute("user");
		if (u != null) {
			Actividad natv = Actividad.createActividad("Se ha desconectado", u, new Date());
			u.getActividad().add(natv);

			logger.info("User '{}' logged out", u.getEmail());
			session.invalidate();
		}
		return "redirect:home";
	}

	/**
	 * Uploads a photo for a user
	 * 
	 * @param id
	 *            of user
	 * @param photo
	 *            to upload
	 * @return
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody String handleFileUpload(@RequestParam("photo") MultipartFile photo,
			@RequestParam("id") String id) {
		if (!photo.isEmpty()) {
			try {
				byte[] bytes = photo.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(localData.getFile("user", id)));
				stream.write(bytes);
				stream.close();
				return "You successfully uploaded " + id + " into "
						+ localData.getFile("user", id).getAbsolutePath() + "!";
			} catch (Exception e) {
				return "You failed to upload " + id + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload a photo for " + id + " because the file was empty.";
		}
	}

	/**
	 * Olvidar contraseña.
	 */

	@RequestMapping(value = { "/olvidopass", "/mail/nuevo/", "/forgot", "/olvide" }, method = RequestMethod.GET)
	@Transactional
	public String olvidoPassWebb(Locale locale, Model model, HttpSession session) {

		model.addAttribute("pageTitle", "Recuperar contraseña");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		return "user/olvidopass";
	}

	@RequestMapping(value = "/recuperarpass", method = RequestMethod.POST)
	@Transactional
	public String regenerarpass(@RequestParam("email") String email, @RequestParam("alias") String alias,
			@RequestParam("respuesta") String respuesta, Locale locale, Model model, HttpSession session) {
		String returnn = "user/enviarpass";
		model.addAttribute("pageTitle", "Recuperar contraseña");
		try {	
			User user = (User) getSingleResultOrNull(entityManager.createNamedQuery("userByEmail")
					.setParameter("emailParam", Encode.forHtmlContent(email)));
			
				if(user == null){
					model.addAttribute("error", "Alguno de los datos ingresados no coincide.");
					returnn = "user/olvidopass";
				} else {
					if (user.getLogin().equals(Encode.forHtmlContent(alias))
							&& user.getRespuestaDeSeguridad().equals(Encode.forHtmlContent(respuesta))) {
						logger.debug("Nueva contraseña asignada.");
						String random = Encode.forHtmlContent(generarStringPass());
						model.addAttribute("newPass", random);
						user.cambiarUserPass(random);
					} else {
						model.addAttribute("error", "Alguno de los datos ingresados no coincide.");
						logger.info(user.getLogin() + "!=" + Encode.forHtmlContent(alias) + "  + " + user.getRespuestaDeSeguridad() + "!= " + Encode.forHtmlContent(respuesta));
					}
				}
		} catch (NullPointerException e) {
			logger.debug("Algun error:", e);
			returnn = "redirect:/noregistro/";
		}
		return returnn;
	}
	
	public static Object getSingleResultOrNull(Query query){
		@SuppressWarnings("rawtypes")
		List results = query.getResultList();
        if (results.isEmpty()) 
        	return null;
        else 
        	return results.get(0);
	}

	private String generarStringPass() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	/*
	 * Returns true if the user is logged in
	 */
	static boolean ping(HttpSession session) {
		boolean returnn = false;
		User u = (User) session.getAttribute("user");
		if (u != null) {
			returnn = true;
		}
		return returnn;
	}

	/**
	 * Returns true if the user is logged in and is an admin
	 */
	static boolean isAdmin(HttpSession session) {
		boolean returnn = false;
		User u = (User) session.getAttribute("user");
		if (u != null) {
			returnn = u.getRole().equals("admin");
		}
		return returnn;
	}

	/**
	 * Checks the anti-csrf token for a session against a value
	 * 
	 * @param session
	 * @param token
	 * @return the token
	 */
	static boolean isTokenValid(HttpSession session, String token) {
		Object t = session.getAttribute("csrf_token");
		return (t != null) && t.equals(token);
	}

	/**
	 * Returns an anti-csrf token for a session, and stores it in the session
	 * 
	 * @param session
	 * @return
	 */
	static String getTokenForSession(HttpSession session) {
		String token = UUID.randomUUID().toString();
		session.setAttribute("csrf_token", token);
		return token;
	}
}
