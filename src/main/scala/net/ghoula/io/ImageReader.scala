package net.ghoula.io

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageReader(imagePath: String) {
  private val image: BufferedImage = ImageIO.read(new File(imagePath))

  def getImage: BufferedImage = image

  def getDownscaledImage(scale: Double): BufferedImage = {
    val width = (image.getWidth * scale).toInt
    val height = (image.getHeight * scale).toInt
    val downscaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT)
    val bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    bufferedImage.getGraphics.drawImage(downscaledImage, 0, 0, null)
    bufferedImage
  }
}
