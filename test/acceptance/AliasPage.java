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
        try {
            return !html(response).contains("//p[contains(@class, 'alias-not-found')]");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return response.toString();
    }
}
