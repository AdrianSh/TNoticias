/*
 *  Edicion de post
 */
try {
	var selecciones = [];
	var seleccionActiva = false;

	$(function() {
		$(document.body).bind('mouseup', function(e) {
			var selection;

			if (window.getSelection) {
				selection = window.getSelection();
			} else if (document.selection) {
				selection = document.selection.createRange();
			}

			// selection.toString() !== '' && console.log('"' +
			// selection.toString() + '" was selected at ' + e.pageX + '/' +
			// e.pageY);

			if (!exist(selection.toString()) && seleccionActiva) {
				selecciones.push({
					'pageX' : e.pageX,
					'pageY' : e.pageY,
					'seleccion' : selection.toString()
				});
				updateSeleccion();
			}
		});
	});
	function updateSeleccion() {
		$(".seleccion").html = '';
		$.each(selecciones, function(key, value) {
			if (this['seleccion'] != '') {
				var temporal = $(".seleccion").val(); // Obtener el valor
				// actual
				$(".seleccion").val(
						temporal + '"...' + this['seleccion'] + '..."');
				$(".wysibb-text-editor").focus();
				$("#articuloContenedor").focus();
			}
		});
		selecciones = [];
		changeseleccionActiva();
	}
	function deleteSeleccion(valuestring) {
		/*
		 * valuestring = atob(valuestring); $.each(selecciones, function(key,
		 * value) { if (this['seleccion'] == valuestring) this['seleccion'] =
		 * ''; }); updateSeleccion();
		 */
	}
	function exist(valuestring) {
		encontrado = false;
		$.each(selecciones, function(key, value) {
			if (this['seleccion'] == valuestring) {
				encontrado = true;
			}
		});
		return encontrado;
	}

	function changeseleccionActiva() {

		if (seleccionActiva)
			seleccionActiva = false;
		else
			seleccionActiva = true;
	}
} catch (err) {
	console.log(err.message);
}

/*
 * Basico
 */

$(document).ready(function() {
	$(".btn-pref .btn").click(function() {
		$(".btn-pref .btn").removeClass("btn-primary").addClass("btn-default");
		// $(".tab").addClass("active"); // instead of this do the below
		$(this).removeClass("btn-default").addClass("btn-primary");
	});
});

$(function() {
	$('.button-checkbox')
			.each(
					function() {

						// Settings
						var $widget = $(this), $button = $widget.find('button'), $checkbox = $widget
								.find('input:checkbox'), color = $button
								.data('color'), settings = {
							on : {
								icon : 'glyphicon glyphicon-check'
							},
							off : {
								icon : 'glyphicon glyphicon-unchecked'
							}
						};

						// Event Handlers
						$button.on('click',
								function() {
									$checkbox.prop('checked', !$checkbox
											.is(':checked'));
									$checkbox.triggerHandler('change');
									updateDisplay();
								});
						$checkbox.on('change', function() {
							updateDisplay();
						});

						// Actions
						function updateDisplay() {
							var isChecked = $checkbox.is(':checked');

							// Set the button's state
							$button.data('state', (isChecked) ? "on" : "off");

							// Set the button's icon
							$button
									.find('.state-icon')
									.removeClass()
									.addClass(
											'state-icon '
													+ settings[$button
															.data('state')].icon);

							// Update the button's color
							if (isChecked) {
								$button.removeClass('btn-default').addClass(
										'btn-' + color + ' active');
							} else {
								$button.removeClass('btn-' + color + ' active')
										.addClass('btn-default');
							}
						}

						// Initialization
						function init() {

							updateDisplay();

							// Inject the icon if applicable
							if ($button.find('.state-icon').length == 0) {
								$button.prepend('<i class="state-icon '
										+ settings[$button.data('state')].icon
										+ '"></i>');
							}
						}
						init();
					});
});

$("input.label_better").label_better({
	position : "top", // This will let you define the position where the label
	// will appear when the user clicked on the input
	// fields. Acceptable options are "top", "bottom",
	// "left" and "right". Default value is "top".
	animationTime : 250, // This will let you control the animation speed
	// when the label appear. This option accepts value
	// in milliseconds. The default value is 500.
	easing : "ease-in-out", // This option will let you define the CSS easing
	// you would like to see animating the label. The
	// option accepts all default CSS easing such as
	// "linear", "ease" etc. Another extra option is you
	// can use is "bounce". The default value is
	// "ease-in-out".
	offset : 0, // You can add more spacing between the input and the label.
	// This option accepts value in pixels (without the unit). The
	// default value is 20.
	hidePlaceholderOnFocus : true
// The default placeholder text will hide on focus
});

function choose(e) {
	$("#table1").hide();
	$("#selection1").show();
	$("#selection1 .panel-body").html($(e).find("p"));
}

