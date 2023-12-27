package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.SignInActivity
import com.oukschub.checkmate.ui.component.BottomNavBar

@Composable
fun Profile(
    onNavigateToChecklists: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomNavBar(
        onClickChecklists = { onNavigateToChecklists() },
        onClickHome = { onNavigateToHome() },
        onClickProfile = { /*TO-DO*/ }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current

            Text(text = "Profile")

            Column(
                modifier = Modifier
                    .width(intrinsicSize = IntrinsicSize.Max)
                    .wrapContentSize()
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Change Password")
                }

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Settings")
                }

                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        context.startActivity(SignInActivity.createIntent(context))
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign Out")
                }
            }
        }
    }
}

@Composable
@Preview
fun Test() {
    BottomNavBar(
        onClickChecklists = {},
        onClickHome = {},
        onClickProfile = {}
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = Icons.Default.AccountCircle, contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )

                    Text(
                        text = "Profile",
                        modifier = Modifier.padding(bottom = 5.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Change Password")
                    }

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Settings")
                    }

                    val context = LocalContext.current
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            context.startActivity(SignInActivity.createIntent(context))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Sign Out")
                    }
                }
            }
        }
    }
}