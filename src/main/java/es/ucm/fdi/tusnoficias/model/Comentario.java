package es.ucm.fdi.tusnoficias.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "allComentarios", query = "select u from Comentario u"),
		@NamedQuery(name = "allComentariosByArticulo", query = "select u from Comentario u where u.articulo = :articuloParam") })
public class Comentario {
	@Id
	@GeneratedValue
	private long id;
	private String comment;

	@ManyToOne()
	private User owner;

	@ManyToOne(targetEntity = Articulo.class)
	private Articulo articulo;

	@OneToOne(targetEntity = Comentario.class)
	private Comentario responde;

	@OneToMany(targetEntity = Comentario.class)
	private List<Comentario> respuestas;
	private Date fecha;

	@OneToMany(mappedBy = "comentario")
	private List<PuntuacionComentario> puntuaciones;

	public Comentario(User owner, Articulo art) {
		this.puntuaciones = new ArrayList<>();
		this.respuestas = new ArrayList<>();
		this.owner = owner;
		this.fecha = new Date();
		this.articulo = art;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public List<Comentario> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<Comentario> respuestas) {
		this.respuestas = respuestas;
	}

	public void anadirRespuesta(Comentario respuesta) {
		this.respuestas.add(respuesta);
	}

	public Comentario getResponde() {
		return responde;
	}

	public void setResponde(Comentario responde) {
		this.responde = responde;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<PuntuacionComentario> getPuntuaciones() {
		return puntuaciones;
	}

	public void setPuntuaciones(List<PuntuacionComentario> puntuaciones) {
		this.puntuaciones = puntuaciones;
	}
}
