Ext.define('App.view.bookmark.Update', {
	extend : 'Ext.form.Panel',
	model : 'App.model.Bookmark',
	alias : 'widget.bookmarkUpdate',
	title : 'Bookmark',
	id : 'bookmarkUpdate',
	store : 'Bookmark',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'link',
	xtype : 'bookmarkUpdate',
	autoScroll : true,
	items : [ {
				xtype : 'textfield',
				name : 'link',
				fieldLabel : 'Link',
				anchor : '80%'
			},
			{
				xtype : 'textfield',
				name : 'description',
				fieldLabel  : 'Descripción',
				anchor : '80%'
			},
			{
				xtype : 'hidden',
				name : 'id',
				fieldLabel : 'id'
			}
		],
	buttons : [{
		text : 'Guardar',
		operation : 'save'
	},
	{
		text : 'Cancelar',
		operation : 'cancel'
	}]
	
});

