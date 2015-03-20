var convDivHeight = "0";
var userId = sessionStorage.getItem("userId");
var currentConvId = "";			//TODO cannot applied by multi chat windows

function loadUserMessages(){
	$.ajax({
		url : "https://graph.facebook.com/v2.1/me/inbox?fields=unread,to&access_token=" + sessionStorage.getItem("accessToken"),
		type : "GET",
		Accept : 'application/json',
		contentType : 'application/json',
		dataType : "json",
		success : function(data, status, jqXHR) {
			
			$('#msgCount').text(data.summary.unread_count);
			$('#msgHeader').text("You have " + data.summary.unread_count + " messages");
			
			$.each(data.data, function(key, value) {
				
				if(value.unread == 0)
					return;		//do effect of continue
				
				var msgItem = "<li>" +
						"<a href='" + value.id + "' class='msg-item' >" +
							"<h4>" + value.to.data[0].name + "&nbsp&nbsp&nbsp" + value.unread + "<small>" +
//							"<i class='fa fa-clock-o' ></i>" +
//							"<abbr class='timeago' title='"+ value.created_time +"' />" +
							"</small></h4></a> </li>" ;
				
				//Post data
				$('#msgsMenu').append(msgItem);
			});

		}
	});
   
   $('#conversationBox').draggable();
}

function appendMessageItem(value, userId){
	
	var name = value.from.name;
	if(value.from.id = userId)
		name = "me";
		
	var commentItem = "<div class='item'>" +
	"<img src='img/avatar.png' alt='user image' class='online' />" +
	"<p class='message'>" +
		"<a href='#' class='name'>" +
		"<small class='text-muted pull-right'>" +
			"<i class='fa fa-clock-o'></i>" +
			"<abbr class='timeago' title='"+ value.created_time +"'/>" + 
		"</small>" + name +
		"</a> </br>" + value.message +
	"</p><div style='border-bottom: #c0c0c0 solid 1px'/></div>";

	$('#conversationBody').append(commentItem);
}

function openConversation(convId){
	
	currentConvId = convId;
	$('#newMessage').val(null);		
	$('#conversationBody').empty();		//remove old body and all its children
	
	$.ajax({
		url : "https://graph.facebook.com/v2.1/" + convId + "?access_token=" + sessionStorage.getItem("accessToken"),
		type : "GET",
		Accept : 'application/json',
		contentType : 'application/json',
		dataType : "json",
		success : function(data, status, jqXHR) {
			
			$('#conversationHeader').text("Conversation With " + data.to.data[0].name);
			
			$.each(data.comments.data, function(key, value) {
				appendMessageItem(value);				
			});
			
			showConversationWindow();
		}
	});
}

function showConversationWindow(){
	if(convDivHeight != '70%'){
		convDivHeight = '70%';
		$("#conversationBox").css("height", 0);
		$("#conversationBox").show();
		$("#conversationBox").animate({height: convDivHeight}, 700);
	}
}

function conversationActions() {
	
	//set comments close action
	$('#closeConv').on('click', function(c){
		convDivHeight = "0";
		$("#conversationBox").fadeOut(700);
	});
	
	//set comments min/max action			
	$('#minMaxConv').on('click', function(c){
		if(convDivHeight == '2%')
			convDivHeight = '70%';
		else
			convDivHeight = '2%';
		
		$("#conversationBox").animate({height: convDivHeight});
	});
	
   //set conversation body scroll
   $('#conversationBody').slimScroll({
        height: '75%',		//TODO and find how to not affect div height
        start : 'top'
    });
	
   //open conversation action
	$(document).on('click', "a.msg-item", function(event) {
		 var postId = $(this).attr("href");
		 event.preventDefault();
		 openConversation(postId);
	});
   
   
	//add message
	$('#addMessage').on('click', function(event){
		
		$('#newMessage').attr("disabled", true);
		var newMessage = $('#newMessage').val();
		
		if(newMessage == null || newMessage.length == 0)
			return;

		// add comment in facebook
		$.ajax({
			url : "https://graph.facebook.com/v2.1/" + currentConvId + "/comments?access_token=" + sessionStorage.getItem("accessToken"),
			type : "POST",
			Accept : 'application/json',
			contentType : 'application/json',
			dataType : "json",
			data: { 
		        'message': newMessage
		    }
		}).always(function(jqXHR, status) {
				//append message
				var object = {
					created_time : Date(),
					from : {name : "me"},
					message : newMessage
				}
				appendMessageItem(object);
				
				$('#newMessage').attr("disabled", false);
			});
	});
}