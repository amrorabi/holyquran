var accessToken = "CAACEdEose0cBAMjL3ZCPSj0DjkDlUX1YIATlmTjJ0TSWiUdcfH7PCL1SJJXIeTk5y0BZAq02GrMZBTT2M0ZCXlgY2w3dGI6aK9o27KoCe9EkvfZA0HtmZBI0ludX8jbLuGMXY234Y48klUiN6Bl2zOYTuya0e0QQwUyZBAh7aSb0uKSCv7l8dx1ekHfge3hvDEQSQCZCcm9stVkFjZCUmLy0c";

jQuery.ajaxSetup({
 	'complete': function() { 
 		jQuery('abbr.timeago').timeago();		  //initialize timeago plugin
 		$("[data-toggle='tooltip']").tooltip();	  //activate tooltip feature in bootstrap
 	}
});

function loadUserHome(){
	$.ajax({
		url : "https://graph.facebook.com/v2.1/me/home?access_token=" + accessToken,
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
				
				var likesNames = getLikesNames(value.likes.data);
				
				var timeLineItem = "<li><i class=\"fa fa-user bg-aqua\"></i>" +
					"<div class=\"timeline-item\">" +
					
					"<span class=\"time\"><i class=\"fa fa-clock-o\"></i> " +
						"<abbr class=\"timeago\" title=\""+ value.created_time +"\"/>" +
						"&nbsp&nbsp&nbsp&nbsp" +
						" <button class=\"btn btn-sm like-btn\" value=" + value.id + ">" +
						"<i class=\"fa fa-fw " + likeBtnClass + " like-icon\"></i></button>" +
					"</span>" +
					
					"<h3 class=\"timeline-header no-border\">" +
						"<a href=\"#\">" + value.from.name + "</a> </br>" + value.message +
					"</h3>";
				
				timeLineItem += 
						" <div class=\"timeline-footer\">";
				
				if(value.likes != null){
					timeLineItem += 
						"<a href=\"#\" class=\"likes-link\" data-toggle=\"tooltip\" data-placement=\"top\" data-html=\"true\" " +
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
				
				//Post data
				$('#timeline').append(timeLineItem);
			});

		}
	});

}

$(document).ready(function() {
		// load user home
		loadUserHome();
});