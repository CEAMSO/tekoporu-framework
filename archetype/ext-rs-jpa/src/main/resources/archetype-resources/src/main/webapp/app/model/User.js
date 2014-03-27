Ext.define('App.model.User', {
	requires: ['App.model.Rol'],
    extend: 'Ext.data.Model',
    fields: ['id', 'activo', 'apellido', 'email', 'nombre', 'pwd', 'telefono', 'username'],
    hasMany: {model: 'App.model.Rol', name: 'roles'},
    
	proxy : {
		type : 'rest',	
		url : '/${artifactId}/rest/user',
		reader : {
			type : 'json'
		},
		
		writer : Ext.data.writer.Json.override({
		      getRecordData: function(record) {
		          Ext.apply(record.data, record.getAssociatedData());
		          return record.data;
		      }
		}),
		
		 /*writer: new Ext.data.JsonWriter({
			 encode: false,
			 writeAllFields: true,
			 listful: true,
			 getRecordData: function (record) {
				 record.set('roles', record.roles().data.items);
				 return record.data;
			 }
		 }),*/
		
		/*
		
		afterRequest : function(req, res) {
			console.log("Ahoy!", req.operation.response);
		},

		doRequest : function(operation, callback, scope) {
			var writer = this.getWriter(), request = this.buildRequest(
					operation, callback, scope);

			if (operation.allowWrite()) {
				request = writer.write(request);
			}

			Ext.apply(request, {
				headers : this.headers,
				timeout : this.timeout,
				scope : this,
				callback : this.createRequestCallback(request, operation,
						callback, scope),
				method : this.getMethod(request),
				disableCaching : false
			// explicitly set it to false, ServerProxy handles caching
			});

			console.log('request', request);
			console.log('request.params', request.params);

			Ext.Ajax.request(request);

			return request;
		},
		*/
	}
});
