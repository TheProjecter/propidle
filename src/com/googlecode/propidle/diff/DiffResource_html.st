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
                        <p><input name="left"   value="$left:{$it.url$}$"  class="$left:{$it.status$}$" size="40"/></p>
                        <p><span class="error-text">$left:{$it.message$}$</span></p>
                    </td>
                    <td>
                        <p>Right</p>
                        <p><input name="right"  value="$right:{$it.url$}$" class="$right:{$it.status$}$" size="40"/></p>
                        <p><span class="error-text">$right:{$it.message$}$</span></p>
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