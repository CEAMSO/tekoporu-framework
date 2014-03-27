Ext.define('App.view.user.Create', {
	extend : 'Ext.form.Panel',
	model : 'App.model.User',
	alias : 'widget.userCreate',
	title : 'User',
	id : 'userCreate',
	store : 'User',
	bodyStyle : 'padding: 10px',
	defaultFocus : 'link',
	xtype : 'userCreate',
	autoScroll : true,
	monitorValid: true,
	items : [ {
				xtype : 'textfield',
				name : 'nombre',
				fieldLabel : 'Nombre',
				anchor : '80%',
				allowBlank : false,
				validator: function(val) {
				     if (!Ext.isEmpty(val)) {
				        return true;
				     } else {
				         return "Value cannot be empty";
				     }
				   }
			},
			{
				xtype : 'textfield',
				name : 'apellido',
				fieldLabel  : 'Apellido',
				anchor : '80%',
				allowBlank:false,
			},
			{
				xtype : 'textfield',
				name : 'email',
				fieldLabel  : 'Email',
				anchor : '80%',
				allowBlank:true,
				vtype : 'email',
			},
			{
				xtype : 'textfield',
				name : 'username',
				fieldLabel  : 'Usuario',
				anchor : '80%',
				allowBlank:false,
			},
			{
				xtype : 'textfield',
				inputType: 'password',
				name : 'pwd',
				fieldLabel  : 'Contraseña',
				anchor : '80%',
				allowBlank:false,
			},
			{
                xtype:'textfield',
                inputType: 'password',
                fieldLabel:'Confirm Password',
                name:'confirmPass',
                allowBlank:false,
                //vtype : 'password',
		        initialPassField : 'pass',
                anchor:'80%',
                /*validate: function() {
            		var errors = this.callParent(arguments);
            		if( this.get("pwd") != this.get("confirmPass"))
            		{
            			errors.add(Ext.create('Ext.data.Error', {
            				field  : passwordRepeat,
            				message: _("PasswordRepeatError")
            			}));
            		}
            		return errors;
            	}*/
            },
			{
				xtype : 'textfield',
				name : 'telefono',
				fieldLabel  : 'Teléfono',
				anchor : '80%',
				allowBlank:true,
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

