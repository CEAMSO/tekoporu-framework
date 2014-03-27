Ext.define('App.view.permiso.List', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.permisoList',
	title : 'Lista de Permisos',
	store : 'Permiso',
	id : 'permisoList',
	xtype : 'permisoList',
	requires : ['Ext.ux.grid.FiltersFeature', 'App.view.PagingToolbar'],
	
	features : [ {
		ftype : 'filters',
		local : true
	} ],

	/** para paginación**/
	dockedItems : [ {
		xtype : 'appPagingToolBar',
		store : 'Permiso', // same store GridPanel is using
		dock : 'bottom',
		displayInfo : true
	}],
	
	collapsible : true,
	
	//multiSelect: true, para usar hay que ver como borrar varios
	
	viewConfig : {
        stripeRows : true,
        enableTextSelection : true
    },
    
    fit : 1,
    center : true,
	
	initComponent : function() {
		this.columns = [
		/*
		 * {header:'Id', dataIndex:'id', flex:1}, {header:'Link',
		 * dataIndex:'link', flex:1}, {header:'Description',
		 * dataIndex:'description', flex:1},
		 */
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
		}, {
			text : 'Clave',
			sortable : true,
			dataIndex : 'clave',
			hidden : false,
			groupable : false,
			//width : 470,
			flex:1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('clave');
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
								property : 'clave',
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
			text : 'Recurso',
			sortable : true,
			dataIndex : 'recurso',
			hidden : false,
			groupable : false,
			//width : 470,
			flex:1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('recurso');
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
								property : 'recurso',
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
			text : 'Operación',
			sortable : true,
			dataIndex : 'operacion',
			hidden : false,
			groupable : false,
			//width : 470,
			flex:1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('operacion');
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
								property : 'operacion',
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
		/*
		{
			text : 'Instancia',
			sortable : true,
			dataIndex : 'instancia',
			hidden : false,
			groupable : false,
			//width : 470,
			flex:1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('instancia');
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
								property : 'instancia',
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
		*/
		
		];

		this.callParent(arguments);
	}
});