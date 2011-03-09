<html>
<head>
    <title>Status Report</title>
</head>
<body>
<div class="section">
    <h2>Status Report</h2>
        $checks:{check |
            <table>
                <tr colspan="2" align="left">
                    <th>$check.name$</th>
                </tr>
                $check.properties:{property |
                    <tr>
                        <td>$property.key$</td>
                        <td>$property.value$</td>
                    </tr>
                }$
            </table>
            <hr/>
        }$
</div>
</body>
</html>