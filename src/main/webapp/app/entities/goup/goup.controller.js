(function() {
    'use strict';

    angular
        .module('studentsApp')
        .controller('GoupController', GoupController);

    GoupController.$inject = ['Goup', 'GoupSearch'];

    function GoupController(Goup, GoupSearch) {

        var vm = this;

        vm.goups = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Goup.query(function(result) {
                vm.goups = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            GoupSearch.query({query: vm.searchQuery}, function(result) {
                vm.goups = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
