/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.job.command

import net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf.allOf
import org.mockito.Answers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobsBrokerService
import org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator
import org.onap.vid.job.impl.JobSharedData
import org.onap.vid.model.Action
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.properties.Features
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.testUtils.TestUtils
import org.onap.vid.testUtils.TestUtils.initMockitoMocks
import org.testng.annotations.BeforeMethod
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import org.togglz.core.manager.FeatureManager
import org.mockito.Mockito.`when` as _when

class VnfCommandTest {

    @Mock lateinit var asyncInstantiationBL: AsyncInstantiationBusinessLogic
    @Mock lateinit var restMso: RestMsoImplementation
    @Mock lateinit var msoRequestBuilder: MsoRequestBuilder
    @Mock lateinit var msoResultHandlerService: MsoResultHandlerService
    @Mock lateinit var inProgressStatusService:InProgressStatusService
    @Mock lateinit var watchChildrenJobsBL: WatchChildrenJobsBL
    @Mock lateinit var jobsBrokerService: JobsBrokerService
    @Mock lateinit var jobAdapter: JobAdapter
    @Mock lateinit var featureManager: FeatureManager

    @Mock lateinit var jobSharedData: JobSharedData
    @Mock(answer = Answers.RETURNS_MOCKS) lateinit var vnfJobRequest: org.onap.vid.model.serviceInstantiation.Vnf

    @InjectMocks lateinit var vnfCommand: VnfCommand;

    @BeforeMethod
    fun initMocks() {
        initMockitoMocks(this)
    }

    @Test(dataProvider = "trueAndFalse", dataProviderClass = TestUtils::class)
    fun `childVfModuleWithVnfRegionAndTenant -- given vfmodule -- tenant and region are copied from vnf`(featureToggleOn: Boolean) {
        runChildVfModuleWithVnfRegionAndTenant(featureToggleOn, Action.Create, featureToggleOn)
    }

    @DataProvider
    fun allPossibleActions(): Array<Array<out Any?>> {
        return Action.values().map { arrayOf(it) }.toTypedArray()
    }

    @Test(dataProvider = "allPossibleActions")
    fun `childVfModuleWithVnfRegionAndTenant -- given vfmodule in different actions -- only "action_Create" copies tenant and region from vnf`(vfModuleAction: Action) {
        runChildVfModuleWithVnfRegionAndTenant(true, vfModuleAction, vfModuleAction == Action.Create)
    }

    private fun runChildVfModuleWithVnfRegionAndTenant(featureToggleOn: Boolean, vfModuleAction: Action, isCopyVnfToVfmoduleExpected: Boolean) {

        val vfModule = FakeResourceCreator.createVfModule(vfModuleAction)
                        .cloneWith("vfmodule-lcp-cloud-region-id", "vfmodule-tenant-id")

        _when(featureManager.isActive(Features.FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF)).thenReturn(featureToggleOn)

        _when(vnfJobRequest.lcpCloudRegionId).thenReturn("vnf-lcp-cloud-region-id")
        _when(vnfJobRequest.tenantId).thenReturn("vnf-tenant-id")
        _when(jobSharedData.request).thenReturn(vnfJobRequest)

        vnfCommand.init(jobSharedData, mapOf())

        val expectedSource = if (isCopyVnfToVfmoduleExpected) "vnf" else "vfmodule"

        assertThat(vnfCommand.childVfModuleWithVnfRegionAndTenant(vfModule),
                allOf(
                        jsonPartEquals("lcpCloudRegionId", "${expectedSource}-lcp-cloud-region-id"),
                        jsonPartEquals("tenantId", "${expectedSource}-tenant-id")
                )
        )
    }

}
