package com.weltevree.ego.model;

import java.util.ArrayList;

public class TreeParent extends TreeObject
{
    private ArrayList children;


    public TreeParent(Object object, String name, String sortCode, IGoEventListener listener)
    {
        super(object, name, sortCode, listener);
        children = new ArrayList();
        fireEvent(EVENT_ADD);
            }

    public void addChild(TreeObject child)
    {
        children.add(child);
        child.setParent(this);
        child.fireEvent(EVENT_CHANGE);
        fireEvent(EVENT_CHANGE);
    }

    public void removeChild(TreeObject child)
    {
        children.remove(child);
        child.fireEvent(EVENT_DELETE);
        child = null;
        fireEvent(EVENT_CHANGE);
        
    }

    public void removeChildren()
    {
        while (hasChildren())
        {
            getChildren()[0].fireEvent(EVENT_DELETE);
            getChildren()[0].setParent(null);
            children.remove(getChildren()[0]);
        }
        fireEvent(EVENT_CHANGE);
    }

    public TreeObject[] getChildren()
    {
        return (TreeObject[]) children.toArray(new TreeObject[children
                .size()]);
    }

    public boolean hasChildren()
    {
        return children.size() > 0;
    }
}
