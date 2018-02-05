/**
 * PCRFSoapCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.chinamobile.pcrf;


/**
 *  PCRFSoapCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class PCRFSoapCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public PCRFSoapCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public PCRFSoapCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for updateUsrLocation method
     * override this method for handling normal response from updateUsrLocation operation
     */
    public void receiveResultupdateUsrLocation(
        com.chinamobile.pcrf.PCRFSoapStub.UpdateUsrLocationResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateUsrLocation operation
     */
    public void receiveErrorupdateUsrLocation(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateSubscriber method
     * override this method for handling normal response from updateSubscriber operation
     */
    public void receiveResultupdateSubscriber(
        com.chinamobile.pcrf.PCRFSoapStub.UpdateSubscriberResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateSubscriber operation
     */
    public void receiveErrorupdateSubscriber(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for unSubscribeService method
     * override this method for handling normal response from unSubscribeService operation
     */
    public void receiveResultunSubscribeService(
        com.chinamobile.pcrf.PCRFSoapStub.UnSubscribeServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from unSubscribeService operation
     */
    public void receiveErrorunSubscribeService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateBatSubscriber method
     * override this method for handling normal response from updateBatSubscriber operation
     */
    public void receiveResultupdateBatSubscriber(
        com.chinamobile.pcrf.PCRFSoapStub.UpdateBatSubscriberResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateBatSubscriber operation
     */
    public void receiveErrorupdateBatSubscriber(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for delSubscriber method
     * override this method for handling normal response from delSubscriber operation
     */
    public void receiveResultdelSubscriber(
        com.chinamobile.pcrf.PCRFSoapStub.DelSubscriberResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from delSubscriber operation
     */
    public void receiveErrordelSubscriber(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateBatService method
     * override this method for handling normal response from updateBatService operation
     */
    public void receiveResultupdateBatService(
        com.chinamobile.pcrf.PCRFSoapStub.UpdateBatServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateBatService operation
     */
    public void receiveErrorupdateBatService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addBatSubscriber method
     * override this method for handling normal response from addBatSubscriber operation
     */
    public void receiveResultaddBatSubscriber(
        com.chinamobile.pcrf.PCRFSoapStub.AddBatSubscriberResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addBatSubscriber operation
     */
    public void receiveErroraddBatSubscriber(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addSubscriber method
     * override this method for handling normal response from addSubscriber operation
     */
    public void receiveResultaddSubscriber(
        com.chinamobile.pcrf.PCRFSoapStub.AddSubscriberResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addSubscriber operation
     */
    public void receiveErroraddSubscriber(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for delBatSubscriber method
     * override this method for handling normal response from delBatSubscriber operation
     */
    public void receiveResultdelBatSubscriber(
        com.chinamobile.pcrf.PCRFSoapStub.DelBatSubscriberResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from delBatSubscriber operation
     */
    public void receiveErrordelBatSubscriber(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for subscribeService method
     * override this method for handling normal response from subscribeService operation
     */
    public void receiveResultsubscribeService(
        com.chinamobile.pcrf.PCRFSoapStub.SubscribeServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from subscribeService operation
     */
    public void receiveErrorsubscribeService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSubscriberAllUsrSessionPolicy method
     * override this method for handling normal response from getSubscriberAllUsrSessionPolicy operation
     */
    public void receiveResultgetSubscriberAllUsrSessionPolicy(
        com.chinamobile.pcrf.PCRFSoapStub.GetSubscriberAllUsrSessionPolicyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSubscriberAllUsrSessionPolicy operation
     */
    public void receiveErrorgetSubscriberAllUsrSessionPolicy(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateUsrSessionPolicy method
     * override this method for handling normal response from updateUsrSessionPolicy operation
     */
    public void receiveResultupdateUsrSessionPolicy(
        com.chinamobile.pcrf.PCRFSoapStub.UpdateUsrSessionPolicyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateUsrSessionPolicy operation
     */
    public void receiveErrorupdateUsrSessionPolicy(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getUsrLocation method
     * override this method for handling normal response from getUsrLocation operation
     */
    public void receiveResultgetUsrLocation(
        com.chinamobile.pcrf.PCRFSoapStub.GetUsrLocationResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getUsrLocation operation
     */
    public void receiveErrorgetUsrLocation(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addBatService method
     * override this method for handling normal response from addBatService operation
     */
    public void receiveResultaddBatService(
        com.chinamobile.pcrf.PCRFSoapStub.AddBatServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addBatService operation
     */
    public void receiveErroraddBatService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for pCRF_CHKBAT method
     * override this method for handling normal response from pCRF_CHKBAT operation
     */
    public void receiveResultpCRF_CHKBAT(
        com.chinamobile.pcrf.PCRFSoapStub.PCRF_CHKBATResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from pCRF_CHKBAT operation
     */
    public void receiveErrorpCRF_CHKBAT(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addBatUsrSessionPolicy method
     * override this method for handling normal response from addBatUsrSessionPolicy operation
     */
    public void receiveResultaddBatUsrSessionPolicy(
        com.chinamobile.pcrf.PCRFSoapStub.AddBatUsrSessionPolicyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addBatUsrSessionPolicy operation
     */
    public void receiveErroraddBatUsrSessionPolicy(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addUsrLocation method
     * override this method for handling normal response from addUsrLocation operation
     */
    public void receiveResultaddUsrLocation(
        com.chinamobile.pcrf.PCRFSoapStub.AddUsrLocationResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addUsrLocation operation
     */
    public void receiveErroraddUsrLocation(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSubscriberAllService method
     * override this method for handling normal response from getSubscriberAllService operation
     */
    public void receiveResultgetSubscriberAllService(
        com.chinamobile.pcrf.PCRFSoapStub.GetSubscriberAllServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSubscriberAllService operation
     */
    public void receiveErrorgetSubscriberAllService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSubscriber method
     * override this method for handling normal response from getSubscriber operation
     */
    public void receiveResultgetSubscriber(
        com.chinamobile.pcrf.PCRFSoapStub.GetSubscriberResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSubscriber operation
     */
    public void receiveErrorgetSubscriber(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for delBatService method
     * override this method for handling normal response from delBatService operation
     */
    public void receiveResultdelBatService(
        com.chinamobile.pcrf.PCRFSoapStub.DelBatServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from delBatService operation
     */
    public void receiveErrordelBatService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for delUsrLocation method
     * override this method for handling normal response from delUsrLocation operation
     */
    public void receiveResultdelUsrLocation(
        com.chinamobile.pcrf.PCRFSoapStub.DelUsrLocationResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from delUsrLocation operation
     */
    public void receiveErrordelUsrLocation(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for unsubscribeUsrSessionPolicy method
     * override this method for handling normal response from unsubscribeUsrSessionPolicy operation
     */
    public void receiveResultunsubscribeUsrSessionPolicy(
        com.chinamobile.pcrf.PCRFSoapStub.UnsubscribeUsrSessionPolicyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from unsubscribeUsrSessionPolicy operation
     */
    public void receiveErrorunsubscribeUsrSessionPolicy(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateSubscribedService method
     * override this method for handling normal response from updateSubscribedService operation
     */
    public void receiveResultupdateSubscribedService(
        com.chinamobile.pcrf.PCRFSoapStub.UpdateSubscribedServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateSubscribedService operation
     */
    public void receiveErrorupdateSubscribedService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for delBatUsrSessionPolicy method
     * override this method for handling normal response from delBatUsrSessionPolicy operation
     */
    public void receiveResultdelBatUsrSessionPolicy(
        com.chinamobile.pcrf.PCRFSoapStub.DelBatUsrSessionPolicyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from delBatUsrSessionPolicy operation
     */
    public void receiveErrordelBatUsrSessionPolicy(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateBatUsrSessionPolicy method
     * override this method for handling normal response from updateBatUsrSessionPolicy operation
     */
    public void receiveResultupdateBatUsrSessionPolicy(
        com.chinamobile.pcrf.PCRFSoapStub.UpdateBatUsrSessionPolicyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateBatUsrSessionPolicy operation
     */
    public void receiveErrorupdateBatUsrSessionPolicy(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for subscribeUsrSessionPolicy method
     * override this method for handling normal response from subscribeUsrSessionPolicy operation
     */
    public void receiveResultsubscribeUsrSessionPolicy(
        com.chinamobile.pcrf.PCRFSoapStub.SubscribeUsrSessionPolicyResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from subscribeUsrSessionPolicy operation
     */
    public void receiveErrorsubscribeUsrSessionPolicy(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSubscriberAllInf method
     * override this method for handling normal response from getSubscriberAllInf operation
     */
    public void receiveResultgetSubscriberAllInf(
        com.chinamobile.pcrf.PCRFSoapStub.GetSubscriberAllInfResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSubscriberAllInf operation
     */
    public void receiveErrorgetSubscriberAllInf(java.lang.Exception e) {
    }
}
