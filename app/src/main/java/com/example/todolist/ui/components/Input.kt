package com.example.todolist.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Input(
    label: String,
    placeholder:String,
    value: String,
    onValueChange:(String)-> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    error: String? = null
) {
    var showPassword by remember { mutableStateOf(false) }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF121418),
        unfocusedContainerColor = Color(0xFF121418),
        focusedBorderColor = Color(0xFF3F4759),
        unfocusedBorderColor = Color(0xFF2A2F3E),
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        unfocusedPlaceholderColor = Color.Gray,
        focusedPlaceholderColor = Color.Gray
    )

    Column (
        modifier = modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        if (isPassword) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = placeholder, color = Color.Gray) },
                visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                colors = colors,
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showPassword = !showPassword
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) "Ocultar senha" else "Mostrar senha"
                        )
                    }
                },
                isError = error != null,
                supportingText = {
                    error?.let {
                        Text(
                            text = it
                        )
                    }
                }
            )
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = placeholder, color = Color.Gray) },
                colors = colors,
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = error != null,
                supportingText = {
                    error?.let {
                        Text(
                            text = it
                        )
                    }
                }
            )
        }
    }
}