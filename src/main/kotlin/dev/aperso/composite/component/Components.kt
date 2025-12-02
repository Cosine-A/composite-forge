package dev.aperso.composite.component

import androidx.compose.runtime.Composable
import net.minecraft.world.item.ItemStack

object Components {
    @Composable
    fun Item(item: ItemStack) = dev.aperso.composite.component.Item(item)
}