/*
 * BBcode decode :B
 */
$
		.each(
				$("section"),
				function(key, value) {
					$str = $(this).html();

					$format_search = [ /\[b\](.*?)\[\/b\]/ig,
							/\[i\](.*?)\[\/i\]/ig, /\[u\](.*?)\[\/u\]/ig,
							/\[s\](.*?)\[\/s\]/ig,
							/\[center\](.*?)\[\/center\]/ig,
							/\[sup\](.*?)\[\/sup\]/ig,
							/\[sub\](.*?)\[\/sub\]/ig,
							/\[list\](.*?)\[\/list\]/ig, /\[*\](.*?)\[\/*\]/ig,
							/\[list=1\](.*?)\[\/list\]/ig,
							/\[img\](.*?)\[\/img\]/ig,
							/\[video\](.*?)\[\/video\]/ig, ,
							/\[url=(.*?)](.*?)\[\/url\]/ig,
							/\[color=(.*?)\](.*?)\[\/color\]/ig,
							/\[size=(.*?)\](.*?)\[\/size\]/ig,
							/\[left\](.*?)\[\/left\]/ig,
							/\[right\](.*?)\[\/right\]/ig,
							/\[quote\](.*?)\[\/quote\]/ig,
							/\[table\](.*?)\[\/table\]/ig,
							/\[tr\](.*?)\[\/tr\]/ig, /\[td\](.*?)\[\/td\]/ig,
							/\[thead\](.*?)\[\/thead\]/ig ];

					$format_replace = [
							'<strong>$1</strong>',
							'<em>$1</em>',
							'<span style="text-decoration: underline;">$1</span>',
							'<strike>$1</strike>',
							'<p style="text-align:center">$1</p>',
							'<sup>$1</sup>',
							'<sub>$1</sub>',
							'<ul>$1</ul>',
							'<li>$1</li>',
							'<ol>$1</ol>',
							'<img src="$1">',
							'<iframe src="http://www.youtube.com/embed/$1" width="640" height="480" frameborder="0"></iframe>',
							'<a href="$1">$2</a>',
							'<font color="$1">$2</font>',
							'<font size="$1">$2</font>',
							'<p style="text-align:left">$1</p>',
							'<p style="text-align:right">$1</p>',
							'<blockquote>$1</blockquote>', '<table>$1</table>',
							'<tr>$1</tr>', '<td>$1</td>', '<thead>$1</thead>' ];

					for (var i = 0; i < $format_search.length; i++) {
						$str = $str.replace($format_search[i],
								$format_replace[i]);
					}
					$(this).html($str);
				});



var favLinks = $(".opciones a[href$='favorito']");
var puntPLinks = $(".opciones a[href$='puntuarP']");
var puntNLinks = $(".opciones a[href$='puntuarN']");


favLinks.each(function(i){	
	$(this).click(function(e){
		e.preventDefault();
		let ahref = e.target.parentElement.href;
		var artId = ahref.substring(ahref.lastIndexOf("articulo") + 9, ahref.lastIndexOf("favorito") - 1);
		var csrf = $("input[name='_csrf']")[0].value;
		$.post( "/articulo/favorito", { idArticulo: artId, _csrf: csrf},  function(){
			$(".opciones a[href$='articulo/" + artId + "/favorito']").each(function(i){
				this.style.color = this.style.color == "red" ? '' : 'red';
			});
		});

	});
});

puntPLinks.each(function(i){	
	$(this).click(function(e){
		e.preventDefault();
		let ahref = e.target.parentElement.href;
		var artId = ahref.substring(ahref.lastIndexOf("articulo") + 9, ahref.lastIndexOf("puntuarP") - 1);
		var csrf = $("input[name='_csrf']")[0].value;
		$.post( "/articulo/puntuarP", { id: artId, _csrf: csrf},  function(){
			$(".opciones a[href$='articulo/" + artId + "/puntuarP']").each(function(i){
				this.style.color = 'blue';
				$(".opciones a[href$='articulo/" + artId + "/puntuarN']").each(function(i){
					this.style.color = '';
				});
			});
		});

	});
});

puntNLinks.each(function(i){	
	$(this).click(function(e){
		e.preventDefault();
		let ahref = e.target.parentElement.href;
		var artId = ahref.substring(ahref.lastIndexOf("articulo") + 9, ahref.lastIndexOf("puntuarN") - 1);
		var csrf = $("input[name='_csrf']")[0].value;
		$.post( "/articulo/puntuarN", { id: artId, _csrf: csrf},  function(){
			$(".opciones a[href$='articulo/" + artId + "/puntuarN']").each(function(i){
				this.style.color = 'blue';
				$(".opciones a[href$='articulo/" + artId + "/puntuarP']").each(function(i){
					this.style.color = '';
				});
			});
		});

	});
});