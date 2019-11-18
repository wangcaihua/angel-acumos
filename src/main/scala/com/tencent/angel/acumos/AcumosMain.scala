package com.tencent.angel.acumos

import com.tencent.angel.acumos.DatasetProto.{DataFrame, Prediction}
import com.tencent.angel.mlcore.PredictResult
import com.tencent.angel.mlcore.local.LocalModel
import com.tencent.angel.mlcore.local.data.LocalMemoryDataBlock

class AcumosMain() {
  private val model: LocalModel = ServerModel.init(Options(-1, -1))

  def classify(request: DataFrame): Prediction = {
    val rowsCount = request.getRowsCount
    val responseBuilder = Prediction.newBuilder()
    responseBuilder.setThreshold(0.5f)

    if (rowsCount == 1) {
      val row = request.getRows(0)
      val labeledData = Utils.row2LabeledData(row)
      val result: PredictResult = model.predict(labeledData)

      responseBuilder.putId2Prediction(labeledData.getAttach, result.pred.toFloat)
    } else if (rowsCount > 1) {
      val datablock = new LocalMemoryDataBlock(rowsCount, Int.MaxValue)

      (0 until rowsCount).foreach { i =>
        val row = request.getRows(i)
        datablock.put(Utils.row2LabeledData(row))
      }

      model.predict(datablock).foreach { restult: PredictResult =>
        responseBuilder.putId2Prediction(restult.sid, restult.pred.toFloat)
      }
    } else {
      throw new Exception("no input data in DataFrame")
    }

    responseBuilder.build()
  }
}
