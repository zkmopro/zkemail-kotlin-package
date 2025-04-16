package uniffi.mopro

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import uniffi.mopro.generateCircomProof
import uniffi.mopro.ProofLib
import java.io.File


@RunWith(AndroidJUnit4::class)
class MoproTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.github.zkmopro.test", appContext.packageName)
    }

    @Test
    fun testZkemailFunctionality() {
        val appContext = InstrumentationRegistry.getInstrumentation().context

        // Copy srs.local from assets to a temp file
        val srsFile = File(appContext.cacheDir, "srs.local")
        appContext.assets.open("srs.local").use { input ->
            srsFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val srsPath = srsFile.absolutePath

        // Copy zkemail_input.json from assets to a temp file and parse it
        val inputFile = File(appContext.cacheDir, "zkemail_input.json")
        appContext.assets.open("zkemail_input.json").use { input ->
            inputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val jsonContent = inputFile.readText()
        val jsonObject = org.json.JSONObject(jsonContent)
        val inputs = HashMap<String, List<String>>()
        val headerStorage = jsonObject.getJSONObject("header").getJSONArray("storage")
        val headerStorageList = mutableListOf<String>()
        for (i in 0 until headerStorage.length()) {
            headerStorageList.add(headerStorage.getInt(i).toString())
        }
        inputs["header_storage"] = headerStorageList
        inputs["header_len"] = listOf(jsonObject.getJSONObject("header").getInt("len").toString())
        val pubkeyModulus = jsonObject.getJSONObject("pubkey").getJSONArray("modulus")
        val pubkeyModulusList = mutableListOf<String>()
        for (i in 0 until pubkeyModulus.length()) {
            pubkeyModulusList.add(pubkeyModulus.getString(i))
        }
        inputs["pubkey_modulus"] = pubkeyModulusList
        val pubkeyRedc = jsonObject.getJSONObject("pubkey").getJSONArray("redc")
        val pubkeyRedcList = mutableListOf<String>()
        for (i in 0 until pubkeyRedc.length()) {
            pubkeyRedcList.add(pubkeyRedc.getString(i))
        }
        inputs["pubkey_redc"] = pubkeyRedcList
        val signature = jsonObject.getJSONArray("signature")
        val signatureList = mutableListOf<String>()
        for (i in 0 until signature.length()) {
            signatureList.add(signature.getString(i))
        }
        inputs["signature"] = signatureList
        inputs["date_index"] = listOf(jsonObject.getInt("date_index").toString())
        inputs["subject_index"] = listOf(jsonObject.getJSONObject("subject_sequence").getInt("index").toString())
        inputs["subject_length"] = listOf(jsonObject.getJSONObject("subject_sequence").getInt("length").toString())
        inputs["from_header_index"] = listOf(jsonObject.getJSONObject("from_header_sequence").getInt("index").toString())
        inputs["from_header_length"] = listOf(jsonObject.getJSONObject("from_header_sequence").getInt("length").toString())
        inputs["from_address_index"] = listOf(jsonObject.getJSONObject("from_address_sequence").getInt("index").toString())
        inputs["from_address_length"] = listOf(jsonObject.getJSONObject("from_address_sequence").getInt("length").toString())

        try {
            val proof = proveZkemail(srsPath, inputs)
            val valid = verifyZkemail(srsPath, proof)
            assertEquals(true, valid)
        } finally {
            srsFile.delete()
            inputFile.delete()
        }
    }
} 