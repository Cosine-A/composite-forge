package dev.aperso.composite.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.scene.CanvasLayersComposeScene
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import dev.aperso.composite.CompositeForge
import dev.aperso.composite.skia.LocalSkiaSurface
import dev.aperso.composite.skia.SkiaSurface
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.jetbrains.skiko.currentNanoTime

@OptIn(InternalComposeUiApi::class, ExperimentalComposeUiApi::class)
@Mod.EventBusSubscriber(modid = CompositeForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
class ComposeHud(
    content: @Composable () -> Unit
) {
    private val scene = CanvasLayersComposeScene()
    private val surface = SkiaSurface()

    init {
        scene.setContent {
            CompositionLocalProvider(LocalSkiaSurface provides surface) {
                content()
            }
        }
    }

    @SubscribeEvent
    fun onHudRender(event: RenderGuiOverlayEvent.Post) {
        val minecraft = Minecraft.getInstance()
        val window = minecraft.window
        val guiGraphics = event.guiGraphics

        scene.size = IntSize(window.guiScaledWidth, window.guiScaledHeight)
        scene.density = Density(window.guiScale.toFloat())

        surface.resize(window.width, window.height)
        surface.render(guiGraphics) {
            scene.render(it, currentNanoTime())
        }
    }
}