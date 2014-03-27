Ext.define('App.store.Rol', {
	extend : 'Ext.data.Store',
	model : 'App.model.Rol',
	storeId : 'rolStore',
	id: 'rolStore',
	xtype: 'rolStore',
	autoLoad : {
		start : 0,
		limit : 4
	},
	pageSize : 4,
	remoteSort : true,
	sorters : {
		property : 'id',
		direction : 'asc'
	},
		
	proxy : {
		type : 'rest',
		url : '/${artifactId}/rest/rol',
		
		reader : {
			type : 'json',
			root : 'list',
			totalProperty : 'total'
		}
		
	}
});
