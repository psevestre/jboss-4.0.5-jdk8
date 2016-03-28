// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


import java.util.Map;
import java.util.HashMap;

public class ScheduleStateEnum {
    private java.lang.String value;
    private static java.util.Map valueMap = new HashMap();
    public static final java.lang.String _ReadyString = "Ready";
    public static final java.lang.String _RunningString = "Running";
    public static final java.lang.String _PausedString = "Paused";
    public static final java.lang.String _ExpiredString = "Expired";
    public static final java.lang.String _FailingString = "Failing";
    
    public static final java.lang.String _Ready = new java.lang.String(_ReadyString);
    public static final java.lang.String _Running = new java.lang.String(_RunningString);
    public static final java.lang.String _Paused = new java.lang.String(_PausedString);
    public static final java.lang.String _Expired = new java.lang.String(_ExpiredString);
    public static final java.lang.String _Failing = new java.lang.String(_FailingString);
    
    public static final ScheduleStateEnum Ready = new ScheduleStateEnum(_Ready);
    public static final ScheduleStateEnum Running = new ScheduleStateEnum(_Running);
    public static final ScheduleStateEnum Paused = new ScheduleStateEnum(_Paused);
    public static final ScheduleStateEnum Expired = new ScheduleStateEnum(_Expired);
    public static final ScheduleStateEnum Failing = new ScheduleStateEnum(_Failing);
    
    protected ScheduleStateEnum(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static ScheduleStateEnum fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (Ready.value.equals(value)) {
            return Ready;
        } else if (Running.value.equals(value)) {
            return Running;
        } else if (Paused.value.equals(value)) {
            return Paused;
        } else if (Expired.value.equals(value)) {
            return Expired;
        } else if (Failing.value.equals(value)) {
            return Failing;
        }
        throw new java.lang.IllegalArgumentException();
    }
    
    public static ScheduleStateEnum fromString(java.lang.String value)
        throws java.lang.IllegalStateException {
        ScheduleStateEnum ret = (ScheduleStateEnum)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_ReadyString)) {
            return Ready;
        } else if (value.equals(_RunningString)) {
            return Running;
        } else if (value.equals(_PausedString)) {
            return Paused;
        } else if (value.equals(_ExpiredString)) {
            return Expired;
        } else if (value.equals(_FailingString)) {
            return Failing;
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
        if (!(obj instanceof ScheduleStateEnum)) {
            return false;
        }
        return ((ScheduleStateEnum)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}