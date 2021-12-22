app.controller("account-ctrl", function ($scope, $http) {
    // alert("Tài khoản")
    $scope.items = [];
    $scope.form = {};
    $scope.initialize = function () {
        // $("#myModal").modal("show");

        $http.get("/rest/accounts/getall").then(resp => {
            $scope.items = resp.data;
        });
        $scope.form = {
            photo: 'cloud-upload.jpg'
        }
    }
    $scope.search = function () {
        var x = document.getElementById("searchName").value;
        if (x == '') {
            $http.get("/rest/accounts/getall").then(resp => {
                $scope.items = resp.data;
            });
        } else {
            $http.get(`/rest/accounts/search/${x}`).then(resp => {
                $scope.items = resp.data;
            });
        }
    }

    //khởi đầu
    $scope.initialize();

    //xóa form
    $scope.reset = function () {
        $scope.form = {
            photo: 'cloud-upload.jpg'
        };
    }

    //Hiển thị lên form
    $scope.edit = function (item) {
        $scope.form = angular.copy(item);
        $(".nav-tabs a:eq(1)").tab('show')
    }

    //Thêm 
    $scope.create = function () {
        var item = angular.copy($scope.form);
        $http.post(`/rest/accounts`, item).then(resp => {
            $scope.items.push(resp.data);
            $scope.reset();
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Thêm mới tài khoản thành công!");
            $("#myModal").modal("show");
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi thêm mới tài khoản!");
            $("#myModal").modal("show");
            console.log("Error", error);
        })
    }

    //Cập nhật
    $scope.update = function () {
        var item = angular.copy($scope.form);
        $http.put(`/rest/accounts/${item.username}`, item).then(resp => {
            var index = $scope.items.findIndex(p => p.username == item.username);
            $scope.items[index] = item;
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Cập nhật tài khoản thành công!");
            $("#myModal").modal("show");
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi cập nhật tài khoản!");
            $("#myModal").modal("show");
            console.log("Error", error);
        })
    }

    //Xóa
    $scope.delete = function (item) {
        $http.delete(`/rest/accounts/${item.username}`).then(resp => {
            var index = $scope.items.findIndex(p => p.username == item.username);
            $scope.items.splice(index, 1);
            $scope.reset();
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Xóa tài khoản thành công!");
            $("#myModal").modal("show");
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi xóa tài khoản!");
            $("#myModal").modal("show");
            console.log("Error", error);
        })
    }

    //Upload hình
    $scope.imageChanged = function (files) {
        var data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/upload/images/imageUser', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.form.photo = resp.data.name;
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi upload hình ảnh");
            $("#myModal").modal("show");
            console.log("Error", error)
        })
    }

    $scope.pager = {
        page: 0,
        size: 10,
        get items() {
            var start = this.page * this.size;
            return $scope.items.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.items.length / this.size);
        },

        first() {
            this.page = 0;
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.last();
            }
        },
        next() {
            this.page++;
            if (this.page >= this.count) {
                this.first();
            }
        },
        last() {
            this.page = this.count - 1;
        }
    }
});
