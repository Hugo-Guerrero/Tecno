package com.hugoguerrero.tecno.ui.screens.donation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hugoguerrero.tecno.R

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // --- Logo y nombre ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 32.dp, bottom = 24.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Tecno-Ciencia",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // --- Título y descripción ---
            Text(
                text = "Crea tu cuenta",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Completa tus datos para registrarte.",
                color = Color(0xFFB0BEC5),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- Imagen superior ---
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // usa la imagen correcta
                contentDescription = "Registro",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1B263B))
                    .padding(bottom = 24.dp)
            )

            // --- Campo de nombre ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                placeholder = { Text("Tu nombre completo") },
                singleLine = true,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Icono persona",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(2.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1B263B),
                    unfocusedContainerColor = Color(0xFF1B263B),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00BFA6),
                    unfocusedBorderColor = Color(0xFF455A64),
                    focusedLabelColor = Color(0xFF00BFA6),
                    unfocusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // --- Campo de correo ---
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                placeholder = { Text("nombre@universidad.edu") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Icono correo",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(2.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1B263B),
                    unfocusedContainerColor = Color(0xFF1B263B),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00BFA6),
                    unfocusedBorderColor = Color(0xFF455A64),
                    focusedLabelColor = Color(0xFF00BFA6),
                    unfocusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // --- Campo de contraseña ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                placeholder = { Text("••••••••") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Icono contraseña",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(2.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1B263B),
                    unfocusedContainerColor = Color(0xFF1B263B),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00BFA6),
                    unfocusedBorderColor = Color(0xFF455A64),
                    focusedLabelColor = Color(0xFF00BFA6),
                    unfocusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // --- Campo de confirmar contraseña ---
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                placeholder = { Text("••••••••") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Icono contraseña",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(2.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1B263B),
                    unfocusedContainerColor = Color(0xFF1B263B),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF00BFA6),
                    unfocusedBorderColor = Color(0xFF455A64),
                    focusedLabelColor = Color(0xFF00BFA6),
                    unfocusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // --- Botón registrarse ---
            Button(
                onClick = { onRegisterSuccess() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFA6)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Registrarse",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            // --- Texto de inicio de sesión ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    color = Color(0xFF90A4AE),
                    fontSize = 13.sp
                )
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFF00BFA6),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }

            // --- Footer ---
            Text(
                text = "© 2025 Tecno-Ciencia     Términos · Privacidad",
                color = Color(0xFF607D8B),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(
        onRegisterSuccess = { },
        onLoginClick = { }
    )
}