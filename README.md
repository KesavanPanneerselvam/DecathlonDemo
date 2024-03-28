```
val mutableString = mutableStateOf("")
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //EMFHelper.initEMF(this@ActivityTest)
        setContent {
            JPMCDemoAppTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = {
                            EMFHelper.showAlert1()
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(text = "Show Alert 1")
                    }
                    Button(
                        onClick = {
                            EMFHelper.showAlert2()
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(text = "Show Alert 2")
                    }
                    Button(
                        onClick = {
                            getLogs()
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(text = "Get Logs")
                    }
                    Text(text = mutableString.value)
                }
            }
        }
    }

    private fun getLogs(){
        val fileName = "logfilename_"+Date().time+".txt"
        val logFile = File(Environment.getExternalStorageDirectory(),fileName)
        if(!logFile.exists()) {
            logFile.createNewFile()
        }
        try {
            //Runtime.getRuntime().exec("logcat --pid=\$(pidof -s com.aa.aa)")
            Log.d("EMFHelper","1 This is a test log from EMF app at "+Date())
            Log.d("EMFHelper","2 This is a test log from EMF app at "+Date())
            Log.d("EMFHelper","3 This is a test log from EMF app at "+Date())
            //process = Runtime.getRuntime().exec("logcat -d com.interview.jpmcdemoapp:* -f $logFile")
            Runtime.getRuntime().exec("logcat --pid=\$(pidof -s com.aa.aa)")
            Runtime.getRuntime().exec("logcat -f $logFile")
            //process = Runtime.getRuntime().exec("logcat  -f $logFile")

            Log.d("EMFHelper","4 This is a test log from EMF app at "+Date())
            mutableString.value = "Success"
        } catch (e: IOException) {
            e.printStackTrace()
            mutableString.value = "Failed "+ e.message
        }
    }
```
