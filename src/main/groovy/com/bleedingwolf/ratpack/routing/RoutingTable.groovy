package com.bleedingwolf.ratpack.routing

import com.bleedingwolf.ratpack.RatpackRequestDelegate


class RoutingTable {

    def delegateClassName

    def routeHandlers = []
    
    public RoutingTable(delegateClassName) {
        this.delegateClassName = delegateClassName
    }
    
    def attachRoute(route, handler) {
        routeHandlers << [route: route, handler: handler]
    }
    
    def route(subject) {
        def found = routeHandlers.find { null != it.route.match(subject) }
        if (found) {
            def urlparams = found.route.match(subject)
            def foundHandler = { ->            
                found.handler.delegate = delegate
                found.handler()
            }
            foundHandler.delegate = Class.forName(delegateClassName).newInstance();
            foundHandler.delegate.urlparams = urlparams
            return foundHandler
        }
        return null
    }
}
