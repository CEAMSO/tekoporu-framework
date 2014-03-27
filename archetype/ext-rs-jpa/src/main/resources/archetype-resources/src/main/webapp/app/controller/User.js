Ext.define("App.controller.User", {
    extend:'App.controller.Base',

    stores:'User',
    models:['User'],
    views:['user.Index', 'user.Create', 'user.Update', 'user.List'],

    url : 'user',
	modelName : 'App.model.User',
	id : 'userController',
	
	requires: ['App.store.UnpagedRol', 'App.store.Rol'],
	
	init : function () {
        this.control({
        	'userIndex' : { 
                afterrender : this.checkRights
            },
            
            'userIndex button[action=create]' : { 
                click : this.createItem
            },
            
            'userIndex button[action=update]': {
                click : this.updateItem
            },
            
            'userUpdate': {
                show : this.loadItemForm,
                hide : this.removeGrid
            },
            
            'userIndex button[action=destroy]': {
                click : this.destroyItem
            },
            
            'userCreate' : {
            	show : this.loadNewRecord,
            	hide : this.removeGrid2
            },
            
            'userCreate button[operation=save]' : {
            	click : this.saveNew
            },
            
            'userCreate button[operation=cancel]' : {
            	click : this.cancel
            },

            'userUpdate button[operation=save]' : {
            	click : this.saveEdited
            },
            
            'userUpdate button[operation=cancel]' : {
            	click : this.cancel
            },
            
            'userList' : {
            	itemdblclick : this.imprimirRoles
            },
                        
        });
    },
    
	imprimirRoles : function (grid, record) {
	    console.log('{test.controller.User} Double clicked on ' + record.get('id'));
	    /*
	    App.model.User.load(40,{success:function(user){
	    	console.log("User "+user.get('nombre'));
	    	//console.log("User "+user.get('roles'));
	    	user.roles().each(function(rol){
	    		console.log("Rol "+rol.get('descripcion'));
	    		rol.permisos().each(function(permiso){
		    		console.log("Permiso "+permiso.get('clave'));
		    	});
	    	});
	    }});
	     */
	    record.roles().each(function(rol){
    		console.log("Rol "+rol.get('descripcion'));
    		rol.permisos().each(function(permiso){
	    		console.log("Permiso "+permiso.get('clave'));
	    	});
    	});
	},
	
	loadItemForm : function () {
    	
    	var selection = Ext.getCmp(this.url + 'List').getSelectionModel().getSelection();
    	var record = selection[0];

        var view = Ext.getCmp(this.url + 'Update');
        view.loadRecord(record);

    	var roles = record.roles().getRange();
    	var storeRolesUser = Ext.create('App.store.UnpagedRol', {data : roles});

    	var rolesSinAsignar = Ext.Ajax.request({
			url : '/${artifactId}/rest/rol/unAssigned',
			params : { userId : record.get('id') },
			method : 'GET',
			scope: this,
			success: function (response) {
				var respuesta = Ext.decode(response.responseText);
				
				if (respuesta.success) {
					var storeRoles = Ext.create('App.store.UnpagedRol', {data : respuesta.roles});
					var grid = Ext.create('App.view.GridToGrid', 
							{ storeGrid1 : storeRoles,
							  storeGrid2 : storeRolesUser,
							  titleGrid1 : 'Roles Disponibles',
							  titleGrid2 : 'Roles Asignados'});
			        
					view.add(grid);
		            return;					 			
				} else {
					alert('Problemas de comunicación con el servidor');
					var grid = Ext.create('App.view.GridToGrid', 
							{ storeGrid1 : null,
							  storeGrid2 : storeRolesUser,
							  titleGrid1 : 'Roles Disponibles',
							  titleGrid2 : 'Roles Asignados'});
			        
					view.add(grid);
		            return;	
				}
			},
			failure: function () {
				alert('Servidor no disponible');
			}
		});       
    },

	saveEdited: function(request) {
		
    	//se obtienen los valores del form
		var form = Ext.ComponentQuery.query(this.url + 'Update')[0];
    	var rec = form.getValues();
    	var toSave = Ext.ComponentQuery.query(this.url + 'Update')[0].getRecord();
    	
    	
    	if (!form.isValid()) {
    		Ext.Msg.alert('Info', 'Campos incorrectos, Corrija antes de guardar');
    		return;
    	}
    	
    	toSave.set('nombre', rec.nombre);
    	toSave.set('apellido', rec.apellido);
    	toSave.set('email', rec.email);
    	toSave.set('username', rec.username);
    	toSave.set('pwd', rec.pwd);
    	toSave.set('telefono', rec.telefono);
    	
    	//var roless = Ext.ComponentQuery.query(this.url + 'Update'+' #grid2')[0].getStore().getRange(); 
    	var roless = Ext.ComponentQuery.query(this.url + 'Update'+' grid[nombre=grid2]')[0].getStore().getRange(); 
    	
    	
    	//console.log("cantidad de roles model" + toSave.roles().getCount());
    	//var roless = toSave.roles().getRange(); 
    	
    	//console.log("array roles length " + roless.length);
    	toSave.roles().removeAll();
    	//console.log("cant despues remove " + toSave.roles().getCount());
    	toSave.roles().insert(0, roless);
    	//console.log("cant despues insert " + toSave.roles().getCount());
    	
		toSave.save({ 
					scope: this,
					success : function() {
						Ext.History.add(this.url, true);
						this.refreshList();
					}
		});
	},
	
	loadNewRecord : function() {
		
		var view = Ext.getCmp(this.url + 'Create');
		var model = Ext.create('App.model.User');
	    view.loadRecord(model);
	    
	    var storeRolesUser = model.roles();
		var storeRoles = Ext.create('App.store.UnpagedRol');
		
	    var grid = Ext.create('App.view.GridToGrid', 
				{ storeGrid1 : storeRoles,
				  storeGrid2 : storeRolesUser,
				  titleGrid1 : 'Roles Disponibles',
				  titleGrid2 : 'Roles Asignados'});
        
		view.add(grid);
        
		//cargar el grid
		var grid1 = Ext.ComponentQuery.query(this.url + 'Create'+' grid[nombre=grid1]')[0];
        grid1.getStore().load();
	},
	
	saveNew: function(request) {
		
		//se obtienen los valores del form
		var form = Ext.ComponentQuery.query(this.url + 'Create')[0];
    	var rec = form.getValues();
    	
    	if (rec.pwd != rec.confirmPass) {
    		Ext.Msg.alert('Info', 'Las contraseñas no coinciden');
    		return;
    	} else if (!form.isValid()) {
    		Ext.Msg.alert('Info', 'Campos incorrectos, Corrija antes de guardar');
    		return;
    	}
    	
    	var toSave = Ext.ComponentQuery.query(this.url + 'Create')[0].getRecord();
    	toSave.set('nombre', rec.nombre);
    	toSave.set('apellido', rec.apellido);
    	toSave.set('email', rec.email);
    	toSave.set('username', rec.username);
    	toSave.set('pwd', rec.pwd);
    	toSave.set('telefono', rec.telefono);
    	
    	form.getForm().reset();
		toSave.save({ 
					scope: this,
					success : function() {
						Ext.History.add(this.url, true);
						this.refreshList();
					}
		});
	},
	
	removeGrid : function(request) {
    	var view = Ext.getCmp(this.url + 'Update');
    	view.remove(view.down('GridToGrid'));
	},
	
	removeGrid2 : function(request) {
    	var view = Ext.getCmp(this.url + 'Create');
    	view.remove(view.down('GridToGrid'));
	},
        
});
