var linkOfPost = '';
var likeBtnClass = 'fa-heart-o';
var url = "https://graph.facebook.com/v2.1/me/feed";

function reShareActions(){
	
	//open re-share window
	$(document).on('click', '.share-btn', function(event){
		event.preventDefault();
		
		var btn = $(this);
		var method = "";
		linkOfPost = btn.val();
		
		window.open(
				"https://www.facebook.com/dialog/share?app_id=636370543136997" +
				"&display=popup" +
				"&caption=myFace" +
				"&href=" + linkOfPost +
				"&redirect_uri=http://localhost:8080/myface/after_reshare.html", "Redirected From Myface",
				"height=500,width=500,scorllbars=true");

//		$('#reshareDialog').modal({ keyboard: false });

	});
	
	// Submit post
	$("#statusForm").submit(function(event) {
		// Stop form from submitting normally
		 event.preventDefault();
		 
		 $('#statusText').attr("disabled", true);
		 
		 // Get some values from elements on the page:
		 status = $("#statusText").val();						 
		 
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