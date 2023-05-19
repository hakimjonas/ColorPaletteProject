package net.ghoula.preprocessing

case class ImageColor(r: Int, g: Int, b: Int) {
  def toHex: String = f"#$r%02x$g%02x$b%02x"

}

abstract class ColorSpace {
  def toThisSpace(color: ImageColor): ImageColor

  def contrastRatio(color1: ImageColor, color2: ImageColor): Double

  def clamp(value: Double): Int

  def findClosestColor(
      colors: Array[ImageColor],
      target: ImageColor
  ): ImageColor

}

object RGB extends ColorSpace {
  private def toLinear(c: Int): Int = {
    val sRGB = c / 255.0
    (if (sRGB <= 0.04045) sRGB / 12.92
     else Math.pow((sRGB + 0.055) / 1.055, 2.4) * 255.0).toInt
  }

  private def relativeLuminance(color: ImageColor): Double = {
    val r = color.r / 255.0
    val g = color.g / 255.0
    val b = color.b / 255.0
    0.2126 * r + 0.7152 * g + 0.0722 * b
  }

  override def toThisSpace(color: ImageColor): ImageColor = {
    ImageColor(toLinear(color.r), toLinear(color.g), toLinear(color.b))
  }

  override def contrastRatio(color1: ImageColor, color2: ImageColor): Double = {
    val l1 = relativeLuminance(toThisSpace(color1))
    val l2 = relativeLuminance(toThisSpace(color2))

    if (l1 > l2) (l1 + 0.05) / (l2 + 0.05)
    else (l2 + 0.05) / (l1 + 0.05)
  }

  override def clamp(value: Double): Int = value.max(0).min(255).toInt

  override def findClosestColor(
      colors: Array[ImageColor],
      target: ImageColor
  ): ImageColor = {
    colors.minBy(color => contrastRatio(color, target))
  }
}
