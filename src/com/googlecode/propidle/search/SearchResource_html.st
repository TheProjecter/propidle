<html>
<head>
    <title>$title$</title>
</head>
<body>
<div class="section">
    <h2>Search properties</h2>

    <form method="GET">
        <p>
            <input name="q" value="$query$"/>
            <input type="submit" value="Search"/>
        </p>
    </form>
</div>
<table class="data-table">
    <tr>
        <th>file</th>
        <th>property</th>
        <th>value</th>
    </tr>
    $matches:{match |
    <tr>
        <td><a href="$first(match.url).url$">$first(match.url).name$</a></td>
        <td>$match.propertyName$</td>
        <td>$match.propertyValue$</td>
    </tr>
    }$
</table>
</body>
</html>