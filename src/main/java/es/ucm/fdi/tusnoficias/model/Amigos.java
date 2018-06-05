package es.ucm.fdi.tusnoficias.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({ @NamedQuery(name = "allAmigosByUserName", query = "select a from Amigos a where a.user = :userParam")})
public class Amigos {
	private long id;
	private User user;
	private User amigo;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public static Boolean comprobarAmigo(List<Amigos> amigos, User posibleamigo){
		Boolean amigoencontrado = false;
		for(Amigos a : amigos)
			if(a.amigo == posibleamigo)
				amigoencontrado = true;
		return amigoencontrado;
	}
	public static Amigos createAmistad(User user, User amigo) {
		Amigos a = new Amigos();
		a.user = user;
		a.amigo = amigo;
		return a;
	}

	@ManyToOne(targetEntity = User.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@OneToOne(targetEntity=User.class)
	public User getAmigo() {
		return amigo;
	}

	public void setAmigo(User amigo) {
		this.amigo = amigo;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Amigos) {
			Amigos o = (Amigos) obj;
			return o.id == this.id;
		}
		return false;
	}
}
