<html>
<head>
    <title>$title$</title>
</head>
<body>
<div>
    <div class="section">
        <h2>Create a composite properties file</h2>
        <p>List the urls of the property files you would like to compose. Property values from files lower in the list will override values in files above them.</p>
        <form method="GET">
            <p>Revision:</p>
            <p><input name="revision" size="20" type="text" value="$revision$" placeholder="latest"/></p>
            <p>Urls:</p>
            <p>$urls : urlInput()$</p>
            <p><input name="url" size="40"/></p>
            <p><input type="submit" value="get"/></p>
        </form>
    </div>
    <div class="section">
        <h2>Create an alias for this composite</h2>
        <form method="GET" action="$aliasesUrl$">
            <input name="to" size="40" type="hidden" value="$thisUrl$"/>
            <p>
                $aliasesUrl$/<input name="from" size="40"/>
                <input type="submit" value="Create"/>
            </p>
        </form>
    </div>
</div>
<table>
    <tr>
        <th>name</th>
        <th>value</th>
        <th>overrides</th>
    </tr>
    $properties : compositeProperty()$
</table>
</body>
</html>