package data.warehouse

import com.mkuligowski.warehouse.CampaignCache
import grails.gorm.transactions.Transactional

import java.time.LocalDate

@Transactional
class StatsLoadingService {

    def loadFromStream(InputStream is) {

        CampaignCache campaignCache = new CampaignCache(Campaign.findAll().groupBy {it.name})

        is.splitEachLine(','){ csvLine ->
            if (isHeaderLine(csvLine))
                return

            String datasource = csvLine[0]
            String campaign = csvLine[1]
            String date = csvLine[2]
            String clicks = csvLine[3]
            String impressions = csvLine[4]

            if (campaignCache.get(datasource, campaign)){
                Campaign c = campaignCache.get(datasource, campaign)
                new CampaignStatistic(campaign: c,
                        statsDate: LocalDate.parse(date, 'M/d/yy') ,
                        clicks: clicks as Integer,
                        impressions: impressions as Integer).save()
            } else {
                Datasource ds = Datasource.findByName(datasource)
                ds = ds ?: new Datasource(name: datasource).save()
                Campaign c = new Campaign(datasource: ds, name: campaign).save()
                campaignCache.put(c)

                new CampaignStatistic(campaign: c,
                        statsDate: LocalDate.parse(date, 'M/d/yy') ,
                        clicks: clicks as Integer,
                        impressions: impressions as Integer).save()
            }


        }
    }

    private static boolean isHeaderLine(List<String> strings) {
        strings[0] == 'Datasource'
                && strings[1] == 'Campaign'
                && strings[2] == 'Daily'
                && strings[3] == 'Clicks'
                && strings[4] == 'Impressions'

    }
}
