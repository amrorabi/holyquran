var accessToken = "CAACEdEose0cBAMjL3ZCPSj0DjkDlUX1YIATlmTjJ0TSWiUdcfH7PCL1SJJXIeTk5y0BZAq02GrMZBTT2M0ZCXlgY2w3dGI6aK9o27KoCe9EkvfZA0HtmZBI0ludX8jbLuGMXY234Y48klUiN6Bl2zOYTuya0e0QQwUyZBAh7aSb0uKSCv7l8dx1ekHfge3hvDEQSQCZCcm9stVkFjZCUmLy0c";
var userId = "10152313707746156";		//TODO get from profile and set in session
var idOfPost = '';
var likeBtnClass = '';

function getLikesNames(data){
	var names = "";
	$.each(data, function getName(key, value){
		if(userId == value.id)
			likeBtnClass = 'fa-heart';
		else
			likeBtnClass = 'fa-heart-o';
			
		names += value.name;
		names += "</br>";
	});
	
	return names;
}

function loadLikes(postId){
	$.ajax({
		url : "https://graph.facebook.com/v2.1/" + postId + "/likes?access_token=" + accessToken,
		type : method,
		Accept : 'application/json',
		contentType : 'application/json',
		dataType : "json",
		success : function(jqXHR, status) {				
			//change icon
			btn.find('.like-icon').toggleClass('fa-heart');
			btn.find('.like-icon').toggleClass('fa-heart-o');
			
			//reload likes
		}
	});
}

function likeUnlikePost(){
	$(document).on('click', '.like-btn' ,function(){
		var btn = $(this);
		var url = "";
		var method = "";
		idOfPost = btn.val();
		
		//case like
		if(btn.find('.like-icon').find('fa-heart-o') != null){
			url = "https://graph.facebook.com/v2.1/" + idOfPost + "/likes";		//val is post id
			method = "POST";
		}
		//case unlike
		else{
			url =  "https://graph.facebook.com/v2.1/" + idOfPost;			//val is my like id
			method = "DELETE";
		}
		
		//send to facebook
		$.ajax({
			url : url + "?access_token=" + accessToken,
			type : method,
			Accept : 'application/json',
			contentType : 'application/json',
			dataType : "json",
			success : function(jqXHR, status) {				
				//change icon
				btn.find('.like-icon').toggleClass('fa-heart');
				btn.find('.like-icon').toggleClass('fa-heart-o');
				
				//reload likes
			}
		});
	});
}

$(document).ready(function() {			
		//like post method
	likeUnlikePost();
});