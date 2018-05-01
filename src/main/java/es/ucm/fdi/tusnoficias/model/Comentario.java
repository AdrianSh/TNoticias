package es.ucm.fdi.tusnoficias.model;

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
	 @NamedQuery(name = "allComentariosByArticulo", query = "select u from Comentario u where u.articulo = :articuloParam")})
public class Comentario {
	private long id;
	private String comment; 
	private User owner;
	private Articulo articulo;
	private Comentario responde;
	private List<Integer> puntuacionesId;
	private List<Comentario> respuestas;
	private Date fecha;

	@Id
	@GeneratedValue
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
	
	@ManyToOne(targetEntity=User.class)
	public User getUser() {
		return owner;
	}

	public void setUser(User owner) {
		this.owner = owner;
	}

	@ManyToOne(targetEntity=Articulo.class)
	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}
	
	public int PuntuacionReal(){
		int i = 0;
		for(int l: puntuacionesId)
			i += l;
		return i;
	}
	
	@ElementCollection
	public List<Integer> getPuntuacionesId() {
		//aqui consulta para encontrar puntuacion
		return puntuacionesId;
	}

	public void setPuntuacionesId(List<Integer> puntuacionesId) {
		this.puntuacionesId = puntuacionesId;
		//aqui consulta para guardar puntuacion
	}
	@OneToMany(targetEntity=Comentario.class)
	public List<Comentario> getRespuestas() {
		return respuestas;
	}

	public void setRespuestas(List<Comentario> respuestas) {
		this.respuestas = respuestas;
	}
	
	public void anadirRespuesta(Comentario respuesta){
		this.respuestas.add(respuesta);
	}
	@OneToOne(targetEntity=Comentario.class)
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
}
