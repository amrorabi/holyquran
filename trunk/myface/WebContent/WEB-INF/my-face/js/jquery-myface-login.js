function getParam(queryString, key) 
{
	// Find the key and everything up to the ampersand delimiter
	var value=RegExp("" + key + "[^&]+").exec(queryString);
	
	// Return the unescaped value minus everything starting from the equals sign or an empty string
	return !!value ? decodeURI(value.toString().replace(/^[^=]+./,"")) : null;
}

function login(){

	//check login
	if(sessionStorage.getItem("accessToken") == null){
		
			var code = getParam(window.location.search, "code");
			
			window.history.replaceState("Replace", "My Face", "/myface/");
			
			if(code == null){
				//show login button
				$('#loginDiv').modal({ backdrop: 'static', keyboard: false });
			}
			else{	//Confirming code from facebook
				getAccessToken(code);
			}
	}
	
	//when click login button, redirect to facebook login page
	$('#loginBtn').click(function(){
		window.location.replace(
				"https://www.facebook.com/dialog/oauth?client_id=636370543136997&redirect_uri=http://localhost:8080/myface/" +
				"&scope=manage_notifications,manage_pages,publish_actions,read_stream,user_likes,user_status");
	});
}

function getAccessToken(code){
	$.ajax({
		url : "https://graph.facebook.com/oauth/access_token?client_id=636370543136997&redirect_uri=http://localhost:8080/myface/&client_secret=a097df4a9d03030cbeb03fccc546c654&code="
			  + code,
		type : "GET",
		success : function(data, jqXHR, status) {
			
			var accessToken = getParam(data, "access_token");
			var expires = getParam(data, "expires");
			
			//set timer for token expiring
			if(accessToken != null && expires != null){
				
				sessionStorage.setItem("accessToken", accessToken);
				
				clearTimeout(sessionStorage.getItem("accessTokenExpire"));
				accessTokenExpire = setTimeout(function() {
										accessToken = null;
										accessTokenExpire = null;
									}, expires);
				
				sessionStorage.setItem("accessTokenExpire", accessTokenExpire);
				
				//load all components
				loadMyFace();
			}
			
		}
	});
}