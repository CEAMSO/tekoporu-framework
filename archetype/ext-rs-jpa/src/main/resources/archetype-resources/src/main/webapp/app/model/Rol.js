Ext.define('App.model.Rol', {
	requires: ['App.model.Permiso'],
    extend: 'Ext.data.Model',
    fields: ['id', 'descripcion'],
    //belongsTo: 'App.model.User',
    hasMany: {model: 'App.model.Permiso', name: 'permisos'},
    //hasMany: {model: 'User', name: 'users'},

	proxy : {
		type : 'rest',
		url : '/${artifactId}/rest/rol',
		
		reader : {
			type : 'json',
			root : 'list',
			totalProperty : 'total'
		}
		
	},
	
	writer : Ext.data.writer.Json.override({
	      getRecordData: function(record) {
	          Ext.apply(record.data, record.getAssociatedData());
	          return record.data;
	      }
	}),
});

