$(function() {
	String.prototype.replaceAll = function(search, replacement) {
		var target = this;
		return target.replace(new RegExp(search, 'g'), replacement);
	};
	$('#msg-in-box').focus();
	getAllUsers();
	loadAllMessages();
	loadAllEmojis();
	$('#msg-in-box').on('keyup', function(e) {
		if (!e.shiftKey) {
			var key = e.keyCode || e.charCode;
			if (key == 13) {
				if ($('#enter-check').is(':checked')) {
					$('#send-btn').submit();
				}
			}
		}
	});
	$('#chat-form').submit(function(e) {
		sendMessage();
		e.preventDefault();
	});
	$("#msg-in-box").bind("paste", function(e) {
		e.preventDefault();
		var pastedData = e.originalEvent.clipboardData.getData('text');
		document.execCommand("insertText", false, pastedData);
		scrollToBottom('msg-in-box');
	});
	$('#logout-btn').click(function(e) {
		logout();
		e.preventDefault();
	});
	var size = [ window.outerWidth, window.outerHeight ];
	$('#err-box').bind("DOMSubtreeModified", function() {
		if ($('#err-box').text() != '') {
			size = [ window.outerWidth, window.outerHeight + 27 ];
			$(window).resize();
			$('#err-box').fadeOut(10000, 'linear', function() {
				$('#err-box').empty();
				$('#err-box').show();
				size = [ window.outerWidth, window.outerHeight - 27 ];
				$(window).resize();
			});
		}
	});
	$('#emoji-list-opener').click(function() {
		var modifier = -50;
		if ($('#emoji-container').is(':hidden')) {
			modifier = -modifier;
		}
		$('#emoji-container').toggle();
		size = [ window.outerWidth, window.outerHeight + modifier ];
		$(window).resize();
	});
	$(window).resize(function() {
		window.resizeTo(size[0], size[1]);
	});

});

function loadAllMessages() {
	var url = contextPath + "/chat/recover";
	var jqXhr = $.ajax({
		type : "GET",
		url : url,
		data : {
			token : $('#token').val(),
			loginId : $('#login-id').val()
		}
	});
	jqXhr
			.done(function(data) {
				if (data !== null && data !== '') {
					if (data !== '-1') {
						messageAppender(data);
						$('#err-box').empty();
					} else {
						$('#err-box')
								.text(
										"Could not load previous messages. Please retry by refreshing!");
					}
				}
			});
}

function getAllUsers() {
	var url = contextPath + "/chat/allusers";
	var jqXhr = $.ajax({
		type : "GET",
		url : url,
		data : {
			token : $('#token').val(),
			loginId : $('#login-id').val()
		}
	});
	jqXhr.done(function(data) {
		if (data !== null && data !== '') {
			if (data === "-1") {
				window.location.replace(contextPath + '/error');
			} else {
				$.each(data, function(i, item) {
					var statusClass = 's-off';
					if(item.online){
						statusClass = 's-on';
					}
					$('#user-list').append(
							'<div class="u-details"><span class="users">'
									+ '<div class="status ' + statusClass + '"></div> '
									+ item.fullname
									+ '</span><input type="hidden" value="'
									+ item.loginId
									+ '" class="users-login-id"></div>');
				});
			}
		}
	});
}
function loadAllEmojis() {
	var url = contextPath + "/chat/emoji";
	var jqXhr = $.ajax({
		type : "GET",
		url : url,
		data : {
			token : $('#token').val(),
			loginId : $('#login-id').val()
		}
	});
	jqXhr
			.done(function(data) {
				if (data !== null && data !== 'false') {
					if (data === "-1") {
						window.location.replace(contextPath + '/error');
					} else {
						$
								.each(
										data,
										function(i, item) {
											var div = $('<div class="emoji-div"></div>');
											$('#emoji-container').append(div);
											var img = $('<img class="emoji" src="data:image/png;base64, '
													+ item.data
													+ '" alt="<<'
													+ item.name
													+ '>>" title="'
													+ item.name + '" />');
											div.append(img);
											img
													.click(function() {
														var newImg = $(this)
																.clone();
														newImg
																.removeAttr('title');
														newImg.css('margin',
																'0 4px');
														$('#msg-in-box')
																.append(newImg);
													});
										});
					}
				}
			});
}