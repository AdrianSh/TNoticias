<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../../jspf/admin/header.jspf"%>
<div id="wrapper">
	<%@ include file="../../jspf/admin/nav.jspf"%>

	<div id="page-wrapper">
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">Tablas</h1>
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<!-- /.row -->
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">Usuarios</div>
					<!-- /.panel-heading -->
					<div class="panel-body">
						<div class="dataTable_wrapper">
							<table class="table table-striped table-bordered table-hover"
								id="dataTables-example">
								<thead>
									<tr>
										<th>Id</th>
										<th>Username</th>
										<th>Rango</th>
										<th>Nombre</th>
										<th>Apellido</th>
										<th>Email</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${tabla_users}" var="a">
										<tr class="odd gradeX">
											<td>${e:forHtmlContent(a.id)}</td>
											<td>${e:forHtmlContent(a.login)}</td>
											<td>${e:forHtmlContent(a.role)}</td>
											<td>${e:forHtmlContent(a.name)}</td>
											<td>${e:forHtmlContent(a.lname)}</td>
											<td>${e:forHtmlContent(a.email)}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- /.table-responsive -->

					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-12 -->
		</div>
		<!-- /.row -->
		<div class="row">
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-heading">Comentarios en Articulos</div>
					<!-- /.panel-heading -->
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>#</th>
										<th>Comentario</th>
										<th>Autor</th>
										<th>Articulo</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${tabla_comentarios}" var="a">
										<tr>
											<td>${e:forHtmlContent(a.id)}</td>
											<td>${e:forHtmlContent(a.comment)}</td>
											<td>${e:forHtmlContent(a.owner.name)}</td>
											<td>	${e:forHtmlContent(a.owner.lname)}</td>
											<td>${e:forHtmlContent(a.articulo.titulo)}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- /.table-responsive -->
					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-6 -->
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-heading">Comentarios de Perfil</div>
					<!-- /.panel-heading -->
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table">
								<thead>
									<tr>
										<th>#</th>
										<th>Comentario</th>
										<th>Usuario que comento</th>
										<th>Dueño del perfil</th>
										<th>Fecha de actualización</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${tabla_comentarios_perfil}" var="a">
										<tr>
											<td>${e:forHtmlContent(a.id)}</td>
											<td>${e:forHtmlContent(a.comentario)}</td>
											<td>${e:forHtmlContent(a.user.name)}
												${e:forHtmlContent(a.user.lname)}</td>
											<td>${e:forHtmlContent(a.userPerfil.name)}
												${e:forHtmlContent(a.userPerfil.lname)}</td>
											<td>${e:forHtmlContent(a.updatedAt)}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- /.table-responsive -->
					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-6 -->
		</div>
		<!-- /.row -->
		<div class="row">
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-heading">Periodicos</div>
					<!-- /.panel-heading -->
					<div class="panel-body">
						<div class="table-responsive">
							<table class="table table-striped">
								<thead>
									<tr>
										<th>#</th>
										<th>Url</th>
										<th>Nombre</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${tabla_periodicos}" var="a">
										<tr>
											<td>${e:forHtmlContent(a.id)}</td>
											<td>${e:forHtmlContent(a.url)}</td>
											<td>${e:forHtmlContent(a.nombre)}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- /.table-responsive -->
						<form method="post" action="${prefix}/admin/tables/addPeriodico">
							<div class="form-group">
								<label>Nombre:</label> <input class="form-control" name="periodico">
							</div>
							<div class="form-group">
								<label>Url:</label> <input type="url" class="form-control" name="url">
							</div>
							<div class="form-group">
								<input class="form-control" type="submit" value="Agregar">
							</div>
						</form>
					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-6 -->
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-heading">Tags</div>
					<!-- /.panel-heading -->
					<div class="panel-body">
						<div class="table-responsive table-bordered">
							<table class="table">
								<thead>
									<tr>
										<th>Tag</th>
										<th>Articulos</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${tabla_tags}" var="a">
										<tr>
											<td>${e:forHtmlContent(a.nombre)}</td>
											<td><c:forEach items="${a.articulo}" var="art">
	                                             "${e:forHtmlContent(art.titulo)}", 
	                                             </c:forEach></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<!-- /.table-responsive -->
						<form method="post" action="${prefix}/admin/tables/addTag">
							<div class="form-group">
								<label>Tag:</label> <input class="form-control" name="tag">
							</div>
							<div class="form-group">
								<input class="form-control" type="submit" value="Agregar">
							</div>
						</form>

					</div>
					<!-- /.panel-body -->
				</div>
				<!-- /.panel -->
			</div>
			<!-- /.col-lg-6 -->
		</div>
		<!-- /.row -->
		<%
			/*
			<div class="row">
			    <div class="col-lg-6">
			        <div class="panel panel-default">
			            <div class="panel-heading">
			                Hover Rows
			            </div>
			            <!-- /.panel-heading -->
			            <div class="panel-body">
			                <div class="table-responsive">
			                    <table class="table table-hover">
			                        <thead>
			                            <tr>
			                                <th>#</th>
			                                <th>First Name</th>
			                                <th>Last Name</th>
			                                <th>Username</th>
			                            </tr>
			                        </thead>
			                        <tbody>
			                            <tr>
			                                <td>1</td>
			                                <td>Mark</td>
			                                <td>Otto</td>
			                                <td>@mdo</td>
			                            </tr>
			                            <tr>
			                                <td>2</td>
			                                <td>Jacob</td>
			                                <td>Thornton</td>
			                                <td>@fat</td>
			                            </tr>
			                            <tr>
			                                <td>3</td>
			                                <td>Larry</td>
			                                <td>the Bird</td>
			                                <td>@twitter</td>
			                            </tr>
			                        </tbody>
			                    </table>
			                </div>
			                <!-- /.table-responsive -->
			            </div>
			            <!-- /.panel-body -->
			        </div>
			        <!-- /.panel -->
			    </div>
			    <!-- /.col-lg-6 -->
			    <div class="col-lg-6">
			        <div class="panel panel-default">
			            <div class="panel-heading">
			                Context Classes
			            </div>
			            <!-- /.panel-heading -->
			            <div class="panel-body">
			                <div class="table-responsive">
			                    <table class="table">
			                        <thead>
			                            <tr>
			                                <th>#</th>
			                                <th>First Name</th>
			                                <th>Last Name</th>
			                                <th>Username</th>
			                            </tr>
			                        </thead>
			                        <tbody>
			                            <tr class="success">
			                                <td>1</td>
			                                <td>Mark</td>
			                                <td>Otto</td>
			                                <td>@mdo</td>
			                            </tr>
			                            <tr class="info">
			                                <td>2</td>
			                                <td>Jacob</td>
			                                <td>Thornton</td>
			                                <td>@fat</td>
			                            </tr>
			                            <tr class="warning">
			                                <td>3</td>
			                                <td>Larry</td>
			                                <td>the Bird</td>
			                                <td>@twitter</td>
			                            </tr>
			                            <tr class="danger">
			                                <td>4</td>
			                                <td>John</td>
			                                <td>Smith</td>
			                                <td>@jsmith</td>
			                            </tr>
			                        </tbody>
			                    </table>
			                </div>
			                <!-- /.table-responsive -->
			            </div>
			            <!-- /.panel-body -->
			        </div>
			        <!-- /.panel -->
			    </div>
			    <!-- /.col-lg-6 -->
			</div>
			<!-- /.row -->
			*/
		%>
	</div>
	<!-- /#page-wrapper -->

</div>
<%@ include file="../../jspf/admin/sfooter.jspf"%>
<!-- DataTables JavaScript -->
    
<script
	src="${s}/admin/bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
    
<script
	src="${s}/admin/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
<!-- Page-Level Demo Scripts - Tables - Use for reference -->
<script>
	$(document).ready(function() {
		$('#dataTables-example').DataTable({
			responsive : true
		});
	});
</script>
</body>