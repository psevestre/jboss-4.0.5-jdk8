// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class SetReportHistoryLimit {
    protected java.lang.String report;
    protected boolean useSystem;
    protected int historyLimit;
    
    public SetReportHistoryLimit() {
    }
    
    public SetReportHistoryLimit(java.lang.String report, boolean useSystem, int historyLimit) {
        this.report = report;
        this.useSystem = useSystem;
        this.historyLimit = historyLimit;
    }
    
    public java.lang.String getReport() {
        return report;
    }
    
    public void setReport(java.lang.String report) {
        this.report = report;
    }
    
    public boolean isUseSystem() {
        return useSystem;
    }
    
    public void setUseSystem(boolean useSystem) {
        this.useSystem = useSystem;
    }
    
    public int getHistoryLimit() {
        return historyLimit;
    }
    
    public void setHistoryLimit(int historyLimit) {
        this.historyLimit = historyLimit;
    }
}
