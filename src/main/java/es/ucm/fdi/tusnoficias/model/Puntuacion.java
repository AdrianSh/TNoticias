package es.ucm.fdi.tusnoficias.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({ @NamedQuery(name = "puntuacionById", query = "select u from Puntuacion u where u.id= :idParam")})
@Entity
public class Puntuacion {
	private long id;
	private int positivos;
	private int negativos;
	private long usuarioId;
	
	public Puntuacion(){};
	
	public Puntuacion(int pos, int neg){	
		this.positivos = pos;
		this.negativos = neg;
	}
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}

	public int getPositivos() {
		return positivos;
	}

	public void setPositivos(int positivos) {
		this.positivos = positivos;
	}

	public int getNegativos() {
		return negativos;
	}

	public void setNegativos(int negativos) {
		this.negativos = negativos;
	}
	
	public void positivo(){
		this.positivos++;
	}
	
	public void negativo(){
		this.negativos++;
	}
	
	//@ManyToOne(targetEntity=User.class)
	public long getUsuario() {
		return usuarioId;
	}
	
	public void setUsuario(long usuario) {
		this.usuarioId = usuario;
	}
}
