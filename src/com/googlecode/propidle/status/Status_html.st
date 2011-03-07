<html>
<head>
    <title>Status Report</title>
</head>
<body>
<div class="section">
    <h2>Status Report</h2>
        $checks:{check |
            <table>
                <tr colspan="2">
                    <th>$check.name$</th>
                </tr>
                $check.properties:{property |
                    <tr>
                        <td>$property.key$</td>
                        <td>$property.value$</td>
                    </tr>
                }$
            </table>
        }$
</div>
</body>
</html>