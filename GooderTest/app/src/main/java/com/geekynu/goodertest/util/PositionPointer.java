package com.geekynu.goodertest.util;

/**
 * Created by yuanhonglei on 8/11/16.
 */
public class PositionPointer {
    static int positionItem;

    static int position;

    static int alarmPosition;

    public static int getPositionItem() {
        return positionItem;
    }

    public static void setPositionItem(int positionItem) {
        PositionPointer.positionItem = positionItem;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        PositionPointer.position = position;
    }

    public static int getAlarmPosition() {
        return alarmPosition;
    }

    public static void setAlarmPosition(int alarmPosition) {
        PositionPointer.alarmPosition = alarmPosition;
    }
}
