<html>
<head>
    <title>Diff</title>
</head>
<body>
    <div class="section">
        <h2>Compare two property files</h2>
        <p>Type a url in the two boxes and click "show diff"</p>
        <form method="GET">
            <table style="width:0%  ">
                <tr>
                    <td>
                        <p>Left</p>
                        <p><input name="left" autocomplete="off" class="property-autocomplete $left:{$it.status$}$" value="$left:{$it.url$}$" size="40"/></p>
                        <p><span class="error-text $left:{$it.status$}$">$left:{$it.message$}$</span></p>
                    </td>
                    <td>
                        <p>Right</p>
                        <p><input name="right" autocomplete="off" class="property-autocomplete $right:{$it.status$}$" value="$right:{$it.url$}$" size="40"/></p>
                        <p><span class="error-text $right:{$it.status$}$">$right:{$it.message$}$</span></p>
                    </td>
                </tr>
            </table>
            <p><input type="submit" value="show diff"/></p>
        </form>
    </div>
    <div>
        <table>
            <tr>
                <th>property</th>
                <th>left</th>
                <th>right</th>
            </tr>
            $diffs : diff()$
        </table>
    </div>
</body>
</html>