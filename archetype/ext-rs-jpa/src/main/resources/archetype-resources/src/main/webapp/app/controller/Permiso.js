Ext.define("App.controller.Permiso", {
    extend:'App.controller.Base',

    stores:'Permiso',
    models:['Permiso'],
    views:['permiso.Index', 'permiso.Create', 'permiso.Update', 'permiso.List'],

    url : 'permiso',
	modelName : 'App.model.Permiso',
	id : 'permisoController',
	
	//requires: ['App.view.permiso.RolesGrid'],
	
	init : function () {
        this.control({
        	'permisoIndex' : { 
                afterrender : this.checkRights
            },
            
            'permisoIndex button[action=create]' : { 
                click : this.createItem
            },
            
            'permisoIndex button[action=update]': {
                click : this.updateItem
            },
            
            'permisoUpdate': {
                afterrender : this.loadItemForm
            },
            
            'permisoIndex button[action=destroy]': {
                click : this.destroyItem
            },
            
            'permisoCreate button[operation=save]' : {
            	click : this.saveNew
            },
            
            'permisoCreate button[operation=cancel]' : {
            	click : this.cancel
            },

            'permisoUpdate button[operation=save]' : {
            	click : this.saveEdited
            },
            
            'permisoUpdate button[operation=cancel]' : {
            	click : this.cancel
            },
            
            /*'permisoList' : {
            	itemdblclick:this.imprimir
            },*/
                        
        });
    },
    
	imprimir:function (grid, record)
	{
	    console.log('{test.controller.Permiso} Double clicked on ' + record.get('id'));

	    record.roles().each(function(rol){
    		console.log("Rol "+rol.get('descripcion'));
    		rol.permisos().each(function(permiso){
	    		console.log("Permiso "+permiso.get('clave'));
	    	});
    	});
	},
        
});
