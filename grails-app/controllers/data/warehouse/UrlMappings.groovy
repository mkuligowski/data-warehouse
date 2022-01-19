package data.warehouse

class UrlMappings {

    static mappings = {
        post "/campaign-statistics"(controller: 'stats', action: 'query')

        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
