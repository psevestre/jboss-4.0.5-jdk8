// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, build R40)
// Generated source version: 1.1.2

package org.jboss.test.webservice.marshalltest._arrays.java.lang;


public class StringArray {
    private java.lang.String[] value;
    
    public StringArray() {
    }
    
    public StringArray(java.lang.String[] sourceArray) {
        value = sourceArray;
    }
    
    public void fromArray(java.lang.String[] sourceArray) {
        this.value = sourceArray;
    }
    
    public java.lang.String[] toArray() {
        return value;
    }
    
    public java.lang.String[] getValue() {
        return value;
    }
    
    public void setValue(java.lang.String[] value) {
        this.value = value;
    }
}
