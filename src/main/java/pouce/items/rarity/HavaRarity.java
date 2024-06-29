package pouce.items.rarity;

public enum HavaRarity {
    COMMUN("COMMUN"),
    ATYPIQUE("ATYPIQUE"),
    RARE("RARE"),
    EPIQUE("EPIQUE"),
    LEGENDAIRE("LEGENDAIRE"),
    MYTHIQUE("MYTHIQUE");

    private String type;

    HavaRarity (String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

//    @Override
//    public String toString() {
//        switch (type){
//            case "COMMUN":
//                return "§f§lCOMMUN";
//            case "ATYPIQUE":
//                return "§2§lATYPIQUE";
//            case "RARE":
//                return "§9§lRARE";
//            case "EPIQUE":
//                return "§5§lEPIQUE";
//            case "LEGENDAIRE":
//                return "§e§lLEGENDAIRE";
//            case "MYTHIQUE":
//                return "§b§lMYTHIQUE";
//            default:
//                return "§4§lERROR";
//        }
//    }
    public String getFormattedName() {
        switch (type) {
            case "COMMUN":
                return "§f§lCOMMUN";
            case "ATYPIQUE":
                return "§2§lATYPIQUE";
            case "RARE":
                return "§9§lRARE";
            case "EPIQUE":
                return "§5§lEPIQUE";
            case "LEGENDAIRE":
                return "§e§lLEGENDAIRE";
            case "MYTHIQUE":
                return "§b§lMYTHIQUE";
            default:
                return "§4§lERROR";
        }
    }
}
