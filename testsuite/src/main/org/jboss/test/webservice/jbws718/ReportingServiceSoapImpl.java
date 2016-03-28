/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.webservice.jbws718;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.jboss.logging.Logger;

public class ReportingServiceSoapImpl implements ReportingServiceSoap, Remote
{
   // logging support
   protected Logger log = Logger.getLogger(ReportingServiceSoapImpl.class);

   public ArrayOfString listSecureMethods() throws RemoteException
   {
      ArrayOfString _retVal = null;
      return _retVal;
   }

   public String createBatch() throws RemoteException
   {
      String _retVal = null;
      return _retVal;
   }

   public CancelBatchResponse cancelBatch() throws RemoteException
   {

      CancelBatchResponse _retVal = null;
      return _retVal;
   }

   public ExecuteBatchResponse executeBatch() throws RemoteException
   {

      ExecuteBatchResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfProperty getSystemProperties(ArrayOfProperty properties)
         throws RemoteException
   {

      ArrayOfProperty _retVal = null;
      return _retVal;
   }

   public SetSystemPropertiesResponse setSystemProperties(ArrayOfProperty properties)
         throws RemoteException
   {

      SetSystemPropertiesResponse _retVal = null;
      return _retVal;
   }

   public DeleteItemResponse deleteItem(String item) throws RemoteException
   {

      DeleteItemResponse _retVal = null;
      return _retVal;
   }

   public MoveItemResponse moveItem(String item, String target) throws RemoteException
   {

      MoveItemResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfCatalogItem listChildren(String item, boolean recursive) throws RemoteException
   {

      ArrayOfCatalogItem _retVal = null;
      return _retVal;
   }

   public ArrayOfProperty getProperties(String item, ArrayOfProperty properties)
         throws RemoteException
   {

      ArrayOfProperty _retVal = null;
      return _retVal;
   }

   public SetPropertiesResponse setProperties(String item, ArrayOfProperty properties)
         throws RemoteException
   {

      SetPropertiesResponse _retVal = null;
      return _retVal;
   }

   public ItemTypeEnum getItemType(String item) throws RemoteException
   {

      ItemTypeEnum _retVal = null;
      return _retVal;
   }

   public CreateFolderResponse createFolder(String folder, String parent,
         ArrayOfProperty properties) throws RemoteException
   {

      CreateFolderResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfWarning createReport(String report, String parent, boolean overwrite, byte[] definition,
         ArrayOfProperty properties) throws RemoteException
   {

      ArrayOfWarning _retVal = null;
      return _retVal;
   }

   public byte[] getReportDefinition(String report) throws RemoteException
   {

      byte[] _retVal = null;
      return _retVal;
   }

   public ArrayOfWarning setReportDefinition(String report, byte[] definition) throws RemoteException
   {

      ArrayOfWarning _retVal = null;
      return _retVal;
   }

   public CreateResourceResponse createResource(String resource, String parent, boolean overwrite,
         byte[] contents, String mimeType, ArrayOfProperty properties) throws RemoteException
   {

      CreateResourceResponse _retVal = null;
      return _retVal;
   }

   public SetResourceContentsResponse setResourceContents(String resource, byte[] contents, String mimeType)
         throws RemoteException
   {

      SetResourceContentsResponse _retVal = null;
      return _retVal;
   }

   public GetResourceContentsResponse getResourceContents(String resource) throws RemoteException
   {

      GetResourceContentsResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfReportParameter getReportParameters(String report, String historyID, boolean forRendering,
         ArrayOfParameterValue values, ArrayOfDataSourceCredentials credentials)
         throws RemoteException
   {

      ArrayOfReportParameter _retVal = null;
      return _retVal;
   }

   public SetReportParametersResponse setReportParameters(String report,
         ArrayOfReportParameter parameters) throws RemoteException
   {

      SetReportParametersResponse _retVal = null;
      return _retVal;
   }

   public CreateLinkedReportResponse createLinkedReport(String report, String parent, String link,
         ArrayOfProperty properties) throws RemoteException
   {

      CreateLinkedReportResponse _retVal = null;
      return _retVal;
   }

   public String getReportLink(String report) throws RemoteException
   {
      log.info("getReportLink: " + report);
      return report;
   }

   public SetReportLinkResponse setReportLink(String report, String link) throws RemoteException
   {

      SetReportLinkResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfCatalogItem listLinkedReports(String report) throws RemoteException
   {

      ArrayOfCatalogItem _retVal = null;
      return _retVal;
   }

   public RenderResponse render(String report, String format, String historyID,
         String deviceInfo, ArrayOfParameterValue parameters,
         ArrayOfDataSourceCredentials credentials, String showHideToggle) throws RemoteException
   {

      RenderResponse _retVal = null;
      return _retVal;
   }

   public RenderStreamResponse renderStream(String report, String format, String streamID,
         String historyID, String deviceInfo, ArrayOfParameterValue parameters) throws RemoteException
   {

      RenderStreamResponse _retVal = null;
      return _retVal;
   }

   public GetRenderResourceResponse getRenderResource(String format, String deviceInfo)
         throws RemoteException
   {

      GetRenderResourceResponse _retVal = null;
      return _retVal;
   }

   public SetExecutionOptionsResponse setExecutionOptions(javax.xml.soap.SOAPElement parameters) throws RemoteException
   {

      SetExecutionOptionsResponse _retVal = null;
      return _retVal;
   }

   public javax.xml.soap.SOAPElement getExecutionOptions(String report) throws RemoteException
   {

      javax.xml.soap.SOAPElement _retVal = null;
      return _retVal;
   }

   public SetCacheOptionsResponse setCacheOptions(javax.xml.soap.SOAPElement parameters) throws RemoteException
   {

      SetCacheOptionsResponse _retVal = null;
      return _retVal;
   }

   public javax.xml.soap.SOAPElement getCacheOptions(String report) throws RemoteException
   {

      javax.xml.soap.SOAPElement _retVal = null;
      return _retVal;
   }

   public UpdateReportExecutionSnapshotResponse updateReportExecutionSnapshot(String report)
         throws RemoteException
   {

      UpdateReportExecutionSnapshotResponse _retVal = null;
      return _retVal;
   }

   public FlushCacheResponse flushCache(String report) throws RemoteException
   {

      FlushCacheResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfJob listJobs() throws RemoteException
   {

      ArrayOfJob _retVal = null;
      return _retVal;
   }

   public boolean cancelJob(String jobID) throws RemoteException
   {

      boolean _retVal = false;
      return _retVal;
   }

   public CreateDataSourceResponse createDataSource(String dataSource, String parent, boolean overwrite,
         DataSourceDefinition definition, ArrayOfProperty properties)
         throws RemoteException
   {

      CreateDataSourceResponse _retVal = null;
      return _retVal;
   }

   public DataSourceDefinition getDataSourceContents(String dataSource) throws RemoteException
   {

      DataSourceDefinition _retVal = null;
      return _retVal;
   }

   public SetDataSourceContentsResponse setDataSourceContents(String dataSource,
         DataSourceDefinition definition) throws RemoteException
   {

      SetDataSourceContentsResponse _retVal = null;
      return _retVal;
   }

   public EnableDataSourceResponse enableDataSource(String dataSource) throws RemoteException
   {

      EnableDataSourceResponse _retVal = null;
      return _retVal;
   }

   public DisableDataSourceResponse disableDataSource(String dataSource) throws RemoteException
   {

      DisableDataSourceResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfCatalogItem listReportsUsingDataSource(String dataSource) throws RemoteException
   {

      ArrayOfCatalogItem _retVal = null;
      return _retVal;
   }

   public SetReportDataSourcesResponse setReportDataSources(String report,
         ArrayOfDataSource dataSources) throws RemoteException
   {

      SetReportDataSourcesResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfDataSource getReportDataSources(String report) throws RemoteException
   {

      ArrayOfDataSource _retVal = null;
      return _retVal;
   }

   public ArrayOfDataSourcePrompt getReportDataSourcePrompts(String report) throws RemoteException
   {

      ArrayOfDataSourcePrompt _retVal = null;
      return _retVal;
   }

   public CreateReportHistorySnapshotResponse createReportHistorySnapshot(String report) throws RemoteException
   {

      CreateReportHistorySnapshotResponse _retVal = null;
      return _retVal;
   }

   public SetReportHistoryOptionsResponse setReportHistoryOptions(javax.xml.soap.SOAPElement parameters)
         throws RemoteException
   {

      SetReportHistoryOptionsResponse _retVal = null;
      return _retVal;
   }

   public javax.xml.soap.SOAPElement getReportHistoryOptions(String report) throws RemoteException
   {

      javax.xml.soap.SOAPElement _retVal = null;
      return _retVal;
   }

   public SetReportHistoryLimitResponse setReportHistoryLimit(String report, boolean useSystem, int historyLimit)
         throws RemoteException
   {

      SetReportHistoryLimitResponse _retVal = null;
      return _retVal;
   }

   public GetReportHistoryLimitResponse getReportHistoryLimit(String report) throws RemoteException
   {

      GetReportHistoryLimitResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfReportHistorySnapshot listReportHistory(String report) throws RemoteException
   {

      ArrayOfReportHistorySnapshot _retVal = null;
      return _retVal;
   }

   public DeleteReportHistorySnapshotResponse deleteReportHistorySnapshot(String report, String historyID)
         throws RemoteException
   {

      DeleteReportHistorySnapshotResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfCatalogItem findItems(String folder,
         BooleanOperatorEnum booleanOperator, ArrayOfSearchCondition conditions)
         throws RemoteException
   {

      ArrayOfCatalogItem _retVal = null;
      return _retVal;
   }

   public String createSchedule(String name, javax.xml.soap.SOAPElement scheduleDefinition) throws RemoteException
   {

      String _retVal = null;
      return _retVal;
   }

   public DeleteScheduleResponse deleteSchedule(String scheduleID) throws RemoteException
   {

      DeleteScheduleResponse _retVal = null;
      return _retVal;
   }

   public SetSchedulePropertiesResponse setScheduleProperties(String name, String scheduleID,
         javax.xml.soap.SOAPElement scheduleDefinition) throws RemoteException
   {

      SetSchedulePropertiesResponse _retVal = null;
      return _retVal;
   }

   public Schedule getScheduleProperties(String scheduleID) throws RemoteException
   {

      Schedule _retVal = null;
      return _retVal;
   }

   public ArrayOfCatalogItem listScheduledReports(String scheduleID) throws RemoteException
   {

      ArrayOfCatalogItem _retVal = null;
      return _retVal;
   }

   public ArrayOfSchedule listSchedules() throws RemoteException
   {

      ArrayOfSchedule _retVal = null;
      return _retVal;
   }

   public PauseScheduleResponse pauseSchedule(String scheduleID) throws RemoteException
   {

      PauseScheduleResponse _retVal = null;
      return _retVal;
   }

   public ResumeScheduleResponse resumeSchedule(String scheduleID) throws RemoteException
   {

      ResumeScheduleResponse _retVal = null;
      return _retVal;
   }

   public String createSubscription(String report, ExtensionSettings extensionSettings,
         String description, String eventType, String matchData, ArrayOfParameterValue parameters)
         throws RemoteException
   {

      String _retVal = null;
      return _retVal;
   }

   public String createDataDrivenSubscription(String report, ExtensionSettings extensionSettings,
         javax.xml.soap.SOAPElement dataRetrievalPlan, String description, String eventType, String matchData,
         ArrayOfParameterValueOrFieldReference parameters) throws RemoteException
   {

      String _retVal = null;
      return _retVal;
   }

   public SetSubscriptionPropertiesResponse setSubscriptionProperties(String subscriptionID,
         ExtensionSettings extensionSettings, String description, String eventType, String matchData,
         ArrayOfParameterValue parameters) throws RemoteException
   {

      SetSubscriptionPropertiesResponse _retVal = null;
      return _retVal;
   }

   public SetDataDrivenSubscriptionPropertiesResponse setDataDrivenSubscriptionProperties(String dataDrivenSubscriptionID,
         ExtensionSettings extensionSettings, javax.xml.soap.SOAPElement dataRetrievalPlan, String description,
         String eventType, String matchData, ArrayOfParameterValueOrFieldReference parameters)
         throws RemoteException
   {

      SetDataDrivenSubscriptionPropertiesResponse _retVal = null;
      return _retVal;
   }

   public GetSubscriptionPropertiesResponse getSubscriptionProperties(String subscriptionID)
         throws RemoteException
   {

      GetSubscriptionPropertiesResponse _retVal = null;
      return _retVal;
   }

   public GetDataDrivenSubscriptionPropertiesResponse getDataDrivenSubscriptionProperties(String dataDrivenSubscriptionID)
         throws RemoteException
   {

      GetDataDrivenSubscriptionPropertiesResponse _retVal = null;
      return _retVal;
   }

   public DeleteSubscriptionResponse deleteSubscription(String subscriptionID) throws RemoteException
   {

      DeleteSubscriptionResponse _retVal = null;
      return _retVal;
   }

   public PrepareQueryResponse prepareQuery(javax.xml.soap.SOAPElement dataSource,
         DataSetDefinition dataSet) throws RemoteException
   {

      PrepareQueryResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfExtensionParameter getExtensionSettings(String extension) throws RemoteException
   {

      ArrayOfExtensionParameter _retVal = null;
      return _retVal;
   }

   public ArrayOfExtensionParameter validateExtensionSettings(String extension,
         ArrayOfParameterValueOrFieldReference parameterValues) throws RemoteException
   {

      ArrayOfExtensionParameter _retVal = null;
      return _retVal;
   }

   public ArrayOfSubscription listSubscriptions(String report, String owner) throws RemoteException
   {

      ArrayOfSubscription _retVal = null;
      return _retVal;
   }

   public ArrayOfSubscription listSubscriptionsUsingDataSource(String dataSource) throws RemoteException
   {

      ArrayOfSubscription _retVal = null;
      return _retVal;
   }

   public ArrayOfExtension listExtensions(ExtensionTypeEnum extensionType)
         throws RemoteException
   {

      ArrayOfExtension _retVal = null;
      return _retVal;
   }

   public ArrayOfEvent listEvents() throws RemoteException
   {

      ArrayOfEvent _retVal = null;
      return _retVal;
   }

   public FireEventResponse fireEvent(String eventType, String eventData) throws RemoteException
   {

      FireEventResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfTask listSystemTasks() throws RemoteException
   {

      ArrayOfTask _retVal = null;
      return _retVal;
   }

   public ArrayOfTask listTasks() throws RemoteException
   {

      ArrayOfTask _retVal = null;
      return _retVal;
   }

   public ArrayOfRole listSystemRoles() throws RemoteException
   {

      ArrayOfRole _retVal = null;
      return _retVal;
   }

   public ArrayOfRole listRoles() throws RemoteException
   {

      ArrayOfRole _retVal = null;
      return _retVal;
   }

   public CreateRoleResponse createRole(String name, String description,
         ArrayOfTask tasks) throws RemoteException
   {

      CreateRoleResponse _retVal = null;
      return _retVal;
   }

   public DeleteRoleResponse deleteRole(String name) throws RemoteException
   {

      DeleteRoleResponse _retVal = null;
      return _retVal;
   }

   public GetRolePropertiesResponse getRoleProperties(String name) throws RemoteException
   {

      GetRolePropertiesResponse _retVal = null;
      return _retVal;
   }

   public SetRolePropertiesResponse setRoleProperties(String name, String description,
         ArrayOfTask tasks) throws RemoteException
   {

      SetRolePropertiesResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfPolicy getSystemPolicies() throws RemoteException
   {

      ArrayOfPolicy _retVal = null;
      return _retVal;
   }

   public SetSystemPoliciesResponse setSystemPolicies(ArrayOfPolicy policies)
         throws RemoteException
   {

      SetSystemPoliciesResponse _retVal = null;
      return _retVal;
   }

   public GetPoliciesResponse getPolicies(String item) throws RemoteException
   {

      GetPoliciesResponse _retVal = null;
      return _retVal;
   }

   public SetPoliciesResponse setPolicies(String item, ArrayOfPolicy policies)
         throws RemoteException
   {

      SetPoliciesResponse _retVal = null;
      return _retVal;
   }

   public InheritParentSecurityResponse inheritParentSecurity(String item) throws RemoteException
   {

      InheritParentSecurityResponse _retVal = null;
      return _retVal;
   }

   public ArrayOfString3 getSystemPermissions() throws RemoteException
   {

      ArrayOfString3 _retVal = null;
      return _retVal;
   }

   public ArrayOfString3 getPermissions(String item) throws RemoteException
   {

      ArrayOfString3 _retVal = null;
      return _retVal;
   }

   public LogonUserResponse logonUser(String userName, String password, String authority)
         throws RemoteException
   {

      LogonUserResponse _retVal = null;
      return _retVal;
   }

   public LogoffResponse logoff() throws RemoteException
   {

      LogoffResponse _retVal = null;
      return _retVal;
   }
}
