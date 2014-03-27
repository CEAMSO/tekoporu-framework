Ext.define('App.view.user.Index', {
	extend : 'Ext.panel.Panel',
	//alias : 'widget.userIndex',
	id : 'userIndex',
	xtype :'userIndex',
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
			xtype : 'userList',
	}]
});
