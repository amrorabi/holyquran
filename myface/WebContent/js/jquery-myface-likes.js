var idOfPost = '';
var likeBtnClass = 'fa-heart-o';

function getLikesNames(data){
	var names = "";
	var userId = sessionStorage.getItem("userId");
	$.each(data, function getName(key, value){				
		names += value.name;
		names += "&lt;/br&gt;";
		if(userId == value.id)
			likeBtnClass = 'fa-heart';	
	});
	
	return names;
}

function getLikeButtonClass(isLiked){
	if(isLiked)
		return 'fa-heart';
	else
		return 'fa-heart-o';
}

//function loadLikes(postId){
//	$.ajax({
//		url : "https://graph.facebook.com/v2.1/" + postId + "/likes?access_token=" + sessionStorage.getItem("accessToken"),
//		type : "GET",
//		Accept : 'application/json',
//		contentType : 'application/json',
//		dataType : "json",
//		success : function(data, jqXHR, status) {				
//			var likesNames = getLikesNames(data.data);
//			$('a').find("#" + postId ).text(data.data.length + "")
//		}
//	});
//}

function likeUnlikePost(){
	$(document).on('click', '.like-btn' ,function(event){
		event.preventDefault();
		
		var btn = $(this);
		var url = "";
		var method = "";
		idOfPost = btn.val();
		
		//case like
		if(btn.find('.like-icon').hasClass('fa-heart-o'))
			method = "POST";
		//case unlike
		else
			method = "DELETE";
		
		//send to facebook
		$.ajax({
			url : "https://graph.facebook.com/v2.1/" + idOfPost + "/likes?access_token=" + sessionStorage.getItem("accessToken"),
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