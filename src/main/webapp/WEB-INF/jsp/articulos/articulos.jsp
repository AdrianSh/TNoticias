<%@ include file="../../jspf/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../../jspf/column-left.jspf"%>
		<section class="col-md-7" id="contenedorDeArts">
			<c:forEach items="${lastarticulos}" var="articulo">
				<article class="articulo">
					<header>
						<a href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}">${e:forHtmlContent(articulo.titulo)}</a><span
							class="fecha">${e:forHtmlContent(articulo.fecha)}</span>
					</header>
					<c:if test="${not empty user}">
					<header class="opciones dropdown">
						<a
							href="${siteUrl}/articulo/borrar/${e:forHtmlContent(articulo.id)}">
							<span class="glyphicon glyphicon-remove"></span>
						</a>
						<c:choose>
							<c:when test="${user.favoritos.contains(articulo)}">
								<a style="color: red;"
									href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/favorito">
									<span class="glyphicon glyphicon-heart"></span>
								</a>
							</c:when>
							<c:otherwise>
								<a
									href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/favorito">
									<span class="glyphicon glyphicon-heart"></span>
								</a>
							</c:otherwise>
						</c:choose>
						
						<a
											onclick="$('#addTagForm').show()">
											<span class="glyphicon glyphicon-pushpin"></span>
										</a>
										<a
											onclick="$('#removeTagForm').show()">
											<span class="glyphicon glyphicon-scissors"></span>
										</a>
										
										<form method='post' id="addTagForm" class="dropdown-menu" style="display: none;" action='./../articulo/anadirTag'>
										<input type='text' name='Tag' class='btn-sm' placeholder='Tag'>
										<input type='hidden' name='idArticulo' value='${e:forHtmlContent(articulo.id)}'>
										<input type='submit' value='Añadir Tag'>
										<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" />
										<div class='btn-sm' onclick='$(this).parent().hide()'>Cancelar</div>
										
										</form>
										
										<form method='post' id="removeTagForm" class="dropdown-menu" style="display: none;" action='./../articulo/eliminarTag'>
										<input type='text' name='Tag' class='btn-sm' placeholder='Tag'>
										<input type='hidden' name='idArticulo' value='${e:forHtmlContent(articulo.id)}'>
										<input type='submit' value='Eliminar Tag'>
										<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" />
										<div class='btn-sm' onclick='$(this).parent().hide()'>Cancelar</div>
										
										</form>

					</header>
					</c:if>
					<section>
						<c:forEach items="${articulo.contenido}" var="paragraph">${e:forHtmlContent(paragraph)}</c:forEach>
					</section>
					<footer>
						<div class="ranking">${e:forHtmlContent(articulo.ranking)}</div>
						<div class="autor">${e:forHtmlContent(articulo.autor.login)}</div>
					</footer>
				</article>
				<section class="separadorDeArticulo"></section>
			</c:forEach>
			<c:if test="${empty lastarticulos}">
				<article class="articulo">
					<header>¡No hay articulos!</header>
					<section>¡A que esperas! ¡A publicar!</section>
				</article>
			</c:if>
			<!-- 
			Desactivado ya que no se piensa poner ajax para obtener los articulos por cuestiones
			de seguridad, ya sea que nos toca usar sql con limits, etc.
			<ul id="articulopag" class="pagination-sm"></ul>
			<script>
			$('#articulopag').twbsPagination({
				totalPages : 35,
				visiblePages : 7,
				onPageClick : function(event, page) {
					// $('.articulo').text('Page ' + page);
					$('.articulo').text($('.articulo').html);
				}
			});
			</script> -->
		</section>
		<%@ include file="../../jspf/column-right.jspf"%>
		<section class="col-md-10 loadMore">
			<a class="label label-default" onclick="cargarArts()" href="#">Cargar mas articulos</a>
		</section>
	</div>
</section>
<!-- /.container -->

<%@ include file="../../jspf/footer.jspf"%>

<script type="text/javascript">
	var articulos = $('.articulo');
	var articulosMostrados;
	var art = [];
	var contenedorDeArts = $('#contenedorDeArts');

	function removeArt(removeItem) {
		articulos = articulos.filter(function(e) {
			return !$(articulos[e]).is(removeItem);
		});
	}
	function addArt(art) {
		articulosMostrados = art;
	}
	function mostrarArts() {
		contenedorDeArts.html(contenedorDeArts.html()
				+ "<article class='articulo'>" + articulosMostrados
				+ "</article><section class='separadorDeArticulo'></section>");
	}
	if (articulos.length > 10) {
		cargarArts();
	}
	function cargarArts() {
		var i = 0;
		if (articulos.length > 0) {
			contenedorDeArts.html("");
			articulos.each(function(index, element) {
				if (i <= 10) {
					addArt($(this).html());
					removeArt($(this));
					i++;
					mostrarArts();
				} else {
					return false;
				}
			});
			if(articulos.length > 0){
				$('.loadMore').show();
			}
		}
	}
</script>