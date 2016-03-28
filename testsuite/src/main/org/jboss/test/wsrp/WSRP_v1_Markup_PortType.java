// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.wsrp;
 
import org.jboss.test.wsrp.core.*;
 
public interface WSRP_v1_Markup_PortType extends java.rmi.Remote
{
   public MarkupResponse getMarkup( GetMarkup getMarkup) 
   throws UnsupportedWindowStateFault,  
       InvalidCookieFault, 
        InvalidSessionFault, 
        AccessDeniedFault, 
        InconsistentParametersFault, 
        InvalidHandleFault, 
        UnsupportedLocaleFault, 
        UnsupportedModeFault, 
        OperationFailedFault, 
        MissingParametersFault, 
        InvalidUserCategoryFault, 
        InvalidRegistrationFault, 
        UnsupportedMimeTypeFault, 
       java.rmi.RemoteException;

   public BlockingInteractionResponse performBlockingInteraction(PerformBlockingInteraction performBlockingInteraction) throws
      InvalidSessionFault, 
      UnsupportedModeFault, 
      UnsupportedMimeTypeFault, 
      OperationFailedFault, 
      UnsupportedWindowStateFault, 
      UnsupportedLocaleFault, 
      AccessDeniedFault, 
      PortletStateChangeRequiredFault, 
      InvalidRegistrationFault, 
      MissingParametersFault, 
      InvalidUserCategoryFault, 
      InconsistentParametersFault, 
      InvalidHandleFault, 
      InvalidCookieFault, 
      java.rmi.RemoteException;

   public ReturnAny releaseSessions(ReleaseSessions releaseSessions) throws
      InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, AccessDeniedFault, java.rmi.RemoteException;

   public ReturnAny initCookie(InitCookie initCookie) throws
      AccessDeniedFault, OperationFailedFault, InvalidRegistrationFault, java.rmi.RemoteException;
}