```
package com.interview.emf

import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EMFCryptoManager {

    private val my_text = ""
     fun generateSecretKey() {
         val ks = KeyStore.getInstance("AndroidKeyStore").apply {
             load(null)
         }

         val keyGen = KeyGenerator.getInstance("AES")
         keyGen.init(256)

         val secretKey: SecretKey = keyGen.generateKey()

         val start: Calendar = Calendar.getInstance()
         val end: Calendar = Calendar.getInstance()
         end.add(Calendar.YEAR, 2)

         val entry = KeyStore.SecretKeyEntry(secretKey)
         val protectionParameter =
             KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                 .setKeyValidityStart(start.time)
                 .setKeyValidityEnd(end.time)
                 .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                 .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                 .build()

         ks.setEntry(ALIAS, entry, protectionParameter)
     }

     fun encrypt(plainText: String): String? {
         val ks = KeyStore.getInstance("AndroidKeyStore").apply {
             load(null)
         }

         val secretKey = ks.getKey(ALIAS, null)

         return try {
             val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
             cipher.init(Cipher.ENCRYPT_MODE, secretKey)

             val cipherText = Base64.encodeToString(cipher.doFinal(plainText.toByteArray()), Base64.DEFAULT)
             val iv = Base64.encodeToString(cipher.iv, Base64.DEFAULT)

             "${cipherText}.$iv"
         } catch (e: Exception) {
             Log.e(TAG, "encrypt: error msg = ${e.message}")
             null
         }
     }

     fun decrypt(cipherText: String): String? {
         val ks = KeyStore.getInstance("AndroidKeyStore").apply {
             load(null)
         }

         val secretKey = ks.getKey(ALIAS, null)

         val array = cipherText.split(".")
         val cipherData = Base64.decode(array[0], Base64.DEFAULT)
         val iv = Base64.decode(array[1], Base64.DEFAULT)

         return try {
             val cipher = Cipher.getInstance(TRANSFORMATION)
             val spec = IvParameterSpec(iv)

             cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

             val clearText = cipher.doFinal(cipherData)

             String(clearText, 0, clearText.size, StandardCharsets.UTF_8)
         } catch (e: Exception) {
             Log.e(TAG, "decrypt: error msg = ${e.message}")
             null
         }
     }

    companion object {
        private const val TAG = "CRYPTO-MANAGER-TAG"
        private const val ALIAS = "EMF_RSA_KEY"
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}
---------------
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import java.security.KeyPair
import java.security.KeyStore
import java.security.SecureRandom
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


internal class DataProtectionHelper(keyGenParameterSpec: EMFKeyGenParameter) {
    private var symmetricKey: SecretKey
    private var iv: IvParameterSpec
    private var asymmetricKeyPair: KeyPair

    init {
        asymmetricKeyPair = keyGenParameterSpec.createAsymmetricKeyPair()
        iv = generateIv()
        symmetricKey = getSecretKey()
        println("symmetricKey format: " + symmetricKey.format)
        println("symmetricKey algorithm: " + symmetricKey.algorithm)
        println("symmetricKey isDestroyed: " + symmetricKey.isDestroyed)
    }

    private fun getSecretKey(): SecretKey {
        val keystore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }
        val entry: KeyStore.Entry? = keystore.getAESKeyEntry()
        return if (entry is KeyStore.SecretKeyEntry) {
            /*val decryptedAESKey = rsaDecrypt(entry.secretKey.encoded)
            SecretKeySpec(decryptedAESKey, 0, decryptedAESKey.size, SYMMETRIC_KEY_ALGORITHM)*/
            entry.secretKey
        } else {
            val aesSecretKey = generateKey()
            /*val encryptedAESKey = rsaEncrypt(aesSecretKey.encoded)
            val secretKey =
                SecretKeySpec(encryptedAESKey, 0, encryptedAESKey.size, SYMMETRIC_KEY_ALGORITHM)*/
            keystore.setEntry(
                ENCRYPTED_KEY_ALIAS,
                KeyStore.SecretKeyEntry(aesSecretKey),
                getProtectionParameter()
            )
            aesSecretKey
        }
    }

    private fun KeyStore.getAESKeyEntry(): KeyStore.Entry?{
        return try {
            this.getEntry(ENCRYPTED_KEY_ALIAS, getProtectionParameter())
        }catch (e: Exception){
            null
        }
    }

    private fun getProtectionParameter(): KeyStore.ProtectionParameter {
        val start: Calendar = Calendar.getInstance()
        val end: Calendar = Calendar.getInstance()
        end.add(Calendar.YEAR, 2)
        return KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setKeyValidityStart(start.time)
            .setKeyValidityEnd(end.time)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()
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

    fun encrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(SYMMETRIC_KEY_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey, iv)
        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(SYMMETRIC_KEY_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey, iv)
        return cipher.doFinal(data)
    }

    private fun rsaEncrypt(data: ByteArray): ByteArray =
        Cipher.getInstance(ASYMMETRIC_KEY_TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, asymmetricKeyPair.public)
        }.doFinal(data)

    private fun rsaDecrypt(data: ByteArray): ByteArray =
        Cipher.getInstance(ASYMMETRIC_KEY_TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, asymmetricKeyPair.private)
        }.doFinal(data)

    companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val ENCRYPTED_KEY_ALIAS = "EMF_KEY_1234566"
        const val KEY_ALIAS = "EMF_KEY"
        const val AES_KEY_SIZE = 256
        const val IV_SIZE = 16
        const val SYMMETRIC_KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        const val SYMMETRIC_KEY_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        const val SYMMETRIC_KEY_PADDING = "PKCS5PADDING"
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

internal class EMFPublicKey(private val data: ByteArray): PublicKey{
    override fun getAlgorithm(): String = "RSA"

    override fun getFormat(): String = "RAW"

    override fun getEncoded(): ByteArray = data

}

internal class EMFCertificate(private val data: ByteArray, type: String = "EMF"): Certificate(type) {
    override fun toString(): String = data.toString()

    override fun getEncoded(): ByteArray = data

    override fun verify(p0: PublicKey?) = Unit

    override fun verify(p0: PublicKey?, p1: String?) = Unit

    override fun getPublicKey(): PublicKey = EMFPublicKey(data)

}

-------------------------------------------------

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.spec.AlgorithmParameterSpec

class EMFKeyGenParameter {

    fun createAsymmetricKeyPair(): KeyPair {
        val keyPairGen = KeyPairGenerator.getInstance(
            DataProtectionHelper.ASYMMETRIC_KEY_ALGORITHM,
            DataProtectionHelper.ANDROID_KEYSTORE
        ).apply {
            initialize(getKeyGenParameterSpec())
        }
        val keystore = KeyStore.getInstance(DataProtectionHelper.ANDROID_KEYSTORE).apply {
            load(null)
        }
        var privateKey = keystore.getKey(DataProtectionHelper.KEY_ALIAS, null) as PrivateKey?
        if(privateKey == null){
            keyPairGen.generateKeyPair()
            privateKey = keystore.getKey(DataProtectionHelper.KEY_ALIAS, null) as PrivateKey?
        }
        val publicKey = keystore.getCertificate(DataProtectionHelper.KEY_ALIAS).publicKey
        return KeyPair(publicKey, privateKey)
    }

    fun getKeyGenParameterSpec(): AlgorithmParameterSpec = KeyGenParameterSpec.Builder(
            DataProtectionHelper.KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).run {
            setBlockModes(DataProtectionHelper.ASYMMETRIC_KEY_BLOCK_MODE)
            setEncryptionPaddings(DataProtectionHelper.ASYMMETRIC_KEY_PADDING)
            setDigests(DataProtectionHelper.ASYMMETRIC_KEY_DIGEST)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //setIsStrongBoxBacked(true)
            }
            //Todo:: After successfully login
            //setUserAuthenticationRequired(true)
            build()
        }
}


----------------------------------------------------------------
val plainText = "I am some plain text that needs to be encrypted"
                        val encryptedItem = EMFHelper.encrypt(plainText.toByteArray())
                        val base64 = Base64.getEncoder().encodeToString(encryptedItem)
                        println("Encryption Done")
                        println("EncryptBase64: " +base64)
                        val decryptedText = EMFHelper.decrypt(encryptedItem)
                        println("Decryption Done")
                        Text(text = "Plain Text: $plainText")
                        Text(text = "Decrypted Text: ${String(decryptedText)}")

```
