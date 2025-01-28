import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.components.layout.Subtitle
import com.xpeho.xpeapp.ui.uiState.UserInfosUiState
import com.xpeho.xpeapp.ui.viewModel.user.UserInfosViewModel
import com.xpeho.xpeho_ui_android.foundations.Fonts
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory

@Composable
fun SidebarItemProfile(
    navigationController: NavController,

    userInfosViewModel: UserInfosViewModel = viewModel(
        factory = viewModelFactory {
            UserInfosViewModel(
                wordpressRepo = XpeApp.appModule.wordpressRepository,
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    )
) {
    val userInfosState = userInfosViewModel.state

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigationController.navigate(route = Screens.Profile.name)
            }
            .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = com.xpeho.xpeho_ui_android.R.drawable.contactfill),
            contentDescription = "Profile Icon",
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            when (userInfosState) {
                is UserInfosUiState.SUCCESS -> {
                    val user = userInfosState.userInfos
                    Row {
                        Subtitle(
                            label = user.lastname.uppercase()
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Subtitle(
                            label = user.firstname.replaceFirstChar { it.uppercase()},
                        )
                    }
                    Text(
                        text = user.email,
                        fontSize = 16.sp,
                        fontFamily = Fonts.raleway,
                        color = Color.White,
                    )
                }
                is UserInfosUiState.ERROR -> {
                    Text(
                        text = "Error: ${userInfosState.error}",
                        fontSize = 16.sp,
                        fontFamily = Fonts.raleway,
                        color = Color.Red,
                    )
                }
                else -> {
                    Text(
                        text = stringResource(id = R.string.profil_page_modify_password_loading_message),
                        fontSize = 16.sp,
                        fontFamily = Fonts.raleway,
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}