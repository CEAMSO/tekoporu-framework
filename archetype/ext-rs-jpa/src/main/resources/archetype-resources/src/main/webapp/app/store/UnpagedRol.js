Ext.define('App.store.UnpagedRol', {
	extend : 'Ext.data.Store',
	model : 'App.model.Rol',
	storeId : 'unpagedRolStore',
	xtype: 'unpagedRolStore',

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
