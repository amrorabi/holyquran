function loadUserProfile(){
		$
		.ajax({
			url : "https://graph.facebook.com/v2.1/me/picture?redirect=false&access_token="
					+ sessionStorage.getItem("accessToken"),
			type : "GET",
			Accept : 'application/json',
			contentType : 'application/json',
			dataType : "json",
			success : function(data, status, jqXHR) {
				$('#usrImg1').attr('src', data.data.url);
				$('#profileImg').attr('src', data.data.url);
			}
		});
	
		$.ajax({
				url : "https://graph.facebook.com/v2.1/me?access_token="
						+ sessionStorage.getItem("accessToken"),
				type : "GET",
				Accept : 'application/json',
				contentType : 'application/json',
				dataType : "json",
				success : function(data, status, jqXHR) {
					$('#welcomeMsg1').text(
							"Hello, " + data.first_name);
					$('#profileUserName').html(data.name + " <i class=\"caret\"></i>");
					$('#aboutUser').text(data.bio);
					
					sessionStorage.setItem("userId", data.id);
				}
		});
}