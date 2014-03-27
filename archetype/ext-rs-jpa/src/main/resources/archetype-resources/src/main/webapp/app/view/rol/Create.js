Ext.define('App.view.rol.Create', {
	extend : 'Ext.form.Panel',
	model : 'App.model.Rol',
	alias : 'widget.rolCreate',
	title : 'Rol',
	id : 'rolCreate',
	store : 'Rol',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'descripcion',
	xtype : 'rolCreate',
	autoScroll : true,
	items : [ {
				xtype : 'textfield',
				name : 'descripcion',
				fieldLabel : 'Descripción',
				anchor : '80%',
				allowBlank : false,
			},
			{
				xtype : 'hidden',
				name : 'roId',
				fieldLabel : 'rolId'
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

