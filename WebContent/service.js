  angular.module('myApp').factory('LoginService',['$http', 'userModel','$q', function($http, userModel, $q) {
    var isAuthenticated = true;
    
    return {
      login : function(username, password) {
    	  var user = {"userName":username,"password":password};
    	 return $http({
    			method : 'POST',
    			url : 'TrackingController',
    			data : {user:user},
    			params : {methodType : 'login'}
    	 		//param : {user:angular.toJson(user)}
    		}).then(function success(response){
    			isAuthenticated = response.data.isAuthenticated;
    			return isAuthenticated;
    			//$scope.users = response.data;
    			//$scope.gridOptions.data = $scope.users;
    		},function error(response){
    			
    			alert('Error Occurred');
    		});
    		
    	  
    	  
    	  
        //isAuthenticated = username === admin && password === pass;
        return isAuthenticated;
      },
      logout : function(){
    	  return $http({
  			method : 'POST',
  			url : 'TrackingController',
  			//data : {user:user},
  			params : {methodType : 'logout'}
  	 		//param : {user:angular.toJson(user)}
  		}).then(function success(response){
  			isAuthenticated = false;
  			return true;
  			//$scope.users = response.data;
  			//$scope.gridOptions.data = $scope.users;
  		},function error(response){
  			
  			alert('Error Occurred');
  		});
      },
      getLoggedInUser : function(){
    	  // call service to fetch user getLoggedinUserModel
    	  var deferred = $q.defer();
    	   $http({
  			method : 'POST',
  			url : 'TrackingController',
  			params : {methodType : 'getLoggedinUserModel'}
  		}).then(function success(response){
  			console.log('response :: '+response.data);
  			var user = response.data;
  			userModel.setName(user.userName);
  			userModel.setEmail(user.email);
  			userModel.setRoles(user.roles.split(","));
  			userModel.setUserId(user.userId);
  			userModel.setMobileNumber(user.mobileNumber);
  			userModel.setCountry(user.country);
  			userModel.setAddress(user.address);
  			userModel.setAboutMe(user.aboutMe);
  			deferred.resolve(true);
  		},function error(response){
  			alert('Error Occurred');
  			deferred.resolve(false);
  		});
    	   return deferred.promise;
      },
      isAuthenticated : function() {
        return isAuthenticated;
      }
    };
    
  }]);
  
  angular.module('myApp').factory('profileService',['$http', 'userModel','$q', 'LoginService', function($http, userModel, $q, LoginService) {
	  return {
		  getAllSubjects : function(){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getSubjects'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('subjects::::::::::::::::::::::::::::'+response.data.subjects);
	    			//response.data ={}
	    			//response.data.subjects =[{"name": "Clinical", "id": "111"},{"name": "Medical", "id": "112"},{"name": "Engineering", "id": "113"}];
	    			deferred1.resolve(response.data.subjects);
	    		});
			  return deferred1.promise;
			  
		  },
		  getJournalsBySubject : function(subjectId){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {'methodType' : 'getJournalsBySubject', 'subjectId': subjectId}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('journals ::::::::::::::::::::::::::::'+response.data.journals);
	    			/*response.data ={};
	    			response.data.journals =[{"name": "Clinical", "id": "111", "url": "www.cancer.com", "subject" :{"name" : "Clinical","id": "111"}, "classifications" :"clin1, clin2, clin3"},
	    									 {"name": "Engineering", "id": "112", "url": "www.cancer.com", "subject" :{"name" : "Medical","id":"112"}, "classifications" :"clin1, clin2, clin3"}
	    									];*/
	    			deferred1.resolve(response.data.journals);
	    		});
			  return deferred1.promise;
			  
		  },updateUserProfile : function(user) {

			  return $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			data : {'user':user},
	    			params : {methodType : 'updateUserProfile'}
	    		}).then(function success(response){
	    			return response;
	    		},function error(response){
	    			return response;
	    			alert('Error Occurred');
	    		});
	       
	      },
		  getRoles : function(){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {'methodType' : 'getRoles'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('journals ::::::::::::::::::::::::::::'+response.data.journals);
	    			response.data ={};
	    			response.data.roles =[{"name": "Editor", "id": "111"},
	    									 {"name": "Reviewer", "id": "112"} ];
	    			deferred1.resolve(response.data.roles);
	    		});
			  return deferred1.promise;
		  },
		  getLoggedInUser : function(){
			 return LoginService.getLoggedInUser();
			  
		  }
		  
	  }
	  
  }]);
  angular.module('myApp').factory('RegistrationService',['$http', function($http) {
	    var admin = 'admin';
	    var pass = 'pass';
	    var confPass = 'pass';
	    var isRegistered = false;
	    
	    return {
	      register : function(user) {
	    	//  $scope.username, $scope.password, $scope.email, $scope.mobileNumber, $scope.country, $scope.address
	    	  //var user = {"userName":username,"password":password,"email":email,"mobileNumber":mobileNumber,"country":country,"address":address};
	    	 return $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			data : {'user':user},
	    			params : {methodType : 'registration'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			isRegistered = response.data.isRegistered;
	    			return response;
	    			//$scope.users = response.data;
	    			//$scope.gridOptions.data = $scope.users;
	    		},function error(response){
	    			return response;
	    			alert('Error Occurred');
	    		});
	       
	      }
	     
	    };
	    
	  }]);
  angular.module('myApp').factory('DashboardService',['$http', '$q', function($http, $q) {
	  
	  return {
		  getUsers : function(){
			  var deferred = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getUsers'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			deferred.resolve(response.data.users);
	    		});
			  return deferred.promise;
			  
		  },
		  getAuthorArticles : function(){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getArticleDataForAuthor'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('articles::::::::::::::::::::::::::::'+response.data.articles);
	    			deferred1.resolve(response.data.articles);
	    		});
			  return deferred1.promise;
			  
		  },
		  getEditorArticles : function(){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getArticlesForEditor'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('articles::::::::::::::::::::::::::::'+response.data.articles);
	    			deferred1.resolve(response.data.articles);
	    		});
			  return deferred1.promise;
			  
		  },
		  getReviewerArticles : function(){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getArticlesForReviewer'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('articles::::::::::::::::::::::::::::'+response.data.articles);
	    			deferred1.resolve(response.data.articles);
	    		});
			  return deferred1.promise;
			  
		  },
		  getSubjects : function(){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getSubjects'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('subjects::::::::::::::::::::::::::::'+response.data.subjects);
	    			//response.data ={}
	    			//response.data.subjects =[{"name": "Clinical", "id": "111"},{"name": "Medical", "id": "112"},{"name": "Engineering", "id": "113"}];
	    			deferred1.resolve(response.data.subjects);
	    		});
			  return deferred1.promise;
			  
		  },
		  saveSubject : function(subject){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'saveSubject',data : {"subject":angular.toJson(subject)}},
	    	 		
	    		}).then(function success(response){
	    			console.log('subject ::::::::::::::::::::::::::::'+response.data.message);
	    			deferred1.resolve(response.data.message);
	    		});
			  return deferred1.promise;
			  
		  },
		  getJournals : function(){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getJournals'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			console.log('journals ::::::::::::::::::::::::::::'+response.data.journals);
	    			/*response.data ={};
	    			response.data.journals =[{"name": "Clinical", "id": "111", "url": "www.cancer.com", "subject" :{"name" : "Clinical","id": "111"}, "classifications" :"clin1, clin2, clin3"},
	    									 {"name": "Engineering", "id": "112", "url": "www.cancer.com", "subject" :{"name" : "Medical","id":"112"}, "classifications" :"clin1, clin2, clin3"}
	    									];*/
	    			deferred1.resolve(response.data.journals);
	    		});
			  return deferred1.promise;
			  
		  },
		  saveJournal : function(journal){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'saveJournal', "journal":angular.toJson(journal)},
	    	 		
	    		}).then(function success(response){
	    			console.log('journal ::::::::::::::::::::::::::::'+response.data);
	    			deferred1.resolve(response.data);
	    		});
			  return deferred1.promise;
			  
		  },saveArticle : function(article){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'saveArticle', "article":angular.toJson(article)},
	    	 		
	    		}).then(function success(response){
	    			console.log('Article ::::::::::::::::::::::::::::'+response.data);
	    			deferred1.resolve(response.data);
	    		});
			  return deferred1.promise;
			  
		  },getReviewersForArticle : function(article){
			  var deferred1 = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'getReviewersForArticle', "article":angular.toJson(article)},
	    	 		
	    		}).then(function success(response){
	    			console.log('Article ::::::::::::::::::::::::::::'+response.data);
	    			deferred1.resolve(response.data.reviewers);
	    		});
			  return deferred1.promise;
			  
		  }
		  
		  
	  }
  }]).factory('articleService',['$http', '$q', function($http, $q) {
	  
	  return {
		  uploadArticle : function(){
			  
			  $("form#articleUploadForm").submit(function(e) {
				    e.preventDefault();    
				    var formData = new FormData(this);

				    $.ajax({
				        url: 'ArticleSubmissionServlet',
				        type: 'POST',
				        data: formData,
				        success: function (data) {
				            alert(data);
				        },
				        cache: false,
				        contentType: false,
				        processData: false
				    });
				});
			/*  var deferred = $q.defer();
			  $http({
	    			method : 'POST',
	    			url : 'TrackingController',
	    			params : {methodType : 'submitArticle'}
	    	 		//param : {user:angular.toJson(user)}
	    		}).then(function success(response){
	    			deferred.resolve(response.data.users);
	    		});
			  return deferred.promise;*/
			  
		  }
	  }
  }]);
  angular.module('myApp').factory('userModel', [ function(){
	  //getLoggedinUserModel
	  var user ={};
	  user.userId = 0;
	  user.name="";
	  user.email="";
	  user.mobileNumber ="";
	  user.country ="";
	  user.address ="";
	  user.aboutMe ="";
	  user.roles =[];
	 
	  user.getMobileNumber = function(){
			return this.mobileNumber;
	  };
	  user.setMobileNumber = function(mobileNumber){
			this.mobileNumber =mobileNumber;
	  };
	  user.getCountry = function(){
			return this.country;
	  };
	  user.setCountry = function(country){
			this.country =country;
	  };
	  user.getAddress = function(){
			return this.address;
	  };
	  user.setAddress = function(address){
			this.address =address;
	  };
	  user.getAboutMe = function(){
			return this.aboutMe;
	  };
	  user.setAboutMe = function(aboutMe){
			this.aboutMe =aboutMe;
	  };
	  user.getUserId = function(){
			return this.userId;
	  };
	  user.setUserId = function(userId){
			this.userId =userId;
	  };								
	  user.getName = function(){
				return this.name;
		};
	  user.setName = function(name){
			this.name =name;
	  };
	  user.getEmail = function(){
		return this.email;
	  };
	  user.setEmail = function(email){
			this.email =email;
	  };
	  user.setRoles = function(roles){
		  this.roles = roles;
	  }
	  user.getRoles = function(){
			return this.roles;
		  };
	 user.isAdmin = function(){
		 return this.getRoles().indexOf("Admin") >-1;
	 };
	 user.isSuperAdmin = function(){
		 return this.getRoles().indexOf("SuperAdmin") >-1;
	 };
	 user.isAuthor = function(){
		 return this.getRoles().indexOf("Author") >-1;
	 };
	 user.isEditor = function(){
		 return this.getRoles().indexOf("Editor") >-1;
	 };
	 user.isReviewer = function(){
		 return this.getRoles().indexOf("Reviewer") >-1;
	 }
	  return user;
  }]);