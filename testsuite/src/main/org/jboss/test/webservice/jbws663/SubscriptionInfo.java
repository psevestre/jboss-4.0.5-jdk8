// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.webservice.jbws663;


public class SubscriptionInfo {
    protected int licenseStatusCode;
    protected java.lang.String licenseStatus;
    protected int licenseActionCode;
    protected java.lang.String licenseAction;
    protected int remainingHits;
    protected java.math.BigDecimal amount;
    
    public SubscriptionInfo() {
    }
    
    public SubscriptionInfo(int licenseStatusCode, java.lang.String licenseStatus, int licenseActionCode, java.lang.String licenseAction, int remainingHits, java.math.BigDecimal amount) {
        this.licenseStatusCode = licenseStatusCode;
        this.licenseStatus = licenseStatus;
        this.licenseActionCode = licenseActionCode;
        this.licenseAction = licenseAction;
        this.remainingHits = remainingHits;
        this.amount = amount;
    }
    
    public int getLicenseStatusCode() {
        return licenseStatusCode;
    }
    
    public void setLicenseStatusCode(int licenseStatusCode) {
        this.licenseStatusCode = licenseStatusCode;
    }
    
    public java.lang.String getLicenseStatus() {
        return licenseStatus;
    }
    
    public void setLicenseStatus(java.lang.String licenseStatus) {
        this.licenseStatus = licenseStatus;
    }
    
    public int getLicenseActionCode() {
        return licenseActionCode;
    }
    
    public void setLicenseActionCode(int licenseActionCode) {
        this.licenseActionCode = licenseActionCode;
    }
    
    public java.lang.String getLicenseAction() {
        return licenseAction;
    }
    
    public void setLicenseAction(java.lang.String licenseAction) {
        this.licenseAction = licenseAction;
    }
    
    public int getRemainingHits() {
        return remainingHits;
    }
    
    public void setRemainingHits(int remainingHits) {
        this.remainingHits = remainingHits;
    }
    
    public java.math.BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }
}
