package net.ghoula.modeling

import net.ghoula.preprocessing.{ImageColor, RGB}

trait PaletteModel {
  def name: String
  def colorMapping: Array[(String, ImageColor)]
  def getPalette(
      dominantColors: Array[ImageColor],
      baseColors: Array[ImageColor]
  ): Palette
}

object SolarizedModel extends PaletteModel {
  private val solarizedColorMapping: Map[String, ImageColor] = Map(
    "base3" -> ImageColor(131, 148, 150),
    "base2" -> ImageColor(238, 232, 213),
    "base1" -> ImageColor(147, 161, 161),
    "base0" -> ImageColor(88, 110, 117),
    "base03" -> ImageColor(0, 43, 54),
    "base02" -> ImageColor(7, 54, 66),
    "base01" -> ImageColor(0, 43, 54),
    "base00" -> ImageColor(101, 123, 131),
    "yellow" -> ImageColor(181, 137, 0),
    "orange" -> ImageColor(203, 75, 22),
    "red" -> ImageColor(220, 50, 47),
    "magenta" -> ImageColor(211, 54, 130),
    "violet" -> ImageColor(108, 113, 196),
    "blue" -> ImageColor(38, 139, 210),
    "cyan" -> ImageColor(42, 161, 152),
    "green" -> ImageColor(133, 153, 0)
  )

  override val name: String = "Solarized"
  override val colorMapping: Array[(String, ImageColor)] =
    solarizedColorMapping.toArray

  override def getPalette(
      dominantColors: Array[ImageColor],
      solarizedBaseColors: Array[ImageColor]
  ): Palette = {
    val imageBaseColors = solarizedBaseColors.map { baseColor =>
      RGB.findClosestColor(dominantColors, baseColor)
    }

    val palette = colorMapping.map { case (_, solarizedColor) =>
      val closestBaseIndex = solarizedBaseColors.zipWithIndex.minBy {
        case (baseColor, _) =>
          RGB.contrastRatio(solarizedColor, baseColor)
      }._2

      val solarizedBaseColor = solarizedBaseColors(closestBaseIndex)
      val imageBaseColor = imageBaseColors(closestBaseIndex)

      val rDiff = solarizedColor.r - solarizedBaseColor.r
      val gDiff = solarizedColor.g - solarizedBaseColor.g
      val bDiff = solarizedColor.b - solarizedBaseColor.b

      val r = RGB.clamp(imageBaseColor.r + rDiff)
      val g = RGB.clamp(imageBaseColor.g + gDiff)
      val b = RGB.clamp(imageBaseColor.b + bDiff)

      ImageColor(r, g, b)
    }

    Palette(palette.toArray)
  }
}
