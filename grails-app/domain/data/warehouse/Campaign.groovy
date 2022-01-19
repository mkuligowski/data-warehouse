package data.warehouse

class Campaign {

    String name
    Datasource datasource

    static constraints = {
        name nullable: false
        datasource nullable: false
    }
}
