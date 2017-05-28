<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Sign In</title>

    <link rel="stylesheet" href="css/saipal.css" type="text/css"/>
    <link href="css/bootstrap.min.css" rel="stylesheet">
  

</head>
<body style="background-color: #5D92BA;">

<div class="login_container">
    <div class="login">
        <h1 class="login-heading">
            <strong>Welcome.</strong> Please login.</h1>
        <form action="user" method="post">
            <input type="text" name="login" placeholder="Username" required="required" class="input-txt" />
            <input type="password" name="password" placeholder="Password" required="required" class="input-txt" />
            <input type="hidden" name="act" value="login">   
            <div class="login-footer">
                <button type="submit" class="btn btn--right">Sign in  </button>

            </div>
        </form>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>

