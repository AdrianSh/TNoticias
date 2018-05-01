<%@ include file="../../jspf/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../../jspf/column-left.jspf"%>
		<section class="col-md-7">
			<div class="row">
				<c:if test="${not empty error }">
				<p>${e:forHtmlContent(error)}</p>
				</c:if>
				<h2>Tu nueva contraseña</h2>
				<p>
					Su nueva contraseña es:<span style="font-weight: bolder">${newPass}</span>
					recuerde cambiarla en ajustes!
				</p>
			</div>
		</section>
	</div>
</section>
<!-- /.container -->

<%@ include file="../../jspf/footer.jspf"%>