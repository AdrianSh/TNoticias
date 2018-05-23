package es.ucm.fdi.tusnoficias.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import org.owasp.encoder.Encode;
import org.springframework.transaction.annotation.Transactional;

@Entity
@NamedQueries({ @NamedQuery(name = "allUsers", query = "select u from User u"),
		@NamedQuery(name = "userByLogin", query = "select u from User u where u.login = :loginParam"),
		@NamedQuery(name = "delUser", query = "delete from User u where u.id= :idParam"),
		@NamedQuery(name = "userByEmail", query = "select u from User u where u.email = :emailParam") })
public class User {

	// do not change these fields - all web applications with user
	// authentication need them
	private long id;
	private String login;
	private String roles; // split by , to separate roles
	private boolean enabled = true;
	private String password;
	private String name;
	private String lname;
	private String email;
	private String avatar = "http://lorempixel.com/100/100/people/10/";
	private String profileBackground = "http://lorempixel.com/100/100/people/10/";
	private String preguntaDeSeguridad;
	private String respuestaDeSeguridad;

	// change fields below here to suit your application
	private List<Comentario> tablon;
	private List<Articulo> favoritos;
	private List<User> seguidores;
	private List<User> seguidos;

	private List<Amigos> amigos;
	private List<Actividad> actividad;

	private List<Mensajes> mensajes;
	private List<ComentarioPerfil> comentariosPerfil;

	private List<Puntuacion> puntuaciones;

	private List<PuntuacionComentario> puntuacionesComentarios;

	public User() {
	}

	public static User createUser(String login, String pass, String roles, String nombre, String apellido, String email,
			String preguntaDeSeguridad, String respuestaDeSeguridad) {
		User u = new User();
		u.login = Encode.forHtmlContent(login);
		u.password = pass; // Encode.forHtmlContent(pass));
		u.roles = Encode.forHtmlContent(roles);
		u.name = Encode.forHtmlContent(nombre);
		u.email = Encode.forHtmlContent(email);
		u.lname = Encode.forHtmlContent(apellido);
		u.preguntaDeSeguridad = Encode.forHtmlContent(preguntaDeSeguridad);
		u.respuestaDeSeguridad = Encode.forHtmlContent(respuestaDeSeguridad);
		u.tablon = new ArrayList<>();
		u.favoritos = new ArrayList<>();
		u.seguidores = new ArrayList<>();
		u.seguidos = new ArrayList<>();
		u.amigos = new ArrayList<>();
		u.actividad = new ArrayList<>();
		u.mensajes = new ArrayList<>();
		u.comentariosPerfil = new ArrayList<>();
		u.puntuaciones = new ArrayList<>();
		u.puntuacionesComentarios = new ArrayList<>();
		u.enabled = true;
		return u;
	}

	/**
	 * Converts a byte array to a hex string
	 * 
	 * @param b
	 *            converts a byte array to a hex string; nice for storing
	 * @return the corresponding hex string
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	/**
	 * Converts a hex string to a byte array
	 * 
	 * @param hex
	 *            string to convert
	 * @return equivalent byte array
	 */
	public static byte[] hexStringToByteArray(String hex) {
		byte[] r = new byte[hex.length() / 2];
		for (int i = 0; i < r.length; i++) {
			String h = hex.substring(i * 2, (i + 1) * 2);
			r[i] = (byte) Integer.parseInt(h, 16);
		}
		return r;
	}

	public void anadirATablon(Comentario coment) {
		this.tablon.add(coment);
	}

	public void eliminarDeTablon(Comentario coment) {
		if (!this.tablon.remove(coment)) {

		}
	}

	public void anadirFavorito(Articulo articulo) {
		this.favoritos.add(articulo);
	}

	public void eliminarFavorito(Articulo articulo) {
		if (!this.favoritos.remove(articulo)) {

		}
	}

	public void seguir(User user) {
		this.seguidos.add(user);
	}

	public void dejarDeSeguir(User user) {
		if (!this.seguidos.remove(user)) {

		}
	}

	public void nuevoSeguidor(User user) {
		this.seguidores.add(user);
	}

	public void eliminarSeguidor(User user) {
		if (!this.seguidores.remove(user)) {

		}
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(unique = true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String toString() {
		return "" + id + " " + login + " " + password;
	}

	@OneToMany(targetEntity = Comentario.class)
	@JoinColumn(name = "receptor")
	public List<Comentario> getTablon() {
		return tablon;
	}

	public void setTablon(List<Comentario> tablon) {
		this.tablon = tablon;
	}

	@ManyToMany(targetEntity = Articulo.class)
	public List<Articulo> getFavoritos() {
		return favoritos;
	}

	public void setFavoritos(List<Articulo> favoritos) {
		this.favoritos = favoritos;
	}

	@OneToMany(targetEntity = User.class)
	public List<User> getSeguidores() {
		return seguidores;
	}

	public void setSeguidores(List<User> seguidores) {
		this.seguidores = seguidores;
	}

	@OneToMany(targetEntity = User.class)
	public List<User> getSeguidos() {
		return seguidos;
	}

	public void setSeguidos(List<User> seguidos) {
		this.seguidos = seguidos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public List<Amigos> getAmigos() {
		return amigos;
	}

	public void setAmigos(List<Amigos> amigos) {
		this.amigos = amigos;
	}

	public boolean esMiAmigo(List<Amigos> amigos, User us) {
		return amigos.contains(us);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public List<Actividad> getActividad() {
		return actividad;
	}

	public void setActividad(List<Actividad> actividad) {
		this.actividad = actividad;
	}

	public void addActividad(List<Actividad> actividades, Actividad a) {
		actividades.add(a);
		this.actividad = actividades;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	public List<Mensajes> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensajes> mensajes) {
		this.mensajes = mensajes;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
	public List<ComentarioPerfil> getComentariosPerfil() {
		return comentariosPerfil;
	}

	public void setComentariosPerfil(List<ComentarioPerfil> comentariosPerfil) {
		this.comentariosPerfil = comentariosPerfil;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getProfileBackground() {
		return profileBackground;
	}

	public void setProfileBackground(String profileBackground) {
		this.profileBackground = profileBackground;
	}

	public String getPreguntaDeSeguridad() {
		return preguntaDeSeguridad;
	}

	public void setPreguntaDeSeguridad(String preguntaDeSeguridad) {
		this.preguntaDeSeguridad = preguntaDeSeguridad;
	}

	public String getRespuestaDeSeguridad() {
		return respuestaDeSeguridad;
	}

	public void setRespuestaDeSeguridad(String respuestaDeSeguridad) {
		this.respuestaDeSeguridad = respuestaDeSeguridad;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@OneToMany(mappedBy = "user")
	public List<Puntuacion> getPuntuaciones() {
		return puntuaciones;
	}

	public void setPuntuaciones(List<Puntuacion> puntuaciones) {
		this.puntuaciones = puntuaciones;
	}

	@OneToMany(mappedBy = "user")
	public List<PuntuacionComentario> getPuntuacionesComentarios() {
		return puntuacionesComentarios;
	}

	public void setPuntuacionesComentarios(List<PuntuacionComentario> puntuacionesComentarios) {
		this.puntuacionesComentarios = puntuacionesComentarios;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof User) {
			User u2 = (User) o;
			return this.id == u2.id;
		}
		return false;
	}
}