package pouce.nbt;

public class HavaNBT {
    private static final String GUI_PROTECT = "GuiProtect";
    private static final String UNIQUE_ID = "unique_id";
    private static final String GUI_NAV_ACTION = "guiNavAction";
    private static final String GUI_DONJON_ITEM_ACTION = "guiDonjonItemAction";
    private static final String GUI_DONJON_NAV_ACTION = "guiDonjonNavAction";
    private static final String GUI_EDIT_DONJON_ITEM_ACTION = "guiEditDonjonItemAction";
    private static final String GUI_DONJON_SELECTED_SPELL_ACTION = "guiForwardDonjonNavAction";
    public static String GetGuiDonjonSelectedSpellAction() {
        return GUI_DONJON_SELECTED_SPELL_ACTION;
    }

    // ITEM

    private static final String ITEM_DONJON_ID = "itemDonjonID";
    public static String GetItemDonjonID() {return ITEM_DONJON_ID;}

    private static final String ITEM_DONJON_TYPE = "itemDonjonType";
    public static String GetItemDonjonType() {return ITEM_DONJON_TYPE;}

    private static final String ITEM_DAMAGE = "itemDamage";
    public static String GetItemDamage(){return ITEM_DAMAGE;}

    private static final String ITEM_STRENGTH = "itemStrength";
    public static String GetItemStrength(){return ITEM_STRENGTH;}

    private static final String ITEM_RARITY = "itemRarity";
    public static String GetItemRarity(){return ITEM_RARITY;}

    private static final String ITEM_SPELL = "itemSpell";
    public static String GetItemSpell(){return ITEM_SPELL;}


    // MOBS

    private static final String ENTITY_DONJON_TYPE = "entityDonjonType";
    public static String GetEntityDonjonType() { return ENTITY_DONJON_TYPE;}

    private static final String ENTITY_DONJON_BOSS = "entityDonjonBoss";
    public static String GetEntityDonjonBoss() { return ENTITY_DONJON_BOSS;}


//    TCHAT

    private static final String RENAME_ITEM = "renameItem";


    public static String GetNBTGuiProtect() {
        return GUI_PROTECT;
    }

    public static String GetNBTUniqueId(){
        return UNIQUE_ID;
    }

    public static String GetNBTGuiNavAction(){
        return GUI_NAV_ACTION;
    }

    public static String GetNBTGuiDonjonItemAction(){
        return GUI_DONJON_ITEM_ACTION;
    }

    public static String GetNBTGuiDonjonNavAction(){
        return GUI_DONJON_NAV_ACTION;
    }

    public static String GetNBTGuiEditDonjonItemAction(){
        return GUI_EDIT_DONJON_ITEM_ACTION;
    }



//    TCHAT

    public static String GetRenameItem(){
        return RENAME_ITEM;
    }
}
