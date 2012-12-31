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
<table id="panes" style="width:100%; height:100%;">
    <tr>
        <td id="corner-pane" width="160" height="80" style="padding:0px;"><div style="width:160;height:100%;overflow:hidden"><img src="$base$static/title.png"/></div></td>
        <td id="title-pane" valign="middle"><h1>$title$</h1></td>
    </tr>
    <tr>
        <td id="nav-pane" valign="top">
            <div id="nav-pane-content">
                $include.({/nav?decorator=none}).body$
                </div>
        </td>
        <td id="body-pane" valign="top">$body$</td>
    </tr>
</table>
</body>
</html>
