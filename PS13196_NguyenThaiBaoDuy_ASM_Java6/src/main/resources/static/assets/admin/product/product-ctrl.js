app.controller("product-ctrl", function ($scope, $http) {
    $scope.items = [];
    $scope.form = {};
    $scope.cates = [];
    $scope.initialize = function () {
        //load products
        $http.get("/rest/products").then(resp => {
            $scope.items = resp.data;
            $scope.items.forEach(item => {
                item.createDate = new Date(item.createDate);
            })
        });
        $scope.form = {
            image: 'cloud-upload.jpg'
        };
        //load categories
        $http.get("/rest/categories").then(resp => {
            $scope.cates = resp.data;
        })
    }

    $scope.search = function () {
        var x = document.getElementById("searchName").value;
        if (x == '') {
            $http.get("/rest/products").then(resp => {
                $scope.items = resp.data;
                $scope.items.forEach(item => {
                    item.createDate = new Date(item.createDate);
                })
            });
        } else {
            $http.get(`/rest/products/search/${x}`).then(resp => {
                $scope.items = resp.data;
                $scope.items.forEach(item => {
                    item.createDate = new Date(item.createDate);
                })
            });
        }
    }

    //khởi đầu
    $scope.initialize();

    //xóa form
    $scope.reset = function () {
        $scope.form = {
            createDate: new Date(),
            image: 'cloud-upload.jpg',
            available: true
        };
    }

    //Hiển thị lên form
    $scope.edit = function (item) {
        $scope.form = angular.copy(item);
        $(".nav-tabs a:eq(1)").tab('show')
    }

    //Thêm sản phẩm mới
    $scope.create = function () {
        var item = angular.copy($scope.form);
        $http.post(`/rest/products`, item).then(resp => {
            resp.data.createDate = new Date(resp.data.createDate)
            $scope.items.push(resp.data);
            $scope.reset();
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Thêm mới sản phẩm thành công!");
            $("#myModal").modal("show");
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi thêm mới sản phẩm!");
            $("#myModal").modal("show");
            console.log("Error", error);
        })
    }

    //Cập nhật sản phẩm
    $scope.update = function () {
        var item = angular.copy($scope.form);
        $http.put(`/rest/products/${item.id}`, item).then(resp => {
            var index = $scope.items.findIndex(p => p.id == item.id);
            $scope.items[index] = item;
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Cập nhật sản phẩm thành công!");
            $("#myModal").modal("show");
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi cập nhật sản phẩm!");
            $("#myModal").modal("show");
            console.log("Error", error);
        })
    }

    //Xóa sản phẩm
    $scope.delete = function (item) {
        $http.delete(`/rest/products/${item.id}`).then(resp => {
            var index = $scope.items.findIndex(p => p.id == item.id);
            $scope.items.splice(index, 1);
            $scope.reset();
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Xóa sản phẩm thành công!");
            $("#myModal").modal("show");
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi xóa sản phẩm!");
            $("#myModal").modal("show");
            console.log("Error", error);
        })
    }

    //Upload hình
    $scope.imageChanged = function (files) {
        var data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/upload/images/imageproduct/', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.form.image = resp.data.name;
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi upload hình ảnh!");
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
