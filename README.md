```

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

internal object Logger {

    private const val TAG_INFO = "Info"
    private const val TAG_DEBUG = "debug"
    private const val TAG_ERROR = "error"
    private const val TAG_WARNING = "warning"
    private const val TAG_VERBOSE = "verbose"
    private const val TAG_ASSERT = "assert"

    private val timeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    private lateinit var packageName: String

    private lateinit var logFile: File
    fun init(context: Context){
        logFile = File(context.cacheDir, "EMFLog.txt")
        packageName = context.packageName
        if(!logFile.exists()){
            logFile.createNewFile()
        }
    }

    fun i(msg: String){
        writeLog(TAG_INFO, msg)
    }

    fun d(msg: String){
        writeLog(TAG_DEBUG, msg)
    }

    fun v(msg: String){
        writeLog(TAG_VERBOSE, msg)
    }

    fun w(msg: String){
        writeLog(TAG_WARNING, msg)
    }

    fun a(msg: String){
        writeLog(TAG_ASSERT, msg)
    }

    fun e(msg: String){
        writeLog(TAG_ERROR, msg)
    }

    private fun writeLog(tag: String,message: String){
        val logText = "$tag $message"
        Log.v("EMFLogger",logText)
        logFile.appendText("${timeFormatter.format(Date())} $packageName $logText\n")
    }
}

  fun initEMF(application: Application){
      Logger.init(application)

  fun logInfo(msg: String){
      Logger.i(msg)
  }

  fun logDebug(msg: String){
      Logger.d(msg)
  }

  fun logVerbose(msg: String){
      Logger.v(msg)
  }

  fun logWarning(msg: String){
      Logger.v(msg)
  }

  fun logAssert(msg: String){
      Logger.a(msg)
  }

  fun logError(msg: String){
      Logger.e(msg)
  }
```
