<html>
<head>
    <title>Migration Report</title>
</head>
<body>
<div class="section">
    <h2>Migration Report</h2>
        <table>
            <tr>
                <td>Total run time</td>
                <td>$runTime$</td>
            </tr>
            <tr>
                <td valign="top">Migrations</td>
                <td>
                    <table>
                        $migrations:{migration |
                            <tr>
                                <td>$migration$</td>
                            </tr>
                        }$
                    </table>
                </td>
            </tr>
            <tr>
                <td>Status Page</td>
                <td><a href="$statusReport$">Here</a></td>
            </tr>
        </table>
</div>
</body>
</html>