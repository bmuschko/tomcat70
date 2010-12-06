/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.coyote;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;

import javax.management.MBeanRegistration;
import javax.management.ObjectName;

import org.apache.juli.logging.Log;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.res.StringManager;

public abstract class AbstractProtocolHandler implements ProtocolHandler,
        MBeanRegistration {

    /**
     * The string manager for this package.
     */
    protected static final StringManager sm =
        StringManager.getManager(Constants.Package);


    /**
     * Name of MBean for the Global Request Processor.
     */
    protected ObjectName rgOname = null;

    
    /**
     * Name of MBean for the ThreadPool.
     */
    protected ObjectName tpOname = null;

    
    /**
     * Endpoint that provides low-level network I/O - must be matched to the
     * ProtocolHandler implementation (ProtocolHandler using BIO, requires BIO
     * Endpoint etc.).
     */
    protected AbstractEndpoint endpoint = null;

    
    // ----------------------------------------------- Generic property handling

    /**
     * Attributes provide a way for configuration to be passed to sub-components
     * without the {@link ProtocolHandler} being aware of the properties
     * available on those sub-components. One example of such a sub-component is
     * the {@link org.apache.tomcat.util.net.ServerSocketFactory}.
     */
    protected HashMap<String, Object> attributes =
        new HashMap<String, Object>();


    /** 
     * Generic property setter called when a property for which a specific
     * setter already exists within the {@link ProtocolHandler} needs to be
     * made available to sub-components. The specific setter will call this
     * method to populate the attributes.
     */
    @Override
    public void setAttribute(String name, Object value) {
        if (getLog().isTraceEnabled()) {
            getLog().trace(sm.getString("abstractProtocolHandler.setAttribute",
                    name, value));
        }
        attributes.put(name, value);
    }

    
    /**
     * Used by sub-components to retrieve configuration information.
     */
    @Override
    public Object getAttribute(String key) {
        Object value = attributes.get(key);
        if (getLog().isTraceEnabled()) {
            getLog().trace(sm.getString("abstractProtocolHandler.getAttribute",
                    key, value));
        }
        return value;
    }


    /**
     * Used by sub-components to retrieve configuration information.
     */
    @Override
    public Iterator<String> getAttributeNames() {
        return attributes.keySet().iterator();
    }


    /**
     * Generic property setter used by the digester. Other code should not need
     * to use this. The digester will only use this method if it can't find a
     * more specific setter. That means the property belongs to the Endpoint,
     * the ServerSocketFactory or some other lower level component. This method
     * ensures that it is visible to both.
     */
    public boolean setProperty(String name, String value) {
        setAttribute(name, value);
        return endpoint.setProperty(name, value);
    }


    /**
     * Generic property getter used by the digester. Other code should not need
     * to use this.
     */
    public String getProperty(String name) {
        // Since all calls to setProperty() will place the property in the
        // attributes list, just retrieve it from there. 
        return (String)getAttribute(name);
    }


    // ------------------------------- Properties managed by the ProtocolHandler
    
    /**
     * The adapter provides the link between the ProtocolHandler and the
     * connector.
     */
    protected Adapter adapter;
    @Override
    public void setAdapter(Adapter adapter) { this.adapter = adapter; }
    @Override
    public Adapter getAdapter() { return adapter; }


    // ---------------------- Properties that are passed through to the EndPoint

    @Override
    public Executor getExecutor() { return endpoint.getExecutor(); }
    public void setExecutor(Executor executor) {
        endpoint.setExecutor(executor);
    }


    public int getMaxThreads() { return endpoint.getMaxThreads(); }
    public void setMaxThreads(int maxThreads) {
        endpoint.setMaxThreads(maxThreads);
    }


    public int getMinSpareThreads() { return endpoint.getMinSpareThreads(); }
    public void setMinSpareThreads(int minSpareThreads) {
        endpoint.setMinSpareThreads(minSpareThreads);
    }


    public int getThreadPriority() { return endpoint.getThreadPriority(); }
    public void setThreadPriority(int threadPriority) {
        endpoint.setThreadPriority(threadPriority);
    }


    public int getPort() { return endpoint.getPort(); }
    public void setPort(int port) { endpoint.setPort(port); }


    public int getBacklog() { return endpoint.getBacklog(); }
    public void setBacklog(int backlog) { endpoint.setBacklog(backlog); }


    public boolean getTcpNoDelay() { return endpoint.getTcpNoDelay(); }
    public void setTcpNoDelay(boolean tcpNoDelay) {
        endpoint.setTcpNoDelay(tcpNoDelay);
    }


    public int getSoLinger() { return endpoint.getSoLinger(); }
    public void setSoLinger(int soLinger) { endpoint.setSoLinger(soLinger); }


    // ------------------------ Properties that are made available as attributes
    // -------------------------------------(and passed through to the EndPoint)

    
    
    // -------------------------------------------------------- Abstract methods
    /**
     * Concrete implementations need to provide access to their logger to be
     * used by the abstract classes.
     */
    protected abstract Log getLog();
}