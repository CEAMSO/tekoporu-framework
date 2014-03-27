Ext.define('App.view.GridToGrid', {
    extend: 'Ext.container.Container',
    xtype: 'GridToGrid',
    
    requires: [
        'Ext.grid.*',
        'Ext.layout.container.HBox'
    ],

    width: 650,
    height: 300,
    anchor : '80%',
    layout: {
        type: 'hbox',
        align: 'stretch',
        padding: 5
    },
    
    titleGrid1 : 'Disponibles',
    titleGrid2 : 'Asignados',
    
    storeGrid1 : null,
    storeGrid2 : null,
    
    dataIndex : 'descripcion',
    
        
    initComponent: function(){

        var group1 = this.id + 'group1',
            group2 = this.id + 'group2',
            columns = [{
                text : 'Descripción', 
                flex : 1, 
                sortable : true, 
                dataIndex : this.dataIndex
            }];
            
        this.items = [{
            //itemId: 'grid1',
            nombre : 'grid1',
        	flex: 1,
            xtype: 'grid',
            multiSelect : true,
            viewConfig : {
                plugins : {
                    ptype : 'gridviewdragdrop',
                    dragGroup : group1,
                    dropGroup : group2,
                    dragText : '{0} fila{1} seleccionada{1}'
                },
                listeners: {
                    drop: function(node, data, dropRec, dropPosition) {
                        var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get(this.dataIndex) : ' on empty view';
                    }
                }
            },
            store : this.storeGrid1,
            columns : columns,
            stripeRows : true,
            title : this.titleGrid1,
            headerPosition : 'left',
            margins: '0 5 0 0'
        }, {
            //itemId: 'grid2',
        	nombre : 'grid2',
        	flex: 1,
            xtype: 'grid',
            viewConfig: {
                plugins: {
                    ptype: 'gridviewdragdrop',
                    dragGroup: group2,
                    dropGroup: group1,
                    dragText : '{0} fila{1} seleccionada{1}'
                },
                listeners: {
                    drop: function(node, data, dropRec, dropPosition) {
                        var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get(this.dataIndex) : ' on empty view';
                    }
                }
            },
            store : this.storeGrid2,
            columns : columns,
            stripeRows : true,
            title : this.titleGrid2,
            headerPosition : 'left',
        }];

        this.callParent();
    }
});