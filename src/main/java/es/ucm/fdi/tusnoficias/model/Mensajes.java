package es.ucm.fdi.tusnoficias.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "allMensajesByUser", query = "select a from Mensajes a where a.user = :userParam"),
	@NamedQuery(name = "allMensajes", query = "select a from Mensajes a")})
public class Mensajes {
	private long id;
	private String mensaje;
	private User user;
	private Date createdAt;
	private User author;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public static Mensajes createMensaje(String mensaje, User user, User author,Date createdAt) {
		Mensajes a = new Mensajes();
		a.mensaje = mensaje;
		a.user = user;
		a.author = author;
		a.createdAt = createdAt;
		return a;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje (String mensaje) {
		this.mensaje = mensaje;
	}
	
	@OneToOne(targetEntity=User.class)
	public User getAuthor(){
		return author;
	}
	public void setAuthor(User author){
		this.author = author;
	}
	
	@ManyToOne(targetEntity=User.class)
	public User getUser(){
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Date date){
		this.createdAt = date;
	}
}
