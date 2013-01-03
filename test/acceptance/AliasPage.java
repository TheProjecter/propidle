package acceptance;

import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.html.Html;

import static com.googlecode.utterlyidle.html.Html.html;

public class AliasPage {

    private Response response;

    public AliasPage(Response response) {
        this.response = response;
    }

    public boolean aliasExists() {
        return !htmlPage().contains("//p[contains(@class, 'alias-not-found')]");
    }

    public String destination() {
        return htmlPage().selectContent("//a[contains(@class, 'alias-destination')]");
    }

    @Override
    public String toString() {
        return response.toString();
    }

    private Html htmlPage() {
        try {
            return html(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
