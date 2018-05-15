<%@ include file="../../jspf/header.jspf"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../../jspf/column-left.jspf"%>
		<c:choose>
			<c:when test="${not empty articulo}">
				<section class="col-md-7">
					<article class="articulo">
						<header>
							${e:forHtmlContent(articulo.titulo)}<span class="fecha">${e:forHtmlContent(articulo.fecha)}</span>
						</header>
						<c:if test="${not empty user}">
							<header class="opciones dropdown">
								<c:choose>
									<c:when test="${articulo.autor == user}">
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

										<c:choose>
											<c:when test="${not empty puntuacionP}">
												<a style="color: blue;"
													href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/puntuarP">
													<span class="glyphicon glyphicon-thumbs-up"></span>
												</a>
											</c:when>
											<c:otherwise>
												<a
													href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/puntuarP">
													<span class="glyphicon glyphicon-thumbs-up"></span>
												</a>
											</c:otherwise>
										</c:choose>

										<c:choose>
											<c:when test="${not empty puntuacionN}">
												<a style="color: blue;"
													href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/puntuarN">
													<span class="glyphicon glyphicon-thumbs-down"></span>
												</a>
											</c:when>
											<c:otherwise>
												<a
													href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/puntuarN">
													<span class="glyphicon glyphicon-thumbs-down"></span>
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
							
										
										
														
														
									</c:when>
									<c:otherwise>
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
											href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/puntuarP">
											<span class="glyphicon glyphicon-thumbs-up"></span>
										</a>
										<a
											href="${siteUrl}/articulo/${e:forHtmlContent(articulo.id)}/puntuarN">
											<span class="glyphicon glyphicon-thumbs-down"></span>
										</a>
									</c:otherwise>
								</c:choose>
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
					<section class="commentarios">
						<c:forEach items="${articuloComentarios}" var="coment">
							<div class="row">
								<div class="col-sm-2 right">
									<div class="thumbnail">
										<img class="img-responsive user-photo"
											src="${siteUrl}/user/${coment.user.id}/photo">
									</div>
									<!-- /thumbnail -->
								</div>
								<!-- /col-sm-1 -->
								<div class="col-sm-8 right">
									<div class="panel panel-default">
										<div class="panel-heading comentario-heading">
											<strong>${e:forHtmlContent(coment.user.name)}
												${e:forHtmlContent(coment.user.lname)}</strong> <span
												class="text-muted"> ${e:forHtmlContent(coment.fecha)}</span>
										</div>
										<div class="panel-body comentario-body">${e:forHtmlContent(coment.comment)}</div>
										<!-- /panel-body -->
										<div class="vote">
											<section class="puntuacionc">
												<span class="text-puntuar">Puntuar:</span>
												<form method="post" action="${siteUrl}/comentario/puntuarP">
													<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" /> <input type="submit"
														class="voto_positivo" title="Positivo"> <input
														type="hidden" value="${e:forHtmlContent(coment.id)}"
														name="id">
												</form>
												<form method="post" action="${siteUrl}/comentario/puntuarN">
													<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" /> <input type="submit"
														class="voto_negativo" title="Negativo"> <input
														type="hidden" value="${e:forHtmlContent(coment.id)}"
														name="id">
												</form>
											</section>
											<div class="puntuacionReal">${e:forHtmlContent(puntosCom)}</div>

											<section class="responderc">
												<a
													onClick="document.getElementById('oculto${coment.id}').style.display='block'"
													class="btn btn-default btn-sm"> Responde</a>

											</section>
										</div>
									</div>
									<div id="oculto${coment.id}" class="row" style="display: none">
										<div class="col-sm-8 left">
											<div class="panel panel-default">
												<form method="post" action="${siteUrl}/comentario/responder"
													class="comment-form">
													<input type="hidden" name="${_csrf.parameterName}"
														value="${_csrf.token}" /> <input type="hidden"
														value="${e:forHtmlContent(articulo.id)}" name="articulo">
													<input type="hidden" value="${e:forHtmlContent(coment.id)}"
														name="comentario original">
													<textarea name="comment" class="panel-body"></textarea>
													<input type="submit" value="Responder"
														class="btn btn-default">
												</form>
												<!-- /panel-body -->
											</div>
											<!-- /panel panel-default -->
										</div>
									</div>
									<!-- Respuestas de los comentarios -->
									<c:forEach items="${coment.respuestas}" var="respuesta">
										<div class="col-sm-2 right">
											<div class="thumbnail">
												<img class="img-responsive user-photo"
													src="${siteUrl}/user/${respuesta.user.id}/photo">
											</div>
											<!-- /thumbnail -->
										</div>
										<!-- /col-sm-1 -->
										<div class="col-sm-8 right">
											<div class="panel panel-default">
												<div class="panel-heading comentario-heading">
													<strong class="respuesta">${e:forHtmlContent(respuesta.user.name)}
														${e:forHtmlContent(respuesta.user.lname)}</strong> <span
														class="text-muted">
														${e:forHtmlContent(respuesta.fecha)}</span>
												</div>
												<div class="panel-body comentario-body">${e:forHtmlContent(respuesta.comment)}</div>
												<!-- /panel-body -->
												<div class="vote">
													<span class="text-puntuar">Puntuar:</span>
													<form method="post" action="${siteUrl}/comentario/puntuarP">
														<input type="hidden" name="${_csrf.parameterName}"
															value="${_csrf.token}" /> <input type="submit"
															class="voto_positivo" title="Positivo"> <input
															type="hidden" value="${e:forHtmlContent(respuesta.id)}"
															name="id">
													</form>
													<form method="post" action="${siteUrl}/comentario/puntuarN">
														<input type="hidden" name="${_csrf.parameterName}"
															value="${_csrf.token}" /> <input type="submit"
															class="voto_negativo" title="Negativo"> <input
															type="hidden" value="${e:forHtmlContent(respuesta.id)}"
															name="id">
													</form>
													<div class="puntuacionRealR">${e:forHtmlContent(puntosCom)}</div>
												</div>
											</div>
										</div>
										<!-- 
										<p class="text-right">
											<a
												onClick="document.getElementById('ocultoRes').style.display='block'"
												class="btn btn-default btn-sm"> Responde</a>
										</p>
										<div id="ocultoRes" class="row" style="display: none">
											<div class="col-sm-8 left">
												<div class="panel panel-default">
													<form method="post" action="${siteUrl}/comentario/responder"
														class="comment-form">
														<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
														<input type="hidden" value="${articulo.id}"
															name="articulo"> <input type="hidden"
															value="${respuesta.id}" name="comentario original">
														<textarea name="comment" class="panel-body"></textarea>
														<input type="submit" value="Responder"
															class="btn btn-default">
													</form>
													<!-- /panel-body -->
										<!--   </div> -->
										<!-- /panel panel-default -->
										<!-- </div>
										</div>
										-->
									</c:forEach>
									<!-- /panel panel-default -->
								</div>
								<!-- /col-sm-5 -->
							</div>
						</c:forEach>
						<!--  Formulario de comentarios -->
						<div class="row">
							<!-- /col-sm-1 -->

							<div class="col-sm-8 left">
								<div class="panel panel-default">
									<form method="post" action="${siteUrl}/comentario/anadir"
										class="comment-form">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" /> <input type="hidden"
											value="${e:forHtmlContent(articulo.id)}" name="id">
										<textarea name="comment" class="panel-body"></textarea>
										<input type="submit" value="Comentar" class="btn btn-default">
									</form>
									<!-- /panel-body -->
								</div>
								<!-- /panel panel-default -->
							</div>
						</div>
						<!--  /Formulario de comentarios -->
					</section>
				</section>
			</c:when>
			<c:otherwise>
				<section class="col-md-7">
					<p>Articulo no encontrado!</p>
				</section>

			</c:otherwise>
		</c:choose>

		<%@ include file="../../jspf/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../../jspf/footer.jspf"%>