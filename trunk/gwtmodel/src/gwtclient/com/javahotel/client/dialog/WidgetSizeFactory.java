package com.javahotel.client.dialog;

import com.google.gwt.user.client.ui.Widget;

public class WidgetSizeFactory {

    private WidgetSizeFactory() {
    }

    private static class SizeW implements IWidgetSize {

        private final Widget w;

        SizeW(Widget w) {
            this.w = w;
        }

        public int getTop() {
            return w.getAbsoluteTop();
        }

        public int getLeft() {
            return w.getAbsoluteLeft();
        }

        public int getHeight() {
            return w.getOffsetHeight();
        }

        public int getWidth() {
            return w.getOffsetWidth();
        }
    }

    public static IWidgetSize getW(Widget w) {
        if (w == null) {
            return null;
        }
        return new SizeW(w);
    }

    private static class AW implements IWidgetSize {

        private final int top, left, height, width;

        AW(int top, int left, int height, int width) {
            this.top = top;
            this.left = left;
            this.height = height;
            this.width = width;
        }

        public int getTop() {
            return top;
        }

        public int getLeft() {
            return left;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    public static IWidgetSize getW(int top, int left, int height, int width) {
        return new AW(top, left, height, width);
    }
}
