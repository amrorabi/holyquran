var config = {
		showChars : 300,
		threshold: 300,
		ellipsesText : "...",
		moreText : "more",
		lessText : "less",
		errMsg : null,
		force : false		
	};


$(document).ready(function($) {
	$(document).on({
		click : function() {
			var $this = $(this);
			if ($this.hasClass('less')) {
				$this.removeClass('less');
				$this.html(config.moreText);
				$this.parent().prev().prev().show(); // shortcontent
				$this.parent().prev().hide(); // allcontent
			} else {
				$this.addClass('less');
				$this.html(config.lessText);
				$this.parent().prev().prev().hide(); // shortcontent
				$this.parent().prev().show(); // allcontent
			}
			return false;
		}
	}, '.morelink');
});

function getTextElement(content) {
	
	var contentlen = content.length;
	
	if ((contentlen - config.showChars) > config.threshold) {
		
		var c = content.substr(0, config.showChars) + config.ellipsesText;
		return '<div class="shortcontent">' + c
				+ '</div><div class="allcontent" style="display: none; margin-bottom:0">'
				+ content
				+ '</div><span><a href="javascript://nop/" class="morelink">'
				+ config.moreText + '</a></span>';
	}
	else{
		return content;
	}
}