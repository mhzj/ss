//mockdata 

(function() {
    var app;
    app = angular.module('app', []);
    Mock.mockjax(app);
    return app.controller('appCtrl', function($scope, $http) {
      var box;
      box = $scope.box = [];
      $scope.get = function() {
        $http({
          url: 'http://g.cn',
          method: 'POST',
          params: {a: 1},
          data  : {b:1}
        }).success(function(data) {
          return box.push(data);
        });

        $http({
          url: 'http://baidu.com'
        }).success(function(data) {
            console.log(data);
        });
      };
      return $scope.get();
    });
  })();