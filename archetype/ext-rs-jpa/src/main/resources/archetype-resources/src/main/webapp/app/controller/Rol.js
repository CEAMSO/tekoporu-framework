Ext.define("App.controller.Rol", {
    extend:'App.controller.Base',

    stores:'Rol',
    models:['Rol'],
    views:['rol.Index', 'rol.Create', 'rol.Update', 'rol.List'],

    url : 'rol',
	modelName : 'App.model.Rol',
	id : 'rolController',
	
	requires: ['App.view.GridToGrid', 'App.store.UnpagedPermiso'],
	
	init : function () {
        this.control({
        	'rolIndex' : { 
                afterrender : this.checkRights
            },
            
            'rolIndex button[action=create]' : { 
                click : this.createItem
            },
            
            'rolIndex button[action=update]': {
                click : this.updateItem
            },
            
            
            'rolIndex button[action=destroy]': {
                click : this.destroyItem
            },
            
            'rolCreate' : {
            	show : this.loadNewRecord,
            	hide : this.removeGrid2
            },
            
            'rolCreate button[operation=save]' : {
            	click : this.saveNew
            },
            
            'rolCreate button[operation=cancel]' : {
            	click : this.cancel
            },

            'rolUpdate button[operation=save]' : {
            	click : this.saveEdited
            },
            
            'rolUpdate button[operation=cancel]' : {
            	click : this.cancel
            },
            
            'rolList' : {
            	itemdblclick:this.imprimirPermisos
            },
            
            'rolUpdate' : {
            	show : this.loadItemForm,
            	hide : this.removeGrid
            },
                       
        });
    },
    
    imprimirPermisos : function (grid, record) {
	    console.log('{test.controller.Rol} Double clicked on ' + record.get('id'));

	    console.log("permisos ");
	    record.permisos().each(function(permiso){
    		console.log("Permiso "+permiso.get('clave'));
    	});
	},
	
	loadItemForm : function () {
    	
    	var selection = Ext.getCmp(this.url + 'List').getSelectionModel().getSelection();
    	var record = selection[0];

        var view = Ext.getCmp(this.url + 'Update');
        view.loadRecord(record);

        var permisos = record.permisos().getRange();
    	var storePermisosRol = Ext.create('App.store.UnpagedPermiso', {data : permisos});

    	var permisosSinAsignar = Ext.Ajax.request({
			url : '/${artifactId}/rest/permiso/unAssigned',
			params : { rolId : record.get('id') },
			method : 'GET',
			scope: this,
			success: function (response) {
				var respuesta = Ext.decode(response.responseText);
				
				if (respuesta.success) {
					var storePermisos = Ext.create('App.store.UnpagedPermiso', {data : respuesta.permisos});
					var grid = Ext.create('App.view.GridToGrid', 
							{ storeGrid1 : storePermisos,
							  storeGrid2 : storePermisosRol,
							  titleGrid1 : 'Permisos Disponibles',
							  titleGrid2 : 'Permisos Asignados',
							  dataIndex : 'clave'});
			        
					view.add(grid);
		            return;					 			
				} else {
					alert('Problemas de comunicación con el servidor');
					var grid = Ext.create('App.view.GridToGrid', 
							{ storeGrid1 : null,
							  storeGrid2 : storePermisosRol,
							  titleGrid1 : 'Permisos Disponibles',
							  titleGrid2 : 'Permisos Asignados',
							  dataIndex : 'clave'});
			        
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
    	
    	toSave.set('descripcion', rec.descripcion);
    	
    	
    	//var roless = Ext.ComponentQuery.query(this.url + 'Update'+' #grid2')[0].getStore().getRange(); 
    	var permisoss = Ext.ComponentQuery.query(this.url + 'Update'+' grid[nombre=grid2]')[0].getStore().getRange(); 
    	
    	//console.log("array permisos length " + permisoss.length);
    	toSave.permisos().removeAll();
    	toSave.permisos().insert(0, permisoss);
    	
    	//console.log("cant despues insert " + toSave.permisos().getCount());
    	
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
		var model = Ext.create('App.model.Rol');
	    view.loadRecord(model);

	    var storePermisosRol = model.permisos();
		var storePermisos = Ext.create('App.store.UnpagedPermiso');
		
        var grid = Ext.create('App.view.GridToGrid', 
				{ storeGrid1 : storePermisos,
				  storeGrid2 : storePermisosRol,
				  titleGrid1 : 'Permisos Disponibles',
				  titleGrid2 : 'Permisos Asignados',
				  dataIndex : 'clave'});
        
		view.add(grid);
        
        //cargar el grid
		var grid1 = Ext.ComponentQuery.query(this.url + 'Create'+' grid[nombre=grid1]')[0];
        grid1.getStore().load();
	},
	
	saveNew: function(request) {
		
		//se obtienen los valores del form
		var form = Ext.ComponentQuery.query(this.url + 'Create')[0];
    	var rec = form.getValues();
    	
    	if (!form.isValid()) {
    		Ext.Msg.alert('Info', 'Campos incorrectos, Corrija antes de guardar');
    		return;
    	}
    	
    	var toSave = Ext.ComponentQuery.query(this.url + 'Create')[0].getRecord();
    	toSave.set('descripcion', rec.descripcion);
    	
    	//var permisos = Ext.ComponentQuery.query(this.url + 'Create'+' #grid2')[0].getStore();
    	form.getForm().reset();
   	
		toSave.save({ 
					scope: this,
					success : function() {
						//console.log('Esto es en el save de ' + this.url);
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
