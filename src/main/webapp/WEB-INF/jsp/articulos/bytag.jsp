<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib
	uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project"
	prefix="e"%>
<article class="articulo">
	<header>Articulos</header>
	<section>
		<section class="col-md-12 articulosASeleccionar">
			<c:choose>
				<c:when test="${not empty articulos}">
					<c:forEach items="${articulos}" var="ars">
						<section class="col-md-5 articulo">
							<header>${e:forHtmlContent(ars.titulo)}</header>
							<section>
								<c:forEach items="${ars.contenido}" var="arss">${e:forHtmlContent(arss)}</c:forEach>
								Autor: ${e:forHtmlContent(ars.autor.name)}
								${e:forHtmlContent(ars.autor.lname)}
							</section>
						</section>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<section class="col-md-5 articulo">No hay articulos con
						ese tag.</section>
				</c:otherwise>
			</c:choose>
		</section>
		<c:if test="${user}">
			<span onclick="changeseleccionActiva()">Seleccionar parrafo</span>
			<form method="post" action="${prefix}/articulo/nuevo/publicar">
				<div class="col-md-10">
					<textarea class="seleccion form-control" name="articulo" rows="8"></textarea>
					<input class="form-control" type="text" name="tags"
						placeholder="Tags separados con comas (,)"> <input
						class="form-control" type="text" name="titulo"
						placeholder="Titulo">
				</div>


				<input type="submit" value="Publicar">
			</form>
		</c:if>
	</section>
</article>