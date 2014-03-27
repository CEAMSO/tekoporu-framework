Ext.define('App.view.user.List', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.userList',
	title : 'Lista de Users',
	store : 'User',
	id : 'userList',
	xtype : 'userList',
	requires : [ 'Ext.ux.grid.FiltersFeature', 'App.view.PagingToolbar' ],

	features : [ {
		ftype : 'filters',
		local : true
	} ],

	/** para paginación* */
	dockedItems : [ {
		xtype : 'appPagingToolBar',
		store : 'User', // same store GridPanel is using
		dock : 'bottom',
		displayInfo : true
	} ],

	collapsible : true,

	// multiSelect: true, para usar hay que ver como borrar varios

	viewConfig : {
		stripeRows : true,
		enableTextSelection : true
	},

	fit : 1,
	center : true,

	initComponent : function() {
		this.columns = [
		{
			text : 'Id',
			width : 150,
			sortable : true,
			dataIndex : 'id',
			locked : false,
			editor : {
				xtype : 'textfield',
			},
			items : {
				xtype : 'textfield',
				flex : 1,
				margin : 2,
				enableKeyEvents : true,
				listeners : {
					keyup : function() {
						var store = this.up('tablepanel').store;
						store.clearFilter();
						if (this.value) {
							store.filter({
								property : 'id',
								value : this.value,
								anyMatch : true,
								caseSensitive : false
							});
						}
					},
					buffer : 500
				}
			}
		},
		{
			text : 'Nombre',
			// width : 150,
			flex : 1,
			sortable : true,
			dataIndex : 'nombre',
			locked : false,
			editor : {
				xtype : 'textfield',
			},
			items : {
				xtype : 'textfield',
				flex : 1,
				margin : 2,
				enableKeyEvents : true,
				listeners : {
					keyup : function() {
						var store = this.up('tablepanel').store;
						store.clearFilter();
						if (this.value) {
							store.filter({
								property : 'nombre',
								value : this.value,
								anyMatch : true,
								caseSensitive : false
							});
						}
					},
					buffer : 500
				}
			}
		}, {
			text : 'Apellido',
			// width : 475,
			flex : 1,
			sortable : true,
			dataIndex : 'apellido',
			hidden : false,
			locked : false,
			editor : {
				xtype : 'textfield',
			},
			items : {
				xtype : 'textfield',
				flex : 1,
				margin : 2,
				enableKeyEvents : true,
				listeners : {
					keyup : function() {
						var store = this.up('tablepanel').store;
						store.clearFilter();
						if (this.value) {
							store.filter({
								property : 'apellido',
								value : this.value,
								anyMatch : true,
								caseSensitive : false
							});
						}
					},
					buffer : 500
				}
			}
		}, {
			text : 'Email',
			sortable : true,
			dataIndex : 'email',
			hidden : false,
			groupable : false,
			// width : 470,
			flex : 1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('email');
			},
			editor : {
				xtype : 'textfield',
			},
			items : {
				xtype : 'textfield',
				flex : 1,
				margin : 2,
				enableKeyEvents : true,
				listeners : {
					keyup : function() {
						var store = this.up('tablepanel').store;
						store.clearFilter();
						if (this.value) {
							store.filter({
								property : 'email',
								value : this.value,
								anyMatch : true,
								caseSensitive : false
							});
						}
					},
					buffer : 500
				}
			}
		}, {
			text : 'Usuario',
			sortable : true,
			dataIndex : 'username',
			hidden : false,
			groupable : false,
			// width : 470,
			flex : 1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('username');
			},
			editor : {
				xtype : 'textfield',
			},
			items : {
				xtype : 'textfield',
				flex : 1,
				margin : 2,
				enableKeyEvents : true,
				listeners : {
					keyup : function() {
						var store = this.up('tablepanel').store;
						store.clearFilter();
						if (this.value) {
							store.filter({
								property : 'username',
								value : this.value,
								anyMatch : true,
								caseSensitive : false
							});
						}
					},
					buffer : 500
				}
			}
		}, ];

		this.callParent(arguments);
	}
});