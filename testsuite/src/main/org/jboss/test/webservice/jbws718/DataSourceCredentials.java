// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class DataSourceCredentials {
    protected java.lang.String dataSourceName;
    protected java.lang.String userName;
    protected java.lang.String password;
    
    public DataSourceCredentials() {
    }
    
    public DataSourceCredentials(java.lang.String dataSourceName, java.lang.String userName, java.lang.String password) {
        this.dataSourceName = dataSourceName;
        this.userName = userName;
        this.password = password;
    }
    
    public java.lang.String getDataSourceName() {
        return dataSourceName;
    }
    
    public void setDataSourceName(java.lang.String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    
    public java.lang.String getUserName() {
        return userName;
    }
    
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    
    public java.lang.String getPassword() {
        return password;
    }
    
    public void setPassword(java.lang.String password) {
        this.password = password;
    }
}