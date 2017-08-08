function loadUsers() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			// console.log(xhttp.responseText);
			self.postMessage(xhttp.responseText);
		}
	};

	var token;
	onmessage = function(e) {
		xhttp.open("GET", e.data, true);
		xhttp.send();
	}

	t = setTimeout(function() {
		loadUsers();
	}, 3000);
}
