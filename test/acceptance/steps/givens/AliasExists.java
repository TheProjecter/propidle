package acceptance.steps.givens;

import com.googlecode.propidle.aliases.Alias;
import com.googlecode.propidle.aliases.Aliases;
import com.googlecode.propidle.aliases.AliasPath;
import com.googlecode.propidle.aliases.AliasDestination;
import static com.googlecode.propidle.aliases.Alias.alias;
import acceptance.Values;
import acceptance.steps.Given;

import java.util.concurrent.Callable;

public class AliasExists implements Callable<Alias> {
    private final Aliases aliases;
    private final AliasPath from;
    private final AliasDestination to;

    public static Given<Alias> aliasExists(Values values) {
        return new Given<Alias>(AliasExists.class, values);
    }

    public AliasExists(Aliases aliases, AliasPath from, AliasDestination to) {
        this.aliases = aliases;
        this.from = from;
        this.to = to;
    }

    public Alias call() throws Exception {
        Alias alias = alias(from, to);
        aliases.put(alias);
        return alias;
    }
}
