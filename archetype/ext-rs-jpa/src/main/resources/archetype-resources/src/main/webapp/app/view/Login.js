Ext.define('App.view.Login', {
	extend : 'Ext.window.Window',
	// alias:'widget.view.Login',
	xtype : 'loginwindow',
	id : 'loginwindow',
	cls : 'form-login-dialog',
	iconCls : 'form-login-icon-title',
	width : 350,
	height : 183,
	resizable : false,
	closable : false,
	draggable : false,
	modal : true,
	closeAction : 'hide',
	layout : 'border',
	title : 'Login',

    initComponent : function () {
    	Ext.apply(
				this,
				{
					items : [
					/*
					 * { itemId:'headerPanel',
					 * xtype:'panel',
					 * cls:'form-login-header',
					 * baseCls:'x-plain', html:'intro',
					 * region:'north', height:60 },
					 */
					{
						xtype : 'form',
						id : 'loginform',
						bodyPadding : 10,
						header : false,
						region : 'center',
						border : false,
						waitMsgTarget : true,
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						defaults : {
							labelWidth : 85
						},
						items : [
								{
									itemId : 'userName',
									xtype : 'textfield',
									fieldLabel : 'Usuario',
									name : 'userName',
									allowBlank : false,
									anchor : '100%',
									validateOnBlur : false
								},
                        {
							xtype : 'textfield',
							fieldLabel : 'Contraseña',
							name : 'password',
							allowBlank : false,
							inputType : 'password',
							anchor : '100%',
							validateOnBlur : false,
							enableKeyEvents : true,
							listeners : {
								render : {
									fn : function(field, eOpts) {
										field.capsWarningTooltip = Ext
												.create(
														'Ext.tip.ToolTip',
														{
															target : field.bodyEl,
															anchor : 'top',
															width : 150,
															html : 'Caps lock ACTIVADO'
														});

										// disable
										// to
										// tooltip
										// from
										// showing
										// on
										// mouseover
										field.capsWarningTooltip.disable();
									},
									scope : this
								},

                                keypress:{
                                    fn:function (field, e, eOpts) {
                                        var charCode = e.getCharCode();
                                        if ((e.shiftKey && charCode >= 97 && charCode <= 122) ||
                                            (!e.shiftKey && charCode >= 65 && charCode <= 90)) {

                                            field.capsWarningTooltip.enable();
                                            field.capsWarningTooltip.show();
                                        }
                                        else {
                                            if (field.capsWarningTooltip.hidden === false) {
                                                field.capsWarningTooltip.disable();
                                                field.capsWarningTooltip.hide();
                                            }
                                        }
                                    },
                                    scope : this
                                },

                                blur : function (field) {
                                    if (field.capsWarningTooltip.hidden === false) {
                                        field.capsWarningTooltip.hide();
                                    }
                                }
                            }
                        },
                        {
                        	xtype: 'panel',
                        	layout : {
    							type : 'vbox',
    							align : 'center'
    						},
    						border: false,
                        	items : [
                        	         {
                        	        	 border: false,
                        	        	 xtype: 'panel',
                        	        	 html : '<font style="font-size: 11px;">Por defecto: admin/admin, user/user</font>'
                        	         }
                        	         ]
                        }
                    ]
                }
            ],
			buttons : [ {
				id : 'loginButton',
				type : "submit",
				action : "login",
				formBind : true,
				text : 'Entrar',
				//ref : './rest/login',
				// iconCls:'form-login-icon-login',
				scale : 'medium',
				width : 70
			} ]
        });
        this.callParent(arguments);
    },
    defaultFocus:'userName'
});
