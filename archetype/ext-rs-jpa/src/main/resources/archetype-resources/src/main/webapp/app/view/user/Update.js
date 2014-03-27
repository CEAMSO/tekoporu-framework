Ext.define('App.view.user.Update', {
	extend : 'Ext.form.Panel',
	model : 'App.model.User',
	alias : 'widget.userUpdate',
	title : 'User',
	id : 'userUpdate',
	store : 'User',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'link',
	xtype : 'userUpdate',
	//requires: ['App.view.user.Prueba'],
	autoScroll : true,
	items : [ {
				xtype : 'textfield',
				name : 'nombre',
				fieldLabel : 'Nombre',
				anchor : '80%'
			},
			{
				xtype : 'textfield',
				name : 'apellido',
				fieldLabel  : 'Apellido',
				anchor : '80%'
			},
			{
				xtype : 'textfield',
				name : 'email',
				vtype: 'email',
				fieldLabel  : 'Email',
				anchor : '80%'
			},
			{
				xtype : 'textfield',
				name : 'username',
				fieldLabel  : 'Usuario',
				anchor : '80%'
			},/*
			{
				xtype : 'textfield',
				name : 'pwd',
				fieldLabel  : 'Contraseña',
				anchor : '80%'
			},*/
			{
				xtype : 'textfield',
				name : 'telefono',
				fieldLabel  : 'Teléfono',
				anchor : '80%'
			},
			{
				xtype : 'hidden',
				name : 'id',
				fieldLabel : 'id'
			},
			
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

