<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Log into the Battleship game</title>
</head>
<body>
<a href="/signUp">Register</a>
<form action="/signIn" method="post">
    <label>Email
        <input type="email" placeholder="Type in your email" name="email">
    </label>
    <br>
    <label>Password
        <input type="password" placeholder="Type in your password" name="password">
    </label>
    <br>
    <label>
        <input type="checkbox" name="rememberMe">Remember me
    </label>
    <br>
    <input type="submit">
</form>
</body>
</html>