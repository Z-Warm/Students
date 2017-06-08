(function() {
    'use strict';

    angular
        .module('studentsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('goup', {
            parent: 'entity',
            url: '/goup',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentsApp.goup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goup/goups.html',
                    controller: 'GoupController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('goup-detail', {
            parent: 'goup',
            url: '/goup/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'studentsApp.goup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goup/goup-detail.html',
                    controller: 'GoupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Goup', function($stateParams, Goup) {
                    return Goup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'goup',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('goup-detail.edit', {
            parent: 'goup-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goup/goup-dialog.html',
                    controller: 'GoupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Goup', function(Goup) {
                            return Goup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('goup.new', {
            parent: 'goup',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goup/goup-dialog.html',
                    controller: 'GoupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                groupName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('goup', null, { reload: 'goup' });
                }, function() {
                    $state.go('goup');
                });
            }]
        })
        .state('goup.edit', {
            parent: 'goup',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goup/goup-dialog.html',
                    controller: 'GoupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Goup', function(Goup) {
                            return Goup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goup', null, { reload: 'goup' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('goup.delete', {
            parent: 'goup',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goup/goup-delete-dialog.html',
                    controller: 'GoupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Goup', function(Goup) {
                            return Goup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goup', null, { reload: 'goup' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
