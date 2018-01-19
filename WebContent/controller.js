  angular.module('myApp').controller('LoginController', function($scope, $rootScope, $state, LoginService) {
    $rootScope.title = "User Login ";
    
    $scope.user ={};
    $scope.error ={};
    //$scope.user.email
    $scope.formSubmit = function() {
    	
    	LoginService.login($scope.user.email, $scope.user.password).then(function(isAuthenticated){
    		 if(isAuthenticated) {
    		        $state.transitionTo('dashboard');
    		      } else {
    		        $scope.error.msg = "Incorrect username/password !";
    		      } 	
    		
    	});
    	
    }
   
  });
  angular.module('myApp').controller('profileController', function($scope, $state, profileService, $timeout, userModel) {
	  	
	  var selected ={ subject : {},  journal :undefined, roles :[] };
	  $scope.selectedProfiles = [];
	  profileService.getLoggedInUser().then(function(isLoaded){
		  $scope.user = angular.copy(userModel);
	  });
	  profileService.getAllSubjects().then(function(subjects){
	    	$scope.subjects = angular.copy(subjects);
	    	selected.subject = $scope.subjects[0];
	    });
	  profileService.getRoles().then(function(roles){
	    	$scope.roles = angular.copy(roles);
	    });
	    
	    $scope.getJournalsBySubject = function(index){
	    	profileService.getJournalsBySubject($scope.selectedProfiles[index]['subject']['id']).then(function(journals){
	    		$scope.selectedProfiles[index]['allJournals'] = angular.copy(journals);
	    		//$scope.selectedProfiles[index]['journal'] =  $scope.selectedProfiles[index]['allJournals'][0];
	 	    	$timeout(function(){
	 	    		$('#profileJournalpicker'+index).selectpicker('refresh');
	 	    		},0);
	 	    });
	    };
	    
	    $scope.addInterest = function(){ 
	    	$scope.selectedProfiles.push(angular.copy(selected));
	    	var index = $scope.selectedProfiles.length-1;
	    	 $timeout(function(){
	    	   		$('#profileSubjectpicker'+index).selectpicker('refresh');
	    	   		$('#profileJournalpicker'+index).selectpicker('refresh');
	    	   		$('#profileRolepicker'+index).selectpicker('refresh');
	    	   },10);
	    };

	    $scope.updateUserProfile = function(){
	    	$scope.user.interests = [];
	    	angular.forEach($scope.selectedProfiles, function(profile){
	    		var pf = {
	    				'subject': angular.copy(profile.subject),
	    				'journal' : {'id' :profile.journal.id,'name' : profile.journal.name},
	    				'roles' : angular.copy(profile.roles),
	    				'action' : ''
	    		};
	    		$scope.user.interests.push(pf);
	    	});
	    	profileService.updateUserProfile($scope.user).then(function(response){
	    		if(response.data.success){
	    			alert(response.data.message);
	    		}else{
	    			alert('Error while updating User Profile');
	    		}
	    	})
	    	
	    }
	    //$scope.addProfile();
	   
  });
  
  angular.module('myApp').controller('RegisterController', function($scope, $rootScope, $stateParams, $state, RegistrationService) {
	    $rootScope.title = "User Login ";
	    $scope.user ={
	    		"password": undefined,
	    		"confirmPassword" :undefined,
	    		"userName" : undefined,
	    		"email" : undefined,
	    		"mobileNumber" : undefined,
	    		"address" : undefined,
	    		"acceptance" : false
	    };
	    var isAuthenticated = false;
	    
	    $scope.registerUser = function() {
	    	
	    	if($scope.user.password != undefined && $scope.user.password === $scope.user.confirmPassword && $scope.user.acceptance){
	    		RegistrationService.register($scope.user).then(function(result) {  

		    	       // this is only run after getData() resolves
		    		isRegistered =result.data.isRegistered;
		    		
		    		 if(isRegistered) {
		    			 	$scope.message = 'User registration successful. Please login';
		    			 	alert($scope.message);
		    		        $scope.error = '';
		    		        $scope.username = '';
		    		        $scope.password = '';
		    		        $state.transitionTo('login');
		    		      } else {
		    		        $scope.error = "Registration failed !";
		    		    	alert($scope.error);
		    		      }  
	    		
	    		});
	    		
	    	}else{
	    		 $scope.error = "Password entered not matching !";
	    			alert($scope.error);
	    	}
	    };
	    
	  });
  
  
  angular.module('myApp').controller('HomeController', function($scope, $rootScope, $stateParams, $state, LoginService,DashboardService, $http, userModel, $timeout, $uibModal) {
    $rootScope.title = "User Login ";
    $scope.user= userModel;
    $scope.subjects ={"list" :[], "showEditor": false, editSubj:{}};
    $scope.journals ={"list" :[], "showEditor": false, editJourn:{}};
    $scope.users={"list" : []};
    $scope.authors = {"articles" :[]};
    $scope.editors = {"articles" : []};
    $scope.reviewers = {"articles":[]};
    $scope.articleReviewers = {"list" : []};
    
    if(!LoginService.isAuthenticated()) {
        $state.transitionTo('login');
      }else{
    	  LoginService.getLoggedInUser().then(function(isLoaded){
    		  if(isLoaded && $scope.user.isSuperAdmin()){
    			  $scope.loadSubjectsGrid();
    		  }
    		  if(isLoaded && $scope.user.isSuperAdmin()){
    			  $scope.loadJournalsGrid();
    		  }
    		  if(isLoaded && $scope.user.isSuperAdmin()){
    			  DashboardService.getUsers().then(function(users){
            		  $scope.users.list = users;//users.slice(4);
            	  });
    		  }
    		  if(isLoaded){
    			  DashboardService.getAuthorArticles().then(function(articles){
    				  console.log('articles for Author::::::::::::::::::::::::::::'+articles);
            		  $scope.authors.articles = articles;//users.slice(4);
            	  });
    		  }
    		  if(isLoaded && $scope.user.isEditor()){
    			  DashboardService.getEditorArticles().then(function(articles){
    				  console.log('articles For Editor::::::::::::::::::::::::::::'+articles);
            		  $scope.editors.articles = articles;//users.slice(4);
            	  });
    		  }
    		  if(isLoaded && $scope.user.isReviewer()){
    			  DashboardService.getReviewerArticles().then(function(articles){
    				  console.log('articles For Reviewer::::::::::::::::::::::::::::'+articles);
            		  $scope.reviewers.articles = articles;//users.slice(4);
            	  });
    		  }
    		  
    	  });
    	  
    	  
      }
    $scope.goToSubmitArticle = function(){
    	$timeout(function(){
    		$('#subjectPicker').selectpicker('refresh');
    		},0); 
    	$state.transitionTo('article.submit');
    	 
    }
    $scope.updateSubject = function(subject){
    	$scope.subjects.showEditor= true;
    	$scope.subjects.editSubj = angular.copy(subject);
    };
    $scope.loadSubjectsGrid = function(){
    	DashboardService.getSubjects().then(function(subjects){
    	  $scope.subjects.showEditor= false;
  		  $scope.subjects.list = subjects;
  	  });
    };
    $scope.loadJournalsGrid = function(){
    	DashboardService.getJournals().then(function(journals){
    	  $scope.journals.showEditor= false;
  		  $scope.journals.list = journals;
  	  });
    };
    $scope.updateJournal = function(journal){
    	$scope.journals.editJourn = angular.copy(journal);
    	$scope.journals.showEditor= true;
    	$timeout(function(){
    		$('#journalSubjectPicker').selectpicker('refresh');
    		},0);
    };
    $scope.saveSubject = function(subject){
    	  DashboardService.saveSubject(subject).then(function(resp){
    		  if(resp.success) $scope.loadSubjectsGrid();
    		  //$scope.alert = {"msg":resp.message, "type": resp.success? "success":"danger"};
    		  console.log('saveSubject::::::::::::::::::::::::::::'+resp);
    	  });
    };
    $scope.saveJournal = function(journal){
    	  DashboardService.saveJournal(journal).then(function(resp){
    		  if(resp.success) $scope.loadJournalsGrid();
    		 // $scope.alert = {"msg":resp.message, "type": resp.success? "success":"danger"};
    		  console.log('saveJournal::::::::::::::::::::::::::::'+resp);
    	  });
    };
    $scope.saveArticle = function(article){
  	  DashboardService.saveArticle(article).then(function(resp){
  		 // if(resp.success) $scope.loadJournalsGrid();
  		 // $scope.alert = {"msg":resp.message, "type": resp.success? "success":"danger"};
  		  console.log('saveArticle::::::::::::::::::::::::::::'+resp);
  	  });
  };;
    $scope.getReviewersForArticle = function(article){
  	  DashboardService.getReviewersForArticle(article).then(function(resp){
  		 // if(resp.success) $scope.loadJournalsGrid();
  		 // $scope.alert = {"msg":resp.message, "type": resp.success? "success":"danger"};
  		  $scope.articleReviewers.list = resp;
  		  console.log('getReviewersForArticle::::::::::::::::::::::::::::'+resp);
  	  });
  };
    $scope.assign =function(article){
    	var scope =  $scope.$new();
    	//scope.articleReviewers = $scope.getReviewersForArticle(article);
    	//scope.articleReviewers =  angular.copy(articleReviewers);
    	scope.article = angular.copy(article);
    	scope.user= $scope.user;
    	var options = {
    			scope: scope,
    			size : 'lg',
    			templateUrl: "/ats/html/assign-article-card.html"
    	};
    	$uibModal.open(options);
    	
    };
    $scope.alert = function (message, callback){
    	
    	var options = {
    			scope: $scope.$new(),
    			size : 'lg',
    			templateUrl: "/ats/html/alert.html"
    	};
    	$uibModal.open(options);
    }
  });
  angular.module('myApp').controller('articleCtrl',['$scope', '$timeout', 'articleService', function($scope,$timeout, articleService){
	  $timeout(function(){
		  $('#articleTypePicker').selectpicker('refresh');
		  $('#subjectPicker').selectpicker('refresh');
		  
	  },10);
	 
	  console.log('articleCtrl loaded');
	  
	  $scope.service = articleService;
	  
  }]);
  
  angular.module('myApp').controller('userCtrl',['$scope', '$timeout', function($scope,$timeout){
		  $timeout(function(){
			  $('#userTypePicker').selectpicker('refresh');
			  $('#subjectPicker').selectpicker('refresh');
			  
		  },0);
		 
		  console.log('articleCtrl loaded');
	  }]);