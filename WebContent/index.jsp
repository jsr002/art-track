<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.myGrid {
width: 100%;
height: 100%;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script src="http://ui-grid.info/release/ui-grid.js"></script>
<link rel="stylesheet" href="http://ui-grid.info/release/ui-grid.css" type="text/css">
<script type="text/javascript">
var app = angular.module("uigridApp", ["ui.grid","ui.grid.edit","ui.grid.selection"]);
app.controller("uigridCtrl", function ($scope,$http) {
	$scope.gridOptions = {
			enableSorting: true,
			enableFiltering: true,
			enableRowSelection : true,
			enablePagination : true,
			paginationPageSizes: [2, 4, 6],
			paginationPageSize: 2,
			enableColumnMoving : true,
			columnDefs: [
			{ field: 'title', enableCellEdit: true },
			{ field: 'name' , enableCellEdit: true},
			{ field: 'abstract', enableCellEdit: true },
			{ field: 'email', enableSorting: false }
			],
			onRegisterApi: function (gridApi) {
			$scope.grid1Api = gridApi;
			}
			};
	$http({
		method : 'GET',
		url : 'TrackingController'
		
	}).then(function success(response){
		
		$scope.users = response.data;
		$scope.gridOptions.data = $scope.users;
	},function error(response){
		
		alert('Error Occurred');
	});
	
	
	/* 
$scope.users = [
{ name: "Madhav Sai", age: 10, location: 'Nagpur' },
{ name: "Suresh Dasari", age: 30, location: 'Chennai' },
{ name: "Rohini Alavala", age: 29, location: 'Chennai' },
{ name: "Praveen Kumar", age: 25, location: 'Bangalore' },
{ name: "Sateesh Chandra", age: 27, location: 'Vizag' }
]; */
});
</script>
</head>
<!-- <body>
 <form action = "TrackingController" method = "GET">
         First Name: <input type = "text" name = "first_name">
         <br />
         Last Name: <input type = "text" name = "last_name" />
         <input type = "submit" value = "Submit" />
      </form> -->

<body ng-app="uigridApp">
<h2>AngularJS UI Grid Example</h2>
<div ng-controller="uigridCtrl">
<div ui-grid="gridOptions" ui-grid-edit = "" ui-grid-selection = "" class="myGrid"></div>
<!-- <div ui-grid="{ data: users }" class="myGrid"></div> -->
</div>
</body>









</html>