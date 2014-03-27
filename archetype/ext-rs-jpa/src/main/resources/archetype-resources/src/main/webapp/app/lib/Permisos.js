Ext.define('App.lib.Permisos', {
	//name: 'Permisos',
	xtype : 'permisos',
	
	permissions: {
		read : true,
		create : true,
		update : true,
		destroy : true,
	},
	
	config: {
		bookmark: {
			read : true,
			create : true,
			update : true,
			destroy : true,
		},
		user : {
			read : true,
			create : true,
			update : true,
			destroy : true,
		},
		rol : {
			read : true,
			create : true,
			update : true,
			destroy : true,
		}
		
	},
	
	constructor: function(config) {
		//alert(this.books.btn_books_delete + " - " + this.books.btn_books_new + " - " + this.books.item_books);
		this.initConfig(config);
		//alert(this.books.btn_books_delete + " - " + this.books.btn_books_new + " - " + this.books.item_books);
		return this;
	},
	
	test: function() {
		//alert(this.books.btn_books_delete + " - " + this.books.btn_books_new + " - " + this.books.item_books);
		return this;     
	}

});
