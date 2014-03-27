Ext.define('App.view.bookmark.Create', {
	extend : 'Ext.form.Panel',
	model : 'App.model.Bookmark',
	alias : 'widget.bookmarkCreate',
	title : 'Bookmark',
	id : 'bookmarkCreate',
	store : 'Bookmark',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'link',
	xtype : 'bookmarkCreate',
	autoScroll : true,
	items : [ {
				xtype : 'textfield',
				name : 'link',
				fieldLabel : 'Link',
				anchor : '80%',
				allowBlank : false,
			},
			{
				xtype : 'textfield',
				name : 'description',
				fieldLabel  : 'Descripción',
				anchor : '80%',
				allowBlank : false,
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

