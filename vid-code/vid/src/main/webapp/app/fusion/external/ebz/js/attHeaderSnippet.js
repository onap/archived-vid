/* Popup Box function */
(function(){

  $.fn.popbox = function(options){
    settings = $.extend({
      selector          : this.selector,
      open              : '.openpopbox',
      box               : '.box1',
      arrow             : '.arrow',
      arrow_border      : '.arrow-border',
      close             : '.close'
    }, options);

    var methods = {
      open: function(event){
        event.preventDefault();
        var pop = $(this);
        var box = $(this).parent().find(settings['box']);
        box.find(settings['arrow']).css({'left': box.width()/2 - 10 + 46});
        box.find(settings['arrow_border']).css({'left': box.width()/2 - 10 - 63});
        if(box.css('display') == 'block'){
          /* methods.close();
          setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
        // $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
            if($(window).scrollTop() > 20){
                 setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
                  // $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
            };*/
			 $('.box1').fadeIn('slow');
        } else {
           $('.chatBox').fadeOut('fast');
          $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
        	if(box.attr('target') == 'auth'){
        		box.find(settings['arrow']).css({'left': box.width()/2 + 36});
        		box.css({'display': 'block', 'top': 0, 'left': ((pop.parent().parent().width()/2) -box.width()/2 ) - 90});
        	}
        	else{
        		box.find(settings['arrow']).css({'left': box.width()/2 - 42});
        		box.css({'display': 'block', 'top': 0, 'left': ((pop.parent().parent().width()/2) -box.width()/2-15 )});
        		box.find("#header_login_id").focus();
        		box.find('#header_password').val('');
        		}
        }
      },

      close: function(){
        $(settings['box']).fadeOut("fast");
      }
    };
    
    $(document).bind('keyup', function(event){
      if(event.keyCode == 27){
        setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
        // $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
        if($(window).scrollTop() > 20){
           // setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
          $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
        };
        methods.close();
      }
    });
 // Close chat-box and popup on clicking out of chat-box
    $(document).bind('click', function(event){
      if(!($(event.target).closest('.chatBox').length || $(event.target).hasClass('chatIcon') || $(event.target).closest(settings['selector']).length)){
        methods.close();
         if ($("#actionsDropDown").css('z-index')=='-999'){
           setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
        }
        if($(window).scrollTop() > 20){
           setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
           // $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
        };
       
          $('.chatBox').fadeOut('fast');
        
      }
    });

    return this.each(function(){

      $(this).css({'width': $(settings['box']).width()}); // Width needs to be set otherwise popbox will not move when window resized.
      $(settings['open'], this).bind('click', methods.open);
      $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
      $(settings['open'], this).parent().find(settings['close']).bind('click', function(event){
        // $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
        setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
        methods.close();
      });
    });
  }

}).call(this);


$('.popbox').popbox();

$(function(){
	var displayName = "";
	if($("#reg-fn-ln-id").length > 0){
		var fnln = $("#reg-fn-ln-id").val().length; 
		if(fnln > 0){
			var fnVar = $("#reg-fname-id").val();
			var lnVar = $("#reg-lname-id").val();
			var numChars = 13;
			var nameLen = fnVar.length + lnVar.length + 1; // 1 for space between the first name and the last name
			if(fnVar != " " && lnVar != " "){
				if(nameLen <= numChars){
					displayName = fnVar+" "+lnVar;
				}else if(nameLen <= (numChars * 2)){
						displayName = fnVar+"<br/>"+lnVar;
					}else
						{
							if(fnVar.length < numChars && lnVar.length > numChars){ //Smaller first name and longer last name							
								displayName = fnVar+"<br/>"+lnVar.substring(0,numChars-1)+"...";
							}
							
							if(fnVar.length >= numChars ){ //Longer first name
								// 2 = 1 for elipses, 1 for space between the first name and last name
								longLName = numChars - ((fnVar.substring(numChars,fnVar.length)).length + 2); 
								displayName = fnVar.substring(0,numChars)+"<br/>"+fnVar.substring(numChars,fnVar.length)+" "+lnVar.substring(0,longLName)+"...";
							}
						}		
			}
			else{
				displayName = fnVar+" "+lnVar;
			}
		}	
		
		$("#reg-userName").html(displayName);
	}
});

/* ClickToChat ANONYMOUS FUNCTION DEFINITION       *
 * =============================================== *
 * On clicking the chat icon displays the chat box */
(function(){

  var chatbox = $('.chatBox'); 

  // var actionsDropDown = $('#actionsDropDown');
  var methods = {
    open: function(){chatbox.css('display','block');},
    close: function(){chatbox.fadeOut('fast');}
  };
  $('.chatIcon').click(function(){
    if (chatbox.css('display') == 'block'){
      methods.close();
      setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
       // $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
      if($(window).scrollTop() > 20){
           setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
            // $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
      };
    } else {
       $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
      methods.open();
      $(settings['box']).fadeOut("fast");
    }
  });

  // Close chat-box on clicking cross icon
  $('.circle_close_chat').click(function(){
    // $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
    setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
    methods.close();
    if($(window).scrollTop() > 20){
       setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
       // $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
    };
  });

  // Close chat-box on Escape key press
  $(document).bind('keyup', function(event){
    if(event.keyCode == 27){ 
      setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','999'); },200);
      methods.close();
      if($(window).scrollTop() > 20){
           setTimeout(function(){ $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999'); },200);
           //$(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
      };
    }
  });

  // Close chat-box on clicking out of chat-box
  // $(document).bind('click', function(event){

  //   console.log(event.target,"2");
  //   if(!($(event.target).closest('.chatBox').length || $(event.target).hasClass('chatIcon'))) {
  //     event.preventDefault();
  //   // console.log($(event.target).closest(settings['selector']).length);
  //   console.log($(event.target).closest('.	').length);
  //   console.log($(event.target).hasClass('chatIcon'));


  //     // $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
  //     methods.close();
  //     if ($("#actionsDropDown").css('z-index')=='-999') {
  //       $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
  //     };
  //     // if (flag!="clicked") {
  //     //   if ($('.box').css('display') == 'block' || $('.chatBox').css('display') == 'block' ){
  //     //     $(".btn-panel-vertical,#actionsDropDown").css('z-index','-999');
  //     //   } else {
  //     //     $(".btn-panel-vertical,#actionsDropDown").css('z-index','999');
  //     //   }
  //     // }
  //    }
  // });

})();