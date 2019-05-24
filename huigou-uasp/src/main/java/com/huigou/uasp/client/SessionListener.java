package com.huigou.uasp.client;

import javax.servlet.http.*;

public class SessionListener extends HttpServlet implements HttpSessionListener, HttpSessionAttributeListener {

    private static final long serialVersionUID = 6360575307672917606L;

    // Notification that a session was created
    public void sessionCreated(HttpSessionEvent se) {
    }

    // Notification that a session was invalidated
    public void sessionDestroyed(HttpSessionEvent se) {

    }

    // Notification that a new attribute has been added to a session
    public void attributeAdded(HttpSessionBindingEvent se) {

    }

    // Notification that an attribute has been removed from a session
    public void attributeRemoved(HttpSessionBindingEvent se) {

    }

    // Notification that an attribute of a session has been replaced
    public void attributeReplaced(HttpSessionBindingEvent se) {

    }

}
