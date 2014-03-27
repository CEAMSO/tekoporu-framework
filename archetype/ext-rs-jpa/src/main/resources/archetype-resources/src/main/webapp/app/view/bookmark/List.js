Ext.define('App.view.bookmark.List', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.bookmarkList',
	title : 'Lista de Bookmarks',
	store : 'Bookmark',
	id : 'bookmarkList',
	xtype : 'bookmarkList',
	requires : ['Ext.ux.grid.FiltersFeature', 'App.view.PagingToolbar'],
	
	features : [ {
		ftype : 'filters',
		local : true
	} ],

	/** para paginación**/
	dockedItems : [ {
		xtype : 'appPagingToolBar',
		store : 'Bookmark', // same store GridPanel is using
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
			text : 'Link',
			//width : 475,
			flex:1,
			sortable : true,
			dataIndex : 'link',
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
								property : 'link',
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
			dataIndex : 'description',
			hidden : false,
			groupable : false,
			//width : 470,
			flex:1,
			locked : false,
			renderer : function(v, cellValues, rec) {
				return rec.get('description');
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
								property : 'description',
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
