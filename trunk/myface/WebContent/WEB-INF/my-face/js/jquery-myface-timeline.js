function loadUserHome(){
	$.ajax({
		url : "https://graph.facebook.com/v2.1/me/home?access_token=" + sessionStorage.getItem("accessToken"),
		type : "GET",
		Accept : 'application/json',
		contentType : 'application/json',
		dataType : "json",
		success : function(data, status, jqXHR) {

			var dateLabel = "";
			$.each(data.data, function(key, value) {
				//Creation date
				var creationDate = new Date(value.created_time);
				if(dateLabel != creationDate.toDateString()){
					dateLabel = creationDate.toDateString();
					var formattedDate = creationDate.toLocaleFormat('%d %b %Y'); 
					$('#timeline').append("<li class=\"time-label\"><span class=\"bg-red\"> " + formattedDate + " </span></li>");
				}
				
				var timeLineItem = buildTimeLineItem(value);
				
				//append data
				$('#timeline').append(timeLineItem);
			});
			
			//start shorten plugin ("read more")
//			$(".short-text").each(shorten());

		}
	});

}

function getToNames(data){
	var toNames = "";
	
	$.each(data, function(key, value) {
		toNames += value.name + ", ";
	});
	
	return toNames;
}

function buildTimeLineItem(value){
	if(value.likes != null)
		var likesNames = getLikesNames(value.likes.data);
	
	var itemClass = "fa fa-comments bg-yellow";
	
	if(value.type == "video")
		itemClass = "fa fa-video-camera bg-maroon";
	else if(value.type == "photo")
		itemClass = "fa fa-camera bg-purple";
	
	var timeLineItem = "<li><i class=\"" + itemClass +"\"></i>" +
		"<div class=\"timeline-item\">" +
		
		"<span class=\"time\"><i class=\"fa fa-clock-o\"></i> " +
			"<abbr class=\"timeago\" title=\""+ value.created_time +"\"/>" +
			"&nbsp&nbsp&nbsp&nbsp" +
			" <button class=\"btn btn-sm like-btn\" value=" + value.id + ">" +
			"<i class=\"fa fa-fw " + likeBtnClass + " like-icon\"></i></button>" +
		"</span>" +
		
		"<h3 class=\"timeline-header no-border\">" +
			"<a href=\"https://www.facebook.com/app_scoped_user_id/" + value.from.id + "\">" + value.from.name + "</a>";
	
	if(value.story != null)
		timeLineItem += "&nbsp&nbsp" + value.story.replace(value.from.name, "");
	
	if(value.to != null)
		timeLineItem += "&nbsp&nbsp to &nbsp" + getToNames(value.to.data);

	if(value.caption != null)
		timeLineItem +=	"</br></br><bdi>" + getTextElement(value.caption) + "</bdi></h3>";
	
	if(value.description != null)
		timeLineItem +=	"</br><bdi>" + getTextElement(value.description) + "</bdi></h3>";
	
	if(value.message != null)
		timeLineItem +=	"</br><bdi>" + getTextElement(value.message) + "</bdi></h3>";
	
	if(value.type == "video"){
		timeLineItem +=	"</br></br>" + "<a id=\"" + value.name + "\" href=\"" + value.source + "\" class=\"video-link\">" +
		"<img style=\"border:1;\" src=\"img/video_icon.jpg\" width=\"10%\" height=\"10%\" \>" + "</a>";
	}
	else if(value.type == "link"){
//		timeLineItem +=	"</br></br>" + "<a id=\"" + value.name + "\" href=\"" + value.link + "\" class=\"\" >" +
//		value.name + "</a></br></br>";
		
		timeLineItem +=	"</br></br>" + "<a id=\"" + value.name + "\" href=\"" + value.link + "\" class=\"\" >" +
		"<img style=\"border:0;\" src=\"" + value.picture + "\" width=\"10%\" height=\"10%\" \>" + "</a></br></br>";
	}
	else if(value.type == "photo"){
		timeLineItem +=	"</br></br>" + "<a id=\"" + value.object_id + "\" href=\"#\" class=\"img-link\" >" +
		"<img style=\"border:0;\" src=\"" + value.picture + "\" width=\"10%\" height=\"10%\" \>" + "</a></br></br>";
	}
	
	timeLineItem += 
			" <div class=\"timeline-footer\">";
	
	if(value.likes != null){
		timeLineItem += 
			"<a id="+ value.id +
			"href=\"#\" data-toggle=\"tooltip\" data-placement=\"top\" data-html=\"true\" " +
			"title=\"" + likesNames + "\">" +
            value.likes.data.length + " likes </a>";							
	}
	
	if(value.comments != null){
		timeLineItem += "&nbsp;<a href=\""+ value.id +"\" class=\"comments-link\">" +
                        value.comments.data.length + " comments </a>";
	}
	else{
		timeLineItem += "&nbsp;<a href=\""+ value.id +"\" class=\"comments-link\">write comment</a>";
	}
	
	timeLineItem += "</div>";
	
	timeLineItem +=	"</div></li>";
	
	return timeLineItem;
}

function homeActions(){
		
		 //open friend profile
//		   $(document).on('click', "a.friend-link", function(event){				
//				event.preventDefault();
//				
//				var friendId = $(this).attr("href");
//				
//				$.ajax({
//					url : "https://graph.facebook.com/v2.1/" + friendId + "?fields=link&access_token=" + sessionStorage.getItem("accessToken"),
//					type : "GET",
//					Accept : 'application/json',
//					contentType : 'application/json',
//					dataType : "json",
//					success : function(data, status, jqXHR) {
//						window.open(data.link, '_blank');
//					}
//				});
//		   });
		
	   //open video
	   $(document).on('click', "a.video-link", function(event){				
			event.preventDefault();
			
			var videoSrc = $(this).attr("href");

			if(/^http:\/\/www\.youtube/.test(videoSrc)){
				$("#youtubeSrc").attr("src", videoSrc);			
				$('#youtubeDiv').modal({ keyboard: false });
			}else{
				$("#videoSrc").attr("src", videoSrc);			
				$('#videoDiv').modal({ keyboard: false });
			}
//			$("#videoLabel").html($(this).attr("id"));
	   });
	   
	   //close video
	   $("#videoDiv").on("hidden", function () {
		   $("#videoSrc").pause();
		   $("#videoSrc").currentTime = 0;
	   });
	   
	   //open photo
	   $(document).on('click', "a.img-link", function(event){				
			event.preventDefault();
			
			var photoId = $(this).attr("id");
			$("#photoSrc").attr("src", "");
			
			$.ajax({
				url : "https://graph.facebook.com/v2.1/" + photoId + "?fields=source&access_token=" + sessionStorage.getItem("accessToken"),
				type : "GET",
				Accept : 'application/json',
				contentType : 'application/json',
				dataType : "json",
				success : function(data, status, jqXHR) {
					$("#photoSrc").attr("src", data.source);
					$('#photoDiv').modal({ keyboard: false });
				}
			});
	   });
}