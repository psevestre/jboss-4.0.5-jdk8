// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class RenderResponse {
    protected byte[] result;
    protected java.lang.String encoding;
    protected java.lang.String mimeType;
    protected org.jboss.test.webservice.jbws718.ArrayOfParameterValue parametersUsed;
    protected org.jboss.test.webservice.jbws718.ArrayOfWarning warnings;
    protected org.jboss.test.webservice.jbws718.ArrayOfString streamIds;
    
    public RenderResponse() {
    }
    
    public RenderResponse(byte[] result, java.lang.String encoding, java.lang.String mimeType, org.jboss.test.webservice.jbws718.ArrayOfParameterValue parametersUsed, org.jboss.test.webservice.jbws718.ArrayOfWarning warnings, org.jboss.test.webservice.jbws718.ArrayOfString streamIds) {
        this.result = result;
        this.encoding = encoding;
        this.mimeType = mimeType;
        this.parametersUsed = parametersUsed;
        this.warnings = warnings;
        this.streamIds = streamIds;
    }
    
    public byte[] getResult() {
        return result;
    }
    
    public void setResult(byte[] result) {
        this.result = result;
    }
    
    public java.lang.String getEncoding() {
        return encoding;
    }
    
    public void setEncoding(java.lang.String encoding) {
        this.encoding = encoding;
    }
    
    public java.lang.String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(java.lang.String mimeType) {
        this.mimeType = mimeType;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfParameterValue getParametersUsed() {
        return parametersUsed;
    }
    
    public void setParametersUsed(org.jboss.test.webservice.jbws718.ArrayOfParameterValue parametersUsed) {
        this.parametersUsed = parametersUsed;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfWarning getWarnings() {
        return warnings;
    }
    
    public void setWarnings(org.jboss.test.webservice.jbws718.ArrayOfWarning warnings) {
        this.warnings = warnings;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfString getStreamIds() {
        return streamIds;
    }
    
    public void setStreamIds(org.jboss.test.webservice.jbws718.ArrayOfString streamIds) {
        this.streamIds = streamIds;
    }
}
