Ext.define('App.view.rol.Index', {
	extend : 'Ext.panel.Panel',
	//alias : 'widget.rolIndex',
	id : 'rolIndex',
	xtype :'rolIndex',
	autoScroll : true,
	dockedItems: [{
		xtype: 'toolbar',
		dock: 'top',
		
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
	}],
	
	
	items: [{
			xtype : 'rolList',
	}]
});
