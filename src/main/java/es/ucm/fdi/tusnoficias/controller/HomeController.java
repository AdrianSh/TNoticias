package es.ucm.fdi.tusnoficias.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.tusnoficias.UserDetails;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Environment env;

	@ModelAttribute
	public void addAttributes(Model model, Locale locale) {
		model.addAttribute("s", "/static");
		model.addAttribute("siteUrl", env.getProperty("es.ucm.fdi.tusnoticias.site-url"));
		model.addAttribute("siteName", env.getProperty("es.ucm.fdi.tusnoticias.site-name"));
		model.addAttribute("shortSiteName", env.getProperty("es.ucm.fdi.tusnoticias.short-site-name"));

		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(new Date());

		model.addAttribute("serverTime", formattedDate);
	}

	@RequestMapping(value = { "/", "/home", "/index", "/login" }, method = RequestMethod.GET)
	public String homePage(Locale locale, Model model,
			@RequestParam(required = false) String error) {
		model.addAttribute("pageTitle", "Home");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("actividades", entityManager.createNamedQuery("allActividad").getResultList());
		model.addAttribute("lastarticulos",
				entityManager.createNamedQuery("allArticulosOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			model.addAttribute("user", uds.getUser());
		}

		if (error != null)
			model.addAttribute("loginError", "Usuario/Contraseña incorrectos.");

		return "articulos/articulos";
	}

	@RequestMapping(value = "/noregistro", method = RequestMethod.GET)
	public String noRegistro(Locale locale, Model model) {
		model.addAttribute("pageTitle", "No Registro");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		model.addAttribute("mMensaje", "Debes estar registrado.");
		return "noregistro";
	}

	@RequestMapping(value = "/inicio_sesion", method = RequestMethod.GET)
	public String login(Locale locale, Model model) {
		model.addAttribute("pageTitle", "Login");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		return "login";
	}

	/*
	 * @RequestMapping(value = "/actividad/{id}", method = RequestMethod.GET) public
	 * String actividad(@PathVariable("id") long id, HttpServletResponse response,
	 * Model model, Locale locale) { model.addAllAttributes(basic(locale));
	 * model.addAttribute("prefix", "../"); model.addAttribute("pageTitle",
	 * "Actividad"); model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Actividad act = entityManager.find(Actividad.class, id); if (act == null) {
	 * response.setStatus(HttpServletResponse.SC_NOT_FOUND); logger.error(
	 * "No such actividad: {}", id); } else { model.addAttribute("actividad", act);
	 * }
	 * 
	 * return "actividad"; }
	 * 
	 * @RequestMapping(value = "/comentario/{id}", method = RequestMethod.GET)
	 * public String comentario(@PathVariable("id") long id, HttpServletResponse
	 * response, Model model, Locale locale) {
	 * model.addAllAttributes(basic(locale)); model.addAttribute("prefix", "../");
	 * model.addAttribute("pageTitle", "Comentario");
	 * model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Comentario com = entityManager.find(Comentario.class, id); if (com == null) {
	 * response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	 * logger.error("No such comentario: {}", id); } else {
	 * model.addAttribute("comentario", com); } return "comentario"; }
	 * 
	 * @RequestMapping(value = "/puntuacion/{id}", method = RequestMethod.GET)
	 * public String puntuacion(@PathVariable("id") long id, HttpServletResponse
	 * response, Model model, Locale locale) {
	 * model.addAllAttributes(basic(locale)); model.addAttribute("prefix", "../");
	 * model.addAttribute("pageTitle", "Puntuacion");
	 * model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Puntuacion pun = entityManager.find(Puntuacion.class, id); if (pun == null) {
	 * response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	 * logger.error("No such puntuacion: {}", id); } else {
	 * model.addAttribute("puntuacion", pun); } return "puntuacion"; }
	 * 
	 * @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET) public
	 * String tag(@PathVariable("id") long id, HttpServletResponse response, Model
	 * model, Locale locale) { model.addAllAttributes(basic(locale));
	 * model.addAttribute("prefix", "../"); model.addAttribute("pageTitle", "Tag");
	 * model.addAttribute("categorias",
	 * entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000)
	 * .getResultList());
	 * 
	 * Tag tag = entityManager.find(Tag.class, id); if (tag == null) {
	 * response.setStatus(HttpServletResponse.SC_NOT_FOUND); logger.error(
	 * "No such tag: {}", id); } else { model.addAttribute("tag", tag); }
	 * 
	 * return "tag"; }
	 * 
	 */

	/**
	 * A not-very-dynamic view that shows an "about us".
	 */
	@RequestMapping(value = { "/about", "/sobre", "/nosotros" }, method = RequestMethod.GET)
	@Transactional
	public String about(Locale locale, Model model) {
		model.addAttribute("pageTitle", "Quienes somos");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			model.addAttribute("user", uds.getUser());
		}

		return "about";
	}
}
