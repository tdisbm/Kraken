package external;

import kraken.Kraken;

public abstract class KrakenApp {
    private Kraken kraken = new Kraken();

    public KrakenApp() {
        this.registerExtensions(this.kraken);
        this.registerResources(this.kraken);
        this.registerResolvers(this.kraken);
        this.kraken.dive();
    }

    final public Kraken getKraken() {
        return this.kraken;
    }

    protected abstract void registerExtensions(Kraken kraken);

    protected abstract void registerResources(Kraken kraken);

    protected abstract void registerResolvers(Kraken kraken);
}
