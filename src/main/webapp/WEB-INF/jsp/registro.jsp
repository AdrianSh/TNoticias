<%@ include file="../jspf/header.jspf"%>
<%@ taglib prefix="e"
	uri="https://www.owasp.org/index.php/OWASP_Java_Encoder_Project"%>
<!-- Page Content -->
<section class="container">
	<div class="row">

		<%@ include file="../jspf/column-left.jspf"%>

		<section class="col-md-7">

			<div class="row">
				<form action="${siteUrl}/usuario/crear" role="form"
					class="form-register" method="POST">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<h2>
						Registrate <small>Es gratis, como debe ser!</small>
					</h2>

					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="text" name="nombre" id="first_name"
									class="form-control input-lg label_better" placeholder="Nombre"
									tabindex="1">
							</div>
						</div>
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="text" name="apellido" id="last_name"
									class="form-control input-lg label_better"
									placeholder="Apellidos" tabindex="2">
							</div>
						</div>
					</div>
					<div class="form-group">
						<input type="text" name="login" id="display_name"
							class="form-control input-lg label_better"
							placeholder="Sobrenombre o Alias" tabindex="3">
					</div>
					<div class="form-group">
						<input type="email" name="email" id="email"
							class="form-control input-lg label_better" placeholder="Email"
							tabindex="4">
					</div>
					<div class="form-group">
						<input type="text" name="pregunta" id="pregunta"
							class="form-control input-lg label_better"
							placeholder="Pregunta de seguridad" tabindex="5">
					</div>
					<div class="form-group">
						<input type="text" name="respuesta" id="respuesta"
							class="form-control input-lg label_better"
							placeholder="Respuesta de seguridad" tabindex="6">
					</div>
					<div class="row">
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="password" name="pass" id="password"
									class="form-control input-lg label_better"
									placeholder="Contraseña" tabindex="7">
							</div>
						</div>
						<div class="col-xs-12 col-sm-6 col-md-6">
							<div class="form-group">
								<input type="password" name="passConf"
									id="password_confirmation"
									class="form-control input-lg label_better"
									placeholder="Repita su contraseña" tabindex="8">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-8 col-sm-9 col-md-9">
							Al presionar <strong class="label label-primary">Registrarme</strong>,
							tu aceptas los <a href="#" data-toggle="modal"
								data-target="#t_and_c_m">Terminos y condiciones</a> del sitio,
							incluyendo nuestro uso de Cookies.
						</div>
					</div>

					<div class="row">
						<div class="col-xs-12 col-md-3"></div>
						<div class="col-xs-12 col-md-6">
							<input type="submit" value="Registrarme"
								class="btn btn-primary btn-block btn-lg" tabindex="9">
						</div>

					</div>
				</form>
			</div>

			<!-- Modal -->
			<div class="modal fade" id="t_and_c_m" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-lg">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">×</button>
							<h4 class="modal-title" id="myModalLabel">Terms & Conditions</h4>
						</div>
						<div class="modal-body">
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Similique, itaque, modi, aliquam nostrum at sapiente
								consequuntur natus odio reiciendis perferendis rem nisi tempore
								possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Similique, itaque, modi, aliquam nostrum at sapiente
								consequuntur natus odio reiciendis perferendis rem nisi tempore
								possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Similique, itaque, modi, aliquam nostrum at sapiente
								consequuntur natus odio reiciendis perferendis rem nisi tempore
								possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Similique, itaque, modi, aliquam nostrum at sapiente
								consequuntur natus odio reiciendis perferendis rem nisi tempore
								possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Similique, itaque, modi, aliquam nostrum at sapiente
								consequuntur natus odio reiciendis perferendis rem nisi tempore
								possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Similique, itaque, modi, aliquam nostrum at sapiente
								consequuntur natus odio reiciendis perferendis rem nisi tempore
								possimus ipsa porro delectus quidem dolorem ad.</p>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Similique, itaque, modi, aliquam nostrum at sapiente
								consequuntur natus odio reiciendis perferendis rem nisi tempore
								possimus ipsa porro delectus quidem dolorem ad.</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary"
								data-dismiss="modal">I Agree</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->


		</section>

		<%@ include file="../jspf/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../jspf/footer.jspf"%>