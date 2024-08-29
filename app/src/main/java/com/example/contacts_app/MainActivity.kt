package com.example.contacts_app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.contacts_app.ui.theme.Contacts_AppTheme
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.contacts_app.model.contacts




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Contacts_AppTheme {
                val context = LocalContext.current
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        TopNav()
                        ContactsScreen(paddingValues = PaddingValues(), context = LocalContext.current)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNav(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_DIAL)

    TopAppBar(
        title = { Text(text = "Contacts App") },
        actions = {
            IconButton(
                onClick = { context.startActivity(intent) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "home",
                    modifier = modifier.size(30.dp)
                )
            }
        }
    )
}


@Composable
fun ContactItem(name: String, phoneNumber: String, image: Painter, onClick: (String) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(phoneNumber) }
        .background(Color.LightGray)
        .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = image,
            contentDescription = "image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        SelectablePhoneNumber(phoneNumber)
    }
}

@Composable
fun SelectablePhoneNumber(phoneNumber: String) {
    val context = LocalContext.current
    BasicText(text = phoneNumber,
        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
        modifier = Modifier
            .background(Color.Transparent)
            .clickable {

                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("phoneNumber", phoneNumber)
                clipboardManager.setPrimaryClip(clip)
                Toast
                    .makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
            }
            .padding(horizontal = 8.dp))
}

@Composable
fun ContactsScreen(paddingValues: PaddingValues, context: Context) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(contacts.size) { index ->
            val contact = contacts[index]
            ContactItem(name = contact.name,
                phoneNumber = contact.phoneNumber,
                image = painterResource(id = contact.imageResId),
                onClick = { number ->
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = android.net.Uri.parse("tel:$number")

                    }
                    context.startActivity(intent)
                })
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable

fun TopNavPreview(){
   Column {
       TopNav()
       ContactsScreen(paddingValues = PaddingValues(), context = LocalContext.current)
   }

}
