package dev.aperso.composite.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

object Components {
    @Composable
    fun Item(item: ItemStack, modifier: Modifier = Modifier) = dev.aperso.composite.component.Item(item, modifier)

    @Composable
    fun Texture(
        texture: ResourceLocation,
        modifier: Modifier = Modifier,
        u: Float = 0f,
        v: Float = 0f,
        uWidth: Float = 1f,
        vHeight: Float = 1f
    ) = dev.aperso.composite.component.Texture(texture, modifier, u, v, uWidth, vHeight)

    @Composable
    fun Texture(
        texture: AbstractTexture,
        modifier: Modifier = Modifier,
        u: Float = 0f,
        v: Float = 0f,
        uWidth: Float = 1f,
        vHeight: Float = 1f
    ) = dev.aperso.composite.component.Texture(texture, modifier, u, v, uWidth, vHeight)
}