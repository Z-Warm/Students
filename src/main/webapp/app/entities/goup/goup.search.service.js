(function() {
    'use strict';

    angular
        .module('studentsApp')
        .factory('GoupSearch', GoupSearch);

    GoupSearch.$inject = ['$resource'];

    function GoupSearch($resource) {
        var resourceUrl =  'api/_search/goups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
