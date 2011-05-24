<html>
<head>
    <title>Status Report</title>
</head>
<body>
<div class="section">
        <table></table>
        $checks:{check |
        <div >
            <table class="compact">
                <tr>
                    <th colspan="2">$check.name$</th>
                </tr>
                $check.properties:{property |
                    <tr>
                        <td>$property.key$</td>
                        <td>$property.value$</td>
                    </tr>
                }$
            </table>
            </div>
            <hr/>
        }$
</div>
</body>
</html>