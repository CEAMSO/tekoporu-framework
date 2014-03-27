Ext.define('App.store.User', {
	extend : 'Ext.data.Store',
	model : 'App.model.User',
	//autoLoad : true,
	storeId : 'userStore',
	id: 'userStore',
	xtype: 'userStore',
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
		url : '/${artifactId}/rest/user',
		
		reader : {
			type : 'json',
			root : 'list',
			totalProperty : 'total'
		},
	}
	
});
