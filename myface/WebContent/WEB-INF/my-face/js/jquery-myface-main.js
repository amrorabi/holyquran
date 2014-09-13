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
			sessionStorage.removeItem("accessToken");
			window.location.reload();
		});
		
		homeActions();
		
});