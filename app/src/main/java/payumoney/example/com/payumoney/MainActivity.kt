package payumoney.example.com.payumoney

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.util.*
import android.content.Intent
import com.facebook.*
import com.facebook.ProfileTracker
import com.facebook.login.LoginManager


class MainActivity : AppCompatActivity() {

    private val EMAIL = "email"
    private lateinit var loginButton : LoginButton
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var callbackManager : CallbackManager
    private lateinit var profileTracker : ProfileTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions(Arrays.asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                logD("Access Token - $accessToken")
                profileTracker = object : ProfileTracker() {
                    override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile?) {
                        val firstName = currentProfile?.firstName
                        val lastName = currentProfile?.lastName
                        val id = currentProfile?.id

                        logD("Id - $id, FirstName - $firstName, lastName - $lastName")
                    }
                }
                if (isLoggedIn){
                    logD("User Logged In")
                }else{
                    logD("Login Failed")
                }
            }
            override fun onCancel() {
                // App code
                logD("On Cancel Called")
            }
            override fun onError(exception: FacebookException) {
                // App code
                logD("On Error Called")
            }
        })
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun logD(message: String){
        Log.d(TAG, message)
    }

}
