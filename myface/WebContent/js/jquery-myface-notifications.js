function loadUserNotifications(){
	$.ajax({
		url : "https://graph.facebook.com/v2.1/me/notifications?access_token=" + sessionStorage.getItem("accessToken"),
		type : "GET",
		Accept : 'application/json',
		contentType : 'application/json',
		dataType : "json",
		success : function(data, status, jqXHR) {
			
			$('#notifyCount').text(data.data.length);
			$('#notifyHeader').text("You have " + data.data.length + " notifications");
			
			$.each(data.data, function(key, value) {
				
				var id = "";
				if(value.object != null)
					id = value.object.id;
				else
					id = value.link;
				
				var notificationItem = "<li>" +
						"<a href='" + id + "' class='notify-item' >" +
//						"<i class='fa fa-clock-o' ></i>" +
//						"<abbr class='timeago' title='"+ value.created_time +"' />" +
							"<i class='ion ion-ios7-people info' />" + value.title +							
							"</a> </li>" ;
				
				//Post data
				$('#notifyMenu').append(notificationItem);
			});

		}
	});
	
	//open notificatin event
	 $(document).on('click', "a.notify-item", function(event) { 
		 
		 var postId = $(this).attr("href");
		 
		 if( ! (/^http/i.test(postId)) ){
			 event.preventDefault();
			 openNotifyItem(postId);
		 }
		 
	});
	
}

function openNotifyItem(postId){
	$.ajax({
		url : "https://graph.facebook.com/v2.1/" + postId + "?access_token=" + sessionStorage.getItem("accessToken"),
		type : "GET",
		Accept : 'application/json',
		contentType : 'application/json',
		dataType : "json",
		success : function(data, status, jqXHR) {			
			
			var notifyItem = buildTimeLineItem(data, "");
			$('#notifyBody').empty();				//remove old body and all its children
			$('#notifyBody').append(notifyItem);
			
			$('#notifyDiv').modal({ keyboard: false });
		}
	});
}