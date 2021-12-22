const app = angular.module("shopping-cart-app", []);
app.controller("shopping-cart-ctrl", function ($scope, $http, $interval) {

    //QUẢN LÝ GIỎ HÀNG
    $scope.cart = {
        items: [],
        //Thêm sản phẩm vào giỏ hàng
        add(id) {
            var item = this.items.find(item => item.id == id);
            if (item) {
                item.qty++;
                this.saveToLocalStorage();
            } else {
                $http.get(`/rest/products/${id}`).then(resp => {
                    resp.data.qty = 1;
                    this.items.push(resp.data);
                    this.saveToLocalStorage();
                })
            }
        },
        //Xóa sản phẩm khỏi giỏ hàng
        remove(id) {
            var index = this.items.findIndex(item => item.id == id);
            this.items.splice(index, 1);
            this.saveToLocalStorage();
        },
        //Xóa sạch các mặt hàng trong giỏ
        clear() {
            this.items = [];
            this.saveToLocalStorage();
        },
        //Tính thành tiền của một sản phẩm
        amt_of(item) { },
        //Tính tổng số lượng các mặt hàng trong giỏ
        get count() {
            return this.items
                .map(item => item.qty)
                .reduce((total, qty) => total += qty, 0);
        },
        //Tổng thành tiền các mặt hàng trong giỏ
        get amount() {
            return this.items
                .map(item => item.qty * item.price)
                .reduce((total, qty) => total += qty, 0);
        },
        //Lưu giỏ hàng vào local storage
        saveToLocalStorage() {
            var json = JSON.stringify(angular.copy(this.items));
            localStorage.setItem("cart", json);
        },
        //Đọc giỏ hàng từ local storage
        loadFromLocalStorage() {
            var json = localStorage.getItem("cart");
            this.items = json ? JSON.parse(json) : [];
        }
    }

    $scope.cart.loadFromLocalStorage();

    $scope.order = {
        createDate: new Date(),
        address: "",
        account: { username: $("#username").text() },
        /*account:{username: document.getElementById("username").innerHTML},*/
        get orderDetails() {
            return $scope.cart.items.map(item => {
                return {
                    product: { id: item.id },
                    price: item.price,
                    quantity: item.qty
                }
            });
        },
        purchase() {
            var order = angular.copy(this);
            //Thực hiện đặt hàng
            $http.post("/rest/orders", order).then(resp => {

                $("#modalTitle").text("Thông báo");
                $("#modalBody").text("Đặt hàng thành công!");
                $("#myModal").modal("show");

                //    alert("Đặt hàng thành công!");
                $scope.cart.clear();
                // location.href = "/order/detail/" + resp.data.id;
            }).catch(error => {
                $("#modalTitle").text("Thông báo");
                $("#modalBody").text("Đặt hàng lỗi!");
                $("#myModal").modal("show");

                // alert("Đặt hàng lỗi")
                console.log(error)
            })
        }
    }




    $scope.items = [];
    $scope.form = {};

    $scope.showhoso = function () {
        // alert($("#username").text());
        // $scope.initialize()
    }

    $scope.initialize = function () {
        var user = $("#username").text();
        $http.get(`/rest/accounts/${user}`).then(resp => {
            $scope.items = resp.data;
            // alert("Lấy dữ liệu thành công")
            // alert($scope.items.username)
        });
    }
    $scope.initialize();

    $scope.edit = function () {
        $scope.form = angular.copy($scope.items);
        // alert($scope.form)
    }
    //Cập nhật
    $scope.update = function () {
        var item = angular.copy($scope.form);
        // alert(item.username)
        $http.put(`/rest/accounts/${item.username}`, item).then(resp => {
            $scope.initialize();
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Cập nhật tài khoản thành công!");
            $("#myModal").modal("show");

            // alert("Cập nhật tài khoản thành công");
        }).catch(error => {
            $("#modalTitle").text("Thông báo");
            $("#modalBody").text("Lỗi cập nhật tài khoản!");
            $("#myModal").modal("show");

            // alert("Lỗi cập nhật tài khoản");
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
            $("#modalBody").text("Lỗi upload hình ảnh!");
            $("#myModal").modal("show");

            // alert("Lỗi upload hình ảnh");
            console.log("Error", error)
        })
    }

    $scope.time = new Date();
    $interval(function () {
        $scope.time = new Date();
    }, 1000);
})
