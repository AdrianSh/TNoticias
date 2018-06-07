<%@ include file="../../jspf/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../../jspf/column-left.jspf"%>
		<section class="col-md-7">
			<article class="articulo">
				<header>Articulo Nuevo</header>
				<section>

					<form method="post" action="${siteUrl}/articulo/nuevo/articulos">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="col-md-10">
							<input type="text" name="tag" placeholder="tag"/>
							<div class="form-group">
								<button type="submit" class="btn btn-default">Cargar</button>
								</label>
							</div>
						</div>
					</form>
					<section class="col-md-12 articulosASeleccionar">
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
					</section>

					<form method="post" action="${siteUrl}/articulo/nuevo/publicar">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="col-md-10">
							<div class="alert alert-info">
								<a href="#" class="close" data-dismiss="alert"
									aria-label="close">&times;</a><strong>Info!</strong> Cuando
								selecciones un parrafo, recuerda tener "[BBCODE]" activado.
							</div>
							<div class="form-group">
								<span onclick="changeseleccionActiva()" class="btn btn-info">Seleccionar
									parrafo</span>
								<button type="button" class="btn btn-outline btn-success">Cargar</button>
							</div>
						</div>
						<div class="col-md-10">
							<div class="form-group">
								<textarea class="seleccion form-control" name="articulo"
									rows="8"></textarea>
							</div>
							<div class="form-group">
								<input class="form-control" type="text" name="tags"
									placeholder="Tags separados con comas (,)">
							</div>
							<div class="form-group">
								<input class="form-control" type="text" name="titulo"
									placeholder="Titulo">
							</div>
						</div>
						<div class="col-md-10">
							<input class="btn btn-default" type="submit" value="Publicar">
						</div>
					</form>
				</section>
			</article>
		</section>
		<%@ include file="../../jspf/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../../jspf/footer.jspf"%>
<script>
	$(function() {
		$("textarea").wysibb();
	})
</script>