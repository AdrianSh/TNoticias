package es.ucm.fdi.tusnoficias.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({ @NamedQuery(name = "puntuacionComentarioById", query = "select u from PuntuacionComentario u where u.id= :idParam"),
	@NamedQuery(name = "topComments", query = "SELECT comentario FROM PuntuacionComentario p GROUP BY p.comentario ORDER BY SUM(p.puntuacion) DESC"),
	@NamedQuery(name = "totalPuntuacionComentario", query = "SELECT SUM(p.puntuacion) FROM PuntuacionComentario p WHERE p.comentario = :comentarioParam"),
	@NamedQuery(name = "puntuacionByUserAndComment", query = "select p from PuntuacionComentario p where p.user = :userParam and p.comentario= :comentarioParam")})
@Entity
public class PuntuacionComentario {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	
	private Integer puntuacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Comentario comentario;
	
	public PuntuacionComentario (PuntuacionComentario p) {
		this.user = p.user;
		this.puntuacion = p.puntuacion;
		this.comentario = p.comentario;
	}
	public PuntuacionComentario(int puntuacion, User u, Comentario c){
		this.puntuacion = puntuacion;
		this.comentario = c;
		this.user = u;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getPuntuacion() {
		return this.puntuacion;
	}
	public void setPuntuacion(Integer p) {
		this.puntuacion = p;
	}
	public Comentario getComentario() {
		return comentario;
	}
	public void setComentario(Comentario o) {
		this.comentario = o;
	}
}
