package org.onap.vid.job.command

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs
import org.mockito.InjectMocks
import org.mockito.Mock
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.model.ServiceModel
import org.onap.vid.model.VfModule
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.mso.model.ModelInfo
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.testUtils.TestUtils.initMockitoMocks
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.togglz.core.manager.FeatureManager

class VfmoduleCommandTest {

    @Mock lateinit var asyncInstantiationBL: AsyncInstantiationBusinessLogic;
    @Mock lateinit var restMso: RestMsoImplementation;
    @Mock lateinit var msoRequestBuilder: MsoRequestBuilder;
    @Mock lateinit var msoResultHandlerService: MsoResultHandlerService;
    @Mock lateinit var inProgressStatusService: InProgressStatusService;
    @Mock lateinit var watchChildrenJobsBL: WatchChildrenJobsBL;
    @Mock lateinit var jobsBrokerService: JobsBrokerService;
    @Mock lateinit var jobAdapter: JobAdapter;
    @Mock lateinit var featureManager: FeatureManager;


    @InjectMocks lateinit var vfModuleCommand: VfmoduleCommand;

    private val uniqueCustomizationName = "my unique customization name"

    @BeforeMethod
    fun initMocks() {
        initMockitoMocks(this)
    }

    @Test
    fun `given correlated modelCustomizationName, selectVfms returns the vfm`() {
        val newestModel = ServiceModel()
        newestModel.vfModules = someVfModules()
                .plus("my vfm" to vfModuleWithCustomizationName())

        val selectedVfm = vfModuleCommand.selectVfm(newestModel, modelInfoWithCustomizationName())

        assertThat(selectedVfm, samePropertyValuesAs(vfModuleWithCustomizationName()))
    }

    @Test(
            expectedExceptions = [IllegalStateException::class],
            expectedExceptionsMessageRegExp =
            """Cannot match vfModule for modelCustomizationName "my unique customization name": Collection contains no element matching the predicate.""")
    fun `given no matching modelCustomizationName, selectVfms throws`() {
        val newestModel = ServiceModel()
        newestModel.vfModules = someVfModules()

        vfModuleCommand.selectVfm(newestModel, modelInfoWithCustomizationName())
    }

    @Test(
            expectedExceptions = [IllegalStateException::class],
            expectedExceptionsMessageRegExp =
            """Cannot match vfModule for modelCustomizationName "my unique customization name": Collection contains more than one matching element.""")
    fun `given a few matching modelCustomizationName, selectVfms throws`() {

        val newestModel = ServiceModel()
        newestModel.vfModules = someVfModules()
                .plus("my vfm" to vfModuleWithCustomizationName())
                .plus("my vfm2" to vfModuleWithCustomizationName())

        vfModuleCommand.selectVfm(newestModel, modelInfoWithCustomizationName())
    }

    private fun modelInfoWithCustomizationName(customizationName: String = uniqueCustomizationName) =
            ModelInfo().also { it.modelCustomizationName = customizationName }

    private fun someVfModules(): Map<String, VfModule> = mapOf(
            "any vfm 1" to vfModuleWithCustomizationName("any customization name 1"),
            "any vfm 2" to vfModuleWithCustomizationName("any customization name 2")
    )

    private fun vfModuleWithCustomizationName(customizationName: String = uniqueCustomizationName) =
            VfModule().also { it.modelCustomizationName = customizationName }

}