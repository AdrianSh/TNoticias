<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../../jspf/admin/header.jspf"%>
<div id="wrapper">
	<%@ include file="../../jspf/admin/nav.jspf"%>
	<div id="page-wrapper">
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">Articulos</h1>
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<!-- /.row -->
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">Articulos de Periodicos Ripper</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-6">
								<form role="form" action="${prefix}/admin/forms" method="post">
									<div class="form-group">
										<label>Periodico</label> <select name="periodico"
											class="form-control">
											<c:forEach items="${periodicos}" var="a">
												<option value="${e:forHtmlContent(a.id)}">${e:forHtmlContent(a.nombre)}</option>
											</c:forEach>
										</select>
									</div>
									<div class="form-group">
										<label>Ingrese la URL del Articulo</label> <input
											class="form-control" name="urlarticulo"
											placeholder="URL del Articulo">
									</div>
									<div class="form-group">
										<label>Tipo de identificador</label> <select name="tipo"
											class="form-control">
											<option value="classs">Clase</option>
											<option value="id">Id</option>
											<option value="attribute">Atributo</option>
										</select>
									</div>
									<div class="form-group">
										<label>Ingrese el identificador del contenedor</label> <input
											class="form-control" name="identificador"
											placeholder="Identificador">
									</div>
									<button type="submit" class="btn btn-default">Cargar
										Articulo</button>
									<button type="reset" class="btn btn-default">Borrar</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- /.row -->
		<div class="row">
			<div class="col-lg-12">
				<c:if test="${not empty articulos}">
					<div class="panel panel-success" style="display: none;"
						id="selection1">
						<div class="panel-heading">Selección</div>
						<div class="panel-body"></div>
					</div>

					<table class="table table-striped" id="table1">
						<thead>
							<tr>
								<th></th>
								<th>Contenido</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${articulos}" var="a">
								<tr onclick="choose(this);">
									<td><button type="button"
											class="btn btn-default btn-circle">
											<i class="fa fa-check"></i>
										</button></td>
									<td><p>${e:forHtmlContent(a)}</p></td>
								</tr>
							</c:forEach>


						</tbody>
					</table>
				</c:if>

				<c:if test="${articulo != null}">
					<div class="panel panel-info">
						<div class="panel-heading">Articulo descargado</div>
						<div class="panel-body">
							<p>${e:forHtmlContent(articulo)}</p>
						</div>
						<div class="panel-footer">Panel Footer</div>
					</div>
				</c:if>

				<form role="form" action="${prefix}/admin/forms/publicar"
					method="post">
					<div class="alert alert-info">
						<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><strong>Info!</strong>
						Cuando selecciones un parrafo, recuerda tener "[BBCODE]" activado.
					</div>
					<div class="form-group">
						<label>Articulo a publicar:
							<button type="button" class="btn btn-outline btn-success"
								onclick="changeseleccionActiva()">Seleccionar parrafo</button>
							<button type="button" class="btn btn-outline btn-success">Cargar</button>
						</label>
						<div class="form-group">
							<textarea class="seleccion form-control" rows="15"
								name="articulo" id="articuloContenedor"></textarea>

						</div>
						<div class="form-group">
							<label><input class="form-control" id="articuloTitulo"
								placeholder="Titulo" name="titulo"></label>
						</div>
						<div class="form-group">
							<label>Ingrese los tags separados con comas: <input
								class="form-control" placeholder="Tags" name="tags"></label>
						</div>
						<!-- No implementado por completo  
						<div class="form-group">
							<input type="file" name="artimagen">
						</div>
						-->
						<div class="form-group">
							<button class="btn btn-primary btn-lg btn-block">Publicar</button>
						</div>
				</form>
			</div>
		</div>
		<!-- /.row -->

		<%
			/*
			
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-default">
							<div class="panel-heading">Basic Form Elements</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-lg-6">
										<form role="form">
											<div class="form-group">
												<label>Text Input</label> <input class="form-control">
												<p class="help-block">Example block-level help text here.</p>
											</div>
											<div class="form-group">
												<label>Text Input with Placeholder</label> <input
													class="form-control" placeholder="Enter text">
											</div>
											<div class="form-group">
												<label>Static Control</label>
												<p class="form-control-static">email@example.com</p>
											</div>
											<div class="form-group">
												<label>File input</label> <input type="file">
											</div>
											<div class="form-group">
												<label>Text area</label>
												<textarea class="form-control" rows="3"></textarea>
											</div>
											<div class="form-group">
												<label>Checkboxes</label>
												<div class="checkbox">
													<label> <input type="checkbox" value="">Checkbox
														1
													</label>
												</div>
												<div class="checkbox">
													<label> <input type="checkbox" value="">Checkbox
														2
													</label>
												</div>
												<div class="checkbox">
													<label> <input type="checkbox" value="">Checkbox
														3
													</label>
												</div>
											</div>
											<div class="form-group">
												<label>Inline Checkboxes</label> <label
													class="checkbox-inline"> <input type="checkbox">1
												</label> <label class="checkbox-inline"> <input
													type="checkbox">2
												</label> <label class="checkbox-inline"> <input
													type="checkbox">3
												</label>
											</div>
											<div class="form-group">
												<label>Radio Buttons</label>
												<div class="radio">
													<label> <input type="radio" name="optionsRadios"
														id="optionsRadios1" value="option1" checked>Radio 1
													</label>
												</div>
												<div class="radio">
													<label> <input type="radio" name="optionsRadios"
														id="optionsRadios2" value="option2">Radio 2
													</label>
												</div>
												<div class="radio">
													<label> <input type="radio" name="optionsRadios"
														id="optionsRadios3" value="option3">Radio 3
													</label>
												</div>
											</div>
											<div class="form-group">
												<label>Inline Radio Buttons</label> <label
													class="radio-inline"> <input type="radio"
													name="optionsRadiosInline" id="optionsRadiosInline1"
													value="option1" checked>1
												</label> <label class="radio-inline"> <input type="radio"
													name="optionsRadiosInline" id="optionsRadiosInline2"
													value="option2">2
												</label> <label class="radio-inline"> <input type="radio"
													name="optionsRadiosInline" id="optionsRadiosInline3"
													value="option3">3
												</label>
											</div>
											<div class="form-group">
												<label>Selects</label> <select class="form-control">
													<option>1</option>
													<option>2</option>
													<option>3</option>
													<option>4</option>
													<option>5</option>
												</select>
											</div>
											<div class="form-group">
												<label>Multiple Selects</label> <select multiple
													class="form-control">
													<option>1</option>
													<option>2</option>
													<option>3</option>
													<option>4</option>
													<option>5</option>
												</select>
											</div>
											<button type="submit" class="btn btn-default">Submit
												Button</button>
											<button type="reset" class="btn btn-default">Reset
												Button</button>
										</form>
									</div>
									<!-- /.col-lg-6 (nested) -->
									<div class="col-lg-6">
										<h1>Disabled Form States</h1>
										<form role="form">
											<fieldset disabled>
												<div class="form-group">
													<label for="disabledSelect">Disabled input</label> <input
														class="form-control" id="disabledInput" type="text"
														placeholder="Disabled input" disabled>
												</div>
												<div class="form-group">
													<label for="disabledSelect">Disabled select menu</label> <select
														id="disabledSelect" class="form-control">
														<option>Disabled select</option>
													</select>
												</div>
												<div class="checkbox">
													<label> <input type="checkbox">Disabled
														Checkbox
													</label>
												</div>
												<button type="submit" class="btn btn-primary">Disabled
													Button</button>
											</fieldset>
										</form>
										<h1>Form Validation States</h1>
										<form role="form">
											<div class="form-group has-success">
												<label class="control-label" for="inputSuccess">Input
													with success</label> <input type="text" class="form-control"
													id="inputSuccess">
											</div>
											<div class="form-group has-warning">
												<label class="control-label" for="inputWarning">Input
													with warning</label> <input type="text" class="form-control"
													id="inputWarning">
											</div>
											<div class="form-group has-error">
												<label class="control-label" for="inputError">Input
													with error</label> <input type="text" class="form-control"
													id="inputError">
											</div>
										</form>
										<h1>Input Groups</h1>
										<form role="form">
											<div class="form-group input-group">
												<span class="input-group-addon">@</span> <input type="text"
													class="form-control" placeholder="Username">
											</div>
											<div class="form-group input-group">
												<input type="text" class="form-control"> <span
													class="input-group-addon">.00</span>
											</div>
											<div class="form-group input-group">
												<span class="input-group-addon"><i class="fa fa-eur"></i>
												</span> <input type="text" class="form-control"
													placeholder="Font Awesome Icon">
											</div>
											<div class="form-group input-group">
												<span class="input-group-addon">$</span> <input type="text"
													class="form-control"> <span class="input-group-addon">.00</span>
											</div>
											<div class="form-group input-group">
												<input type="text" class="form-control"> <span
													class="input-group-btn">
													<button class="btn btn-default" type="button">
														<i class="fa fa-search"></i>
													</button>
												</span>
											</div>
										</form>
									</div>
									<!-- /.col-lg-6 (nested) -->
								</div>
								<!-- /.row (nested) -->
							</div>
							<!-- /.panel-body -->
						</div>
						<!-- /.panel -->
					</div>
					<!-- /.col-lg-12 -->
				</div>
				<!-- /.row -->
				*/
		%>
	</div>
	<!-- /#page-wrapper -->

</div>
<%@ include file="../../jspf/admin/sfooter.jspf"%>
<script>
	$(function() {
		$("#articuloContenedor").wysibb();
	})
</script>
</body>