// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class Render {
    protected java.lang.String report;
    protected java.lang.String format;
    protected java.lang.String historyID;
    protected java.lang.String deviceInfo;
    protected org.jboss.test.webservice.jbws718.ArrayOfParameterValue parameters;
    protected org.jboss.test.webservice.jbws718.ArrayOfDataSourceCredentials credentials;
    protected java.lang.String showHideToggle;
    
    public Render() {
    }
    
    public Render(java.lang.String report, java.lang.String format, java.lang.String historyID, java.lang.String deviceInfo, org.jboss.test.webservice.jbws718.ArrayOfParameterValue parameters, org.jboss.test.webservice.jbws718.ArrayOfDataSourceCredentials credentials, java.lang.String showHideToggle) {
        this.report = report;
        this.format = format;
        this.historyID = historyID;
        this.deviceInfo = deviceInfo;
        this.parameters = parameters;
        this.credentials = credentials;
        this.showHideToggle = showHideToggle;
    }
    
    public java.lang.String getReport() {
        return report;
    }
    
    public void setReport(java.lang.String report) {
        this.report = report;
    }
    
    public java.lang.String getFormat() {
        return format;
    }
    
    public void setFormat(java.lang.String format) {
        this.format = format;
    }
    
    public java.lang.String getHistoryID() {
        return historyID;
    }
    
    public void setHistoryID(java.lang.String historyID) {
        this.historyID = historyID;
    }
    
    public java.lang.String getDeviceInfo() {
        return deviceInfo;
    }
    
    public void setDeviceInfo(java.lang.String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfParameterValue getParameters() {
        return parameters;
    }
    
    public void setParameters(org.jboss.test.webservice.jbws718.ArrayOfParameterValue parameters) {
        this.parameters = parameters;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfDataSourceCredentials getCredentials() {
        return credentials;
    }
    
    public void setCredentials(org.jboss.test.webservice.jbws718.ArrayOfDataSourceCredentials credentials) {
        this.credentials = credentials;
    }
    
    public java.lang.String getShowHideToggle() {
        return showHideToggle;
    }
    
    public void setShowHideToggle(java.lang.String showHideToggle) {
        this.showHideToggle = showHideToggle;
    }
}
