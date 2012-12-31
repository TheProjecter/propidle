package acceptance;

import com.googlecode.propidle.aliases.AliasPath;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.html.Html;

import java.util.Collection;

public class AliasesPage {

    private Response response;

    public AliasesPage(Response response) {
        this.response = response;
    }

    public Collection<AliasPath> getAliases() throws Exception {
        return Html.html(response).selectValues("//table[contains(@class, 'aliases')]/tr/td[contains(@class, 'alias')]/a").map(AliasPath.constructors.aliasPath()).toList();
    }
}
