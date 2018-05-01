package es.ucm.fdi.tusnoficias.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "allActividadByUser", query = "select a from Actividad a where a.user = :userParam"),
	@NamedQuery(name = "allActividad", query = "select a from Actividad a")})
public class Actividad {
	private long id;
	private String estado;
	private User user;
	private Date createdAt;
	private Date updatedAt;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public static Actividad createActividad(String estado, User user, Date createdAt) {
		Actividad a = new Actividad();
		a.estado = estado;
		a.user = user;
		a.createdAt = a.updatedAt = createdAt;
		return a;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@ManyToOne(targetEntity=User.class)
	public User getUser(){
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getUpdatedAt(){
		return updatedAt;
	}
	
	public void setUpdatedAt(Date date){
		this.updatedAt = date;
	}
	
	public Date getCreatedAt(){
		return createdAt;
	}
	
	public void setCreatedAt(Date date){
		this.updatedAt = this.createdAt = date;
	}
}
