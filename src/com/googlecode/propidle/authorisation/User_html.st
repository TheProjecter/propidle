<html>
<head>
    <title>$username$</title>
</head>
<body>
<div class="section">
    <h2>Group memberships</h2>
    <ul>
        $memberships:{membership |
            <li><a href="$membership.url$">$membership.groupName$</a></li>
        }$
    </ul>
    <div class="section">
        <form method="POST">
            <p></p>
            <p><select name="addToGroup" multiple="true" size="8">
                $allGroups:{group |
                <option value="$group.id$">$group.groupName$</option>
                }$
            </select></p>
            <p><input type="submit" value="add memberships" /></p>
        </form>
    </div>
</div>
</body>
</html>