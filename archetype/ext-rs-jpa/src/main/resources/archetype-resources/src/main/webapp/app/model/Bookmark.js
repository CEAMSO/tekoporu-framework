Ext.define('App.model.Bookmark', {
    extend: 'Ext.data.Model',
    fields: ['link', 'id', 'description'],
    
    proxy : {
		type : 'rest',	
		url : '/${artifactId}/rest/bookmark',
    }
});
