package com.roockydev.mactouchmc.config;

public class TouchBarButtonConfig {
    public String name = "Custom Button";
    public String icon = "sword"; // Default icon name or path
    public String color = "0xFFFFFF"; // Hex color
    public int keyId = -1; // -1 means unassigned/auto
    public ActionType type = ActionType.PRESS;

    public enum ActionType {
        PRESS,
        TOGGLE
    }
}
