package com.tencent.angel.acumos

import scala.util.Random

import scala.collection.JavaConverters._

object AcumosMainTest {
  def main(args: Array[String]): Unit = {
    val dim = 148
    val rand = new Random()

    val dfBuilder = DatasetProto.DataFrame.newBuilder()
    (0 until 10).foreach { i =>
      val dataMap = (0 until rand.nextInt(dim)).map { _ =>
        rand.nextInt(dim) -> (rand.nextDouble() - 0.5) * 2
      }.toMap
      dfBuilder.addRows(Utils.dataFrameRow(i.toString, dim, dataMap))
    }

    val dataFrame = dfBuilder.build()
    val acumosMain = new AcumosMain()
    acumosMain.classify(dataFrame).getId2PredictionMap.asScala.foreach{
      case (id: String, pred: java.lang.Float) => println(s"$id\t$pred")
    }
  }
}
