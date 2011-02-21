<html>
<head>
    <title>$title$</title>
</head>
<body>
<div class="section">
    <h2>List of changes</h2>
    <p>
        This is a list of all changes to <a href="$propertiesUrl$">$propertiesUrl$</a>
        $if(!revisionNumber.empty)$
        from revision $revisionNumber$
        $endif$
    </p>
</div>
<table>
<tr><th>properties file</th><th>revision</th><th>property name</th><th>old value</th><th>new value</th></tr>
$changes:change()$
</table>
</body>
</html>