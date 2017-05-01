var popupHeight, windowHeight, scrollTop;
$(document).ready(function(){

    $('.popup_button').click(function(e){
        e.preventDefault();
        var popup = $(this).attr('rel');
        showPopup(popup);
    });

    $('.popup_bg, .popup_close').click(function(e){
        e.preventDefault();
        $('.popup_bg').fadeOut();
        $('.popup_window').fadeOut();
    });


});

$(window).scroll(function(){
    windowHeight = $(window).height();
    scrollTop = $(window).scrollTop();
});

function showPopup(popup){
    $('.popup_bg').fadeIn();
    $('#'+ popup).fadeIn();
    //noinspection JSJQueryEfficiency
    popupHeight = $('#'+ popup).outerHeight();
    windowHeight = $(window).height();
    scrollTop = $(window).scrollTop();

    if(windowHeight < popupHeight){
        $('#'+ popup).css('top',scrollTop);
    }else{
        $('#'+ popup).css('top',(windowHeight - popupHeight) / 2 + scrollTop);
    }
}