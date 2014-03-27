Ext.define('App.store.UnpagedPermiso', {
	extend : 'Ext.data.Store',
	model : 'App.model.Permiso',

	storeId : 'unpagedPermisoStore',
	id: 'unpagedPermisoStore',
	xtype: 'unpagedPermisoStore',
	
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
