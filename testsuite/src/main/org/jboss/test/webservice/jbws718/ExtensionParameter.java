// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class ExtensionParameter {
    protected java.lang.String name;
    protected java.lang.String displayName;
    protected java.lang.Boolean required;
    protected boolean readOnly;
    protected java.lang.String value;
    protected java.lang.String error;
    protected boolean encrypted;
    protected boolean isPassword;
    protected org.jboss.test.webservice.jbws718.ArrayOfValidValue1 validValues;
    
    public ExtensionParameter() {
    }
    
    public ExtensionParameter(java.lang.String name, java.lang.String displayName, java.lang.Boolean required, boolean readOnly, java.lang.String value, java.lang.String error, boolean encrypted, boolean isPassword, org.jboss.test.webservice.jbws718.ArrayOfValidValue1 validValues) {
        this.name = name;
        this.displayName = displayName;
        this.required = required;
        this.readOnly = readOnly;
        this.value = value;
        this.error = error;
        this.encrypted = encrypted;
        this.isPassword = isPassword;
        this.validValues = validValues;
    }
    
    public java.lang.String getName() {
        return name;
    }
    
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    public java.lang.String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }
    
    public java.lang.Boolean getRequired() {
        return required;
    }
    
    public void setRequired(java.lang.Boolean required) {
        this.required = required;
    }
    
    public boolean isReadOnly() {
        return readOnly;
    }
    
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public void setValue(java.lang.String value) {
        this.value = value;
    }
    
    public java.lang.String getError() {
        return error;
    }
    
    public void setError(java.lang.String error) {
        this.error = error;
    }
    
    public boolean isEncrypted() {
        return encrypted;
    }
    
    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
    
    public boolean isIsPassword() {
        return isPassword;
    }
    
    public void setIsPassword(boolean isPassword) {
        this.isPassword = isPassword;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfValidValue1 getValidValues() {
        return validValues;
    }
    
    public void setValidValues(org.jboss.test.webservice.jbws718.ArrayOfValidValue1 validValues) {
        this.validValues = validValues;
    }
}
