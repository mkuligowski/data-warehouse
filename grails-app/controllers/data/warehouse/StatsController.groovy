package data.warehouse

import data.warehouse.query.StatsQueryParams

class StatsController {

    static responseFormats = ['json']

    StatsQueryService statsQueryService

    def query(StatsQueryParams command) {

        if (!command.validate()) {
            response.status = 400
            respond([errors: command.errors.allErrors])
        }

        respond statsQueryService.query(command)
    }
}
