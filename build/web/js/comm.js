$(document).ready(function(){
    $("#submit_button").click( function() {
        //$.post( $("#updateunit").attr("action"),
        // $("#updateunit :input").serializeArray(),function(info){

        var success_or_not = $("#submit_button").attr("success_or_not");
        console.log(success_or_not);

        if(success_or_not==0){
            $("#result").html('<div class="alert alert-success"><button type="button" class="close">×</button>Success!</div>');
        }else if(success_or_not==-1){
            $("#result").html('<div class="alert alert-danger"><button type="button" class="close">×</button>Error! </div>');
        }

        window.setTimeout(function() {
            $(".alert").fadeTo(500, 0).slideUp(500, function(){
                $(this).remove();
            });
        }, 5000);
        $('.alert .close').on("click", function(e){
            $(this).parent().fadeTo(500, 0).slideUp(500);
        });
        //});
    });

    $("#updateunit").submit( function() {
        return false;
    });
});
function clearInput() {
    $("#updateunit :input").each( function() {
        $(this).val('');
    });
}