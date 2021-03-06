package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.internal.SWTEventListener;

import java.util.HashMap;

public abstract class Widget {

      	/* Global state flags */
    static final int DISPOSED = 1<<0;
    static final int CANVAS = 1<<1;
    static final int KEYED_DATA = 1<<2;
    static final int HANDLE = 1<<3;
    static final int DISABLED = 1<<4;
    static final int MENU = 1<<5;
    static final int OBSCURED = 1<<6;
    static final int MOVED = 1<<7;
    static final int RESIZED = 1<<8;
    static final int HIDDEN = 1<<11;
    static final int FOREGROUND = 1<<12;
    static final int BACKGROUND = 1<<13;
    static final int FONT = 1<<14;
    static final int PARENT_BACKGROUND = 1<<15;
    static final int THEME_BACKGROUND = 1<<16;

    /* A layout was requested on this widget */
    static final int LAYOUT_NEEDED	= 1<<17;

    /* The preferred size of a child has changed */
    static final int LAYOUT_CHANGED = 1<<18;

    /* A layout was requested in this widget hierachy */
    static final int LAYOUT_CHILD = 1<<19;

    /* More global state flags */
    static final int RELEASED = 1<<20;
    static final int DISPOSE_SENT = 1<<21;
    static final int FOREIGN_HANDLE = 1<<22;
    static final int DRAG_DETECT = 1<<23;

    /* Notify of the opportunity to skin this widget */
    static final int SKIN_NEEDED = 1<<24;

    /* Should sub-windows be checked when EnterNotify received */
    static final int CHECK_SUBWINDOW = 1<<25;

    /* Default size for widgets */
    static final int DEFAULT_WIDTH	= 64;
    static final int DEFAULT_HEIGHT	= 64;


    Widget parent;
    int style;
    int state;
    PlatformDisplay display;
    EventTable listeners;
    Object data;
    HashMap<String, Object> dataMap;

    public Widget(Widget parent, int style) {
        if (parent != null) {
            this.display = parent.display;
        }
        this.parent = parent;
        this.style = style;
    }


    public void addDisposeListener(DisposeListener listener) {
        addListener(SWT.Dispose, new TypedListener(listener));
    }

    /**
     * Overriden in Control, adding a platform listener registration.
     */
    public void addListener(int eventType, Listener listener) {
        if (listeners == null) {
            listeners = new EventTable();
        }
        listeners.hook(eventType, listener);
    }


    protected void checkWidget() {
        if ((state & DISPOSED) != 0) {
            System.err.println("ERROR in checkWidget(): Widget is disposed: " + this);
            // SWT.error(SWT.ERROR_WIDGET_DISPOSED, null, this.toString());
        }
    }

    protected void checkSubclass() {
    }

    public static int checkBits(int style, int leftToRight, int rightToLeft, int i, int i1, int i2, int i3) {
        return 0;
    }

    public void dispose() {
        if (isDisposed()) {
            return;
        }
        if (parent != null) {
            parent.removeChild(this);
        }
        notifyListeners(SWT.Dispose, new Event());
        if (this instanceof Control) {
            display.disposePeer((Control) this);
        }
        state |= DISPOSED;
    }

    public void error(int code) {
        SWT.error(code);
    }

    public Object getData() {
        return data;
    }

    public Object getData(String key) {
        return dataMap == null ? null : dataMap.get(key);
    }

    public Display getDisplay() {
        return display;
    }

    public Widget getParent() {
        return parent;
    }

    public int getStyle() {
        return style;
    }

    public boolean isDisposed() {
        return (state & DISPOSED) != 0;
    }

    public void notifyListeners(int eventType, Event event) {
        if (listeners != null) {
            if (event == null) {
                event = new Event();
                event.display = display;
                event.widget = this;
            }
            event.type = eventType;
            listeners.sendEvent(event);
        }
    }

    void releaseWidget () {
    }

    // Called from disposePeer
    void removeChild(Widget widget) {
    }

    void removeListener(int eventType, Listener listener) {
        if (listeners != null) {
            listeners.unhook(eventType, listener);
        }
    }

    void removeListener(int eventType, SWTEventListener listener) {
        if (listeners != null) {
            listeners.unhook(eventType, listener);
        }
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setData(String key, Object data) {
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        dataMap.put(key, data);
    }
}
