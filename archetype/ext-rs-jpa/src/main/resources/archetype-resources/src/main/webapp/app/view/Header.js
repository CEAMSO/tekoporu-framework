Ext.define('App.view.Header', {
    extend:'Ext.panel.Panel',
    alias:'widget.view.Header',
    layout:'column',
    id:'header',
    items:[
        {
            xtype:'container',
            columnWidth:.80,
            html:'<font face="arial" size=6 color=#157fcc>Bookmarks App</font>',
            border:'none',
            margin:'5 5 5 5',
        },
        {
            xtype:'container',
            columnWidth:.20,
            items:[
                {
                    xtype:'container',
                    html:'Usuario: ',
                    border:'none',
                    id:'loggedin'
                },
                {
                    id:'logoutButton',
                    xtype:'button',
                    text:'Salir',
                    margin:'3 0 0 0',
                    action:'logout'
                }
            ],
            border:'none'
        }
    ]
});
