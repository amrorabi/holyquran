function loadUserNotifications(){
	$.ajax({
		url : "https://graph.facebook.com/v2.1/me/notifications?access_token=" + accessToken,
		type : "GET",
		Accept : 'application/json',
		contentType : 'application/json',
		dataType : "json",
		success : function(data, status, jqXHR) {
			
			$('#notifyCount').text(data.data.length);
			$('#notifyHeader').text("You have " + data.data.length + " notifications");
			
			$.each(data.data, function(key, value) {
				
				var notificationItem = "<li>" +
						"<a href=\"#\">" +
//						"<i class=\"fa fa-clock-o\"></i>" +
//						"<abbr class=\"timeago\" title=\""+ value.created_time +"\"/>" +
							"<i class=\"ion ion-ios7-people info\" />" + value.title +							
							"</a> </li>" ;
				
				//Post data
				$('#notifyMenu').append(notificationItem);
			});

		}
	});

}