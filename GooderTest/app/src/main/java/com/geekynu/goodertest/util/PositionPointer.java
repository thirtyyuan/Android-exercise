package com.geekynu.goodertest.util;

/**
 * Created by yuanhonglei on 8/11/16.
 */
public class PositionPointer {
    static int position;

    public static int getPositionItem() {
        return positionItem;
    }

    public static void setPositionItem(int positionItem) {
        PositionPointer.positionItem = positionItem;
    }

    static int positionItem;

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        PositionPointer.position = position;
    }

}
