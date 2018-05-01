package es.ucm.fdi.tusnoficias.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({ @NamedQuery(name = "allTags", query = "select u from Tag u"),
	@NamedQuery(name = "allTagsOrderByDate", query = "select u from Tag u order by fecha"),
	@NamedQuery(name = "allByTag", query = "select u from Tag u where u.nombre = :tagParam")
})
public class Tag {

	private List<Articulo> articulo;
	private String nombre;
	private Date fecha;
	
	
	public static Tag newTag(String nombre){
		Tag tg = new Tag();
		tg.nombre=nombre;
		tg.articulo = new ArrayList<Articulo>();
		tg.fecha = new Date();
		return tg;
	}
	
	@Id
	public String getNombre(){
		return nombre;
	} 
	
	@ManyToMany(targetEntity=Articulo.class)
	@JoinColumn(name="Tags") 
	public List<Articulo> getArticulo(){
		return articulo;
	}
	
	public void setArticulo(List<Articulo> articulo) {
		this.articulo = articulo;
	}
	
	public void setNombre(String nombre){
		this.nombre=nombre;
	}
	
	public void setFecha(Date fecha){
		this.fecha = fecha;
	}
	
	public Date getFecha(){
		return fecha;
	}

}
