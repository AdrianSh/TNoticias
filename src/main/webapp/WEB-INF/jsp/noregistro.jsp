<%@ include file="../jspf/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../jspf/column-left.jspf"%>
			<section class="col-md-7">
				<article class="articulo">
					<header>${e:forHtmlContent(mMensaje)}</header>
					<section>Si ya est�s registrado <a href="${siteUrl}/inicio_sesion">inicia sesi�n</a>, si no <a href="${siteUrl}/registro">reg�strate</a>!</section>
				</article>
			</section>
		<%@ include file="../jspf/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../jspf/footer.jspf"%>