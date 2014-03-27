Ext.define('App.view.permiso.Update', {
	extend : 'Ext.form.Panel',
	model : 'App.model.Permiso',
	alias : 'widget.permisoUpdate',
	title : 'Permiso',
	id : 'permisoUpdate',
	store : 'Permiso',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'link',
	xtype : 'permisoUpdate',
	autoScroll : true,
	items : [{
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

