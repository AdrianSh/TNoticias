<%@ include file="../jspf/header.jspf"%>
<!-- Page Content -->
<section class="container">
	<div class="row">
		<%@ include file="../jspf/perfil/column-left.jspf"%>
		<div class="col-md-6">
			<div class="card hovercard">
				<div class="card-background">
					<img class="card-bkimg" alt=""
						src="${e:forUri(user.profileBackground)}">
					<!-- http://lorempixel.com/850/280/people/9/ -->
				</div>
				<div class="useravatar">
					<img alt="" src="${siteUrl}/user/${user.id}/photo">
				</div>
				<div class="card-info">
					<span class="card-title">${e:forHtmlContent(user.name)} ${e:forHtmlContent(user.lname)}</span>

				</div>
			</div>
			<div class="btn-pref btn-group btn-group-justified btn-group-lg"
				role="group" aria-label="...">
				<div class="btn-group" role="group">
					<button type="button" id="stars" class="btn btn-primary"
						href="#tab1" data-toggle="tab">
						<span class="glyphicon glyphicon-star" aria-hidden="true"></span>
						<div class="hidden-xs">Stars</div>
					</button>
				</div>
				<div class="btn-group" role="group">
					<button type="button" id="favorites" class="btn btn-default"
						href="#tab2" data-toggle="tab">
						<span class="glyphicon glyphicon-heart" aria-hidden="true"></span>
						<div class="hidden-xs">Favorites</div>
					</button>
				</div>
				<div class="btn-group" role="group">
					<button type="button" id="following" class="btn btn-default"
						href="#tab3" data-toggle="tab">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
						<div class="hidden-xs">Tu actividad</div>
					</button>
				</div>
			</div>

			<div class="well">
				<div class="tab-content">
					<div class="tab-pane fade in active" id="tab1">
						<h3>This is tab 1</h3>
					</div>
					<div class="tab-pane fade in" id="tab2">
						<h3>This is tab 2</h3>
					</div>
					<div class="tab-pane fade in" id="tab3">
						<c:forEach items="${actividad}" var="a">
							<p>${a.estado} ${a.updatedAt}</p>
						</c:forEach>
					</div>
				</div>
			</div>

		</div>


		<%@ include file="../jspf/perfil/column-right.jspf"%>
	</div>
</section>
<!-- /.container -->

<%@ include file="../jspf/footer.jspf"%>