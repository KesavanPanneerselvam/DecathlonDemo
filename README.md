```
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

internal class DataProtectionHelper(keyGenParameterSpec: EMFKeyGenParameter) {
    private var symmetricKey: SecretKey
    private var iv: IvParameterSpec
    private var asymmetricKeyPair: KeyPair?

    init {
        symmetricKey = generateKey()
        iv = generateIv()
        asymmetricKeyPair = keyGenParameterSpec.createAsymmetricKeyPair()
    }

    private fun generateKey(): SecretKey =
        KeyGenerator.getInstance(SYMMETRIC_KEY_ALGORITHM).apply {
            init(AES_KEY_SIZE)
        }.generateKey()

    private fun generateIv(): IvParameterSpec {
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv)
        return IvParameterSpec(iv)
    }

    fun rsaEncrypt(data: ByteArray): ByteArray =
        Cipher.getInstance(ASYMMETRIC_KEY_TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, asymmetricKeyPair?.public)
        }.doFinal(data)

    fun decrypt(data: ByteArray): ByteArray =
        Cipher.getInstance(ASYMMETRIC_KEY_TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, asymmetricKeyPair?.private)
        }.doFinal(data)

    companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val KEY_ALIAS = "EMF_KEY"
        const val AES_KEY_SIZE = 256
        const val IV_SIZE = 16
        const val SYMMETRIC_KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        const val SYMMETRIC_KEY_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        const val SYMMETRIC_KEY_PADDING = "PKCSSPADDING"
        const val SYMMETRIC_KEY_TRANSFORMATION =
            "$SYMMETRIC_KEY_ALGORITHM/$SYMMETRIC_KEY_BLOCK_MODE/$SYMMETRIC_KEY_PADDING"
        const val RSA_KEY_SIZE = 2048
        const val ASYMMETRIC_KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA
        const val ASYMMETRIC_KEY_BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
        const val ASYMMETRIC_KEY_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
        const val ASYMMETRIC_KEY_DIGEST = KeyProperties.DIGEST_SHA256
        const val ASYMMETRIC_KEY_TRANSFORMATION =
            "$ASYMMETRIC_KEY_ALGORITHM/$ASYMMETRIC_KEY_BLOCK_MODE/$ASYMMETRIC_KEY_PADDING"
    }
}




import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.AlgorithmParameterSpec

class EMFKeyGenParameter {

    fun createAsymmetricKeyPair(): KeyPair? {
        val generator = KeyPairGenerator.getInstance(
            DataProtectionHelper.ASYMMETRIC_KEY_ALGORITHM,
            DataProtectionHelper.ANDROID_KEYSTORE
        ).apply {
            initialize(getKeyGenParameterSpec())
        }
        val keyPair = generator.generateKeyPair()
        return if (keyPair.private != null && keyPair.public != null) KeyPair(
            keyPair.public,
            keyPair.private
        ) else null
    }

    fun getKeyGenParameterSpec(): AlgorithmParameterSpec  =
        KeyGenParameterSpec.Builder(
            DataProtectionHelper.KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(DataProtectionHelper.ASYMMETRIC_KEY_BLOCK_MODE)
            setEncryptionPaddings(DataProtectionHelper.ASYMMETRIC_KEY_PADDING)
            setDigests(DataProtectionHelper.ASYMMETRIC_KEY_DIGEST)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setIsStrongBoxBacked(true)
            }
            //Todo:: After successfully login
            //setUserAuthenticationRequired(true)
        }.build()
}


import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import java.security.KeyPairGenerator


@RunWith(AndroidJUnit4::class)
class DataProtectionHelperTest {
    private val mockParameterSpec = mockk<EMFKeyGenParameter>()
    private lateinit var dataProtectionHelper: DataProtectionHelper

    @BeforeEach
    fun setup() {
        FakeAndroidKeyStoreProvider.setup()
        val wrapped = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
        val keyPair = wrapped.generateKeyPair()
        every { mockParameterSpec.createAsymmetricKeyPair() } returns keyPair
        dataProtectionHelper = DataProtectionHelper(mockParameterSpec)
    }

    @Test
    fun test(){
        val rawText = "This is a sample text to text the encryption and decryption."
        val rawTextByteArray  = rawText.toByteArray()
        val encrypt = dataProtectionHelper.rsaEncrypt(rawTextByteArray)
        val decrypt = dataProtectionHelper.decrypt(encrypt)
        Assert.assertEquals(rawText,String(decrypt))
    }
}

```
