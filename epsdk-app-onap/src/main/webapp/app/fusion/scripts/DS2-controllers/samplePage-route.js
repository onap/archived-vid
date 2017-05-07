appDS2.config(function($routeProvider) {
	$routeProvider
	.when('/collaborate_list', {
		templateUrl: 'app/fusion/scripts/DS2-view-models/ds2-admin/collaborate-list.html',
		controller: 'collaborateListControllerDS2'
	})
	.when('/drools/:filename', {
		templateUrl: 'app/fusion/scripts/DS2-view-models/ds2-samplePages/drools.html',
		controller: 'droolsController'
	})
	.when('/droolsList', {
		templateUrl: 'app/fusion/scripts/DS2-view-models/ds2-samplePages/drools-list.html',
		controller: 'droolsListController'
	})
	.otherwise({ 
		templateUrl: 'app/fusion/scripts/DS2-view-models/ds2-samplePages/net_map.html',
		controller : 'netMapController'
	});
});
