# Spring Security
Para obtener un User de un controlador, es decir, de un código, tenemos el contraste Anónimo vs. Usuario después de su método a invocar:

## getAuthentication:
SecurityContextHolder.getContext().getAuthentication()
-	org.springframework.security.authentication.AnonymousAuthenticationToken
-	org.springframework.security.authentication.UsernamePasswordAuthenticationToken

## getPrincipal:
SecurityContextHolder.getContext().getAuthentication().getPrincipal()
-	es.ucm.fdi.tusnoficias.UserDetails <- Devuelto por el UserDetailsService.loadUserByUsername()
-	anonymousUser

## getAuthorities:
SecurityContextHolder.getContext().getAuthentication().getAuthorities()
-	ROLE_ANONYMOUS
-	ROLE_user, ROLE_admin  <- Roles definidos

## isAuthenticated:
SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
-	true <- Note que en ambos casos devuelve true
-	true 

## getAuthentication:
SecurityContextHolder.getContext().getAuthentication()
-	org.springframework.security.authentication.AnonymousAuthenticationToken@6faeba70: Principal: anonymousUser; Credentials: [PROTECTED]; Authenticated: true; Details: org.springframework.security.web.authentication.WebAuthenticationDetails@fffbcba8: RemoteIpAddress: 0:0:0:0:0:0:0:1; SessionId: 9A9E509BE0C61021622C5816F9A73B27; Granted Authorities: ROLE_ANONYMOUS
-	org.springframework.security.authentication.UsernamePasswordAuthenticationToken@7a257c08: Principal: es.ucm.fdi.tusnoficias.UserDetails@fd63d6; Credentials: [PROTECTED]; Authenticated: true; Details: org.springframework.security.web.authentication.WebAuthenticationDetails@fffbcba8: RemoteIpAddress: 0:0:0:0:0:0:0:1; SessionId: 9A9E509BE0C61021622C5816F9A73B27; Granted Authorities: ROLE_user, ROLE_admin
		
# iw-base

Proyecto base para [IW 2017-18](https://cv4.ucm.es/moodle/course/view.php?id=96754)

El uso de este proyecto como base es opcional (cualquier aplicacin que use Spring MVC, JPA para persistencia y Spring Security para autenticación y roles es admisible) - pero es muy recomendable.

## Contenido
- `src` : fuentes, incluyendo
- `src/main` : fuentes reales, excluyendo ficheros que sólo se usan en las pruebas
- `src/main/webapp` : plantillas JSP y fragmentos de plantilla (JSPFs)
- `src/main/java` : base de los ficheros java que componen el controlador, el modelo, y la configuracin de la aplicacin.
- `src/main/resources` : ficheros no-fuente de la aplicación
- `src/main/resources/application.properties` : propiedades generales de la aplicación, en formato clave-valor
- `src/main/resources/import.sql` : valores a cargar en una BD recién inicializada
- `src/main/resources/static` : raiz donde encontrar los ficheros estáticos (JS, CSS, imágenes) de la aplicación. Contiene versiones de Bootstrap y JQuery, cuyo uso es completamente opcional.
- `pom.xml` : fichero de proyecto. Cambia tu groupId / artifactId para que sea tuyo y original. Permite compilar, probar y desplegar tu proyecto vía [Maven](https://maven.apache.org/); y se puede importar a Eclipse / STS vía `importar > proyecto maven con fuentes existentes`
- `leeme.html` : fichero de "leeme.html" de ejemplo. Úsalo como plantilla para preparar tus entregas.

## Ficheros de aplicación y ficheros de usuario

No se debe incluir contenido "de usuario" en `src/main/resources` - esta carpeta es de sólo lectura mientras la aplicación está desplegada. Para guardar y servir ficheros de usuario, se debe seguir el mecanismo descrito en 
[LocalData](https://github.com/manuel-freire/iw-base/tree/master/src/main/java/es/ucm/fdi/iw/LocalData.java)
