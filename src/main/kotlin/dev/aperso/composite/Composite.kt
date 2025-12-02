package dev.aperso.composite

import dev.aperso.composite.skia.SkiaContext
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Composite : ClientModInitializer {
    val logger: Logger = LoggerFactory.getLogger("Composite")

    override fun onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register {
            logger.info("initializing skia context")
            SkiaContext.initialize()
        }
    }
}
