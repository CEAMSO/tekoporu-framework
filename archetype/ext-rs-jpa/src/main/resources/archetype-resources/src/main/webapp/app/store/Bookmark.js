Ext.define('App.store.Bookmark', {
	extend : 'Ext.data.Store',
	model : 'App.model.Bookmark',
	//requires: [ 'Ext.util.Cookies', 'App.lib.Base64' ],
	autoLoad : true,
	storeId : 'bookmarkStore',
	id: 'bookmarkStore',
	xtype: 'bookmarkStore',
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
	
	//headers : { Authorization : auth },
	
	proxy : {
		type : 'rest',
		url : '/${artifactId}/rest/bookmark',
		
		reader : {
			type : 'json',
			root : 'list',
			totalProperty : 'total'
		}
		
/*
		afterRequest : function(req, res) {
			console.log("Store Operation response: ", req.operation.response);
		},

		doRequest : function(operation, callback, scope) {
			var writer = this.getWriter();
			var request = this.buildRequest(operation, callback, scope);

			if (operation.allowWrite()) {
				request = writer.write(request);
			}
			
			//var base = new Ext.create('App.lib.Base64');
			//console.log(base._keyStr);
			//var auth = base.make_basic_auth(Ext.util.Cookies.get('username'),Ext.util.Cookies.get('password'));
			
			
			Ext.apply(request, {
				//headers : { Authorization : auth },
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
