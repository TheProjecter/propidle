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
    <p>Use % for wildcards. Escape % by using %%</p>
</div>
<table class="data-table">
    <tr><th>file</th><th>property</th><th>value</th></tr>
    $matches:match()$
</table>
</body>
</html>