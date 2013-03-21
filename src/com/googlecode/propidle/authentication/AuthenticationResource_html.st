<html>
<head>
    <title>Login</title>
</head>
<body>
<div class="section">
    $if(errorMessage)$
        <div id="message" class="error-text">$errorMessage$</div>
    $endif$
    <form method="POST" action="$originatingRequestTarget$">
        <p>Username</p>
        <p><input name="username" value="$username$" type="text"/></p>
        <p>Password</p>
        <p><input name="password" value="" type="password"/></p>
        <p><input type="submit" value="login" /></p>
        <p><input name="originatingRequest" type="hidden" value="$originatingRequest$" /></p>
    </form>
</div>
</body>
</html>