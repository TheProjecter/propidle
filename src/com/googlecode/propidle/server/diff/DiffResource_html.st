<html>
<head>
    <title>Diff</title>
</head>
<body>
<table>
<form method="GET">
    <div>
        <table>
            <tr>
                <th><input type="submit" value="show diff"/></th>
                <th><input name="left"   value="$left:{$it.url$}$"  class="$left:{$it.status$}$" size="40"/></th>
                <th><input name="right"  value="$right:{$it.url$}$" class="$right:{$it.status$}$" size="40"/></th>
            </tr>
            <tr>
                <th></th>
                <th><span class="error-text">$left:{$it.message$}$</span></th>
                <th><span class="error-text">$right:{$it.message$}$</span></th>
            </tr>
            $diffs : diff()$
        </table>
    </div>
</form>
</body>
</html>