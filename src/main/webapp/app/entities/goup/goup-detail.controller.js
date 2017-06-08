(function() {
    'use strict';

    angular
        .module('studentsApp')
        .controller('GoupDetailController', GoupDetailController);

    GoupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Goup', 'Student'];

    function GoupDetailController($scope, $rootScope, $stateParams, previousState, entity, Goup, Student) {
        var vm = this;

        vm.goup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('studentsApp:goupUpdate', function(event, result) {
            vm.goup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
