package com.tencent.angel.acumos.jersey.resources

import com.tencent.angel.acumos.ServerModel
import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.{Consumes, POST, Path, Produces, _}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.slf4j.{Logger, LoggerFactory}
import com.tencent.angel.acumos.Utils
import org.json4s.JsonAST.JObject

@Path("/")
class HttpRestApiHandler {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[HttpRestApiHandler])
  private val model = ServerModel.get()

  /*
   * curl -H "Content-Type: application/json" -X POST -d '{"id": "jghjgxvk", "index": [1, 2, 5, 9, 88 ,106, 144]}' localhost:9875/angel/acumos/classify
   * curl -H "Content-Type: application/json" -X POST -d '{"id": "ghgfcgc", "index": [1, 2, 5, 9, 88 ,106, 144], "value": [1.2, 2.2, 2.5, 1.9, 1.88 ,1.06, 1.44]}' localhost:9875/angel/acumos/classify
   * curl -H "Content-Type: application/json" -X POST -d '{"id": "cdescjh", "value": [0.946965999,0.219678426,0.099661971,0.709609036,0.138087037,0.374625979,0.945477034,0.618008034,0.363709403,0.929056481,0.885870533,0.143579956,0.04054522,0.884405337,0.941858189,0.469888547,0.354722786,0.874441354,0.101336306,0.540324574,0.265402886,0.601666255,0.136482338,0.834906194,0.753455512,0.971103455,0.246978652,0.373624441,0.828189946,0.041889056,0.255983148,0.768419492,0.171383886,0.298740146,0.633533535,0.09318154,0.314294346,0.98744944,0.611706448,0.15759447,0.39262817,0.682524333,0.296695075,0.916734358,0.961973423,0.580769719,0.975919272,0.342810091,0.720322261,0.759106531,0.647864944,0.480575538,0.569626588,0.309322116,0.454494203,0.601066973,0.875504543,0.094745578,0.592832746,0.636034702,0.787902956,0.676591135,0.178925219,0.808859439,0.038122928,0.098356034,0.824141389,0.366279449,0.988495205,0.183975171,0.290763114,0.639359457,0.484933699,0.581532265,0.295998054,0.650526995,0.793425838,0.128393793,0.840910675,0.514644877,0.987790148,0.591626507,0.784946965,0.496531699,0.09839539,0.31666209,0.937871175,0.992772615,0.012797383,0.363535632,0.292895966,0.611886604,0.432886017,0.963435248,0.763425006,0.306679198,0.959465494,0.043783088,0.284438335,0.559958017,0.510032204,0.306584678,0.918598585,0.579205102,0.710462826,0.307135529,0.262318396,0.823714382,0.48420897,0.82958572,0.858200264,0.790839313,0.408089517,0.717732246,0.923010673,0.866922745,0.2845741,0.33573116,0.501550214,0.481221377,0.929585982,0.952288927,0.512337569,0.489209453,0.312729427,0.697634868,0.891850233,0.561808125,0.646906196,0.977660161,0.990056091,0.780062484,0.515249345,0.457252195,0.115441135,0.757021206,0.498831925,0.08923524,0.22994902,0.957738922,0.136694348,0.293256306,0.093878853,0.424078956,0.068843803,0.605011841,0.252588625,0.605011841]}' localhost:9875/angel/acumos/classify
   */
  @POST
  @Path("/angel/acumos/{method:(classify|regress|predict)}")
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  def processPredict(requestBody: String, @PathParam("method") method: String): Response = {
    // println(requestBody)
    val labeledData = Utils.json2LabeledData(parse(requestBody).asInstanceOf[JObject])
    method.toLowerCase match {
      case "classify" =>
        val result = model.predict(labeledData)
        val outJson = ("id" , result.sid) ~ ("predict", result.proba) ~ ("threshold", 0.5)
        Response.status(200).entity(compact(render(outJson))).build()
      case "regress" =>
        val result = model.predict(labeledData)
        val outJson = ("id" , result.attached) ~ ("predict", result.proba) ~ ("threshold", 0.5)
        Response.status(200).entity(compact(render(outJson))).build()
      case "predict" =>
        val result = model.predict(labeledData)
        val outJson = ("id" , result.attached) ~ ("predict", result.proba) ~ ("threshold", 0.5)
        Response.status(200).entity(compact(render(outJson))).build()
      case _ =>
        val errorMessage = "Resolve request path error, exception: " + method
        Response.status(500).entity("{\"error\": \"" + errorMessage + "\"}").build()
    }
  }


  // curl -H "Content-Type: application/json" -X GET localhost:9875/angel/acumos/getInfo

  @GET
  @Path("/angel/acumos/{method:getInfo}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def processGetInfo(@PathParam("method") method: String): Response = {
    method.toLowerCase match {
      case "getinfo" =>
        val model = ServerModel.get()
        val json = ("modelType", model.modelType.toString) ~
          ("dataFormat", model.dataFormat.toString) ~
          ("indexRange", model.indexRange.toString)

        Response.status(200).entity(compact(render(json))).build()
      case _ =>
        val errorMessage = "Resolve request path error, exception: " + method
        Response.status(500).entity("{\"error\": \"" + errorMessage + "\"}").build()
    }
  }
}
