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
	
		// Submit post
		$("#statusForm").submit(function(event) {
			// Stop form from submitting normally
			 event.preventDefault();
			 
			 $('#statusText').attr("disabled", true);
			 
			 // Get some values from elements on the page:
			 status = $("#statusText").val();						 
			 url = "https://graph.facebook.com/v2.1/me/feed";
			 // Send the data using post
			 var posting = $.post( url, { message: status ,
			 access_token :sessionStorage.getItem("accessToken")});
		 
			 // Put the results in a div
			 posting.done(function( data ) {
				 alert("Your status is published successfully");
				 $("#statusText").val("");
				 $('#statusText').attr("disabled", false);
			 });
		});
}