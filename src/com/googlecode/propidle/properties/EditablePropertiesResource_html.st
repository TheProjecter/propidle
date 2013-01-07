<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>$title$</title>
</head>
<body>
<div class="section">
    <h2>Edit properties</h2>
    <p>Previous edits can be seen at <a href="$first(changesUrl).url$">$first(changesUrl).name$</a></p>
    <form method="post">
        <input type="submit" value="Delete"/>
        <input type="hidden" name="baseRevisionNumber" value="$revisionNumber$"/>
        <input type="hidden" name="properties" value=""/>
    </form>
    <form method="post" style="height:100%">
        <input type="submit" value="Save"/>
        <input type="hidden" name="baseRevisionNumber" value="$revisionNumber$"/>
        <div height="100%">
            <textarea id="properties" name="properties" placeholder="Enter properties here"
                      style="width:100%;height:300px;max-height:100%;border:1px solid black;overflow:auto;">$properties:htmlproperty()$</textarea>
        </div>
    </form>
</div>

</body>
</html>