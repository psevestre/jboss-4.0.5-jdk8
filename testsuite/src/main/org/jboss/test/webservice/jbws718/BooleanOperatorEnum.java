// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


import java.util.Map;
import java.util.HashMap;

public class BooleanOperatorEnum {
    private java.lang.String value;
    private static java.util.Map valueMap = new HashMap();
    public static final java.lang.String _AndString = "And";
    public static final java.lang.String _OrString = "Or";
    
    public static final java.lang.String _And = new java.lang.String(_AndString);
    public static final java.lang.String _Or = new java.lang.String(_OrString);
    
    public static final BooleanOperatorEnum And = new BooleanOperatorEnum(_And);
    public static final BooleanOperatorEnum Or = new BooleanOperatorEnum(_Or);
    
    protected BooleanOperatorEnum(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static BooleanOperatorEnum fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (And.value.equals(value)) {
            return And;
        } else if (Or.value.equals(value)) {
            return Or;
        }
        throw new java.lang.IllegalArgumentException();
    }
    
    public static BooleanOperatorEnum fromString(java.lang.String value)
        throws java.lang.IllegalStateException {
        BooleanOperatorEnum ret = (BooleanOperatorEnum)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_AndString)) {
            return And;
        } else if (value.equals(_OrString)) {
            return Or;
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
        if (!(obj instanceof BooleanOperatorEnum)) {
            return false;
        }
        return ((BooleanOperatorEnum)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
