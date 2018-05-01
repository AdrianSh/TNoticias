package es.ucm.fdi.tusnoficias.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@NamedQueries({ @NamedQuery(name = "allUsers", query = "select u from User u"),
		@NamedQuery(name = "userByLogin", query = "select u from User u where u.login = :loginParam"),
		@NamedQuery(name = "delUser", query = "delete from User u where u.id= :idParam"),
		@NamedQuery(name = "userByEmail", query = "select u from User u where u.email = :emailParam")
})
public class User {

	private static BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

	// do not change these fields - all web applications with user
	// authentication need them
	private long id;
	private String login;
	private String role;
	private String hashedAndSalted;
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
	private List<Integer> puntuacionesId;
	private List<Integer> puntuacionesHechasId;

	public User() {
	}

	public static User createUser(String login, String pass, String role, String nombre, String apellido,
			String email, String preguntaDeSeguridad, String respuestaDeSeguridad) {
		User u = new User();
		u.login = Encode.forHtmlContent(login);
		u.hashedAndSalted = generateHashedAndSalted(Encode.forHtmlContent(pass));
		u.role = Encode.forHtmlContent(role);
		u.name = Encode.forHtmlContent(nombre);
		u.email = Encode.forHtmlContent(email);
		u.lname = Encode.forHtmlContent(apellido);
		u.preguntaDeSeguridad = Encode.forHtmlContent(preguntaDeSeguridad);
		u.respuestaDeSeguridad = Encode.forHtmlContent(respuestaDeSeguridad);
		u.tablon = new ArrayList<Comentario>();
		u.favoritos = new ArrayList<Articulo>();
		u.seguidores = new ArrayList<User>();
		u.seguidos = new ArrayList<User>();
		u.amigos = new ArrayList<>();
		u.actividad = new ArrayList<>();
		u.mensajes = new ArrayList<Mensajes>();
		u.comentariosPerfil = new ArrayList<ComentarioPerfil>();
		u.puntuacionesHechasId = new ArrayList<Integer>();
		u.puntuacionesId = new ArrayList<Integer>();
		return u;
	}
	public void cambiarUserPass(String pass){
		hashedAndSalted = generateHashedAndSalted(pass);
	}
	/*
	public static void main(String[] args) {
		System.err.println(generateHashedAndSalted("lolo"));
	}
	*/
	public boolean isPassValid(String pass) {
		return bcryptEncoder.matches(pass, hashedAndSalted);
	}
	
	public static boolean isHashValid(String pass, String hashedAndSalted) {
		return bcryptEncoder.matches(pass, hashedAndSalted);
	}

	/**
	 * Generate a hashed&salted hex-string from a user's pass and salt
	 * 
	 * @param pass
	 *            to use; no length-limit!
	 * @param salt
	 *            to use
	 * @return a string to store in the BD that does not reveal the password
	 *         even if the DB is compromised. Note that brute-force is possible,
	 *         but it will have to be targeted (ie.: use the same salt)
	 */
	public static String generateHashedAndSalted(String pass) {
		/*
		 * Código viejo: sólo 1 iteración de SHA-1. bCrypt es mucho más seguro
		 * (itera 1024 veces...)
		 * 
		 * Además, bcryptEncoder guarda la sal junto a la contraseña byte[]
		 * saltBytes = hexStringToByteArray(user.salt); byte[] passBytes =
		 * pass.getBytes(); byte[] toHash = new byte[saltBytes.length +
		 * passBytes.length]; System.arraycopy(passBytes, 0, toHash, 0,
		 * passBytes.length); System.arraycopy(saltBytes, 0, toHash,
		 * passBytes.length, saltBytes.length); return
		 * byteArrayToHexString(sha1hash(toHash));
		 */
		return bcryptEncoder.encode(pass);
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

	public String getHashedAndSalted() {
		return hashedAndSalted;
	}

	public void setHashedAndSalted(String hashedAndSalted) {
		this.hashedAndSalted = hashedAndSalted;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String toString() {
		return "" + id + " " + login + " " + hashedAndSalted;
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public List<Actividad> getActividad() {
		return actividad;
	}

	public void setActividad(List<Actividad> actividad) {
		this.actividad = actividad;
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
	
	@ElementCollection
	public List<Integer> getPuntuacionesId() {
		// aqui consulta para encontrar puntuacion
		return puntuacionesId;
	}

	public void setPuntuacionesId(List<Integer> puntuacionesId) {
		this.puntuacionesId = puntuacionesId;
		// aqui consulta para guardar puntuacion
	}

	@ElementCollection
	public List<Integer> getPuntuacionesHechasId() {
		return puntuacionesHechasId;
	}

	public void setPuntuacionesHechasId(List<Integer> puntuacionesHechasId) {
		this.puntuacionesHechasId = puntuacionesHechasId;
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
	
	public String getPreguntaDeSeguridad(){
		return preguntaDeSeguridad;
	}
	
	public void setPreguntaDeSeguridad(String preguntaDeSeguridad){
		this.preguntaDeSeguridad = preguntaDeSeguridad; 
	}
	
	public String getRespuestaDeSeguridad(){
		return respuestaDeSeguridad;
	}
	
	public void setRespuestaDeSeguridad(String respuestaDeSeguridad){
		this.respuestaDeSeguridad = respuestaDeSeguridad; 
	}
}