package com.xpeho.xpeapp.ui.presentation.componants

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.enums.InputTextFieldKeyboardType
import com.xpeho.xpeapp.ui.theme.SfPro

@Composable
fun InputTextField(
    keyboardType: InputTextFieldKeyboardType,
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
) {
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
            value = value,
            label = { Text(label) },
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = getTypeOfKeyboard(
                    keyboardType,
                )
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
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

private fun getVisualTransformation(
    keyboardType: InputTextFieldKeyboardType,
    passwordVisible: Boolean
): VisualTransformation {
    return if (keyboardType == InputTextFieldKeyboardType.PASSWORD) {
        if (passwordVisible) {
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
        value = "",
    )
}
