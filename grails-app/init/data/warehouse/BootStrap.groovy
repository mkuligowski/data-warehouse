package data.warehouse

import com.mkuligowski.dw.DataLoader
import grails.util.Environment
import org.grails.io.support.ClassPathResource

class BootStrap {

    DataLoader dataLoader

    def init = { servletContext ->
        // TODO: transactional - all or nothing
        if (Environment.current in [Environment.DEVELOPMENT, Environment.PRODUCTION])
            dataLoader.loadFile(new ClassPathResource('./data.csv').getFile().newInputStream())
    }
    def destroy = {
    }
}
