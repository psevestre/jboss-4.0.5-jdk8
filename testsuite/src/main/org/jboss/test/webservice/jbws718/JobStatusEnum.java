// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


import java.util.Map;
import java.util.HashMap;

public class JobStatusEnum {
    private java.lang.String value;
    private static java.util.Map valueMap = new HashMap();
    public static final java.lang.String _NewString = "New";
    public static final java.lang.String _RunningString = "Running";
    public static final java.lang.String _CancelRequestedString = "CancelRequested";
    
    public static final java.lang.String _New = new java.lang.String(_NewString);
    public static final java.lang.String _Running = new java.lang.String(_RunningString);
    public static final java.lang.String _CancelRequested = new java.lang.String(_CancelRequestedString);
    
    public static final JobStatusEnum New = new JobStatusEnum(_New);
    public static final JobStatusEnum Running = new JobStatusEnum(_Running);
    public static final JobStatusEnum CancelRequested = new JobStatusEnum(_CancelRequested);
    
    protected JobStatusEnum(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static JobStatusEnum fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (New.value.equals(value)) {
            return New;
        } else if (Running.value.equals(value)) {
            return Running;
        } else if (CancelRequested.value.equals(value)) {
            return CancelRequested;
        }
        throw new java.lang.IllegalArgumentException();
    }
    
    public static JobStatusEnum fromString(java.lang.String value)
        throws java.lang.IllegalStateException {
        JobStatusEnum ret = (JobStatusEnum)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_NewString)) {
            return New;
        } else if (value.equals(_RunningString)) {
            return Running;
        } else if (value.equals(_CancelRequestedString)) {
            return CancelRequested;
        }
        throw new IllegalArgumentException();
    }
    
    public java.lang.String toString() {
        return value.toString();
    }
    
    private java.lang.Object readResolve()
        throws java.io.ObjectStreamException {
        return fromValue(getValue());
    }
    
    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobStatusEnum)) {
            return false;
        }
        return ((JobStatusEnum)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
