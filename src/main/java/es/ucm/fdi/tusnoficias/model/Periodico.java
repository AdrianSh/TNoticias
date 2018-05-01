package es.ucm.fdi.tusnoficias.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "allPeriodicos", query = "select u from Periodico u") })
public class Periodico {

	private long id;
	private String url;
	private String nombre;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setNombre(String nombre){
	  	this.nombre = nombre;
	}
	public String getNombre(){
	  	return nombre;
	}
}
