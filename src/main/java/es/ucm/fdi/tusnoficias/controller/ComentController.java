package es.ucm.fdi.tusnoficias.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.tusnoficias.UserDetails;
import es.ucm.fdi.tusnoficias.model.*;

@Controller
public class ComentController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Añadir comentario
	 * 
	 * @param model
	 */
	@RequestMapping(value = "/comentario/anadir", params = { "comment", "id" }, method = RequestMethod.POST)
	@Transactional
	public String anadirComentario(@RequestParam("comment") String comment, @RequestParam("id") long idArt,
			Model model) {
		String returnn = "redirect:/noregistro/";
		if (comment != "") {
			Articulo art = entityManager.find(Articulo.class, idArt);
			UserDetails uds = UserController.getInstance().getPrincipal();
			if (art == null)
				returnn = "redirect:/home#error-Articulo-no-encontrado";
			else if (uds != null) {
				User u = uds.getUser();
				Comentario com = new Comentario();
				com.setPuntuacionesId(new ArrayList<Integer>());
				com.setRespuestas(new ArrayList<Comentario>());
				com.setUser(u);
				com.setComment(Encode.forHtmlContent(comment));
				com.setResponde(null);
				com.setFecha(new Date());

				Set<Comentario> listaC = art.getComentario();
				listaC.add(com);
				art.setComentario(listaC);
				com.setArticulo(art);
				// model.addAttribute("puntosCom", sumaPuntuaciones(com));
				entityManager.persist(art);
				entityManager.persist(com);
				logger.info("Comment " + com.getId() + " written in " + com.getArticulo().getTitulo() + " written by "
						+ com.getUser().getLogin());

				returnn = "redirect:/articulo/" + idArt;
			} else {
				returnn = "redirect:/home?loginRequired";
			}
		}
		return returnn;
	}

	/**
	 * Puntuar comentario positivo
	 * 
	 * @param model
	 */

	@RequestMapping(value = "/comentario/puntuarP", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarComentarioPositivo(@RequestParam("id") long id, Model model) {
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User u = uds.getUser();
			Comentario com = entityManager.find(Comentario.class, id);
			Puntuacion p = new Puntuacion(1, 0);
			p.setUsuario(u.getId());
			u.getPuntuacionesHechasId().add((Integer) (int) p.getId());
			com.getPuntuacionesId().add((Integer) (int) p.getId());
			com.getUser().getPuntuacionesId().add((Integer) (int) p.getId());

			entityManager.persist(p);
			entityManager.persist(u);
			entityManager.persist(com);
			model.addAttribute("puntosCom", sumaPuntuaciones(com));
			logger.info("Comentario " + com.getId() + " puntuado positivo");
			return "redirect:/articulo/" + com.getArticulo().getId();
		} else {
			return "redirect:/noregistro/";
		}

	}

	/**
	 * Puntuar comentario negativo
	 * 
	 * @param model
	 */

	@RequestMapping(value = "/comentario/puntuarN", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarComentarioNegativo(@RequestParam("id") long id, Model model) {
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User u = uds.getUser();
			u = entityManager.find(User.class, u.getId());
			Comentario com = entityManager.find(Comentario.class, id);
			Puntuacion p = new Puntuacion(0, 1);
			p.setUsuario(u.getId());
			u.getPuntuacionesHechasId().add((Integer) (int) p.getId());
			com.getPuntuacionesId().add((Integer) (int) p.getId());
			com.getUser().getPuntuacionesId().add((Integer) (int) p.getId());

			entityManager.persist(p);
			entityManager.persist(u);
			entityManager.persist(com);

			model.addAttribute("puntosCom", sumaPuntuaciones(com));
			logger.info("Articulo " + com.getId() + " puntuado negativo");

			return "redirect:articulo/" + com.getArticulo().getId();
		} else {
			return "redirect:/noregistro/";
		}
	}

	/**
	 * Responder comentario
	 */

	@RequestMapping(value = "/comentario/responder", params = { "comment", "articulo",
			"comentario original" }, method = RequestMethod.POST)
	@Transactional
	public String responderComentario(@RequestParam("comment") String comment,
			@RequestParam("articulo") long articuloId, Model model,
			@RequestParam("comentario original") long comentarioOrgId) {
		UserDetails uds = UserController.getInstance().getPrincipal();

		if (comment.length() > 0 && uds != null) {
			User u = uds.getUser();

			Comentario com = new Comentario();
			Comentario org = entityManager.find(Comentario.class, comentarioOrgId);

			com.setPuntuacionesId(new ArrayList<Integer>());
			com.setRespuestas(new ArrayList<Comentario>());
			com.setUser(u);
			com.setComment(Encode.forHtmlContent(comment));
			com.setResponde(org);
			com.setFecha(new Date());

			Articulo art = entityManager.find(Articulo.class, articuloId);

			org.anadirRespuesta(com);

			entityManager.persist(com);
			entityManager.persist(org);

			logger.info("Comment " + com.getComment() + " written in " + art.getTitulo() + " written by " + u.getId());
		}

		return "redirect:/articulo/" + articuloId;
	}

	/**
	 * Borra un comentario
	 */
	@RequestMapping(value = "/comentario/borrar/{id}", method = RequestMethod.DELETE)
	@Transactional
	@ResponseBody
	public String borrarComentario(@PathVariable("id") long id, HttpServletResponse response, Model model) {
		UserDetails uds = UserController.getInstance().getPrincipal();
		Comentario c = entityManager.find(Comentario.class, id);
		if(uds != null && (c.getUser().equals(uds.getUser()) || uds.isAdmin())) {
			for (Comentario com : c.getRespuestas()) {
				entityManager.remove(com);
			}

			for (Integer p : c.getPuntuacionesId()) {
				Puntuacion pun = entityManager.find(Puntuacion.class, (long) (int) p);
				entityManager.remove(pun);
			}

			if (c.getResponde() != null)
				c.getResponde().getRespuestas().remove(c);

			c.getArticulo().getComentario().remove(c);

			entityManager.remove(c);
			response.setStatus(HttpServletResponse.SC_OK);
			return "OK";
		} else {
			logger.error("No existe tal comentario: {}", id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "ERR";
		}
	}

	int sumaPuntuaciones(Comentario com) {

		Iterator<Integer> itera = com.getPuntuacionesId().iterator();
		int total = 0;
		logger.info("ID COMENZ  " + com.getId());
		while (itera.hasNext()) {
			Integer control = itera.next();
			Number num = control;
			Long control2 = num.longValue();
			logger.info("ESTE ES EL ID A MIRAR  " + control2);
			Puntuacion pun = entityManager.find(Puntuacion.class, control2);
			logger.info("PUNTUACION CORRESPONDIENTE ID  " + pun.getId());
			int suma = pun.getPositivos() - pun.getNegativos();

			total = total + suma;

		}
		logger.info("TOTAL   " + total);
		return total;
	}
}
