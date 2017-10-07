var isWindowActive = true;

if (Notification.permission !== "granted")
	Notification.requestPermission();

$(function() {
	$(window).blur(function() {
		isWindowActive = false;
	});
	$(window).focus(function() {
		isWindowActive = true;
	});
});

function notifyMe(senderName) {
	if (Notification.permission !== "granted")
		Notification.requestPermission();
	else {
		var a = $('#my-name').text();
		a = a.substring(a.lastIndexOf(':') + 1, a.length)
		var notification = new Notification('Hi ' + a + '!', {
			icon : contextPath + '/images/cr.png',
			body : 'You have a new message from ' + senderName + '.',
		});
		$(notification).css('cursor', 'pointer');
		notification.onclick = function() {
			window.focus();
			notification.close();
		};
	}
}