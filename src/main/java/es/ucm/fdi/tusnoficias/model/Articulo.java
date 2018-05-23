package es.ucm.fdi.tusnoficias.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({ @NamedQuery(name = "allArticulos", query = "select u from Articulo u"),
		@NamedQuery(name = "allArticulosOrderByDate", query = "select u from Articulo u order by fecha desc"),
		@NamedQuery(name = "allArticulosOrderByRanking", query = "select u from Articulo u order by ranking"),
		@NamedQuery(name = "allArticulosOrderByTipo", query = "select u from Articulo u order by tipo"),
		@NamedQuery(name = "allArticulosByAutor", query = "select u from Articulo u where u.autor = :autorParam order by fecha desc") })
public class Articulo {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(columnDefinition="CLOB")
	private String titulo;
	
	@Column(columnDefinition="CLOB")
	private String contenido;
	
	@ManyToOne(targetEntity = User.class)
	private User autor;

	@ManyToMany(targetEntity = Tag.class, fetch = FetchType.EAGER, mappedBy = "articulos")
	private Set<Tag> tags;

	@OneToMany(mappedBy = "articulo")
	private List<Puntuacion> puntuaciones;
	@OneToMany(targetEntity = Comentario.class, fetch = FetchType.EAGER, mappedBy = "articulo")
	private Set<Comentario> comentario;
	private Date fecha;
	private int ranking;
	private ArticuloTipo tipo;
	private String image = "http://lorempixel.com/200/300/";

	public static Articulo crearArticuloAdministrativo(User autor, String contenido, String titulo,
			Set<Tag> tags) {
		Articulo art = new Articulo();
		art.autor = autor;
		art.comentario = new HashSet<>();
		art.contenido = contenido;
		art.fecha = new Date();
		art.puntuaciones = new ArrayList<>();
		art.ranking = 0;
		art.tags = tags;
		art.tipo = ArticuloTipo.ADMIN;
		art.titulo = titulo;

		return art;
	}
	

	public static Articulo crearArticuloNormal(User autor, String contenido, String titulo, Set<Tag> tags) {
		Articulo art = new Articulo();
		art.autor = autor;
		art.comentario = new HashSet<>();
		art.contenido = contenido;
		art.fecha = new Date();
		art.puntuaciones = new ArrayList<>();
		art.ranking = 0;
		art.tags = tags;
		art.tipo = ArticuloTipo.USUARIO;
		art.titulo = titulo;

		return art;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}



	public User getAutor() {
		return autor;
	}

	public void setAutor(User autor) {
		this.autor = autor;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public Set<Comentario> getComentario() {
		return comentario;
	}

	public void setComentario(Set<Comentario> comentario) {
		this.comentario = comentario;
	}

	public List<String> ponerComentariosEnArticulo() {
		List<String> list = new ArrayList<String>();
		Comentario coment;
		String texto;
		Iterator<Comentario> iterator = comentario.iterator();
		while (iterator.hasNext()) {
			coment = iterator.next();
			texto = coment.getComment();
			StringBuilder builder = new StringBuilder(texto);
			builder.substring(3, texto.indexOf("..." + '"'));
			list.add(builder.toString());
		}
		return null;
	}

	public void setTipo(ArticuloTipo tipo) {
		this.tipo = tipo;
	}

	public void setTipoAdmin() {
		this.tipo = ArticuloTipo.ADMIN;
	}

	public ArticuloTipo getTipo() {
		return tipo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getPuntuacion() {
		Integer puntuacion = 0;
		for (Puntuacion p : this.puntuaciones) {
			puntuacion += p.getPuntuacion();
		}
		return puntuacion;
	}

	public List<Puntuacion> getPuntuaciones() {
		return puntuaciones;
	}

	public void setPuntuaciones(List<Puntuacion> puntuaciones) {
		this.puntuaciones = puntuaciones;
	}
}
