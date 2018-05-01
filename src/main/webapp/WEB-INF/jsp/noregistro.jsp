<%@ include file="../jspf/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../jspf/column-left.jspf"%>
			<section class="col-md-7">
				<article class="articulo">
					<header>${e:forHtmlContent(mMensaje)}</header>
					<section>Si ya estás registrado <a href="/tusnoticias/inicio_sesion">inicia sesión</a>, si no <a href="/tusnoticias/registro">regístrate</a>!</section>
				</article>
			</section>
		<%@ include file="../jspf/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../jspf/footer.jspf"%>