// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class CreateReportHistorySnapshotResponse {
    protected java.lang.String historyID;
    protected org.jboss.test.webservice.jbws718.ArrayOfWarning warnings;
    
    public CreateReportHistorySnapshotResponse() {
    }
    
    public CreateReportHistorySnapshotResponse(java.lang.String historyID, org.jboss.test.webservice.jbws718.ArrayOfWarning warnings) {
        this.historyID = historyID;
        this.warnings = warnings;
    }
    
    public java.lang.String getHistoryID() {
        return historyID;
    }
    
    public void setHistoryID(java.lang.String historyID) {
        this.historyID = historyID;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfWarning getWarnings() {
        return warnings;
    }
    
    public void setWarnings(org.jboss.test.webservice.jbws718.ArrayOfWarning warnings) {
        this.warnings = warnings;
    }
}
