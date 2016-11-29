package external;

import kraken.Kraken;

public abstract class KrakenApp {
    private Kraken kraken = new Kraken();

    public void KrakenApp() {
        this.registerExtensions(this.kraken);
        this.registerResources(this.kraken);
        this.registerResolvers(this.kraken);
        this.kraken.dive();
    }

    final public Kraken getKraken() {
        return this.kraken;
    }

    abstract void registerExtensions(Kraken kraken);

    abstract void registerResources(Kraken kraken);

    abstract void registerResolvers(Kraken kraken);
}
