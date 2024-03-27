interface ApiService {

    @POST("/some-api-method")
    fun sendCrashReport(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String,
        @Body body: RequestBody
    ): Call<ResponseBody>
}


class Test{
    val retrofit = Retrofit.Builder().build().create(ApiService::class.java)
    fun test(){
        val paramValues = mapOf("userId" to "00000000", "bundleId" to "--------")
        val paramBody: RequestBody = RequestBody.create(MultipartBody.FORM,  paramValues.toString())
        val fileBody: RequestBody = RequestBody.create(MultipartBody.FORM,  getCompressData(file))
        val multipartBody = MultipartBody.Builder()
            .addPart(MultipartBody.Part.createFormData("uploadedRequest", null, paramBody))
            .addPart(MultipartBody.Part.createFormData("uploadedCrashReport", "CrashReport.txt", fileBody))
            .build()
        val contentType = "multipart/form-data; charset=utf-8; boundary=" + multipartBody.boundary()

        val token = ""
        retrofit.sendCrashReport("Bearer $token",contentType, multipartBody)
    }

    fun getCompressData(file: File): ByteArray{

    }
}
