// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class ReportHistorySnapshot {
    protected java.lang.String historyID;
    protected java.util.Calendar creationDate;
    protected int size;
    
    public ReportHistorySnapshot() {
    }
    
    public ReportHistorySnapshot(java.lang.String historyID, java.util.Calendar creationDate, int size) {
        this.historyID = historyID;
        this.creationDate = creationDate;
        this.size = size;
    }
    
    public java.lang.String getHistoryID() {
        return historyID;
    }
    
    public void setHistoryID(java.lang.String historyID) {
        this.historyID = historyID;
    }
    
    public java.util.Calendar getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(java.util.Calendar creationDate) {
        this.creationDate = creationDate;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
}
