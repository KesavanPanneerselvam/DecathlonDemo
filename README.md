val userIdBody: RequestBody = RequestBody.create(MultipartBody.FORM,  "00000000")
val bundleID: RequestBody = RequestBody.create(MultipartBody.FORM,  "--------")
val fileBody: RequestBody = RequestBody.create(MultipartBody.FORM,  getCompressData(file))
val multipartBody = MultipartBody.Builder()
    .addPart(MultipartBody.Part.createFormData("userId", null, userIdBody))
    .addPart(MultipartBody.Part.createFormData("bundleId", null, bundleID))
    .addPart(MultipartBody.Part.createFormData("uploadedCrashReport", "CrashReport.txt", fileBody))
    .build()
