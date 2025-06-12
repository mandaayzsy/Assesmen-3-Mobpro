package com.manda0101.assesmen3.ui.screen

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.manda0101.assesmen3.R
import com.manda0101.assesmen3.ui.theme.Mobpro1Theme

@Composable
fun HapusDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
){
    AlertDialog(
        text = { Text(text = stringResource(id = R.string.pesan_hapus)) },
        confirmButton = {
            TextButton (onClick = { onConfirmation() }) {
                Text(text = stringResource(id = R.string.hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.batal))
            }
        },
        onDismissRequest = { onDismissRequest() }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogDeletePreview(){
    Mobpro1Theme  {
        HapusDialog(
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}