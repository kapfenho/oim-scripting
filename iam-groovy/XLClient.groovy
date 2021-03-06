import Thor.API.*
import Thor.API.Operations.*
import com.thortech.xl.util.config.ConfigurationClient
import com.thortech.xl.crypto.tcCryptoUtil
import com.thortech.xl.crypto.tcSignatureMessage

import oracle.iam.platform.OIMClient

import oracle.iam.identity.usermgmt.api.UserManager
import oracle.iam.identity.rolemgmt.api.RoleManager
import oracle.iam.request.api.RequestDataSetService

import oracle.iam.provisioning.api.ProvisioningServiceInternal
import oracle.iam.provisioning.api.ApplicationInstanceService

class XLClient {

    // constants
    static USR_INTF = "Thor.API.Operations.tcUserOperationsIntf"
    static IMP_INTF = "Thor.API.Operations.tcImportOperationsIntf"
    static EXP_INTF = "Thor.API.Operations.tcExportOperationsIntf"
    static ORG_INTF = "Thor.API.Operations.tcOrganizationOperationsIntf"
    static GRP_INTF = "Thor.API.Operations.tcGroupOperationsIntf"
    static RECON_INTF = "Thor.API.Operations.tcReconciliationOperationsIntf"
    static OBJ_INTF = "Thor.API.Operations.tcObjectOperationsIntf"
    static PROP_INTF = "Thor.API.Operations.tcPropertyOperationsIntf"
    static ITDEF_INTF = "Thor.API.Operations.tcITResourceDefinitionOperationsIntf"
    static ITINST_INTF = "Thor.API.Operations.tcITResourceInstanceOperationsIntf"
    static AP_INTF = "Thor.API.Operations.tcAccessPolicyOperationsIntf"
    static FI_INTF = "Thor.API.Operations.tcFormInstanceOperationsIntf"
    
    // service interfaces
    def usrMgr
    def roleMgr
    def provIntSvc
    def aiSvc
    def rdsSvc

    // api interfaces
    def usrIntf
    def impIntf
    def expIntf
    def orgIntf
    def grpIntf
    def reconIntf
    def objIntf
    def propIntf
    def itInstIntf
    def itDefIntf
    def apIntf
    def fiIntf

    private factory 
    private oimClient

    XLClient () {
        this("xelsysadm", "Welcome1")
    }

    XLClient (login, password) {
        println "logging into OIM"
        def jndiProps = ConfigurationClient.getComplexSettingByPath(
            "Discovery.CoreServer").getAllSettings()
        factory = new tcUtilityFactory(jndiProps, login, password)
        oimClient = new OIMClient(jndiProps)
        oimClient.login(login, password)
        println "login successful"
        initInterfaces()
    }    

    XLClient (jndiProps, login, password) {
        println "logging into OIM"
        factory = new tcUtilityFactory(jndiProps, login, password)
        oimClient = new OIMClient(jndiProps)
        oimClient.login(login, password)
        println "login successful"
        initInterfaces()
    }    

    void initInterfaces() {
        //services
        usrMgr = oimClient.getService(UserManager.class)    
        roleMgr = oimClient.getService(RoleManager.class)
        provIntSvc = oimClient.getService(ProvisioningServiceInternal.class)
        aiSvc = oimClient.getService(ApplicationInstanceService.class)
        rdsSvc = oimClient.getService(RequestDataSetService.class)

        // interfaces
        usrIntf = factory.getUtility(USR_INTF)
        impIntf = factory.getUtility(IMP_INTF)
        expIntf = factory.getUtility(EXP_INTF)
        orgIntf = factory.getUtility(ORG_INTF)
        grpIntf = factory.getUtility(GRP_INTF)
        reconIntf = factory.getUtility(RECON_INTF)
        objIntf = factory.getUtility(OBJ_INTF)
        propIntf = factory.getUtility(PROP_INTF)
        itInstIntf = factory.getUtility(ITINST_INTF)
        itDefIntf = factory.getUtility(ITDEF_INTF)
        apIntf = factory.getUtility(AP_INTF)
        fiIntf = factory.getUtility(FI_INTF)
    }

    void close() {
        println "logging out"
        factory.close()
    }
}
