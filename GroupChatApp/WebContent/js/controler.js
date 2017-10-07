$(function() {
	$(this).bind("contextmenu", function(e) {
		e.preventDefault();
	});
	$(window).on('unload', function() {
		if ($('#page-unload-status').val() === 'close')
			$('#logout-btn').click();
	});
	$(window)
			.on(
					'keydown',
					function(e) {
						// F12=123, I=73 (Ctrl+Shift+I), F5=116 ,R=82
						var allowKey = event.keyCode == 123
								|| (event.keyCode == 73 && event.shiftKey && event.ctrlKey)
								|| (event.keyCode == 82 && event.ctrlKey)
								|| event.keyCode == 116;
						if (allowKey) {
							e.preventDefault();
						}
					});
});