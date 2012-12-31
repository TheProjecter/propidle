<html>
<head>
    <title>$title$</title>
</head>
<body>
<div class="section">
    <h2>Create an alias to another properties file URL</h2>
    $if(currentRedirectTo.empty)$
    <p class="error-text alias-not-found">This alias does not yet exist- hit <strong>Save</strong></p>
    $endif$

    <p>Aliases work like <a href="http://tinyurl.com">tinyurl</a> for properties files</p>

    <div class="section">
        <form method="POST" target="$resourcePath$">
            <table>
                <tr>
                    <th style="white-space:nowrap;">Make <a href="$first(aliasUrl).url$">$first(aliasUrl).name$</a> redirect to:</th>
                    <td style="width:100%;"><input name="to" size="40" value="$redirectTo$"/></td>
                </tr>
            </table>
            <input type="submit" value="Save"/>
        </form>
    </div>
    $if(!currentRedirectTo.empty)$
    <p>Currently <a href="$first(aliasUrl).url$">$first(aliasUrl).name$</a> redirects to: <a href="$first(currentRedirectTo).url$">$first(currentRedirectTo).name$</a>
    </p>
    <p><a href="$editUrl$">Edit this Alias</a>
    <form action="$deleteUrl$" method="POST">
        <input type="submit" value="Delete this Alias"></input>
    </form>
    <p>Try it out: <a href="$first(aliasUrl).url$">$first(aliasUrl).name$</a></p>
    $endif$
</div>
</body>
</html>