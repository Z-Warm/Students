(function() {
    'use strict';

    angular
        .module('studentsApp')
        .controller('GoupDialogController', GoupDialogController);

    GoupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Goup', 'Student'];

    function GoupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Goup, Student) {
        var vm = this;

        vm.goup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.students = Student.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.goup.id !== null) {
                Goup.update(vm.goup, onSaveSuccess, onSaveError);
            } else {
                Goup.save(vm.goup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('studentsApp:goupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
