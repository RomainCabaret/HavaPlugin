package pouce.nbt;

public class HavaNBT {
    private static final String GUI_PROTECT = "GuiProtect";
    private static final String UNIQUE_ID = "unique_id";
    private static final String GUI_NAV_ACTION = "guiNavAction";
    private static final String GUI_DONJON_ITEM_ACTION = "guiDonjonItemAction";
    private static final String GUI_DONJON_NAV_ACTION = "guiDonjonNavAction";
    private static final String GUI_EDIT_DONJON_ITEM_ACTION = "guiEditDonjonItemAction";

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
}
