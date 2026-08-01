package com.claudeclient.util;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

/**
 * Rysuje obrys prostopadłościanu (hitboxa) jako 12 krawędzi w świecie 3D.
 * Wykorzystywane przez WorldRendererMixin do podświetlania celu w zasięgu ataku.
 */
public final class BoxOutlineRenderer {

	private BoxOutlineRenderer() {
	}

	/**
	 * Rysuje obrys boxa w podanym kolorze (ARGB) na podanej pozycji względem kamery.
	 *
	 * @param matrices    macierz transformacji już przesunięta względem kamery
	 * @param vertexConsumers dostawca buforów wierzchołków (linie)
	 * @param box         hitbox w lokalnych współrzędnych (już z odjętą pozycją kamery)
	 * @param color       kolor ARGB (np. Theme.HITBOX_HIGHLIGHT)
	 */
	public static void drawOutline(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Box box, int color) {
		VertexConsumer buffer = vertexConsumers.getBuffer(net.minecraft.client.render.RenderLayer.getLines());
		Matrix4f matrix = matrices.peek().getPositionMatrix();

		float a = ((color >> 24) & 0xFF) / 255f;
		float r = ((color >> 16) & 0xFF) / 255f;
		float g = ((color >> 8) & 0xFF) / 255f;
		float b = (color & 0xFF) / 255f;

		float minX = (float) box.minX;
		float minY = (float) box.minY;
		float minZ = (float) box.minZ;
		float maxX = (float) box.maxX;
		float maxY = (float) box.maxY;
		float maxZ = (float) box.maxZ;

		// Dolna ściana (4 krawędzie)
		line(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
		line(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
		line(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
		line(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

		// Górna ściana (4 krawędzie)
		line(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
		line(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
		line(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
		line(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

		// Pionowe krawędzie łączące (4 krawędzie)
		line(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
		line(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
		line(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
		line(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);
	}

	private static void line(VertexConsumer buffer, Matrix4f matrix,
							  float x1, float y1, float z1,
							  float x2, float y2, float z2,
							  float r, float g, float b, float a) {
		float nx = x2 - x1;
		float ny = y2 - y1;
		float nz = z2 - z1;
		float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
		if (len > 0.0001f) {
			nx /= len;
			ny /= len;
			nz /= len;
		}

		buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(nx, ny, nz);
		buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(nx, ny, nz);
	}
}
