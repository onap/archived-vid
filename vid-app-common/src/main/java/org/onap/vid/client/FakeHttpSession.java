package org.onap.vid.client;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pickjonathan on 03/07/2017.
 */
public class FakeHttpSession implements HttpSession {

    /**
     * Setup the creation time
     */
    public FakeHttpSession() {
        File file = new File("resources/roles.json");

        String rolesInputStream = null;
        try {
            rolesInputStream = IOUtils.toString(FakeHttpSession.class.getClassLoader().getResourceAsStream("roles.json"),"UTF8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONTokener tokener = new JSONTokener(rolesInputStream);
        JSONObject roles = new JSONObject(tokener);

        JSONArray rolesArray = roles.getJSONArray("roles");

        //set permissions to the roles from file.
        this.setAttribute("role", rolesArray);

        creationTime = System.currentTimeMillis();
    }


    /**
     * Setup the creation time
     * @param id The new session id
     */
    public FakeHttpSession(String id)
    {
        this.id = id;
        creationTime = System.currentTimeMillis();
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getCreationTime()
     */
    public long getCreationTime()
    {
        return creationTime;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getId()
     */
    public String getId()
    {
        if (id == null)
        {
            System.out.println("Inventing data in FakeHttpSession.getId() to remain plausible.");
            id = "fake";
        }

        return id;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getLastAccessedTime()
     */
    public long getLastAccessedTime()
    {
        return creationTime;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getServletContext()
     */
    public ServletContext getServletContext()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
     */
    public void setMaxInactiveInterval(int maxInactiveInterval)
    {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
     */
    public int getMaxInactiveInterval()
    {
        return maxInactiveInterval;
    }

    /**
     * @see javax.servlet.http.HttpSession#getSessionContext()
     * @deprecated
     */
    @SuppressWarnings({"UnnecessaryFullyQualifiedName"})
    @Deprecated
    public javax.servlet.http.HttpSessionContext getSessionContext()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name)
    {
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
     */
    @Deprecated
    public Object getValue(String name)
    {
        return attributes.get(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames()
    {
        return Collections.enumeration(attributes.keySet());
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValueNames()
     */
    @Deprecated
    public String[] getValueNames()
    {
        return attributes.keySet().toArray(new String[attributes.keySet().size()]);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object value)
    {
        attributes.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
     */
    @Deprecated
    public void putValue(String name, Object value)
    {
        attributes.put(name, value);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name)
    {
        attributes.remove(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
     */
    @Deprecated
    public void removeValue(String name)
    {
        attributes.remove(name);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#invalidate()
     */
    public void invalidate()
    {
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#isNew()
     */
    public boolean isNew()
    {
        return true;
    }

    /**
     * The session id
     */
    private String id = null;

    /**
     * The list of attributes
     */
    private Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * When were we created
     */
    private long creationTime;

    /**
     * How long before we timeout?
     */
    private int maxInactiveInterval = 30 * 60 * 1000;
}
