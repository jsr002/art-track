 angular.module('myApp', ['ui.router', 'ui.bootstrap', 'ui.bootstrap.alert']);
  
  angular.module('myApp').run(function($rootScope, $location, $state, LoginService) {
    $rootScope.$on('$stateChangeStart', 
      function(event, toState, toParams, fromState, fromParams){ 
          console.log(toState);
                setTimeout(function() {
                    // after 1000 ms we add the class animated to the login/register card
                    $('.card').removeClass('card-hidden');
                }, 100);
                
                if(toState.name == 'logout') {// if loggedout
              	  LoginService.logout();
              	  return;
                };
      });
    
      if(!LoginService.isAuthenticated()) {
        $state.transitionTo('login');
      }
  });
  
  angular.module('myApp').config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/login');
    $stateProvider
      .state('login', {
        url : '/login',
        templateUrl : 'html/login.html',
        controller : 'LoginController'
      })
      .state('home', {
        url : '/home',
        templateUrl : 'home.html',
        controller : 'HomeController'
      })
      .state('register', {
          url : '/register',
          templateUrl : 'html/registration.html',
          controller : 'RegisterController'
        })
        .state('dashboard', {
          url : '/dashboard',
          templateUrl : 'html/dashboard.html',
          controller : 'HomeController'
        }).state('userprofile', {
            url : '/userprofile',
            templateUrl : 'html/userProfile.html',
            controller : 'profileController'
          }).state('article', {
              url : '/article',
              abstract:true,
              template: '<ui-view/>'
          }).state('article.submit', {
              url : '/submit',
              templateUrl : 'html/submitArticle.html',
              controller : 'articleCtrl'
          }).state('logout', {
		      url : '/logout',
		      templateUrl : 'html/login.html',
		      controller : 'LoginController'
		    });
  }]);
