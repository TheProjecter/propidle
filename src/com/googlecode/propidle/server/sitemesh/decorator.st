<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="/static/base.css"/>
    <title>$title$</title>
    <script src="/static/jquery-1.6.2.min.js" type="text/javascript"></script>
    <script src="/static/propidle.js" type="text/javascript"></script>
    $head$
</head>

<body>
<table id="panes" style="width:100%; height:100%;">
    <tr>
        <td id="corner-pane" width="160" height="80" style="padding:0px;"><div style="width:160;height:100%;overflow:hidden"><img src="/static/title.png"/></div></td>
        <td id="title-pane" valign="middle"><h1>$title$</h1></td>
    </tr>
    <tr>
        <td id="nav-pane" valign="top">
            <div id="nav-pane-content">
            <ul>
                <li><a href="/properties/">Properties</a></li>
                <li><a href="/composite?url=">Compose properties</a></li>
                <li><a href="/aliases/">Aliases</a></li>
                <li><a href="/search?q=">Search</a></li>
                <li><a href="/diff">Compare properties</a></li>
            </ul>
                </div>
        </td>
        <td id="body-pane" valign="top">$body$</td>
    </tr>
</table>
</body>
</html>
