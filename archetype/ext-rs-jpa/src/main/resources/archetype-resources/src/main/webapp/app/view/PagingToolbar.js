Ext.define('App.view.PagingToolbar', {
		extend : 'Ext.toolbar.Paging',
        displayInfo : true,
        displayMsg  : 'Registros {0} - {1} de {2}',
        beforePageText : 'Página',
        afterPageText : 'de {0}',
        emptyMsg    : 'No hay registros',
        xtype : 'appPagingToolBar'
});