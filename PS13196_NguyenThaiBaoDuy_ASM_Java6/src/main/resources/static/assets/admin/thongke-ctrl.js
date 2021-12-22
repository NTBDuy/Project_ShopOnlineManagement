app.controller("thongke-ctrl", function ($scope, $http) {

    $scope.items = [];
    $scope.initialize= function(){

        $http.get("/rest/report").then(resp =>{
            $scope.items = resp.data;
        });
    }
    $scope.initialize();
});
