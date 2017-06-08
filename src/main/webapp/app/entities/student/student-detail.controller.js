(function() {
    'use strict';

    angular
        .module('studentsApp')
        .controller('StudentDetailController', StudentDetailController);

    StudentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Student', 'Goup'];

    function StudentDetailController($scope, $rootScope, $stateParams, previousState, entity, Student, Goup) {
        var vm = this;

        vm.student = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('studentsApp:studentUpdate', function(event, result) {
            vm.student = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
