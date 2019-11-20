import hu.sztaki.ilab.ps.matrix.factorization.Utils._
import hu.sztaki.ilab.ps.matrix.factorization._
import hu.sztaki.ilab.ps.matrix.factorization.sinks._
import hu.sztaki.ilab.ps.matrix.factorization.data.parsers._
import hu.sztaki.ilab.ps.matrix.factorization.PSOnlineLearnerAndTopKGenerator._
import hu.sztaki.ilab.ps.matrix.factorization.PSOnlineLearnerAndTopKGeneratorWithModelLoad._
import hu.sztaki.ilab.ps.matrix.factorization.PSOfflineMatrixFactorization._
import hu.sztaki.ilab.ps.matrix.factorization.PSTopKGeneratorWithLEMP._


import org.apache.flink.streaming.api.scala._
import java.io.PrintWriter
import java.io.File

object PsTry {
  def main(args: Array[String]): Unit = {

    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    // Input and output file names
    val folder = "recsys-2017-tutorial/"

    val startTimestamp = {
      val infile_reader = new java.io.BufferedReader(new java.io.FileReader(folder + "data.csv"))
      val s =infile_reader.readLine().split(",")(0).toLong
      infile_reader.close()
      s
    }

    val inputPath = "week_"
    val outputPathBatch = "ndcg.batch.out"
    val outputPathBatchOnline = "ndcg.batchonline.out"
    val modelPath = "model"
    val dayInSec = 86400

    /**
    * Evaluates a model with online learning
    * @param   input   the name of the input test data
    * @param   model   the name of the file containing the model
    * @param   output  the name of the output file
    */
    def OnlineEvaluate(input: String, model: String, output: String) {
      val ratings = ImplicitDataToRatingParser.parse(senv, folder + input, ",", 1, 2, 0, startTimestamp)
      val parsedModel = VectorModelFileParser.parse(senv, folder + model, numFactors = 10)

      val topK = psTopKGenerator(ratings, parsedModel)

      nDCGSink.nDCGPeriodsToCsv(topK, folder + output, dayInSec, false)

      senv execute s"Online evaluation of model $model without learning"
    }

    // read the model and evaluate it as-is
    val i = 10
    OnlineEvaluate(inputPath+i, modelPath+i, outputPathBatch+i)
  }
}


