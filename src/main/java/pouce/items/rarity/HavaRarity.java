package pouce.items.rarity;

public enum HavaRarity {
    COMMUN("commun"),
    ATYPIQUE("atypique"),
    RARE("rare"),
    EPIQUE("epique"),
    LEGENDAIRE("legendaire"),
    MYTHIQUE("mythique");

    private String type;

    HavaRarity(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
