// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class DataSetDefinition {
    protected org.jboss.test.webservice.jbws718.ArrayOfField fields;
    protected org.jboss.test.webservice.jbws718.QueryDefinition query;
    protected org.jboss.test.webservice.jbws718.SensitivityEnum caseSensitivity;
    protected java.lang.String collation;
    protected org.jboss.test.webservice.jbws718.SensitivityEnum accentSensitivity;
    protected org.jboss.test.webservice.jbws718.SensitivityEnum kanatypeSensitivity;
    protected org.jboss.test.webservice.jbws718.SensitivityEnum widthSensitivity;
    protected java.lang.String name;
    
    public DataSetDefinition() {
    }
    
    public DataSetDefinition(org.jboss.test.webservice.jbws718.ArrayOfField fields, org.jboss.test.webservice.jbws718.QueryDefinition query, org.jboss.test.webservice.jbws718.SensitivityEnum caseSensitivity, java.lang.String collation, org.jboss.test.webservice.jbws718.SensitivityEnum accentSensitivity, org.jboss.test.webservice.jbws718.SensitivityEnum kanatypeSensitivity, org.jboss.test.webservice.jbws718.SensitivityEnum widthSensitivity, java.lang.String name) {
        this.fields = fields;
        this.query = query;
        this.caseSensitivity = caseSensitivity;
        this.collation = collation;
        this.accentSensitivity = accentSensitivity;
        this.kanatypeSensitivity = kanatypeSensitivity;
        this.widthSensitivity = widthSensitivity;
        this.name = name;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfField getFields() {
        return fields;
    }
    
    public void setFields(org.jboss.test.webservice.jbws718.ArrayOfField fields) {
        this.fields = fields;
    }
    
    public org.jboss.test.webservice.jbws718.QueryDefinition getQuery() {
        return query;
    }
    
    public void setQuery(org.jboss.test.webservice.jbws718.QueryDefinition query) {
        this.query = query;
    }
    
    public org.jboss.test.webservice.jbws718.SensitivityEnum getCaseSensitivity() {
        return caseSensitivity;
    }
    
    public void setCaseSensitivity(org.jboss.test.webservice.jbws718.SensitivityEnum caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
    }
    
    public java.lang.String getCollation() {
        return collation;
    }
    
    public void setCollation(java.lang.String collation) {
        this.collation = collation;
    }
    
    public org.jboss.test.webservice.jbws718.SensitivityEnum getAccentSensitivity() {
        return accentSensitivity;
    }
    
    public void setAccentSensitivity(org.jboss.test.webservice.jbws718.SensitivityEnum accentSensitivity) {
        this.accentSensitivity = accentSensitivity;
    }
    
    public org.jboss.test.webservice.jbws718.SensitivityEnum getKanatypeSensitivity() {
        return kanatypeSensitivity;
    }
    
    public void setKanatypeSensitivity(org.jboss.test.webservice.jbws718.SensitivityEnum kanatypeSensitivity) {
        this.kanatypeSensitivity = kanatypeSensitivity;
    }
    
    public org.jboss.test.webservice.jbws718.SensitivityEnum getWidthSensitivity() {
        return widthSensitivity;
    }
    
    public void setWidthSensitivity(org.jboss.test.webservice.jbws718.SensitivityEnum widthSensitivity) {
        this.widthSensitivity = widthSensitivity;
    }
    
    public java.lang.String getName() {
        return name;
    }
    
    public void setName(java.lang.String name) {
        this.name = name;
    }
}
