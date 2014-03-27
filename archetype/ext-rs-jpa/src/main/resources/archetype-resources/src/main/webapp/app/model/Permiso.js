Ext.define('App.model.Permiso', {
    extend: 'Ext.data.Model',
    //requires: ['App.model.Rol'],

    fields: ['id', 'clave', 'operacion', 'instancia', 'recurso'],
    //belongsTo: 'App.model.Rol',
    //hasMany: {model: 'Rol', name: 'roles'},

	proxy : {
		type : 'rest',	
		url : '/${artifactId}/rest/permiso',
		reader : {
			type : 'json',
			root : 'permisos',
			//totalProperty : 'total'
		},
	}
});
