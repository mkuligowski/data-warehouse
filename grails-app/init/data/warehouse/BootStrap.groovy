package data.warehouse

import grails.util.Environment
import org.grails.io.support.ClassPathResource

class BootStrap {

    StatsLoadingService statsLoadingService

    def init = { servletContext ->

        def env = Environment.current
        def x = Environment.currentEnvironment

        if (Environment.current in [Environment.DEVELOPMENT, Environment.PRODUCTION]){
            CampaignStatistic.deleteAll()
            Campaign.deleteAll()
            Datasource.deleteAll()
            statsLoadingService.loadFromStream(new ClassPathResource('./data.csv').getFile().newInputStream())
        }
    }
    def destroy = {
    }
}
