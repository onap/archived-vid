var feedbackText, pageAddress;
(function ( $ ) {
	$(document).on('click', '.feedbackButtonDiv', function (e) {
		$(".feedbackSection").slideToggle(600, function(){
			if($(".feedbackButton").hasClass("closeFeedbackButton")) {
				$(".footerContainer .feedbackButton").html("Feedback<span class='icon-chevron-down'></span>");
				$(".footerContainer .feedbackButton").removeClass("closeFeedbackButton");
			} else {
				$(".footerContainer .feedbackButton").html("Close<span class='icon-chevron-up'></span>");
				$(".footerContainer .feedbackButton").addClass("closeFeedbackButton");
				$(".footerContainer  .feedbackText").focus();
			}

		});
	});

	$(document).on('click', '.closeFeedback', function (e) {
		$(".feedbackSection").slideUp(600, function(){

		});
	});

	$(document).on('click keypress keyup blur paste','textarea[maxlength]', function(e) {
		var maxlength = $(this).attr('maxlength');
		var val = $(this).val();

		if (val.length > maxlength) {
			$(this).val(val.slice(0, maxlength));
		}
	});

	$(document).on('click', '.feedbackSubmitButton', function (e) {
		var feedbackTextTemp = $(".feedbackText").val();
		feedbackText = $.trim(feedbackTextTemp);
		var feedbackData = feedbackText;
		if (feedbackText != null && feedbackText != '') {
			$.ajax({
				type: "post",
				contentType :'application/json',
				dataType: 'json',
				data: feedbackData,
				url: "/ebiz/dashboard/feedback/createfeedback",
				success: function (data) {
					if (data.success) {
						$(".feedbackSection").slideUp(600, function() {
							$(".feedbackText").val("");
							$(".footerContainer .feedbackButton").html("Feedback<span class='icon-chevron-down'></span>");
							$(".footerContainer .feedbackButton").removeClass("closeFeedbackButton");
							$(".feedbackButtonDiv").slideUp(600);
							$(".readFeedBackMessage").text(data.feedBackMessage);
							$(".feedbackResultMsg, #feedbackResultDivider").slideDown(600);
							$(".readFeedBackMessage").focus();
						});
					} else if (typeof console == "object") {
							
						}
				},
				error: function () {
					
				}
			});
		} else {
			e.preventDefault();
			if (typeof console == 'object') {
				console.log("Invalid Input String");
			}
		}

	});
	
	// Click function definition for OK button in feedback success message
	$(document).on('click', '.feedbackMsgOKButton', function(e) {
		$(".feedbackResultMsg, #feedbackResultDivider").slideUp(600, function(){
			$(".readFeedBackMessage").text("");
		});
		$(".feedbackButtonDiv").slideDown(600, function() {
			$('.feedbackButton').focus();	
		});
	});
	
}( jQuery ));	

$(function() {
	var footerHeight = 0,
	footerTop = 0,
	$footer = $("#footer");

	positionFooter();


 function positionFooter() {

        footerHeight = $footer.height();
        footerTop = ($(window).scrollTop() + $(window).height() - footerHeight) + "px";

        if (($(document.body).height() + footerHeight) < $(window).height()) {
            $footer.addClass('stickyFooter')
                .animate({
                    top: footerTop
                }, -1)
        } else {
            $footer.removeClass('stickyFooter');
        }

    }	

	$(window)
	.scroll(positionFooter)
	.resize(positionFooter)
});