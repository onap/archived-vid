export class VidConfiguration {

  public static VNF_STATUS_CHECK_ENABLED = false;

  /*
   * UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED: Determines the Property to Govern Presence of
   * Upload Supplementary File on Volume Group Screen.
   * Set to false, to disable the check.
   */
  public static UPLOAD_SUPPLEMENTARY_STATUS_CHECK_ENABLED = false;

  /*
   * List of valid VNF status combinations
   */
  public static VNF_VALID_STATUS_LIST = [
    {
      'provStatus': 'preprov',
      'orchestrationStatus': 'pending-create',
      'inMaint': false,
      'operationalStatus': null
    },
    {
      'provStatus': 'preprov',
      'orchestrationStatus': 'created',
      'inMaint': false,
      'operationalStatus': null
    },
    {
      'provStatus': 'preprov',
      'orchestrationStatus': 'active',
      'inMaint': false,
      'operationalStatus': null
    },
    {
      'provStatus': 'nvtprov',
      'orchestrationStatus': 'active',
      'inMaint': false,
      'operationalStatus': null
    },
    {
      'provStatus': 'prov',
      'orchestrationStatus': 'active',
      'inMaint': false,
      'operationalStatus': 'out-of-service-path'
    },
    {
      'provStatus': 'prov',
      'orchestrationStatus': 'activated',
      'inMaint': false,
      'operationalStatus': 'out-of-service-path'
    }
  ];

  /*
   * The model status VID uses to query SDC for a list of models. The possible values are:
   * DISTRIBUTION_NOT_APPROVED,
   * DISTRIBUTION_APPROVED,
   * DISTRIBUTED,
   * DISTRIBUTION_REJECTED,
   * ALL,
   * In the production env, this should always be set to DISTRIBUTED
   */
  public static ASDC_MODEL_STATUS = 'DISTRIBUTED';

  /*
   * Max number of times that VID will poll MSO for a given request status
   */
  public static MSO_MAX_POLLS = 10;

  /*
   * Number of msecs that VID will wait between MSO polls.
   */
  public static MSO_POLLING_INTERVAL_MSECS = 10000;

  public static SCHEDULER_POLLING_INTERVAL_MSECS = 10000;

  public static SCHEDULER_MAX_POLLS = 10;

  public static COMPONENT_LIST_NAMED_QUERY_ID = '0367193e-c785-4d5f-9cb8-7bc89dc9ddb7';

  /*
   * List of all service model invariant UUIDs that need macro instantiation.
   * Example:
   * MACRO_SERVICES : ['3cf30cbb-5fe7-4fb3-b049-559a4997b221', 'b135a703-bab5-4295-a37f-580a4f2d0961']
   *
   */
  public static MACRO_SERVICES = ['c9514b73-3dfe-4d7e-9146-b318d48655d9', '93150ffa-00c6-4ea0-85f2-3536ca46ebd2',
    '2b54297f-72e7-4a94-b451-72df88d0be0b',
    'd27e42cf-087e-4d31-88ac-6c4b7585f800',
    'ec0c4bab-c272-4dab-b087-875031bb0c9f', '0311f998-9268-4fd6-bbba-afff15087b72',
    '43596836-ae36-4608-a987-6608ede10dac', '306caa85-74c7-48a9-aa22-7e3a564b957a',
    'e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0'];

  public static SCHEDULER_CALLBACK_URL = 'https://vid-web-ete.ecomp.cci.att.com:8000/vid/change-management/workflow/';

  public static SCHEDULER_PORTAL_URL = 'http://www.ecomp.att.com';

}
