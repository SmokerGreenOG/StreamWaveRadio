package com.streamwave.radio.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.streamwave.radio.R
import com.streamwave.radio.core.theme.*

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, placeholder: String = "", modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query, onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        placeholder = { Text(placeholder.ifEmpty { stringResource(R.string.search_hint) }, color = SecondaryText) },
        leadingIcon = { Icon(Icons.Default.Search, stringResource(R.string.search_hint), tint = SecondaryText) },
        trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Default.Clear, stringResource(R.string.cancel), tint = SecondaryText) } },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(focusedContainerColor = Card, unfocusedContainerColor = Card, focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText, cursorColor = Purple, focusedIndicatorColor = Purple, unfocusedIndicatorColor = GlassBorder),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), singleLine = true
    )
}
