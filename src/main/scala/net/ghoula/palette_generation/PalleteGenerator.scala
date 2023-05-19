package net.ghoula.palette_generation

import net.ghoula.modeling.{Palette, PaletteModel, SolarizedModel}
import net.ghoula.preprocessing.ColorExtractor

import java.awt.image.BufferedImage

class PaletteGenerator(image: BufferedImage, models: Seq[PaletteModel]) {

  private val colorExtractor = new ColorExtractor(image)

  def generatePalettes(): Seq[Palette] = {
    val dominantColors = colorExtractor.kMeansExtractor(32)
    models.map { model =>
      model.getPalette(dominantColors, model.colorMapping.map(_._2).toArray)
    }
  }
}

class RGBSolarizedPaletteGenerator(private val image: BufferedImage) {
  private val colorExtractor = new ColorExtractor(image)

  def generate(): Palette = {
    val dominantColors = colorExtractor.kMeansExtractor(
      16
    ) // Number of dominant colors can be adjusted.
    SolarizedModel.getPalette(
      dominantColors,
      SolarizedModel.colorMapping.map(_._2).toArray
    )
  }
}
