package data.warehouse

import com.mkuligowski.datawarehouse.query.StatsQueryService

class StatsController {

    static responseFormats = ['json']

    StatsQueryService statsQueryService

    def query(QueryCommand command) {

        // TODO: indices!
        if (!command.validate()) {
            response.status = 400
            respond([errors: command.errors.allErrors])
        }

        respond statsQueryService.query(command)
    }
}
