Ext.define('App.view.permiso.Create', {
	extend : 'Ext.form.Panel',
	model : 'App.model.Permiso',
	alias : 'widget.permisoCreate',
	title : 'Permiso',
	id : 'permisoCreate',
	store : 'Permiso',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'descripcion',
	xtype : 'permisoCreate',
	autoScroll : true,
	items : [ {
				xtype : 'textfield',
				name : 'clave',
				fieldLabel : 'Clave',
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

