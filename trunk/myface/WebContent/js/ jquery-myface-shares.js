var idOfPost = '';
var likeBtnClass = 'fa-heart-o';

function sharePost(){
	
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
	
	
	//re-share post
	$(document).on('click', '.share-btn' ,function(event){
		event.preventDefault();
		
		var btn = $(this);
		var url = "";
		var method = "";
		idOfPost = btn.val();
		
		window.location.replace(
				"https://www.facebook.com/dialog/share?app_id=636370543136997" +
				"&display=popup" +
				"&caption=An%20example%20caption " +
				"&href=" + +
				"&redirect_uri=http://localhost:8080/myface/");
	});
}