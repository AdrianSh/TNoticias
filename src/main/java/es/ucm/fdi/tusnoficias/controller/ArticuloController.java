package es.ucm.fdi.tusnoficias.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.tusnoficias.UserDetails;
import es.ucm.fdi.tusnoficias.model.*;

import java.util.Collections;
import java.util.Comparator;

@Controller
public class ArticuloController {
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
	
	@RequestMapping(value = "/articulo/nuevo/publicar", method = RequestMethod.POST)
	@Transactional
	public String adminRipPublicar(@RequestParam("articulo") String articulo, @RequestParam("tags") String tags,
			@RequestParam("titulo") String titulo, Locale locale, Model model) {
		String returnn = "articulos/nuevo";

		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		if (UserController.ping()) {
			User u = (User) UserController.getInstance().getPrincipal().getUser();
			u = (User) entityManager.find(User.class, u.getId());
			logger.info("Article ripp public by {}", u.getLogin());

			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());

			List<String> contenido = new ArrayList<String>();

			String[] arrayS = Encode.forHtmlContent(articulo).split("\\r?\\n");
			String[] arrayTags = Encode.forHtmlContent(tags).split(",");

			for (String s : arrayS)
				contenido.add(s);

			if (contenido.isEmpty())
				contenido.add(Encode.forHtmlContent(articulo));

			Set<Tag> nTags = new HashSet<>();
			nTags.add(Tag.newTag("administrativo"));

			for (String tg : arrayTags) {
				@SuppressWarnings("unchecked")
				List<Tag> ta = entityManager.createNamedQuery("allByTag").setParameter("tagParam", tg).getResultList();
				if (!ta.isEmpty()) {
					for (Tag taa : ta)
						nTags.add(taa);
				} else {
					Tag tagN = Tag.newTag(tg);
					nTags.add(tagN);
				}
			}

			Articulo article = Articulo.crearArticuloNormal(u, contenido, Encode.forHtmlContent(titulo), nTags);
			
			for (Tag tagf : nTags) {
				tagf.getArticulo().add(article);
				entityManager.persist(tagf);
			}
			
			Actividad atv = Actividad.createActividad(
					"Ha publicado un articulo normal titulado:" + '"' + Encode.forHtmlContent(titulo) + '"', u,
					new Date());
			u.getActividad().add(atv);
			
			
			entityManager.persist(atv);
			entityManager.persist(u);
			entityManager.persist(article);
			entityManager.flush();
			
			returnn = "redirect:/articulo/" + article.getId();
		} else {
			returnn = "redirect:/noregistro/";
		}
		return returnn;
	}

	@RequestMapping(value = { "/articulo/{id}", "/articulo/mostrar/{id}" }, method = RequestMethod.GET)
	public String articulo(@PathVariable("id") long id, HttpServletResponse response, Model model, Locale locale) {
		ponderRanking();
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		Articulo art = entityManager.find(Articulo.class, id);
		if (art == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such articulo: {}", id);
		} else {
			model.addAttribute("articulo", art);

			model.addAttribute("articuloComentarios", entityManager.createNamedQuery("allComentariosByArticulo")
					.setParameter("articuloParam", art).setMaxResults(100).getResultList());
		}
		if (UserController.ping()) {
			User u = UserController.getInstance().getPrincipal().getUser();
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("user", u);
			for (int pid : art.getPuntuacionesId()) {
				if (u.getPuntuacionesId().contains(pid)) {
					Puntuacion pp = (Puntuacion) entityManager.find(Puntuacion.class, pid);

					if (pp.getPositivos() > 0) {
						model.addAttribute("puntuacionP", true);
					}
					if (pp.getNegativos() > 0) {
						model.addAttribute("puntuacionN", true);
					}
					break;
				}
			}
		}
		return "articulos/articulo";
	}

	@RequestMapping(value = { "/articulo/tag/{tag}", "/articulos/tag/{tag}" }, method = RequestMethod.GET)
	public String articulosbytag(@PathVariable("tag") String tagName, HttpServletResponse response, Model model,
			Locale locale) {
		model.addAttribute("prefix", "./");
		model.addAttribute("pageTitle", "Articulo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		Tag tag = entityManager.find(Tag.class, Encode.forHtmlContent(tagName));
		if (tag == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			logger.error("No such tag: {}", tagName);
		} else {
			model.addAttribute("articulos", tag.getArticulo());
		}

		if (UserController.ping())
			model.addAttribute("user", true);

		return "articulos/bytag";
	}

	@RequestMapping(value = { "/articulo/nuevo", "/articulo/new" }, method = RequestMethod.GET)
	@Transactional
	public String nuevoArticulo(HttpServletResponse response, Model model, Locale locale) {
		String returnn = "articulos/nuevo";
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo nuevo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User u = uds.getUser();
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());
		} else {
			model.addAttribute("pageTitle", "Articulo nuevo");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../");
			model.addAttribute("mMensaje", "Debes estar registrado para poder publicar un articulo.");
			returnn = "noregistro";
		}

		return returnn;
	}

	@RequestMapping(value = { "/articulo/nuevo/articulos", "/articulo/new" }, method = RequestMethod.POST)
	@Transactional
	public String nuevoArticuloLoadArticulos(@RequestParam("tag") String tag, HttpServletResponse response, Model model,
			Locale locale) {
		String returnn = "articulos/nuevo";
		model.addAttribute("prefix", "../../");
		model.addAttribute("pageTitle", "Articulo nuevo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User u = uds.getUser();
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			@SuppressWarnings("unchecked")
			List<Tag> tags = entityManager.createNamedQuery("allByTag").setParameter("tagParam", tag).getResultList();
			if (!tags.isEmpty()) {
				for (Tag tg : tags) {
					List<Articulo> articulos = tg.getArticulo();
					// Falta implementar que los articulos que le son pasados al
					// usuario sean ordenados por fecha
					if (articulos.size() > 10)
						for (int i = 10; i < articulos.size(); i++)
							articulos.remove(i);

					model.addAttribute("articulos", articulos);
				}
			} else {
				returnn = "redirect:/articulo/nuevo";
			}

		} else
			returnn = "redirect:/";

		return returnn;
	}

	@RequestMapping(value = { "/articulos", "/mis/articulos" }, method = RequestMethod.GET)
	@Transactional
	public String misArticulos(HttpServletResponse response, Model model, Locale locale) {
		String returnn = "articulos/articulos";
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulo nuevo");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User u = uds.getUser();
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			model.addAttribute("lastarticulos", entityManager.createNamedQuery("allArticulosByAutor")
					.setParameter("autorParam", u).setMaxResults(10000).getResultList());
			model.addAttribute("user", u);
		} else {
			model.addAttribute("pageTitle", "Mis articulos");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

			model.addAttribute("prefix", "../");

			model.addAttribute("mMensaje", "Debes estar registrado para poder ver tus articulos.");

			returnn = "noregistro";
		}
		return returnn;
	}

	@RequestMapping(value = { "/articulos/ranking" }, method = RequestMethod.GET)
	@Transactional
	public String rankingArt(HttpServletResponse response, Model model, Locale locale) {
		String returnn = "articulos/articulos";
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Ranking");
		ponderRanking();
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User u = uds.getUser();
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			model.addAttribute("lastarticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10000).getResultList());
			model.addAttribute("user", u);
		} else {
			model.addAttribute("pageTitle", "Articulo nuevo");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../");

			model.addAttribute("mMensaje", "Debes estar registrado para poder ver el ranking.");

			returnn = "noregistro";
		}
		return returnn;
	}

	@RequestMapping(value = { "/articulos/favoritos", "/mis/articulosfavoritos" }, method = RequestMethod.GET)
	@Transactional
	public String misArticulosFav(HttpServletResponse response, Model model, Locale locale) {
		String returnn = "articulos/articulos";
		model.addAttribute("prefix", "../");
		model.addAttribute("pageTitle", "Articulos favoritos");
		model.addAttribute("categorias",
				entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
		ponderRanking();
		model.addAttribute("rightArticulos",
				entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User u = uds.getUser();
			u = (User) entityManager.find(User.class, u.getId());
			model.addAttribute("tags", entityManager.createNamedQuery("allTags").getResultList());

			model.addAttribute("lastarticulos", u.getFavoritos());
			model.addAttribute("user", u);
		} else {
			model.addAttribute("pageTitle", "Articulos favoritos");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../");

			model.addAttribute("mMensaje", "Debes estar registrado para poder ver tus articulos favoritos.");

			returnn = "noregistro";
		}
		return returnn;
	}

	/**
	 * Borra un articulo
	 */

	@RequestMapping(value = "/articulo/borrar/{id}", method = RequestMethod.GET)
	@Transactional
	public String borrarArticulo(@PathVariable("id") long id, HttpServletResponse response, Model model,
			Locale locale) {
		String returnn;
		
		UserDetails uds = UserController.getInstance().getPrincipal();
		
		if (uds != null) {
			try {
				User u = uds.getUser();

				Articulo art = entityManager.find(Articulo.class, id);
				if (art.getAutor() == u) {
					for (Comentario com : art.getComentario())
						entityManager.remove(com);

					for (Tag tag : art.getTags())
						entityManager.remove(tag);

					for (Integer p : art.getPuntuacionesId()) {
						Puntuacion pun = entityManager.find(Puntuacion.class, (long) (int) p);
						entityManager.remove(pun);
					}

					entityManager.remove(art);
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					returnn = "redirect:/home";
				}

				returnn = "redirect:/mis/articulos";
			} catch (NoResultException nre) {
				logger.error("No existe tal articulo: {}", id, nre);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				returnn = "redirect:/articulos";
			}
		} else {
			model.addAttribute("pageTitle", "Articulo nuevo");
			model.addAttribute("categorias",
					entityManager.createNamedQuery("allTagsOrderByDate").setMaxResults(10000).getResultList());
			model.addAttribute("rightArticulos",
					entityManager.createNamedQuery("allArticulosOrderByRanking").setMaxResults(10).getResultList());
			model.addAttribute("prefix", "../../");
			model.addAttribute("mMensaje",
					"Debes estar registrado y ser el dueño de dicho articulo, para poder borrarlo.");

			returnn = "noregistro";
		}
		return returnn;
	}

	/**
	 * Añadir articulo a favoritos
	 */
	@RequestMapping(value = "/articulo/favorito", method = RequestMethod.POST)
	@Transactional
	public String anadirArticuloFavorito(@RequestParam("idArticulo") long idArticulo, Model model) {

		Articulo art = entityManager.find(Articulo.class, idArticulo);
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();
			user.getFavoritos().add(art);
			
			entityManager.persist(user);
			logger.info("Articulo " + art.getId() + " añadido a favoritos de " + user.getLogin());
		}
		return "redirect:/articulo/" + art.getId();
	}

	@RequestMapping(value = "/articulo/{id}/favorito", method = RequestMethod.GET)
	@Transactional
	public String anadirArticuloFavoritoById(@PathVariable("id") long idArticulo, Model model) {

		Articulo art = entityManager.find(Articulo.class, idArticulo);
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();

			if (user.getFavoritos().contains(art))
				user.getFavoritos().remove(art);
			else
				user.getFavoritos().add(art);

			entityManager.persist(user);
			logger.info("Articulo " + art.getId() + " añadido/eliminado a favoritos de " + user.getLogin());
		}
		return "redirect:/articulo/" + art.getId();
	}

	/**
	 * Puntuar articulo positivo
	 */

	@RequestMapping(value = "/articulo/puntuarP", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarArticuloPositivo(@RequestParam("id") long id) {
		Articulo art = entityManager.find(Articulo.class, id);
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();

			boolean punteado = false;
			int puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion positiva retirada");
			} else {
				Puntuacion p = new Puntuacion(1, 0);
				p.setUsuario(user.getId());
				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado positivo");
			}
			entityManager.persist(art);
			entityManager.persist(user);
		}
		return "redirect:/articulo/" + art.getId();
	}

	@RequestMapping(value = "/articulo/{id}/puntuarP", method = RequestMethod.GET)
	@Transactional
	public String puntuarArticuloPositivob(@PathVariable("id") long id) {
		Articulo art = entityManager.find(Articulo.class, id);
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();
			Boolean punteado = false;
			Integer puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion positiva retirada");
			} else {
				Puntuacion p = new Puntuacion(1, 0);
				p.setUsuario(user.getId());

				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado positivo");
			}
			entityManager.persist(art);
			entityManager.persist(user);
		}
		return "redirect:/articulo/" + art.getId();
	}

	/**
	 * Puntuar articulo negativo
	 */

	@RequestMapping(value = "/articulo/puntuarN", params = { "id" }, method = RequestMethod.POST)
	@Transactional
	public String puntuarArticuloNegativo(@RequestParam("id") long id) {
		Articulo art = entityManager.find(Articulo.class, id);
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();
			boolean punteado = false;
			int puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion negativa retirada");
			} else {
				Puntuacion p = new Puntuacion(0, 1);
				p.setUsuario(user.getId());
				
				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado negativo");
			}
			entityManager.persist(art);
			entityManager.persist(user);
			entityManager.flush();
		}
		return "redirect:/articulo/" + art.getId();
	}

	@RequestMapping(value = "/articulo/{id}/puntuarN", method = RequestMethod.GET)
	@Transactional
	public String puntuarArticuloNegativoB(@PathVariable("id") long id, HttpSession session) {
		Articulo art = entityManager.find(Articulo.class, id);
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();
			boolean punteado = false;
			int puntuacionId = 0;

			for (int pid : art.getPuntuacionesId()) {
				if (user.getPuntuacionesId().contains(pid)) {
					punteado = true;
					puntuacionId = pid;
					break;
				}
			}

			if (punteado) {
				user.getPuntuacionesId().remove(puntuacionId);
				art.getPuntuacionesId().remove(puntuacionId);
				logger.info("Articulo " + art.getId() + " puntuacion negativa retirada");
			} else {
				Puntuacion p = new Puntuacion(0, 1);
				p.setUsuario(user.getId());

				art.getPuntuacionesId().add((Integer) (int) p.getId());
				art.getAutor().getPuntuacionesId().add((Integer) (int) p.getId());
				entityManager.persist(p);
				logger.info("Articulo " + art.getId() + " puntuado negativo");
			}
			entityManager.persist(art);
			entityManager.persist(user);
		}
		return "redirect:/articulo/" + art.getId();
	}

	/**
	 * Añadir tag a un articulo
	 */
	@RequestMapping(value = "/articulo/anadirTag", params = { "idArticulo", "Tag" }, method = RequestMethod.POST)
	@Transactional
	public String anadirTagArticulo(@RequestParam("idArticulo") long id, @RequestParam("Tag") String tag, Model model) {
		Tag t = entityManager.find(Tag.class, tag);
		Articulo art = entityManager.find(Articulo.class, id);

		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();
			if (art.getAutor().equals(user)) {
				if (t == null) {
					t = Tag.newTag(Encode.forHtmlContent(tag));
				}

				t.getArticulo().add(art);
				art.getTags().add(t);

				entityManager.persist(t);
				entityManager.persist(art);
				logger.info("Tag " + t.getNombre() + " puesto a articulo " + art.getId());
			}
		}
		return "redirect:/mis/articulos";
	}

	/**
	 * Quitar tag de un articulo
	 */
	@RequestMapping(value = "/articulo/eliminarTag", params = { "idArticulo", "Tag" }, method = RequestMethod.POST)
	@Transactional
	public String eliminarTagArticulo(@RequestParam("idArticulo") long id, @RequestParam("Tag") String tag, Model model) {
		Tag t = entityManager.find(Tag.class, tag);
		Articulo art = entityManager.find(Articulo.class, id);
		UserDetails uds = UserController.getInstance().getPrincipal();
		if (uds != null) {
			User user = uds.getUser();
			if (art.getAutor() == user) {
				t.getArticulo().remove(art);
				art.getTags().remove(t);

				entityManager.persist(t);
				entityManager.persist(art);
				logger.info("Tag " + t.getNombre() + " eliminado de articulo " + art.getId());
			}
		}
		return "redirect:/mis/articulos";
	}

	int sumaPuntuaciones(Articulo art) {
		Iterator<Integer> itera = art.getPuntuacionesId().iterator();
		int total = 0;
		while (itera.hasNext()) {
			Integer control = itera.next();
			Number num =control;
			Long control2=num.longValue();
			Puntuacion pun =  entityManager.find(Puntuacion.class, control2);
			int suma = pun.getPositivos()-pun.getNegativos();
		 	total = total + suma;	
		}
		return total;
	}
	
	

	void ponderRanking() {
		try {
			List<Articulo> art = new ArrayList<Articulo>();
			art = entityManager.createNamedQuery("allArticulos").setMaxResults(10000).getResultList();
			Comparator<Articulo> comparador = Collections.reverseOrder();
			Comparator<Articulo> prueba2 = new Comparator<Articulo>() {
				public int compare(Articulo s1, Articulo s2) {
				   Integer x = sumaPuntuaciones(s1);
				   Integer y = sumaPuntuaciones(s2);
				   return y.compareTo(x);
			    }};
			    Collections.sort(art, prueba2);
			int ranking = 1;
			Iterator<Articulo> itera = art.iterator();
			while (itera.hasNext()){
				 Articulo arp =itera.next();
				 arp.setRanking(ranking);
				ranking=ranking+1;
				entityManager.persist(arp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


