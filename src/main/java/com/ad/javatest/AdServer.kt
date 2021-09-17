
import com.ad.javatest.controller.AdController
import com.ad.javatest.controller.DealController
import com.ad.javatest.controller.SupplyController
import com.ad.javatest.controller.TagController
import com.ad.javatest.model.enums.CollectionType
import com.ad.javatest.persistence.DataServer
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*


fun main() {
    val app = Javalin.create().start(9000)
    val adController = AdController(DataServer.getClient(CollectionType.SUPPLY.dbName), "supplyId")
    val supplyController = SupplyController(DataServer.getClient(CollectionType.SUPPLY.dbName))
    val dealController = DealController(DataServer.getClient(CollectionType.DEAL.dbName))
    val tagController = TagController(DataServer.getClient(CollectionType.TAG.dbName))

    app.get("/") { ctx -> ctx.result("Hello World") }
    app.routes {
        path("0") {
            path("supply") {
                post { supplyController.save(it) }
                put { supplyController.update(it) }
                path(":id") {
                    get { supplyController.findById(it) }
                }
            }
            path("deals") {
                post { dealController.save(it) }
                path(":id") {
                    get { dealController.findById(it) }
                }
            }
            path("tags") {
                post { tagController.save(it) }
                put { tagController.update(it) }
                path(":id") {
                    get { tagController.findById(it) }
                }
            }
            path("ad") {
                get { adController.findById(it) }
            }
        }
    }

}