Ext.define('App.controller.Base', {
	extend: 'Ext.ux.app.RoutedController',
	//nombre : "base",
	
	index: function(request) {
		this.render("workspace", this["get" + this.id + "IndexView"]());
	},
	
	create: function(request) {
		this.render("workspace", this["get" + this.id + "CreateView"]());
	},

	update: function(request) {
		this.render("workspace", this["get" + this.id + "UpdateView"]());
	},
	
	cancel : function(request) {
		Ext.History.add(this.url, true);
	},
	
    saveNew: function(request) {
    	//se obtienen los valores del form
    	var form = Ext.ComponentQuery.query(this.url + 'Create')[0];
    	var rec = form.getValues();
    	
    	if (!form.isValid()) {
    		Ext.Msg.alert('Info', 'Campos incorrectos, Corrija antes de guardar');
    		return;
    	}
    	
    	var toSave = Ext.create(this.modelName, rec);
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
	
    saveEdited: function(request) {
    	//se obtienen los valores del form
    	var form = Ext.ComponentQuery.query(this.url + 'Update')[0];
    	var rec = form.getValues();
    	
    	if (!form.isValid()) {
    		Ext.Msg.alert('Info', 'Campos incorrectos, Corrija antes de guardar');
    		return;
    	}
    	
    	var toSave = Ext.create(this.modelName, rec);
    	
		toSave.save({ 
					scope: this,
					success : function() {
						//console.log('Esto es en el update de ' + this.url);
						Ext.History.add(this.url, true);
						this.refreshList();
					}
		});
	},
	
	loadItemForm : function () {
    	
    	var selection = Ext.getCmp(this.url + 'List').getSelectionModel().getSelection();
    	var record = selection[0];

        var view = Ext.getCmp(this.url + 'Update');
        view.loadRecord(record);
    },
    
    createItem : function () {
    	Ext.History.add(this.url + '/create', true);
    },
    
    updateItem : function () {
    	var selection = Ext.getCmp(this.url + 'List').getSelectionModel().getSelection();
		if (!selection.length) {
			Ext.Msg.alert('Info', 'No se seleccionó un registro');
			return;
		}
    	Ext.History.add(this.url + '/update', true);
    },
    
    destroyItem : function () {
    	var grid = Ext.getCmp(this.url + 'List');
    	var selection = grid.getSelectionModel().getSelection();

		if (!selection.length) {
			Ext.Msg.alert('Info', 'No se seleccionó un registro');
			return;
		}
		
		var record = selection[0];
		
		Ext.Msg.buttonText.yes = "Sí";

		Ext.Msg.confirm(
				'Borrar Registro', 
				'¿Está seguro?',
				function(buttonId) {
					if (buttonId == 'yes') {
						grid.store.remove(record);
						grid.store.sync({
							success : function() {
								Ext.History.add(this.url, true);
								this.refreshList();
							},
							scope : this,
						});
					}
				},
				this
		);
	},
	
	refreshList : function () {
    	var view = 	Ext.getCmp(this.url + 'List');
    	view.getStore().load({
    	    scope: this,
    	    callback: function(records, operation, success) {
    	        view.child('pagingtoolbar').doRefresh();
    	    }
    	});
    },
    
    checkRights : function () {
		var buttons = Ext.ComponentQuery.query(this.url + 'Index button');
		var permissions = App.getApplication().permissions[this.url]; //obtine los permisos de bookmark
		
		//console.log('length: ' + buttons.length);
		//console.log('pemissions bookmark create: ' + permissions.create);
		if (permissions == undefined) {
			console.log("=== Recurso " + this.url + " no encontrado");
			return;
		}
		
		for (var i = 0; i < buttons.length; i++) {
			var action = buttons[i].action;
			if (action != undefined) {
				if (permissions[action] != undefined && permissions[action] == false) {
					//console.log('escondiendo boton: ' + action);
					buttons[i].hide();
				} 
			}
		}
    }
});