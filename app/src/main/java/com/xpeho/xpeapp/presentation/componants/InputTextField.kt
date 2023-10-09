package com.xpeho.xpeapp.presentation.componants

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.ui.theme.SfPro

enum class InputTextFieldKeyboardType {
    NUMBER,
    TEXT,
    EMAIL,
    PASSWORD,
}

@Composable
fun InputTextField(
    keyboardType: InputTextFieldKeyboardType,
    label: String,
) {
    var textFieldValue by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 8.dp,
            )
            .clip(
                RoundedCornerShape(20.dp)
            ),
    ) {
        TextField(
            value = textFieldValue,
            label = { Text(label) },
            onValueChange = {
                textFieldValue = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = getTypeOfKeyboard(
                    keyboardType,
                )
            ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(
                fontFamily = SfPro,
            ),
            visualTransformation = getVisualTransformation(
                keyboardType,
                passwordVisible,
            ),
            trailingIcon = if (keyboardType == InputTextFieldKeyboardType.PASSWORD) {
                {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null,
                        )
                    }
                }
            } else {
                null
            }
        )
    }
}

private fun getVisualTransformation(keyboardType: InputTextFieldKeyboardType, passwordVisible: Boolean): VisualTransformation {
    return if(keyboardType==InputTextFieldKeyboardType.PASSWORD) {
        if(passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    } else {
        VisualTransformation.None
    }
}

private fun getTypeOfKeyboard(keyboardType: InputTextFieldKeyboardType): KeyboardType {
    return when (keyboardType) {
        InputTextFieldKeyboardType.NUMBER -> KeyboardType.Number
        InputTextFieldKeyboardType.TEXT -> KeyboardType.Text
        InputTextFieldKeyboardType.EMAIL -> KeyboardType.Email
        InputTextFieldKeyboardType.PASSWORD -> KeyboardType.Password
    }
}

@Preview
@Composable
fun InputTextFieldPreview() {
    InputTextField(
        keyboardType = InputTextFieldKeyboardType.NUMBER,
        label = "Numéro de téléphone",
    )
}