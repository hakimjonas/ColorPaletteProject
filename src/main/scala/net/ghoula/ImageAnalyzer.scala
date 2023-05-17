package net.ghoula

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer
import org.apache.commons.math3.ml.clustering.{KMeansPlusPlusClusterer, Clusterable}
import scala.jdk.CollectionConverters._

class ImageAnalyzer(imagePath: String) {
  private val image: BufferedImage = ImageIO.read(new File(imagePath))

  val solarizedColors = Array(
    ColorPoint(131, 148, 150), // base3
    ColorPoint(238, 232, 213), // base2
    ColorPoint(147, 161, 161), // base1
    ColorPoint(88, 110, 117), // base0
    ColorPoint(0, 43, 54), // base03
    ColorPoint(7, 54, 66), // base02
    ColorPoint(0, 43, 54), // base01
    ColorPoint(101, 123, 131), // base00
    ColorPoint(181, 137, 0), // yellow
    ColorPoint(203, 75, 22), // orange
    ColorPoint(220, 50, 47), // red
    ColorPoint(211, 54, 130), // magenta
    ColorPoint(108, 113, 196), // violet
    ColorPoint(38, 139, 210), // blue
    ColorPoint(42, 161, 152), // cyan
    ColorPoint(133, 153, 0) // green
  )

  case class ColorPoint(r: Double, g: Double, b: Double) extends Clusterable {
    override def getPoint: Array[Double] = Array(r, g, b)

    def toHex: String = f"#${r.toInt}%02x${g.toInt}%02x${b.toInt}%02x"

    def toImageColor: ImageColor = ImageColor(r.toInt, g.toInt, b.toInt)
  }

  def getDominantColors(numColors: Int): Array[ColorPoint] = {
    val width = image.getWidth
    val height = image.getHeight

    val pixels = new ArrayBuffer[ColorPoint]()

    for (x <- 0 until width; y <- 0 until height) {
      val color = new Color(image.getRGB(x, y))

      val r = color.getRed.toDouble
      val g = color.getGreen.toDouble
      val b = color.getBlue.toDouble

      pixels += ColorPoint(r, g, b)
    }

    val clusterer = new KMeansPlusPlusClusterer[ColorPoint](numColors, 100)
    val clusters = clusterer.cluster(pixels.toList.asJava)

    clusters.asScala.map(c => {
      val points = c.getCenter.getPoint
      ColorPoint(points(0), points(1), points(2))
    }).toArray

  }

  def findClosestColor(colors: Array[ColorPoint], target: ColorPoint): ColorPoint = {
    colors.minBy(color => ColorPalette.contrastRatio(color.toImageColor, target.toImageColor))
  }


  def findClosestColors(colors: Array[ColorPoint]): Array[(ColorPoint, ColorPoint)] = {


    solarizedColors.map { baseColor =>
      val closestColors = colors.map(color => {
        val contrast = ColorPalette.contrastRatio(baseColor.toImageColor, color.toImageColor)
        (color, contrast)
      })

      val closestColor = closestColors.minBy(_._2)._1
      (baseColor, closestColor)
    }
  }

  def generatePalette(dominantColors: Array[ColorPoint], solarizedBaseColors: Array[ColorPoint] = Array(solarizedColors(5), solarizedColors(1))): Array[ColorPoint] = {
    val imageBaseColors = solarizedBaseColors.map { baseColor =>
      findClosestColor(dominantColors, baseColor)
    }

    // For each solarized color, calculate its relative difference to the closest solarized base color
    // and apply that same relative difference to the corresponding image base color
    val palette = solarizedColors.map { solarizedColor =>
      val closestBaseIndex = solarizedBaseColors.zipWithIndex.minBy { case (baseColor, _) =>
        ColorPalette.contrastRatio(solarizedColor.toImageColor, baseColor.toImageColor)
      }._2

      val solarizedBaseColor = solarizedBaseColors(closestBaseIndex)
      val imageBaseColor = imageBaseColors(closestBaseIndex)

      val rDiff = solarizedColor.r - solarizedBaseColor.r
      val gDiff = solarizedColor.g - solarizedBaseColor.g
      val bDiff = solarizedColor.b - solarizedBaseColor.b

      val r = clamp(imageBaseColor.r + rDiff, 0, 255)
      val g = clamp(imageBaseColor.g + gDiff, 0, 255)
      val b = clamp(imageBaseColor.b + bDiff, 0, 255)

      ColorPoint(r, g, b)
    }

    palette
  }


  private def clamp(value: Double, min: Double, max: Double): Double = {
    if (value < min) min
    else if (value > max) max
    else value
  }

  def generateSvg(palette: Array[ColorPoint]): String = {
    val cellSize = 100
    val gridSize = 4
    val svgSize = cellSize * gridSize

    val svgHeader =
      s"""<svg xmlns="http://www.w3.org/2000/svg" width="$svgSize" height="$svgSize">"""
    val svgFooter = "</svg>"

    val colorSquares = palette.zipWithIndex.map {
      case (color, i) =>
        val x = (i % gridSize) * cellSize
        val y = (i / gridSize) * cellSize
        s"""<rect x="$x" y="$y" width="$cellSize" height="$cellSize" fill="${color.toHex}" />"""
    }

    svgHeader + colorSquares.mkString + svgFooter
  }

  //  val svg = generateSvg(newPalette)


}

object ImageAnalyzer {
  def main(args: Array[String]): Unit = {
    val imagePath = "src/main/resources/input.jpg"
    val imageAnalyzer = new ImageAnalyzer(imagePath)

    val dominantColors = imageAnalyzer.getDominantColors(2)
    val newPalette = imageAnalyzer.generatePalette(dominantColors)

    newPalette.foreach(color => println(color.toHex))

    // Generate SVG
    val svg = imageAnalyzer.generateSvg(newPalette)
    println(svg) // print SVG string to the console

    // Write the SVG string to a file
    import java.nio.file.{Paths, Files}
    import java.nio.charset.StandardCharsets
    Files.write(Paths.get("src/main/resources/output.svg"), svg.getBytes(StandardCharsets.UTF_8))
  }
}

