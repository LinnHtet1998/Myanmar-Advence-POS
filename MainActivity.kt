package com.myanmaradvancedpos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myanmaradvancedpos.data.PosDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = PosDatabase.get(this)
        setContent { MaterialTheme { PosHome(onBackup = { /* Connect Google AuthorizationClient + upload flow here */ }) } }
    }
}

@Composable
private fun PosHome(onBackup: () -> Unit) {
    var selected by remember { mutableStateOf("ပင်မစာမျက်နှာ") }
    val entries = listOf(
        Triple("အရောင်း POS", "ဘောင်ချာဖွင့်ရန်", Icons.Default.PointOfSale),
        Triple("ပစ္စည်းစာရင်း", "ဓာတ်ပုံ၊ ဈေးနှုန်း၊ လက်ကျန်", Icons.Default.Inventory),
        Triple("ဝယ်ယူမှု", "Supplier နှင့် ဝယ်ဈေး", Icons.Default.LocalShipping),
        Triple("အသုံးစရိတ်", "နေ့စဉ် ကုန်ကျစရိတ်", Icons.Default.Payments),
        Triple("Customer", "အကြွေးနှင့် ငွေလက်ခံ", Icons.Default.People),
        Triple("အစီရင်ခံစာ", "အရောင်းနှင့် အမြတ်အရှုံး", Icons.Default.BarChart),
        Triple("Backup", "Google Drive သို့ သိမ်းရန်", Icons.Default.CloudUpload),
        Triple("ဆက်တင်", "ဆိုင်အချက်အလက်နှင့် စနစ်", Icons.Default.Settings)
    )
    Scaffold(topBar = { TopAppBar(title = { Text("Myanmar Advanced POS") }, actions = {
        IconButton(onClick = onBackup) { Icon(Icons.Default.CloudUpload, contentDescription = "Backup") }
    }) }, bottomBar = { NavigationBar {
        listOf("ပင်မစာမျက်နှာ", "အရောင်း", "ပစ္စည်း", "စာရင်း").forEach { label ->
            NavigationBarItem(selected == label, onClick = { selected = label }, icon = { Icon(Icons.Default.Home, null) }, label = { Text(label) })
        }
    } }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("မင်္ဂလာပါ", style = MaterialTheme.typography.headlineSmall)
            Text("သင့်ဆိုင်၏ လုပ်ငန်းများကို တစ်နေရာတည်းတွင် စီမံပါ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
                Text("ယနေ့ အရောင်းအကျဉ်း", style = MaterialTheme.typography.titleMedium)
                Text("ဒေတာများ ထည့်သွင်းပြီးနောက် အရောင်းနှင့် အမြတ်ကို ဤနေရာတွင် ပြသပါမည်။")
            } }
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(entries) { (title, subtitle, icon) ->
                    ElevatedCard(onClick = { selected = title }) {
                        Column(Modifier.padding(14.dp).height(112.dp), verticalArrangement = Arrangement.SpaceBetween) {
                            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Column {
                                Text(title, style = MaterialTheme.typography.titleMedium)
                                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    } }
}
