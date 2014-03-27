Ext.define('App.controller.Login', {
    extend:'App.controller.Base',
    init:function () {
        this.control({
            'viewport': {
                render : this.index,
            },
            'button[action=login]':{
                click : this.login
            },
            '#loginwindow textfield':{
                specialkey : this.keyenter
            },
            'button[action=logout]':{
                click : this.logout
            }
        });
    },
    views:[
        'Login', 'Viewport'
    ],
    refs:[
        {
            ref:'viewport',
            selector:'viewport'
        },
        {
            ref:'loginwindow',
            selector:'loginwindow'
        },
        {
        	ref:'mainpanel',
        	selector:'mainpanel'
        }
        
    ],

    index:function () {
    	//alert('index login');
    	
        console.log('Index function');
        //var loginWin = Ext.getCmp('loginwindow');
        var loginWin = Ext.create('App.view.Login');
        //loginWin.show();
        //var form = Ext.getCmp('loginform');
        
        var checkLoggedIn = Ext.Ajax.request({
			url: '/${artifactId}/rest/islogged',
			scope: this,
			success: function(response){
				var respuesta = Ext.decode(response.responseText);
				
				if (respuesta.success) {
		            this.createDashboard(respuesta.permissions, respuesta.username);
		            return;					 			
				} else {
					loginWin.show();
					//alert('no logueado');
					//lay.setActiveItem(0); por defecto es 0
				}
			},
			failure: function () {
				alert('Servidor de Autenticación no disponible');
				//lay.setActiveItem(0); por defecto es 0
				loginWin.show();
			}
		});

        
        
        //this.login();
    },

    login:function () {
        console.log('Login button');
        //var loginWin = Ext.getCmp('loginwindow');
        var form = Ext.getCmp('loginform');
        var values = form.getValues();

        //var lay = this.getViewport().getLayout();

        var checkLoggedIn = Ext.Ajax.request({
			url: '/${artifactId}/rest/islogged',
			scope: this,
			success: function(response){
				var respuesta = Ext.decode(response.responseText);

				if (respuesta.success) {
					this.createDashboard(respuesta.permissions, respuesta.username);
		            return;					 			
				} else {
					//lay.setActiveItem(0);
					form.getForm().reset();
				}
			},
			failure: function () {
				alert('Servidor de Autenticación no disponible / Comprobación de sessión');
				form.getForm().reset();
				//lay.setActiveItem(0);
			}
		});

        //if (values.userName != '' && values.password != '') {
        if(form.isValid()) {
        	var loginrequest = Ext.Ajax.request({
				scope : this,
				clientValidation : true,
				url : '/${artifactId}/rest/login',
				method :'POST',
				
				jsonData : {
				    username: values.userName,
				    password: values.password
				},
				
        		success: function(response) {
        			var respuesta = Ext.decode(response.responseText);
        			
        			if (respuesta.success) {
        				this.createDashboard(respuesta.permissions, respuesta.username);
        			} else {
        				alert('User/Password incorrectos');
        			}
        		},
        		failure : function () {
        			alert('Servidor de Autenticación no disponible / Intento de login');
        		}
        	});
        }
    },
    
    logout : function (button) {
        Ext.log('Logout user');
        var lay = this.getViewport().getLayout();
        lay.setActiveItem(0);
        
        //var loginWin = Ext.getCmp('loginwindow');
        var form = Ext.getCmp('loginform');
        form.getForm().reset();//resetear los campos
        
        window.top.location.hash = 'dashboard'; //setea el token de la url a dashboard
        
        var logOutRequest = Ext.Ajax.request({
			url: '/${artifactId}/rest/logout',
			scope: this,
			success: function(response){
				console.log('======= Deslogueado exitósamente ======');
				window.location.reload();
			},
			failure: function () {
				Ext.util.Cookies.clear('JSESSIONID');
				alert('Servidor de Autenticación no disponible');
				window.location.reload();
			}
		});
        
    },
    
    keyenter : function (item, event) {
        if (event.getKey() == event.ENTER) {
            this.login();
        }

    },
    
    createDashboard : function(permissions, username) {
    	console.log('================== usuario logueado ==================');
		//TODO comprobar la visualizacion segun los permisos del usuario logueado
		
		/** Carga de la clase permisos desde el json permisos (debe realizarse la consulta por medio del WS)**/

		
		var loginWin = Ext.getCmp('loginwindow');
		loginWin.hide();
		
		var viewport = this.getViewport();
		viewport.getLayout().setActiveItem(1);
		
		
		var permisos = Ext.create('App.lib.Permisos', permissions);
		
		var dash = Ext.getCmp('mainMenu');
			

		//console.log('= clid dash ' + dash.getRootNode().firstChild.raw['action']);//childNodes.length);
		
		var fun = function recursive (root) {
			 var childNodes = root.childNodes,
			 length = childNodes.length,
			 i;
			
			 for (i = 0; i < length; i++) {
			     var action = childNodes[i].raw['action'];
			     
			     //console.log('action : ' + action);
			     if (childNodes[i].hasChildNodes()) {
		    		 recursive(childNodes[i]);
		    	 }
			     
			     if (permissions[action] != undefined) {
			    	 if (!permissions[action].read) {
			    		//console.log('Removing : ' + childNodes[i].raw['action']);
			    		childNodes[i].remove(); 
			    	 }
			     }
			 }
		};
		
		fun(dash.getRootNode());
		
		
		App.getApplication().permissions = permissions;
        Ext.getCmp('loggedin').update('Usuario  ' + ' <b>' + username + '</b>');
        /*
        // Start with dashboard
        token = Ext.History.getToken();
        if (token == null) {
            Ext.History.add('dashboard', true);
        } 
        */
        
       // App.dashboardInit(this.getViewport().down('container[region=center]'));
    }
});
