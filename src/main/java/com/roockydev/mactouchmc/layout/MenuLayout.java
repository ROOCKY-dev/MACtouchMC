package com.roockydev.mactouchmc.layout;

import com.roockydev.mactouchmc.api.TouchBarWidget;
import com.roockydev.mactouchmc.components.TBButtonWidget;
import com.thizzer.jtouchbar.item.TouchBarItem;
import com.thizzer.jtouchbar.item.view.TouchBarTextField;

public class MenuLayout extends BaseLayout {

    public MenuLayout() {
        super("main");
        initWidgets();
    }

    private void initWidgets() {
        // Simple text label for now, wrapped in a generic Item since we don't have a specific LabelWidget yet
        TouchBarTextField tbtf = new TouchBarTextField();
        tbtf.setStringValue("MACtouchMC 1.21 (by ROOCKYdev)");
        
        // Directly adding item for static text (could be refactored to Widget later)
        this.touchBar.addItem(new TouchBarItem("mactb_info", tbtf));
    }
}
