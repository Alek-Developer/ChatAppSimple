package com.gsixacademy.android.chatappsimple

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SignInActivity"
        private const val REQUEST_CODE_SIGN_IN = 1
    }
// Kreiranje na GoogleSignInClient kako i firebaseAuth vo SignInActivity!
    private var googleSignInClient : GoogleSignInClient? = null
    private var firebaseAuth : FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


// 1. Dodavanje Na Animacija so Nut-Circle!

        val nutAnimator = animateNut(nut_image, TimeUnit.SECONDS.toMillis(2))

        updateConstraint(R.layout.activity_main)

        nutAnimator.start()


// Inicijaliziranje na firebaseAuth vo onCreate!
        firebaseAuth = FirebaseAuth.getInstance()

// RequestIdToken se misli na nekoj znak, kljuch neshto vazno kako Id isl sto se dobiva vo slucajov od Google servise.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@SignInActivity,gso)

        sign_in_button.setOnClickListener {
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient!!.signInIntent

        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }
// Override Fun - onActivityResult koja ke sodrzi (rezultat) od ovoj startActivityForResult!
// Vaka se definira codot za User-ot dali e Ok, za da moze da se Konentira ili Ne - SignIn or Not! (if - try - catch)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account!!)
                }catch (e : ApiException){
                    Log.e(TAG, "Google Sign In Failed $e")
                    Toast.makeText(this@SignInActivity, "Google Sign In Failed", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this@SignInActivity) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                }else {
                    Log.e(TAG, "Authentication Failed ${task.exception}")
                    Toast.makeText(this@SignInActivity, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }

            }

    }

// 2. Fun - Dodavanje Na Animacija so Nut-Circle! Od 112 do 136 linija.

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun updateConstraint(@LayoutRes id : Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this, id)
        newConstraintSet.applyTo(rootView)
        TransitionManager.beginDelayedTransition(rootView)

    }

    private fun animateNut(nut : ImageView?, orbitDuration : Long): ValueAnimator {
/// Values: 0,359 e brzinata i rotacijata na kruzenjeto na objektot.
        val anim = ValueAnimator.ofInt(0,359)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParameters = nut?.layoutParams as ConstraintLayout.LayoutParams
            layoutParameters.circleAngle = value.toFloat()
            nut.layoutParams = layoutParameters

            anim.duration = orbitDuration
            anim.interpolator = LinearInterpolator()
            anim.repeatMode = ValueAnimator.RESTART
            anim.repeatCount = ValueAnimator.INFINITE
        }
        return anim
    }



}