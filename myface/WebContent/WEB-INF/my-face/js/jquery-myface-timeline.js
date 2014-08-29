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
				
				if(value.likes != null)
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
				
				//Post data
				$('#timeline').append(timeLineItem);
			});

		}
	});

}