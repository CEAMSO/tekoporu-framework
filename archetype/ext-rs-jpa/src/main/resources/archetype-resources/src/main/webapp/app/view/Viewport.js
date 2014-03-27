Ext.define('App.view.Viewport', {
    extend:'Ext.container.Viewport',
    requires:[
        'App.view.Header',
        'App.view.Footer',
        'App.view.Navigation'
    ],
    layout:'card',
    id:'viewport',
    activeItem:0,
    items:[
        {
            id:'card-0'
        },
        {
            id:'card-1',
            
            xtype:'panel',
            layout:'border',

            items:[
                {
                    xtype:'view.Header',
                	region:'north',
                    height:50
                },
                {
                    xtype:'view.Navigation',
                    region:'west',
                    width:200
                },
                {
                    //xtype: 'tabpanel',	
                    xtype:'container',
                    layout:'card',
                    region:'center',
                    stateful:true,
                    stateId:'center-card'
                },
                {
                    //xtype: 'tabpanel',	
                    xtype:'view.Footer',
                    region:'south'
                }
            ]
            
        }
        
    ]
});
