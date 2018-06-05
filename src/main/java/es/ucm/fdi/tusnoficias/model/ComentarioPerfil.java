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
@NamedQueries({ @NamedQuery(name = "allComentarioPerfilByUser", query = "select a from ComentarioPerfil a where a.userPerfil = :userParam"),
	@NamedQuery(name = "allComentarioPerfil", query = "select a from ComentarioPerfil a")})
public class ComentarioPerfil {
	private long id;
	private String comentario;
	private User user;
	private User userPerfil;
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
	
	public static ComentarioPerfil createComment(String comentario, User user, User userPerfil, Date createdAt) {
		ComentarioPerfil a = new ComentarioPerfil();
		a.comentario = comentario;
		a.user = user;
		a.userPerfil = userPerfil;
		a.createdAt = a.updatedAt = createdAt;
		return a;
	}
	
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	@OneToOne(targetEntity=User.class)
	public User getUser(){
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne(targetEntity=User.class)
	public User getUserPerfil(){
		return userPerfil;
	}
	
	public void setUserPerfil(User userPerfil) {
		this.userPerfil = userPerfil;
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ComentarioPerfil) {
			ComentarioPerfil o = (ComentarioPerfil) obj;
			return o.id == this.id;
		}
		return false;
	}
}
