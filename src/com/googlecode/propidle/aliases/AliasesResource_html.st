<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>$title$</title>
</head>
<body>
<div class="section">
    <h2>What is a property file alias?</h2>

    <p>Aliases work like <a href="http://tinyurl.com">tinyurl</a> for properties files</p>

</div>
<div class="section">
    <h2>Create a new alias:</h2>

    <form method="GET">
        <p>$first(aliasesUrl).name$<input name="from" size="40"/></p>
        <p><input name="to" size="40" type="hidden" value="$thisUrl$"/></p>
        <p><input type="submit" value="Create alias"/></p>
    </form>
</div>

<table class="data-table">
    <tr>
        <th width="50%">If I go to</th>
        <th width="50%">I will be redirected to</th>
    </tr>
    $aliases:alias()$
</table>
</body>
</html>