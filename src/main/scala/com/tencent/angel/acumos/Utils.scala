package com.tencent.angel.acumos

import com.tencent.angel.acumos.DatasetProto._
import com.tencent.angel.ml.math2.VFactory
import com.tencent.angel.ml.math2.utils.LabeledData
import com.tencent.angel.ml.math2.vector.Vector
import org.json4s.DefaultFormats
import org.json4s.JsonAST.{JArray, JNothing, JObject}

import scala.reflect.ClassTag


object Utils {
  private implicit val formats: DefaultFormats = DefaultFormats

  def row2LabeledData(row:DataFrameRow): LabeledData = {
    val (id: String, vec: Vector) = if (row.hasDarr) { // float array
      val rowInner = row.getDarr
      val features = new Array[Double](rowInner.getFeaturesCount)
      features.indices.foreach{ i =>
        features(i) = rowInner.getFeatures(i)
      }

      (rowInner.getId, VFactory.denseDoubleVector(features))
    } else if (row.hasFarr) {// float array
      val rowInner = row.getFarr
      val features = new Array[Float](rowInner.getFeaturesCount)
      features.indices.foreach{ i =>
        features(i) = rowInner.getFeatures(i)
      }
      (rowInner.getId, VFactory.denseFloatVector(features))
    } else if (row.hasIdkv) { // int key, double value
      val rowInner = row.getIdkv
      val mapFeats = rowInner.getFeaturesMap
      val keys = new Array[Int](mapFeats.size())
      val values = new Array[Double](mapFeats.size())

      val iter = mapFeats.entrySet().iterator()
      var i: Int = 0
      while (iter.hasNext) {
        val entry = iter.next()
        keys(i) = entry.getKey
        values(i) = entry.getValue

        i += 1
      }
      (rowInner.getId, VFactory.sparseDoubleVector(rowInner.getDim, keys, values))
    } else if (row.hasIfkv) { // int key, float value
      val rowInner = row.getIfkv
      val mapFeats = rowInner.getFeaturesMap
      val keys = new Array[Int](mapFeats.size())
      val values = new Array[Float](mapFeats.size())

      val iter = mapFeats.entrySet().iterator()
      var i: Int = 0
      while (iter.hasNext) {
        val entry = iter.next()
        keys(i) = entry.getKey
        values(i) = entry.getValue

        i += 1
      }
      (rowInner.getId, VFactory.sparseFloatVector(rowInner.getDim, keys, values))
    } else if (row.hasLdkv) { // long key, double value
      val rowInner = row.getLfkv
      val mapFeats = rowInner.getFeaturesMap
      val keys = new Array[Long](mapFeats.size())
      val values = new Array[Float](mapFeats.size())

      val iter = mapFeats.entrySet().iterator()
      var i: Int = 0
      while (iter.hasNext) {
        val entry = iter.next()
        keys(i) = entry.getKey
        values(i) = entry.getValue

        i += 1
      }
      (rowInner.getId, VFactory.sparseLongKeyFloatVector(rowInner.getDim, keys, values))
    } else { // long key, float value
      val rowInner = row.getLdkv
      val mapFeats = rowInner.getFeaturesMap
      val keys = new Array[Long](mapFeats.size())
      val values = new Array[Double](mapFeats.size())

      val iter = mapFeats.entrySet().iterator()
      var i: Int = 0
      while (iter.hasNext) {
        val entry = iter.next()
        keys(i) = entry.getKey
        values(i) = entry.getValue

        i += 1
      }
      (rowInner.getId, VFactory.sparseLongKeyDoubleVector(rowInner.getDim, keys, values))
    }

    new LabeledData(vec, Double.NaN, id)
  }

