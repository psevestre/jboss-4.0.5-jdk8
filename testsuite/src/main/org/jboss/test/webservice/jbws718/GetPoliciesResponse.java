// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws718;


public class GetPoliciesResponse {
    protected org.jboss.test.webservice.jbws718.ArrayOfPolicy policies;
    protected boolean inheritParent;
    
    public GetPoliciesResponse() {
    }
    
    public GetPoliciesResponse(org.jboss.test.webservice.jbws718.ArrayOfPolicy policies, boolean inheritParent) {
        this.policies = policies;
        this.inheritParent = inheritParent;
    }
    
    public org.jboss.test.webservice.jbws718.ArrayOfPolicy getPolicies() {
        return policies;
    }
    
    public void setPolicies(org.jboss.test.webservice.jbws718.ArrayOfPolicy policies) {
        this.policies = policies;
    }
    
    public boolean isInheritParent() {
        return inheritParent;
    }
    
    public void setInheritParent(boolean inheritParent) {
        this.inheritParent = inheritParent;
    }
}
