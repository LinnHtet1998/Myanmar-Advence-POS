package com.myanmaradvancedpos.backup

import com.myanmaradvancedpos.data.PosDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Google Drive appDataFolder backup transport.
 * Obtain a short-lived OAuth access token through Google's AuthorizationClient
 * with the Drive appdata scope before calling these methods. Never ask for or
 * store the user's Google password.
 */
class GoogleDriveBackup(private val dao: PosDao) {
    suspend fun createBackupJson(): String = withContext(Dispatchers.IO) {
        val items = dao.allItems()
        val invoices = dao.allInvoices()
        val expenses = dao.allExpenses()
        JSONObject().apply {
            put("format", "myanmar-advanced-pos-backup")
            put("version", 1)
            put("createdAt", System.currentTimeMillis())
            put("items", JSONArray().also { a -> items.forEach { a.put(JSONObject().apply {
                put("id", it.id); put("name", it.name); put("code", it.customCode); put("category", it.category)
                put("unit", it.unit); put("costPrice", it.costPrice); put("sellPrice", it.sellPrice); put("stockQty", it.stockQty)
                put("photoUri", it.photoUri ?: JSONObject.NULL)
            }) } })
            put("invoices", JSONArray().also { a -> invoices.forEach { a.put(JSONObject().apply {
                put("id", it.id); put("invoiceNo", it.invoiceNo); put("createdAt", it.createdAt); put("customerName", it.customerName)
                put("subtotal", it.subtotal); put("discount", it.discount); put("total", it.total); put("paymentMethod", it.paymentMethod); put("paid", it.paid)
            }) } })
            put("expenses", JSONArray().also { a -> expenses.forEach { a.put(JSONObject().apply {
                put("id", it.id); put("title", it.title); put("category", it.category); put("amount", it.amount)
                put("note", it.note); put("createdAt", it.createdAt); put("receiptPhotoUri", it.receiptPhotoUri ?: JSONObject.NULL)
            }) } })
        }.toString()
    }

    /** Upload JSON into the private Drive appDataFolder. Returns the created file ID. */
    suspend fun upload(accessToken: String, json: String): String = withContext(Dispatchers.IO) {
        val boundary = "pos-${System.currentTimeMillis()}"
        val metadata = JSONObject().put("name", "myanmar-pos-backup.json").put("parents", JSONArray().put("appDataFolder"))
        val body = "--$boundary\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n$metadata\r\n" +
            "--$boundary\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n$json\r\n--$boundary--"
        val conn = (URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart&fields=id").openConnection() as HttpURLConnection)
        try {
            conn.requestMethod = "POST"; conn.doOutput = true
            conn.setRequestProperty("Authorization", "Bearer $accessToken")
            conn.setRequestProperty("Content-Type", "multipart/related; boundary=$boundary")
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val response = stream.bufferedReader().use { it.readText() }
            check(code in 200..299) { "Google Drive upload failed ($code): $response" }
            JSONObject(response).getString("id")
        } finally { conn.disconnect() }
    }
}
