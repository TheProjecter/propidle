package acceptance.steps.givens;

import com.googlecode.propidle.aliases.Alias;
import com.googlecode.propidle.aliases.Aliases;
import com.googlecode.propidle.aliases.AliasPath;
import com.googlecode.propidle.aliases.AliasDestination;
import static com.googlecode.propidle.aliases.Alias.alias;
import acceptance.Values;

import java.util.concurrent.Callable;

public class AliasExists implements Callable<Alias> {
    private final Aliases aliases;
    private AliasPath from;
    private AliasDestination to;

    public AliasExists(Aliases aliases) {
        this.aliases = aliases;
        this.from = from;
        this.to = to;
    }

    public AliasExists from(AliasPath from) {
        this.from = from;
        return this;
    }

    public AliasExists to(AliasDestination to) {
        this.to = to;
        return this;
    }

    public Alias call() throws Exception {
        Alias alias = alias(from, to);
        aliases.put(alias);
        return alias;
    }
}
