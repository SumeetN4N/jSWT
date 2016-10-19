package org.eclipse.swt.widgets;

import javax.print.Doc;
import org.eclipse.swt.custom.StackLayout;
import org.kobjects.dom.*;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import org.kobjects.dom.Event;

class GwtTabFolder extends Element {

    private static int tabIdCounter = 0;

    protected GwtTabFolder() {

    }

    final Element getTabBar() {
        return getFirstElementChild();
    }


    public static GwtTabFolder create(TabFolder tabFolder) {
        tabFolder.setLayout(new GwtTabLayout());
        GwtTabFolder gwtTabFolder = (GwtTabFolder) Document.get().createElement("jswt-tabfolder");
        gwtTabFolder.setAttribute("style", "display: block");
        Element tabBar = Document.get().createElement("div");
        gwtTabFolder.appendChild(tabBar);
        return gwtTabFolder;
    }


    final void addTab(int index, final TabItem tabItem) {
        boolean isFirst = getFirstElementChild() == getLastElementChild();

        Element tabBar = getTabBar();
        final Element newTab = Document.get().createElement("a");
        newTab.setAttribute("style", "display: inline-block");
        newTab.setTextContent("Tab " + index);
        String tabId = "tab-" + tabIdCounter++;
        newTab.setAttribute("href", "#" + tabId);
        newTab.addEventListener("click", new EventListener() {
            @Override
            public void onEvent(Event event) {
                setSelectionImpl(tabItem, newTab);
            }
        });
        tabBar.insertBefore(newTab, Elements.getChildElement(tabBar, index));

        Element newContent = Document.get().createElement("div");
        newContent.setAttribute("id", tabId);
        insertBefore(newContent, Elements.getChildElement(this, index + 1));

        if (isFirst) {
            newTab.setClassName("selected");
        } else {
            newContent.setAttribute("style", "visibility: hidden");
        }
    }


    final int getSelection() {
        int i = 0;
        Element tab = getTabBar().getFirstElementChild();
        while (tab != null) {
            if ("selected".equals(tab.getClassName())) {
                return i;
            }
            i++;
            tab = tab.getNextElementSibling();
        }
        return -1;
    }


    final void updateTab(int index, TabItem tabItem) {
        if (tabItem.getText() != null && !tabItem.getText().isEmpty()){
            Element tabContainer = getTabBar();
            Element tab = Elements.getChildElement(tabContainer, index);
            tab.setTextContent(tabItem.getText());
        }
        if (tabItem.getControl() != null) {
            Element newContent = (Element) tabItem.getControl().peer;
            Element oldContent = Elements.getChildElement(this, index + 1);

            if (oldContent != newContent) {
                newContent.setAttribute("id", oldContent.getAttribute("id"));
                newContent.setAttribute("style", oldContent.getAttribute("style"));
                replaceChild(newContent, oldContent);
            }
        }
    }

    private final void setSelectionImpl(TabItem selectedTabItem, Element selectedTab) {
        Element tab = getTabBar().getFirstElementChild();
        while (tab != null) {
            tab.setClassName(tab == selectedTab ? "selected" : "");
            tab = tab.getNextElementSibling();
        }

        Element panel = getTabBar().getNextElementSibling();
        while (panel != null) {
            panel.setAttribute("style", "visibility: " + (panel == selectedTabItem.control.peer ? "visible" : "hidden"));
            panel = panel.getNextElementSibling();
        }
    }


    public final PlatformDisplay.Insets getInsets() {
        PlatformDisplay.Insets insets = new PlatformDisplay.Insets();
        insets.top = Elements.getMinHeight(getTabBar());
        return insets;
    }
}
