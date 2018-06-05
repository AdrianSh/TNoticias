package es.ucm.fdi.tusnoficias.controller;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/comentario")
public class ComentController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Añadir comentario
	 * 
	 * @param model
	 */
	@RequestMapping(value = "/anadir", params = { "comment", "id" }, method = RequestMethod.POST)
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
				Comentario com = new Comentario(u, art);
				com.setComment(Encode.forHtmlContent(comment));
				com.setResponde(null);

				Set<Comentario> listaC = art.getComentario();
				listaC.add(com);
				art.setComentario(listaC);
				com.setArticulo(art);
				
				// model.addAttribute("puntosCom", sumaPuntuaciones(com));
				entityManager.persist(art);
				entityManager.persist(com);
				logger.info("Comment " + com.getId() + " written in " + com.getArticulo().getTitulo() + " written by "
						+ com.getOwner().getLogin());

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

	@RequestMapping(value = "/puntuarP", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarComentarioPositivo(@RequestParam("id") long id, Model model) {
		UserDetails uds = UserController.getInstance().getPrincipal();
		Comentario com = entityManager.find(Comentario.class, id);
		if (uds != null) {
			User user = uds.getUser();
			PuntuacionComentario p;
			try {
				p = entityManager.createNamedQuery("puntuacionByUserAndComment", PuntuacionComentario.class).setParameter("userParam", user).setParameter("comentarioParam", com).getSingleResult();
			} catch (NoResultException e) {
				// Primera vez que puntua el articulo
				p = new PuntuacionComentario(1, user, com);
			}
			
			p.setPuntuacion(1);
			
			entityManager.persist(p);
			entityManager.persist(com);
			entityManager.persist(user);
			
			Long puntuacion;
			try {
				puntuacion = entityManager.createNamedQuery("totalPuntuacionComentario", Long.class).setParameter("comentarioParam", com).getSingleResult();
			} catch (NoResultException e) {
				puntuacion = 0L;
			}
			model.addAttribute("puntosCom", puntuacion == null ? 0: puntuacion);
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

	@RequestMapping(value = "/puntuarN", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarComentarioNegativo(@RequestParam("id") long id, Model model) {
		
		UserDetails uds = UserController.getInstance().getPrincipal();
		Comentario com = entityManager.find(Comentario.class, id);
		if (uds != null) {
			User user = uds.getUser();
			
			PuntuacionComentario p;
			try {
				p = entityManager.createNamedQuery("puntuacionByUserAndComment", PuntuacionComentario.class).setParameter("userParam", user).setParameter("comentarioParam", com).getSingleResult();
			} catch (NoResultException e) {
				// Primera vez que puntua el articulo
				p = new PuntuacionComentario(-1, user, com);
			}
			p.setPuntuacion(-1);
			
			entityManager.persist(p);
			entityManager.persist(com);
			entityManager.persist(user);
			
			Long puntuacion;
			try {
				puntuacion = entityManager.createNamedQuery("totalPuntuacionComentario", Long.class).setParameter("comentarioParam", com).getSingleResult();
			} catch (NoResultException e) {
				puntuacion = 0L;
			}
			model.addAttribute("puntosCom", puntuacion == null ? 0: puntuacion);
			logger.info("Comentario " + com.getId() + " puntuado negativo");
			return "redirect:/articulo/" + com.getArticulo().getId();
		} else {
			return "redirect:/noregistro/";
		}
	}

	/**
	 * Responder comentario
	 */

	@RequestMapping(value = "/responder", params = { "comment", "articulo",
			"comentario original" }, method = RequestMethod.POST)
	@Transactional
	public String responderComentario(@RequestParam("comment") String comment,
			@RequestParam("articulo") long articuloId, Model model,
			@RequestParam("comentario original") long comentarioOrgId) {
		UserDetails uds = UserController.getInstance().getPrincipal();

		if (comment.length() > 0 && uds != null) {
			User u = uds.getUser();
			Articulo art = entityManager.find(Articulo.class, articuloId);
			
			Comentario com = new Comentario(u, art);
			com.setComment(Encode.forHtmlContent(comment));
			
			Comentario org = entityManager.find(Comentario.class, comentarioOrgId);
			org.anadirRespuesta(com);
			com.setResponde(org);

			logger.info("Comment " + com.getComment() + " written in " + art.getTitulo() + " written by " + u.getId());
			
			entityManager.persist(com);
			entityManager.persist(org);
		}

		return "redirect:/articulo/" + articuloId;
	}

	/**
	 * Borra un comentario
	 */
	@RequestMapping(value = "/borrar/{id}", method = RequestMethod.DELETE)
	@Transactional
	@ResponseBody
	public String borrarComentario(@PathVariable("id") long id, HttpServletResponse response, Model model) {
		UserDetails uds = UserController.getInstance().getPrincipal();
		Comentario c = entityManager.find(Comentario.class, id);
		if(uds != null && (c.getOwner().equals(uds.getUser()) || uds.isAdmin())) {
			for (Comentario com : c.getRespuestas())
				entityManager.remove(com);

			for (PuntuacionComentario p : c.getPuntuaciones())
				entityManager.remove(p);

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
}
