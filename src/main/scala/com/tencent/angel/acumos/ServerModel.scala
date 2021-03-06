package com.tencent.angel.acumos

import com.tencent.angel.mlcore.conf.{MLCoreConf, SharedConf}
import com.tencent.angel.mlcore.local.{LocalEnvContext, LocalModel}
import com.tencent.angel.mlcore.utils.JsonUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path

case class Options(grpc_port: Int, http_port: Int)

object ServerModel {
  private var model: LocalModel = _

  def init(options: Options): LocalModel = {
    if (model == null) {
      implicit val sharedConf: SharedConf = new SharedConf
      val modelPathUrl = this.getClass.getClassLoader.getResource("fm")
      val modelPath = modelPathUrl.toString.split(":/", 2)(1)
      sharedConf.set(MLCoreConf.ML_LOAD_MODEL_PATH, modelPath)
      val jsonFile: String = new Path(modelPath, "graph.json").toString
      sharedConf.set(MLCoreConf.ML_JSON_CONF_FILE, jsonFile)

      JsonUtils.parseAndUpdateJson(jsonFile, sharedConf, new Configuration())
      model = new LocalModel(sharedConf)
      model.buildNetwork()

      val env = new LocalEnvContext
      model.createMatrices(env)
      model.loadModel(env, modelPath, new Configuration())
    }

    model
  }

  def get(): LocalModel = {
    if (model == null) {
      throw new Exception("please init model first!")
    } else {
      model
    }
  }
}
