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
				<h2>Recuperar contraseña</h2>
				<p>Si ha olvidado su contraseña, ingrese su email, sobrenombre o
					alias y la respuesta a su pregunta de seguridad:</p>
				<div>
					<form method="POST" action="${prefix}/recuperarpass">
						<div class="form-group">
							<label>E-mail:</label>
							<input type="email" name="email" required class="form-control" >
						</div>
						<div class="form-group">
							<label>Sobrenombre o alias:</label>
							<input type="text" name="alias" required class="form-control" >
						</div>
						<div class="form-group">
							<label>Respuesta a su pregunta de seguridad:</label>
							<input type="password" name="respuesta" required class="form-control" >
						</div>
						<input type="submit" value="Recuperar" class="btn btn-primary btn-block btn-lg" tabindex="7">
					</form>
				</div>
			</div>
		</section>
		<%@ include file="../../jspf/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../../jspf/footer.jspf"%>