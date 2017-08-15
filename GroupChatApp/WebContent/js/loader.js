function loadMsg() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			// console.log(xhttp.responseText);
			self.postMessage(xhttp.responseText);
		}
	};

	var lastMsgId = -1;
	var token;
	onmessage = function(e) {
		xhttp.open("GET", e.data, true);
		xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		xhttp.send();
	};

	t = setTimeout(function() {
		loadMsg();
	}, 1000);
}
