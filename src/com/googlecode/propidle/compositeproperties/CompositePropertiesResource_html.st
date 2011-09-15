<html>
    <head>
        <title>$title$</title>
    </head>
    <body>
        <div>
            <div class="section">
                <h2>Create a composite properties file</h2>
                <p>List the urls of the property files you would like to compose. Property values from files lower in
                    the list will override values in files above them.
                </p>
                <form name="pathSearchForm" method="GET">
                    <input name="alias" type="hidden" value="$alias$"/>
                    <p>Revision:</p>
                    <p>
                        <input name="revision" size="20" type="text" value="$revision$" placeholder="latest"/>
                    </p>
                    <p>Urls:</p>
                    <p>$urls : urlInput()$</p>
                    <div>
                        <input name="url" id="urlInput" class="property-autocomplete" size="40" autocomplete="off"/>
                    </div>
                    <p>
                        <input type="submit" value="get"/>
                    </p>
                </form>
            </div>
            <div class="section">
            $if(!alias.empty)$
                <h2> Update the alias </h2>
                <form method="post" action="$aliasesUrl$$alias$">
                    <input name="to" size="40" type="hidden" value="$thisUrl$"/>
                    <p>
                        $aliasesUrl$$alias$
                        <input type="submit" value="Update"/>
                    </p>
                </form>
            $else$
                <h2>Create an alias for this composite</h2>
                <form method="GET" action="$aliasesUrl$">
                    <input name="to" size="40" type="hidden" value="$thisUrl$"/>
                    <p>
                        $aliasesUrl$/
                        <input name="from" size="40"/>
                        <input type="submit" value="Create"/>
                    </p>
                </form>
            $endif$
            </div>
        </div>
        <table>
            <tr>
                <th>name</th>
                <th>value</th>
                <th>overrides</th>
            </tr>
            $properties : compositeProperty()$
        </table>
</body>
</html>