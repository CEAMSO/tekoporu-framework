Ext.define('App.store.Permiso', {
	extend : 'Ext.data.Store',
	model : 'App.model.Permiso',
	//autoLoad : true,
	storeId : 'permisoStore',
	id: 'permisoStore',
	xtype: 'permisoStore',
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
		url : '/${artifactId}/rest/permiso',
		
		reader : {
			type : 'json',
			root : 'list',
			totalProperty : 'total'
		}
		
	}
});
