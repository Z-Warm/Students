(function() {
    'use strict';

    angular
        .module('studentsApp')
        .controller('GoupDeleteController',GoupDeleteController);

    GoupDeleteController.$inject = ['$uibModalInstance', 'entity', 'Goup'];

    function GoupDeleteController($uibModalInstance, entity, Goup) {
        var vm = this;

        vm.goup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Goup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
