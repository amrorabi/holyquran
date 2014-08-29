jQuery.ajaxSetup({
 	'complete': function() { 
 		jQuery('abbr.timeago').timeago();		  //initialize timeago plugin
 		$("[data-toggle='tooltip']").tooltip();	  //activate tooltip feature in bootstrap
 	}
});

function loadMyFace(){
	// load user info
	loadUserProfile();			
	
	// load user notifications
	loadUserNotifications();
	
	// load user home
	loadUserHome();
	
	//comments buttons actions
	commentingActions();
	
	//like post method
	likeUnlikePost();
}

$(document).ready(function() {
		
		if(sessionStorage.getItem("accessToken") == null){
			// login & get access token
			login();			
		}
		
		//load all components
		loadMyFace();
		
});