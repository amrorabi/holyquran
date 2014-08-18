var accessToken = "CAACEdEose0cBAMjL3ZCPSj0DjkDlUX1YIATlmTjJ0TSWiUdcfH7PCL1SJJXIeTk5y0BZAq02GrMZBTT2M0ZCXlgY2w3dGI6aK9o27KoCe9EkvfZA0HtmZBI0ludX8jbLuGMXY234Y48klUiN6Bl2zOYTuya0e0QQwUyZBAh7aSb0uKSCv7l8dx1ekHfge3hvDEQSQCZCcm9stVkFjZCUmLy0c";

jQuery.ajaxSetup({
 	'complete': function() { jQuery('abbr.timeago').timeago(); }
});

$(document).ready(		
		function() {
			// load user home
			$.ajax({
				url : "https://graph.facebook.com/v2.1/me/home?access_token="
						+ accessToken,
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
						
						//Post data
						$('#timeline').append("<li><i class=\"fa fa-user bg-aqua\"></i>" +
								"<div class=\"timeline-item\">" +
									"<span class=\"time\"><i class=\"fa fa-clock-o\"></i> " +
									"<abbr class=\"timeago\" title='"+ value.created_time +"'>" +
										"</span>" +
									"<h3 class=\"timeline-header no-border\">" +
										"<a href=\"#\">" + value.from.name + "</a> </br>" + value.message +
									"</h3>" +
								"</div></li>");
					});

				}
			})

		}
		
);