  def json2LabeledData(json:JObject): LabeledData = {
    val modelType = ServerModel.get().modelType
    val indexRange = ServerModel.get().indexRange

    val id = (json \ "id").extract[String]
    val vec = (json \ "index", json \ "value") match {
      case (JNothing, value@JArray(_)) => // dense
        if (modelType.isDouble) {
          VFactory.denseDoubleVector(value.extract[Array[Double]])
        } else if (modelType.isFloat){
          VFactory.denseFloatVector(value.extract[Array[Float]])
        } else {
          throw new Exception("data type is not supported!")
        }
      case (idx@JArray(_), JNothing) => // dummy
        if (modelType.isIntKey){
          val index = idx.extract[Array[Int]]
          if (modelType.isFloat) {
            VFactory.sparseFloatVector(indexRange.toInt, index, index.indices.map(_ => 1.0f).toArray)
          } else if (modelType.isDouble) {
            VFactory.sparseDoubleVector(indexRange.toInt, index, index.indices.map(_ => 1.0).toArray)
          } else {
            throw new Exception("data type is not supported!")
          }
        } else {
          val index = idx.extract[Array[Long]]
          if (modelType.isFloat) {
            val value = index.indices.map(_ => 1.0f).toArray
            VFactory.sparseLongKeyFloatVector(indexRange, index, value)
          } else if (modelType.isDouble) {
            val value = index.indices.map(_ => 1.0).toArray
            VFactory.sparseLongKeyDoubleVector(indexRange, index, value)
          } else {
            throw new Exception("data type is not supported!")
          }
        }
      case (index@JArray(_), value@JArray(_)) => // dummy
        if (modelType.isIntKey) {
          if (modelType.isFloat) {
            VFactory.sparseFloatVector(indexRange.toInt, index.extract[Array[Int]], value.extract[Array[Float]])
          } else if (modelType.isDouble) {
            VFactory.sparseDoubleVector(indexRange.toInt, index.extract[Array[Int]], value.extract[Array[Double]])
          } else {
            throw new Exception("data type is not supported!")
          }
        } else {
          if (modelType.isFloat) {
            VFactory.sparseLongKeyFloatVector(indexRange, index.extract[Array[Long]], value.extract[Array[Float]])
          } else if (modelType.isDouble) {
            VFactory.sparseLongKeyDoubleVector(indexRange, index.extract[Array[Long]], value.extract[Array[Double]])
          } else {
            throw new Exception("data type is not supported!")
          }
        }
    }

    new LabeledData(vec, Double.NaN, id)
  }

  @throws[Exception]
  def dataFrameRow[T: ClassTag](id: String, data: Array[T]): DataFrameRow = {
    val clz = implicitly[ClassTag[T]].runtimeClass

    val rowBuilder = DataFrameRow.newBuilder()
    clz match {
      case clz if clz == classOf[Float] =>
        val builder = fArrayRow.newBuilder()
        data.foreach{ case f: Float =>
          builder.addFeatures(f)
        }
        rowBuilder.setFarr(builder.build())
      case clz if clz == classOf[Double] =>
        val builder = dArrayRow.newBuilder()
        data.foreach{ case d: Double =>
          builder.addFeatures(d)
        }
        rowBuilder.setDarr(builder.build())
      case _ => throw new Exception("not supported!")
    }

    rowBuilder.build()
  }

  @throws[Exception]
  def dataFrameRow[K: ClassTag, V: ClassTag](id: String, dim: K, dataMap: Map[K, V]): DataFrameRow = {
    val kClz = implicitly[ClassTag[K]].runtimeClass
    val vClz = implicitly[ClassTag[V]].runtimeClass

    val rowBuilder = DataFrameRow.newBuilder()
    (kClz, vClz) match {
      case (k, v) if k == classOf[Int] && v == classOf[Float] =>
        val builder =ifKVRow.newBuilder()
        builder.setId(id)
        builder.setDim(dim.asInstanceOf[Int])
        dataMap.foreach{ case (key: Int, value: Float) =>
          builder.putFeatures(key, value)
        }
        rowBuilder.setIfkv(builder.build())
      case (k, v) if k == classOf[Int] && v == classOf[Double] =>
        val builder =idKVRow.newBuilder()
        builder.setId(id)
        builder.setDim(dim.asInstanceOf[Int])
        dataMap.foreach{ case (key: Int, value: Double) =>
          builder.putFeatures(key, value)
        }
        rowBuilder.setIdkv(builder.build())
      case (k, v) if k == classOf[Long] && v == classOf[Float] =>
        val builder =lfKVRow.newBuilder()
        builder.setId(id)
        builder.setDim(dim.asInstanceOf[Long])
        dataMap.foreach{ case (key: Long, value: Float) =>
          builder.putFeatures(key, value)
        }
        rowBuilder.setLfkv(builder.build())
      case (k, v) if k == classOf[Long] && v == classOf[Double] =>
        val builder =ldKVRow.newBuilder()
        builder.setId(id)
        builder.setDim(dim.asInstanceOf[Long])
        dataMap.foreach{ case (key: Long, value: Double) =>
          builder.putFeatures(key, value)
        }
        rowBuilder.setLdkv(builder.build())
      case _ => throw new Exception("not supported!")
    }

    rowBuilder.build()
  }
}
