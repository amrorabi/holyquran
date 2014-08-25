var commentsDivHeight = "0";
var currentPostId = "";
var commentsCount = 0;

function loadComments(){
		
		$('#newComment').val(null);		
		$('#commentsBody').empty();		//remove old body and all its children		
		
		//load specific post comments
		// load user home
		$.ajax({
			url : "https://graph.facebook.com/v2.1/" + currentPostId + "/comments?access_token=" + accessToken,
			type : "GET",
			Accept : 'application/json',
			contentType : 'application/json',
			dataType : "json",
			success : function(data, status, jqXHR) {
				
				commentsCount = data.data.length;
//				$('#commentsHeader').after("<div id=\"commentsBody\" style=\"overflow: hidden; width: auto; height: 250px;\" class=\"box-body chat\"/>");		//add new body
				
				$.each(data.data, function(key, value) {
					
					var commentItem = "<div class=\"item\">" +
						"<img src=\"img/avatar.png\" alt=\"user image\" class=\"online\" />" +
						"<p class=\"message\">" +
							"<a href=\"#\" class=\"name\">" +
							"<small class=\"text-muted pull-right\">" +
								"<i class=\"fa fa-clock-o\"></i>" +
								"<abbr class=\"timeago\" title=\""+ value.created_time +"\"/>";
					
					if(value.from.id == userId){
						commentItem += "&nbsp&nbsp&nbsp&nbsp" +
							" <button class=\"delete-cmt-btn\" value=" + value.id + ">" +
							"x</button>";
					}
						
					commentItem += 
							"</small>" + value.from.name +
							"</a> </br>" + value.message +
						"</p>";
					
					if(value.like_count != null && value.like_count > 0){
						commentItem += 
							" <div class=\"timeline-footer\">" +
							"<a href=\"#\" data-toggle=\"tooltip\" data-placement=\"top\" data-html=\"true\">" +
	                        value.like_count + " likes </a>" +
	                        "</div>";							
					}
					
					commentItem +=	"<div style=\"border-bottom: #c0c0c0 solid 1px\"/></div>";
					
					//Post data
					$('#commentsBody').append(commentItem);
				});
			}
		});
}

$(document).ready(
		function() {
			
			//set comments close action
			$('#closeCmt').on('click', function(c){
				commentsDivHeight = "0";
				$("#comments").fadeOut(700);
			});
			
			//set comments min/max action			
			$('#minMaxCmt').on('click', function(c){
				if(commentsDivHeight == '5%')
					commentsDivHeight = '70%';
				else
					commentsDivHeight = '5%';
				
				$("#comments").animate({height: commentsDivHeight});
			});
			
//			$("#commentsHeader").on('click', function(c){
//				if(commentsDivHeight == '7%'){
//					commentsDivHeight = '70%';				
//					$("#comments").animate({height: commentsDivHeight});
//				}
//			});
			
		   //set comments body scroll
		   $('#commentsBody').slimScroll({
		        height: '75%',		//TODO and find how to not affect div height
		        start : 'top'
		    });
			
			//open comments action
		   $(document).on('click', "a.comments-link", function(event){				
				event.preventDefault();
				
				commentCountLink = $(this);
				currentPostId = $(this).attr("href");
				
				loadComments();
				
				if(commentsDivHeight != '70%'){
					commentsDivHeight = '70%';
					$("#comments").css("height", 0);
					$("#comments").show();
					$("#comments").animate({height: commentsDivHeight}, 700);
				}
		   });
		   
		   
			//add comment
			$('#addComment').on('click', function(event){
				
				var newComment = $('#newComment').val();
				
				if(newComment == null || newComment.length == 0)
					return;

				// add comment in facebook
				$.ajax({
					url : "https://graph.facebook.com/v2.1/" + currentPostId + "/comments?access_token=" + accessToken,
					type : "POST",
					Accept : 'application/json',
					contentType : 'application/json',
					dataType : "json",
					data: { 
				        'message': newComment
				    }
				}).always(function(jqXHR, status) {
						//reload comments
						loadComments();
						var count = commentsCount + 1;
						commentCountLink.text(count + " comments");
					});
			});
			
			//delete comment			
			$(document).on('click', 'button.delete-cmt-btn', function(event){
				
				var commentId = $(this).val();

				// add comment in facebook
				$.ajax({
					url : "https://graph.facebook.com/v2.1/" + commentId + "?access_token=" + accessToken,
					type : "DELETE",
					Accept : 'application/json',
					contentType : 'application/json',
					dataType : "json"
				}).always(function(jqXHR, status) {
						//reload comments
						loadComments();
						var count = commentsCount - 1;
						commentCountLink.text(count + " comments");
					});
			});
});		
