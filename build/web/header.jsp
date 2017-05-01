<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Jbpm</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/main.css" rel="stylesheet">

</head>

<body>

    <!-- Navigation -->
   
<div class="top-line">

    <div class="col-md-2">
        <div class="logo">
            <img src="img/logo.png" alt="">
        </div>
    </div>

    <div class="col-md-8">
        <div class="navbar">
            <ul>
                <li><a href="profile.jsp">Tasks</a></li>
                <li><a href="processes.jsp?page=track">Processes</a></li>
                <li><a href="processes.jsp?page=create">Create Process</a></li>
            </ul>
        </div>
    </div>

    <div class="col-md-2">
        <form action="user" method="post">
            <input type="submit" value="Logout" class="btn logout-button">
            <input type="hidden" name="act" value="logout">
        </form>
    </div>

</div>
    <center><div id="result"></div></center>
