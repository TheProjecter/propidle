<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <link type="text/css" rel="stylesheet" href="$base$static/base.css"/>
    <title>$title$</title>
    <script src="$base$static/jquery-1.6.2.min.js" type="text/javascript"></script>
    <script src="$base$static/propidle.js" type="text/javascript"></script>
    $head$
</head>

<body>
    <div id="nav-pane-content">
        <h1 id="corner-pane">Property Manager</h1>
        $include.({/nav?decorator=none}).body$
    </div>
    <div id="body-pane">
    $body$
    </div>
</body>
</html>
