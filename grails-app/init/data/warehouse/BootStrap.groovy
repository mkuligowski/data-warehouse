package data.warehouse

import com.mkuligowski.dw.DataLoader
import org.grails.io.support.ClassPathResource

class BootStrap {

    DataLoader dataLoader

    def init = { servletContext ->
        dataLoader.loadFile(new ClassPathResource('./data.csv').getFile().newInputStream())
    }
    def destroy = {
    }
}
