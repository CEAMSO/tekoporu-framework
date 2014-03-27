Ext.define('App.view.rol.Update', {
	extend : 'Ext.form.Panel',
	model : 'App.model.Rol',
	alias : 'widget.rolUpdate',
	title : 'Rol',
	id : 'rolUpdate',
	store : 'Rol',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'link',
	xtype : 'rolUpdate',
	autoScroll : true,
	items : [{
				xtype : 'textfield',
				name : 'descripcion',
				fieldLabel  : 'Descripción',
				anchor : '80%',
				allowBlank : false,
			},
			{
				xtype : 'hidden',
				name : 'rolId',
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

