<%@page import="entities.User"%>
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
    <link href="css/saipal.css" rel="stylesheet">

    <!-- Custom CSS -->

</head>
<style>
    .navbar-default{
        background-color: #c1d9ea;
    }
    html {
        font-family: "Tenor Sans", sans-serif;
    }
    .navbar-default .navbar-nav > li > a{
    text-transform: uppercase;
    color: #ffffff;
    font-size: 16px;
    }    
    .navbar-default .navbar-nav > .open > a, .navbar-default .navbar-nav > .open > a:hover, .navbar-default .navbar-nav > .open > a:focus {
        background-color: white;
    }
    footer {
        margin-top: 300px;
    }
    
    
    
</style>

        <% 
                User user=(User)session.getAttribute("user");
                %> 
<body>
    
     <nav class="nav navbar-default" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <div class="logo">
               </div>
                
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li><a href="profile.jsp">Tasks</a></li>
                    <li class="dropdown">
                     <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Processes <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="processes.jsp?page=track">My Processes</a></li>
                            <li><a href="processes.jsp?page=create">Create Process</a></li>
                        </ul>
                   </li>
                </ul>
           <form class="navbar-form navbar-left">
                <div class="form-group">
                  <input type="text" class="form-control" placeholder="Search">
                </div>
            </form>
            <form class="navbar-form navbar-right" action="user" method="post">
                    <input type="submit" value="Logout" class="btn btn-default">
                    <input type="hidden" name="act" value="logout">
            </form>
            </div>
            
            <!-- /.navbar-collapse -->
             
        </div>
        <!-- /.container -->
    </nav>
    