package es.ucm.fdi.tusnoficias.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.owasp.encoder.Encode;
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

import es.ucm.fdi.tusnoticias.extra.ArticleRipper;
import es.ucm.fdi.tusnoficias.Messages;
import es.ucm.fdi.tusnoficias.model.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Environment env;

	@Autowired
	Messages messages;

	@ModelAttribute
	public void addAttributes(Model model, Locale locale, HttpServletRequest httpServletRequest) {
		model.addAttribute("s", "/static");
		model.addAttribute("siteUrl", env.getProperty("es.ucm.fdi.tusnoticias.site-url"));
		model.addAttribute("siteName", env.getProperty("es.ucm.fdi.tusnoticias.site-name"));
		model.addAttribute("shortSiteName", env.getProperty("es.ucm.fdi.tusnoticias.short-site-name"));
		model.addAttribute("pageTitle", httpServletRequest.getRequestURI());
		model.addAttribute("defaultPageTitle", "Admin.");

		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(new Date());

		model.addAttribute("serverTime", formattedDate);

		try {
			User u = UserController.getInstance().getPrincipal().getUser();
			model.addAttribute("user", u);
			logger.info("Administration loaded by {}", u.getLogin());
		} catch (Exception e) {
			logger.error("ERROR! Somebody without 'admin' role has requested HK loading!");
		}
	}

	@RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
	@Transactional
	public String admin(Locale locale, Model model) {
		String returnn = "admin";

		if (!UserController.isAdmin()) {
			returnn = "redirect:/admin/login";
		} else {
			User u = UserController.getInstance().getPrincipal().getUser();
			Actividad atv = Actividad.createActividad("Ha entrado a la administración", u, new Date());
			@SuppressWarnings("unchecked")
			List<Actividad> actvs = entityManager.createNamedQuery("allActividadByUser").setParameter("userParam", u)
					.getResultList();
			u.addActividad(actvs, atv);
			model.addAttribute("mensajes",
					entityManager.createNamedQuery("allMensajesByUser").setParameter("userParam", u).getResultList());
			model.addAttribute("actividades",
					entityManager.createNamedQuery("allActividad").setMaxResults(10).getResultList());
		}

		return returnn;
	}

	@RequestMapping(value = "/tables/addTag", method = RequestMethod.POST)
	@Transactional
	public String adminTablesAddTag(@RequestParam("tag") String tag, Locale locale, Model model) {
		String returnn = "redirect:/admin/tables";

		if (!UserController.isAdmin()) {
			returnn = "redirect:/admin/login";
		} else {
			Tag nTag = new Tag();
			nTag.setArticulos(new ArrayList<Articulo>());
			nTag.setNombre(tag);
			entityManager.persist(nTag);

			logger.info("Tag added {}", nTag.getNombre());
		}

		return returnn;
	}

	@RequestMapping(value = "/tables/addPeriodico", method = RequestMethod.POST)
	@Transactional
	public String adminTablesAddPeriodico(@RequestParam("periodico") String nombre, @RequestParam("url") String url,
			Locale locale, Model model) {
		String returnn = "redirect:/admin/tables";

		if (!UserController.isAdmin()) {
			returnn = "redirect:/admin/login";
		} else {
			Periodico periodico = new Periodico();
			periodico.setNombre(nombre);
			periodico.setUrl(url);

			entityManager.persist(periodico);

			logger.info("Periodico added {}", periodico.getNombre());
		}

		return returnn;
	}

	@RequestMapping(value = "/tables", method = RequestMethod.GET)
	public String adminTables(Locale locale, Model model) {
		String returnn = "admin/tables";

		if (!UserController.isAdmin()) {
			returnn = "redirect:/admin/login";
		} else {
			User u = UserController.getInstance().getPrincipal().getUser();
			model.addAttribute("mensajes",
					entityManager.createNamedQuery("allMensajesByUser").setParameter("userParam", u).getResultList());
			model.addAttribute("actividades",
					entityManager.createNamedQuery("allActividad").setMaxResults(10).getResultList());
			model.addAttribute("tabla_users",
					entityManager.createNamedQuery("allUsers").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_comentarios",
					entityManager.createNamedQuery("allComentarios").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_comentarios_perfil",
					entityManager.createNamedQuery("allComentarioPerfil").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());
			model.addAttribute("tabla_tags",
					entityManager.createNamedQuery("allTags").setMaxResults(10000).getResultList());
		}

		return returnn;
	}

	@RequestMapping(value = "/forms", method = RequestMethod.GET)
	public String adminForms(Locale locale, Model model) {
		String returnn = "admin/forms";

		if (!UserController.isAdmin()) {
			returnn = "redirect:/admin/login";
		} else {
			User u = UserController.getInstance().getPrincipal().getUser();
			logger.info("Ripper articles interface Opened by {}", u.getLogin());
			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());
		}

		return returnn;
	}

	@RequestMapping(value = "/forms/publicar", method = RequestMethod.POST)
	@Transactional
	public String adminRipPublicar(@RequestParam("articulo") String articulo, @RequestParam("tags") String tags,
			@RequestParam("titulo") String titulo, Locale locale, Model model) {
		String returnn = "admin/forms";

		articulo = Encode.forHtmlContent(articulo);
		tags = Encode.forHtmlContent(tags);

		if (!UserController.isAdmin()) {
			returnn = "redirect:/admin/login";
		} else {
			User u = UserController.getInstance().getPrincipal().getUser();
			logger.info("Article ripp public by {}", u.getLogin());

			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());

			String[] arrayTags = tags.split(",");

			Set<Tag> nTags = new HashSet<>();
			nTags.add(Tag.newTag("administrativo"));

			for (String tg : arrayTags) {
				Tag ta = entityManager.find(Tag.class, tg);
				if (ta != null)
					nTags.add(ta);
				else
					nTags.add(Tag.newTag(tg));
			}

			Articulo article = Articulo.crearArticuloAdministrativo(u, articulo, titulo, nTags);

			for (Tag tagf : nTags) {
				tagf.getArticulos().add(article);
				entityManager.persist(tagf);
			}
			entityManager.persist(article);

			Actividad atv = Actividad.createActividad(
					"Ha publicado un articulo administrativo titulado:" + '"' + titulo + '"', u, new Date());
			@SuppressWarnings("unchecked")
			List<Actividad> actvs = entityManager.createNamedQuery("allActividadByUser").setParameter("userParam", u)
					.getResultList();
			u.addActividad(actvs, atv);
			entityManager.persist(u);

			returnn = "redirect:/articulo/" + article.getId();
		}
		return returnn;
	}

	@RequestMapping(value = "/forms", method = RequestMethod.POST)
	@Transactional
	public String adminRip(@RequestParam("periodico") long periodicoId, @RequestParam("tipo") String tipo,
			@RequestParam("urlarticulo") String url, @RequestParam("identificador") String identificador, Locale locale,
			Model model) {
		String returnn = "admin/forms";

		if (!UserController.isAdmin()) {
			returnn = "redirect:/admin/login";
		} else {
			User u = UserController.getInstance().getPrincipal().getUser();
			logger.info("Article ripp load by {}", u.getLogin());
			model.addAttribute("periodicos",
					entityManager.createNamedQuery("allPeriodicos").setMaxResults(10000).getResultList());
			Periodico p = (Periodico) entityManager.find(Periodico.class, periodicoId);
			if (p != null) {
				ArticleRipper ar = new ArticleRipper(Encode.forHtml(url));
				Document doc = ar.getDocument();
				Elements els = null;
				Element el = null;

				if (tipo.equals("classs")) {
					els = doc.getElementsByClass(identificador);
					// logger.info("Elementos identificado por class,
					// indentificado con {}", identificador);
				} else if (tipo.equals("id")) {
					el = doc.getElementById(identificador);
					// logger.info("Elemento identificado por id, indentificado
					// con {}", identificador);
				} else if (tipo.equals("attribute")) {
					els = doc.getElementsByAttribute(identificador);
					// logger.info("Elementos identificado por atributo,
					// indentificado con {}", identificador);
				} else {
					logger.info("Tipo no existente {}, indentificado con {}", tipo, identificador);
				}

				if (els == null && el == null) {
					returnn = "redirect:/admin/forms?error=Articulo%20no%20encontrado";
				} else {
					if (els != null) {
						List<String> articles = new ArrayList<String>();
						for (int i = 0; i < els.size(); i++) {
							Element artc = els.get(i);
							String arts = Jsoup.parse(artc.toString()).text();
							articles.add(arts);
						}
						model.addAttribute("articulos", articles);

					}
					if (el != null)
						model.addAttribute("articulo", Jsoup.parse(el.toString()).text());
				}

			} else {
				returnn = "redirect:/admin/forms?error=Periodico%20No%20Valido";
			}
		}
		return returnn;
	}

	/* ######## PAGINAS NO USADAS PERO PARA FUTUROS FORMATOS ######### 
	@RequestMapping(value = "/panels-wells", method = RequestMethod.GET)
	public String adminPanelWells(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/panels-wells";
	}

	@RequestMapping(value = "/buttons", method = RequestMethod.GET)
	public String adminButtons(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/buttons";
	}

	@RequestMapping(value = "/notifications", method = RequestMethod.GET)
	public String adminNotifications(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/notifications";
	}

	@RequestMapping(value = "/typography", method = RequestMethod.GET)
	public String adminTypography(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/typography";
	}

	@RequestMapping(value = "/icons", method = RequestMethod.GET)
	public String adminIcons(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/icons";
	}

	@RequestMapping(value = "/grid", method = RequestMethod.GET)
	public String adminGrid(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/grid";
	}

	@RequestMapping(value = "/blank", method = RequestMethod.GET)
	public String adminBlank(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/blank";
	}

	@RequestMapping(value = "/loginSettings", method = RequestMethod.GET)
	public String adminLoginSettings(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String adminlogin(Locale locale, Model model) {
		return UserController.isAdmin() ? "redirect:/admin" : "redirect:/login";
	}

	@RequestMapping(value = "/flot", method = RequestMethod.GET)
	public String adminFlot(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/flot";
	}

	@RequestMapping(value = "/morris", method = RequestMethod.GET)
	public String adminMorris(Locale locale, Model model) {
		return !UserController.isAdmin() ? "redirect:/admin/login" : "admin/morris";
	}
	*/
}
