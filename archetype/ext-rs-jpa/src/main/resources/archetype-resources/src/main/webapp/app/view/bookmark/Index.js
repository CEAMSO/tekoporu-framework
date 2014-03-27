Ext.define('App.view.bookmark.Index', {
	extend : 'Ext.panel.Panel',
	//alias : 'widget.bookmarkIndex',
	id : 'bookmarkIndex',
	xtype :'bookmarkIndex',
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
			xtype : 'bookmarkList',
	}]
});
