package dev.aperso.composite.listener

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.aperso.composite.CompositeForge
import dev.aperso.composite.component.Components
import dev.aperso.composite.component.Texture
import dev.aperso.composite.core.ComposeScreen
import dev.aperso.composite.skia.SkiaContext
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RenderGuiEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = CompositeForge.MOD_ID, value = [Dist.CLIENT])
object ClientForgeListener {
    private lateinit var minecraft: Minecraft

    @JvmStatic
    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        minecraft = Minecraft.getInstance()
    }

    @JvmStatic
    @SubscribeEvent
    fun onLivingSwapItems(event: LivingSwapItemsEvent.Hands) {
        println("왼손 바꾸기")
        minecraft.submit {
            minecraft.setScreen(
                ComposeScreen(Component.literal("1234")) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "테스트 ㅎㅇ",
                            fontSize = 40.sp
                        )
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data("https://img.icons8.com/color/512/google-logo.png")
                                .crossfade(true)
                                .build(),
                            contentDescription = "111",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.clip(CircleShape).size(100.dp),
                        )
                        /*Texture(
                            texture = ResourceLocation("minecraft", "textures/block/oak_wood.png"),
                            modifier = Modifier.size(128.dp)
                        )*/
                    }
                }
            )
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onRender(event: RenderGuiEvent.Pre) {
        SkiaContext.initialize()
    }
}