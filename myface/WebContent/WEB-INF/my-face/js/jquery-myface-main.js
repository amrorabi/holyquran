jQuery.ajaxSetup({
 	'complete': function() { 
 		jQuery('abbr.timeago').timeago();		  //initialize timeago plugin
 		$("[data-toggle='tooltip']").tooltip();	  //activate tooltip feature in bootstrap
 	}
});

function loadMyFace(){
	// load user info
	loadUserProfile();			
	
	// load user notifications (headers)
	loadUserNotifications();
	
	// load user messages (headers)
	loadUserMessages();
	
	// load user home
	loadUserHome();
	
	//comments buttons actions
	commentingActions();
	
	//conversation buttons actions
	conversationActions();
	
	//like post method
	likeUnlikePost();
	
	//share post
	sharePost();
}

$(document).ready(function() {		
		
		if(sessionStorage.getItem("accessToken") != null){		
			//load all components
			loadMyFace();
		}
		else{
			// login & load my face
			login();
		}
		
		//sign out action
		$('#signOutBtn').on('click', function(){			
			var accessToken = sessionStorage.getItem("accessToken");			
			//remove token from session
			sessionStorage.removeItem("accessToken");			
			//logout at facebook
			window.location.replace("https://www.facebook.com/logout.php?next=http://localhost:8080/myface/&access_token=" + accessToken);			
		});
		
		homeActions();
		
});