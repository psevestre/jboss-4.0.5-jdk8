// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class GetReportHistoryLimitResponse {
    protected int historyLimit;
    protected boolean isSystem;
    protected int systemLimit;
    
    public GetReportHistoryLimitResponse() {
    }
    
    public GetReportHistoryLimitResponse(int historyLimit, boolean isSystem, int systemLimit) {
        this.historyLimit = historyLimit;
        this.isSystem = isSystem;
        this.systemLimit = systemLimit;
    }
    
    public int getHistoryLimit() {
        return historyLimit;
    }
    
    public void setHistoryLimit(int historyLimit) {
        this.historyLimit = historyLimit;
    }
    
    public boolean isIsSystem() {
        return isSystem;
    }
    
    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }
    
    public int getSystemLimit() {
        return systemLimit;
    }
    
    public void setSystemLimit(int systemLimit) {
        this.systemLimit = systemLimit;
    }
}
