package dev.aperso.composite

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod

@Mod(CompositeForge.MOD_ID)
class CompositeForge {
    companion object {
        const val MOD_ID = "compositeforge"
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }
}
