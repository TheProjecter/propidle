<html>
<head>
    <title>$username$</title>
</head>
<body>
<div class="section">
    <h2>Group memberships</h2>
    <ul>
        $groups:{group |
            <li><a href="$group.url$">$group.name$</a></li>
        }$
    </ul>
</div>
</body>
</html>