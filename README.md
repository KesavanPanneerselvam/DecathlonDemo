```
val timeFormatter = SimpleDateFormat("MM-dd HH:mm:ss.SSS")
val calendar = Calendar.getInstance()
                    calendar.add(Calendar.MINUTE,-2)
                    val logCmd = "logcat -d -t \""+timeFormatter.format(calendar.time)+"\""
                    println("EMFHelper1231231-$logCmd")
                    val process = Runtime.getRuntime().exec(logCmd)

adb shell 'logcat --pid=$(pidof -s com.interview.jpmcdemoapp)'
```
