function getParam(queryString, key) 
{
	// Find the key and everything up to the ampersand delimiter
	var value=RegExp("" + key + "[^&]+").exec(queryString);
	
	// Return the unescaped value minus everything starting from the equals sign or an empty string
	return !!value ? decodeURI(value.toString().replace(/^[^=]+./,"")) : null;
}

function login(){

	//check login
	if(accessToken == null){
		
			var code = getParam(window.location.search, "code");
			
			if(code == null){
				//show login button
				$('#loginDiv')
					.modal({ backdrop: 'static', keyboard: false })
			        .one('click', '[data-value]', function (e) {
			            if($(this).data('value')) {
			                alert('confirmed');
			            } else {
			                alert('canceled');
			            }
		        });
			}
			else{	//Confirming code from facebook
				getAccessToken(code);
			}
	}
	
	//when click login button, redirect to facebook login page
	$('#loginBtn').click(function(){
		window.location.replace(
				"https://www.facebook.com/dialog/oauth?client_id=636370543136997&redirect_uri=http://localhost:8080/myface/" +
				"&scope=manage_notifications,manage_pages,publish_actions,read_stream");
	});

}

function getAccessToken(code){
	$.ajax({
		url : "https://graph.facebook.com/oauth/access_token?client_id=636370543136997&redirect_uri=http://localhost:8080/myface/&client_secret=a097df4a9d03030cbeb03fccc546c654&code="
			  + code,
		type : "GET",
		success : function(data, jqXHR, status) {
			
			accessToken = getParam(data, "access_token");
			var expires = getParam(data, "expires");
			
			//set timer for token expiring
			if(accessToken != null && expires != null){
				clearTimeout(accessTokenExpire);
				accessTokenExpire = setTimeout(function() {
										accessToken = null;
										accessTokenExpire = null;
									}, expires);
				
				//load all components
				loadMyFace();
			}
			
		}
	});
}