Ext.define("App.controller.Bookmark", {
    extend : 'App.controller.Base',

    stores : ['Bookmark'],
    models : ['Bookmark'],
    
    views:['bookmark.Index', 'bookmark.Create', 'bookmark.Update', 'bookmark.List'],

    //para que funcione el save de Base
	url : 'bookmark',
	
	modelName : 'App.model.Bookmark',
    
	id : 'bookmarkController',
	
	init : function () {
        this.control({
        	'bookmarkIndex' : { 
                afterrender : this.checkRights
            },
            
            'bookmarkIndex button[action=create]' : { 
                click : this.createItem
            },
            
            'bookmarkIndex button[action=update]': {
                click : this.updateItem
            },
            
            'bookmarkUpdate': {
                show : this.loadItemForm
            },
            
            'bookmarkIndex button[action=destroy]': {
                click : this.destroyItem
            },
            
            'bookmarkCreate button[operation=save]' : {
            	click : this.saveNew
            },
            
            'bookmarkCreate button[operation=cancel]' : {
            	click : this.cancel
            },

            'bookmarkUpdate button[operation=save]' : {
            	click : this.saveEdited
            },
            
            'bookmarkUpdate button[operation=cancel]' : {
            	click : this.cancel
            },
            
        });
    }

});
