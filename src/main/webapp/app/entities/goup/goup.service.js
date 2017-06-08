(function() {
    'use strict';
    angular
        .module('studentsApp')
        .factory('Goup', Goup);

    Goup.$inject = ['$resource'];

    function Goup ($resource) {
        var resourceUrl =  'api/goups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
