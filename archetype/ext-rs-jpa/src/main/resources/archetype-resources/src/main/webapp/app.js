Ext.Loader.setPath('Ext.ux', 'ext-4/ux');

Ext.require('Ext.ux.app.RoutedApplication', function () {
App = Ext.create('Ext.ux.app.RoutedApplication', {
    name : 'App',
    appFolder : 'app',
    
    controllers:[
        'Base',
        'Login',
        'Dashboard',
        //'Bookmark',
        //'User',
        //'Rol'
    ],
    
    requires:[
        'App.view.Viewport',
        'App.view.FormWindow',
        'App.view.Login'
    ],

    permissions : null,
    
    launch : function () {
        var me = this;
        
        // create Viewport instance
        var viewport = Ext.create('App.view.Viewport', {
            controller : me
        });

        // Get a reference to main TabPanel.  This is where top-level controllers will render themselves.
        // eg: this.render("workspace", this.getBooksIndexView());
        // Think of it as a "render target".
        var workspace = viewport.down('container[region=center]');
        
        this.addLayout('workspace', workspace);
        
        //TODO Cargar el hash inicial después de loguearse
        window.top.location.hash = 'dashboard'; //setea dashboard inicial
        
        Ext.defer(this.hideLoadingScreen, 250);
        
        Ext.History.init(me.initDispatch, me);
        Ext.History.on('change', me.historyChange, me);
        
        
        // Start with dashboard
        token = Ext.History.getToken();
        if (token == null) {
            Ext.History.add('dashboard', true);
        }
    },
    
    initDispatch : function () {
        var me = this,
        token = Ext.History.getToken();
        console.log('Init dispatch with token: ' + token);
        me.historyChange(token);

    },

    historyChange : function (token) {
        var me = this;
        // and check if token is set
        Ext.log('History changed to: ' + token);
        if (token) {
            Ext.dispatch(token);
//                var route = Ext.Router.recognize(token);
//                //me.dispatch(route);
//                console.log(route);
        }
    },
    
    hideLoadingScreen : function() {
        Ext.get('loading').remove();
        Ext.fly('loading-mask').animate({
            opacity:0,
            remove:true
        });
    }
});
});
