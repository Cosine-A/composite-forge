package dev.aperso.composite.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import dev.aperso.composite.skia.LocalSkiaSurface
import kotlinx.coroutines.isActive
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.resources.ResourceLocation
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil

/*@Composable
fun Texture(
    texture: ResourceLocation,
    modifier: Modifier = Modifier,
    u: Float = 0f,
    v: Float = 0f,
    uWidth: Float = 1f,
    vHeight: Float = 1f
) {
    val abstractTexture = Minecraft.getInstance().textureManager.getTexture(texture)
    Texture(abstractTexture, modifier, u, v, uWidth, vHeight)
}*/

@Composable
fun Texture(
    texture: ResourceLocation,
    modifier: Modifier = Modifier,
    u: Float = 0f,
    v: Float = 0f,
    uWidth: Float = 1f,
    vHeight: Float = 1f
) {
    val surface = LocalSkiaSurface.current
    var coordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    
    val minecraft = Minecraft.getInstance()
    val window = minecraft.window
    val guiScale = window.guiScale.toFloat()
    val density = 1f / guiScale

    LaunchedEffect(coordinates, guiScale, texture) {
        coordinates?.let { coordinates ->
            while (isActive) {
                withFrameNanos {
                    surface.record {
                        if (!coordinates.isAttached) return@record
                        val position = coordinates.positionInWindow()
                        val bounds = coordinates.boundsInWindow()
                        val height = window.height
                        
                        GL30.glEnable(GL30.GL_SCISSOR_TEST)
                        GL30.glScissor(
                            bounds.left.toInt(),
                            height - (bounds.top + bounds.height).toInt(),
                            bounds.width.toInt(),
                            bounds.height.toInt()
                        )
                        
                        pose().pushPose()
                        
                        val guiX = position.x * density
                        val guiY = position.y * density
                        val widthGui = bounds.width * density
                        val heightGui = bounds.height * density
                        
                        pose().translate(guiX, guiY, 1000f)
                        
                        RenderSystem.setShaderTexture(0, texture)
                        RenderSystem.setShader(GameRenderer::getPositionTexShader)
                        val matrix4f = pose().last().pose()
                        val tesselator = Tesselator.getInstance()
                        val bufferBuilder = tesselator.builder
                        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)

                        bufferBuilder.addVertex(matrix4f, 0f, heightGui, 0f).uv(u, v + vHeight)
                        bufferBuilder.addVertex(matrix4f, widthGui, heightGui, 0f).uv(u + uWidth, v + vHeight)
                        bufferBuilder.addVertex(matrix4f, widthGui, 0f, 0f).uv(u + uWidth, v)
                        bufferBuilder.addVertex(matrix4f, 0f, 0f, 0f).uv(u, v)

                        BufferUploader.drawWithShader(bufferBuilder.end())
                        pose().popPose()

                        GL30.glDisable(GL30.GL_SCISSOR_TEST)
                    }
                }
            }
        }
    }
    Spacer(modifier.fillMaxSize().onGloballyPositioned { coordinates = it })
}

private fun VertexConsumer.addVertex(pose: Matrix4f, x: Float, y: Float, z: Float): VertexConsumer {
    val vector3f = pose.transformPosition(x, y, z, Vector3f())
    return this.vertex(vector3f.x().toDouble(), vector3f.y().toDouble(), vector3f.z().toDouble())
}