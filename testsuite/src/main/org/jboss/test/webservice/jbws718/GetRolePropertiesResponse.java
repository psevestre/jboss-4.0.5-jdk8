// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class GetRolePropertiesResponse {
    protected org.jboss.test.webservice.jbws718.ArrayOfTask tasks;
    protected java.lang.String description;
    
    public GetRolePropertiesResponse() {
    }
    
    public GetRolePropertiesResponse(org.jboss.test.webservice.jbws718.ArrayOfTask tasks, java.lang.String description) {
        this.tasks = tasks;
        this.description = description;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfTask getTasks() {
        return tasks;
    }
    
    public void setTasks(org.jboss.test.webservice.jbws718.ArrayOfTask tasks) {
        this.tasks = tasks;
    }
    
    public java.lang.String getDescription() {
        return description;
    }
    
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
}
