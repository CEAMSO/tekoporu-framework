Ext.define('App.view.permiso.Index', {
	extend : 'Ext.panel.Panel',
	//alias : 'widget.permisoIndex',
	id : 'permisoIndex',
	xtype :'permisoIndex',
	autoScroll : true,
	
	dockedItems: [{
		xtype: 'toolbar',
		dock: 'top',
		/*
		items: [{
			text : 'Agregar',
			action : 'create',
		},
		{
			text: 'Editar',
			action : 'update',
		},
		{
			text : 'Borrar',
			action : 'destroy',
		}]
		*/
	}],
	
	
	items: [{
			xtype : 'permisoList',
	}]
});
