var accessToken = "CAACEdEose0cBALi3BO6WZBxsAHMhTwX4EmIE2yWMrHiEuoOQZByE6pcO2GaDF5Glva4fmoZC6BZBMaP76XMKyNMXXWM9W380n49aEXVHfNSxpeaqO4XzMK5zWi6ZCLJ9AkJaX9clwjzSJK7hCFdUJ9fkcPx4sVnP4ZBRuEldj1RhC6CB8ThVNhm32Aw0nX4HKhSiJ298rbVbS3vZCCkdL0W";

$(document)
		.ready(
				function() {

					// load user info
					$
							.ajax({
								url : "https://graph.facebook.com/v2.1/me/picture?redirect=false&access_token="
										+ accessToken,
								type : "GET",
								Accept : 'application/json',
								contentType : 'application/json',
								dataType : "json",
								success : function(data, status, jqXHR) {
									$('#usrImg1').attr('src', data.data.url);
								}
							});

					$
							.ajax({
								url : "https://graph.facebook.com/v2.1/me?access_token="
										+ accessToken,
								type : "GET",
								Accept : 'application/json',
								contentType : 'application/json',
								dataType : "json",
								success : function(data, status, jqXHR) {
									$('#welcomeMsg1').text(
											"Hello, " + data.first_name);
								}
							});

					// Publish data
					$("#statusForm").submit(function(event) {						
						// Stop form from submitting normally
						 event.preventDefault();
						 // Get some values from elements on the page:
						 status = $("#statusText").val();						 
						 url = "https://graph.facebook.com/v2.1/me/feed";
						 // Send the data using post
						 var posting = $.post( url, { message: status ,
						 access_token :accessToken}
						 );
						 // Put the results in a div
						 posting.done(function( data ) {
							 alert("Your status is published successfully");
							 $("#statusText").val("");
						 });
					});
					//////////////

				});