
app.controller("authority-ctrl",function($scope,$http,$location){
    $scope.roles = [];
    $scope.items = [];
    $scope.searchName = '';
    $scope.authorities = [];

    $scope.initialize = function(){
        //load all roles
        $http.get("/rest/roles").then(resp =>{
            $scope.roles = resp.data;
        })

        //load staffs and directors(administrators)
        $http.get("/rest/accounts/getall").then(resp =>{
            $scope.items = resp.data;
        })

        //load authorities of staffs and directors
        $http.get("/rest/authorities/getall").then(resp =>{
            $scope.authorities = resp.data;
        }).catch(error =>{
            $location.path("/unauthorized");
        })
    }

    $scope.authority_of = function(acc,role){
        if($scope.authorities){
            return $scope.authorities.find(ur => ur.account.username == acc.username && ur.role.id == role.id);
        }
    }

    $scope.authority_changed= function(acc,role){
        var authority = $scope.authority_of(acc,role);
        if(authority){
            //đã cấp quyền => thu hồi quyền (xóa)
            $scope.revoke_authority(authority);
        }else{
            //chưa được cấp quyền => cấp quyền (Thêm mới)
            authority = {account: acc, role: role};
            $scope.grant_authority(authority);
        }
    }

    //Thêm mới authority
    $scope.grant_authority = function(authority){
        $http.post(`/rest/authorities`,authority).then(resp =>{
            $scope.authorities.push(resp.data)
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Cấp quyền sử dụng thành công!");
            $("#myModal").modal("show");
        }).catch(error =>{
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi cấp quyền sử dụng!");
            $("#myModal").modal("show");
            console.log("Error",error);
        })
    }

    //Xóa Authority
    $scope.revoke_authority = function(authority){
        $http.delete(`/rest/authorities/${authority.id}`).then(resp =>{
            var index = $scope.authorities.findIndex(a => a.id == authority.id);
            $scope.authorities.splice(index,1);
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Thu hồi quyền sử dụng thành công!");
            $("#myModal").modal("show");
        }).catch(error =>{
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi thu hồi quyền sử dụng!");
            $("#myModal").modal("show");
            console.log("Error",error);
        })
    }
    
    $scope.initialize();
})