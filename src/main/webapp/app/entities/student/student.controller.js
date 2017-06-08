(function() {
    'use strict';

    angular
        .module('studentsApp')
        .controller('StudentController', StudentController);

    StudentController.$inject = ['Student', 'StudentSearch'];

    function StudentController(Student, StudentSearch) {

        var vm = this;

        vm.students = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Student.query(function(result) {
                vm.students = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            StudentSearch.query({query: vm.searchQuery}, function(result) {
                vm.students = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
