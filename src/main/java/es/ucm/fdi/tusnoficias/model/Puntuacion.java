package es.ucm.fdi.tusnoficias.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({ @NamedQuery(name = "puntuacionById", query = "select u from Puntuacion u where u.id= :idParam"),
	@NamedQuery(name = "topArticles", query = "SELECT a FROM Puntuacion p, Articulo a WHERE p.articulo = a GROUP BY a, p.articulo ORDER BY SUM(p.puntuacion) DESC"),
	@NamedQuery(name = "totalArticuloPuntuacion", query = "SELECT SUM(p.puntuacion) FROM Puntuacion p WHERE p.articulo = :articuloParam"),
	@NamedQuery(name = "puntuacionByUserAndArticle", query = "select p from Puntuacion p where p.user = :userParam and p.articulo = :articuloParam")})
@Entity
public class Puntuacion {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	
	private Integer puntuacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Articulo articulo;
	
	public Puntuacion() {}
	public Puntuacion (Puntuacion p) {
		this.user = p.user;
		this.puntuacion = p.puntuacion;
		this.articulo = p.articulo;
	}
	public Puntuacion(int puntuacion, User u, Articulo art){
		this.puntuacion = puntuacion;
		this.articulo = art;
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
	public Articulo getArticulo() {
		return articulo;
	}
	public void setArticulo(Articulo objeto) {
		this.articulo = objeto;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Puntuacion) {
			Puntuacion o = (Puntuacion) obj;
			return o.id == null ? o.id == this.id : o.id.equals(this.id);
		}
		return false;
	}
}
