```
✅ Step 1: Keep AppConfiguration EXACTLY as it is
Example – Production
class ProdAppConfiguration @Inject constructor() : AppConfiguration {
    override val microServiceUrl = "https://api.example.com/"
    override val graphQLUrl = "https://graphql.example.com/"
    override val snowdropUrl = "https://snowdrop.example.com/"
}


Same for StageAppConfiguration, TestAppConfiguration.

👉 No changes needed here ✅

✅ Step 2: Bind the correct AppConfiguration per environment
If you’re still on Dagger (recommended first)

Use source-set based binding (cleanest).

prod/NetworkConfigModule.kt
@Module
abstract class NetworkConfigModule {

    @Binds
    abstract fun bindAppConfiguration(
        impl: ProdAppConfiguration
    ): AppConfiguration
}

stage/NetworkConfigModule.kt
@Module
abstract class NetworkConfigModule {

    @Binds
    abstract fun bindAppConfiguration(
        impl: StageAppConfiguration
    ): AppConfiguration
}

test/NetworkConfigModule.kt
@Module
abstract class NetworkConfigModule {

    @Binds
    abstract fun bindAppConfiguration(
        impl: TestAppConfiguration
    ): AppConfiguration
}


✔ This ensures only one AppConfiguration exists per build variant

✅ Step 3: Define Retrofit Qualifiers (important)
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MicroServiceRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GraphQLRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SnowdropRetrofit

✅ Step 4: Create a shared OkHttpClient
@Module
object OkHttpModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
}

✅ Step 5: Create Retrofit instances using AppConfiguration

This is the key part 🔑

@Module
object RetrofitModule {

    @Provides
    @Singleton
    @MicroServiceRetrofit
    fun provideMicroServiceRetrofit(
        okHttpClient: OkHttpClient,
        appConfig: AppConfiguration
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(appConfig.microServiceUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @GraphQLRetrofit
    fun provideGraphQLRetrofit(
        okHttpClient: OkHttpClient,
        appConfig: AppConfiguration
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(appConfig.graphQLUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @SnowdropRetrofit
    fun provideSnowdropRetrofit(
        okHttpClient: OkHttpClient,
        appConfig: AppConfiguration
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(appConfig.snowdropUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}


✅ Environment-safe
✅ Compile-time guaranteed
✅ Zero runtime switching bugs

✅ Step 6: Create API services
Microservice REST
interface MicroServiceApi {
    @GET("users")
    suspend fun getUsers(): List<User>
}

GraphQL
interface GraphQLApi {
    @POST("graphql")
    suspend fun executeQuery(@Body body: GraphQLRequest): GraphQLResponse
}

Snowdrop
interface SnowdropApi {
    @GET("offers")
    suspend fun getOffers(): List<Offer>
}

✅ Step 7: Provide API instances
@Module
object ApiServiceModule {

    @Provides
    fun provideMicroServiceApi(
        @MicroServiceRetrofit retrofit: Retrofit
    ): MicroServiceApi =
        retrofit.create(MicroServiceApi::class.java)

    @Provides
    fun provideGraphQLApi(
        @GraphQLRetrofit retrofit: Retrofit
    ): GraphQLApi =
        retrofit.create(GraphQLApi::class.java)

    @Provides
    fun provideSnowdropApi(
        @SnowdropRetrofit retrofit: Retrofit
    ): SnowdropApi =
        retrofit.create(SnowdropApi::class.java)
}

✅ Step 8: Inject cleanly in Repository
class HomeRepository @Inject constructor(
    private val microServiceApi: MicroServiceApi,
    private val graphQLApi: GraphQLApi,
    private val snowdropApi: SnowdropApi
)


No qualifiers needed here 👍
DI graph is already resolved.

```
