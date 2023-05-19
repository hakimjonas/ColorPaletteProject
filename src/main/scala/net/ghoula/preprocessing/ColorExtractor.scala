package net.ghoula.preprocessing

import org.apache.commons.math3.ml.clustering.{
  Clusterable,
  KMeansPlusPlusClusterer
}

import java.awt.Color
import java.awt.image.BufferedImage
import scala.jdk.CollectionConverters._

class ColorExtractor(private val image: BufferedImage) {
  case class ColorPoint(r: Double, g: Double, b: Double) extends Clusterable {
    override def getPoint: Array[Double] = Array(r, g, b)

    def toImageColor: ImageColor = ImageColor(r.toInt, g.toInt, b.toInt)
  }

  def kMeansExtractor(numColors: Int): Array[ImageColor] = {
    val width = image.getWidth
    val height = image.getHeight

    val pixels = for {
      x <- 0 until width
      y <- 0 until height
      color = new Color(image.getRGB(x, y))
      r = color.getRed.toDouble
      g = color.getGreen.toDouble
      b = color.getBlue.toDouble
    } yield ColorPoint(r, g, b)

    val clusterFunction =
      new KMeansPlusPlusClusterer[ColorPoint](numColors, 100)
    val clusters = clusterFunction.cluster(pixels.toList.asJava)

    clusters.asScala
      .map(c => {
        val points = c.getCenter.getPoint
        ColorPoint(points(0), points(1), points(2)).toImageColor
      })
      .toArray
  }
}
