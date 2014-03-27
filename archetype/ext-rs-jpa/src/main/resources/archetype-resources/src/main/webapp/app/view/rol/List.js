Ext.define('App.view.rol.List', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.rolList',
	title : 'Lista de Roles',
	store : 'Rol',
	id : 'rolList',
	xtype : 'rolList',
	requires : ['Ext.ux.grid.FiltersFeature', 'App.view.PagingToolbar'],
	
	features : [ {
		ftype : 'filters',
		local : true
	} ],

	/** para paginación**/
	dockedItems : [ {
		xtype : 'appPagingToolBar',
		store : 'Rol', // same store GridPanel is using
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
			text : 'Descripción',
			sortable : true,
			dataIndex : 'descripcion',
			hidden : false,
			groupable : false,
			//width : 470,
			flex:1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('descripcion');
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
								property : 'descripcion',
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
		];

		this.callParent(arguments);
	}
});